package SampleGraphFiltersTest;

import filters.*;
import filters.basicFilter.BasicFilter;
import filters.basicFilter.IntergerFilter;
import filters.basicFilter.StringFilter;
import operation.SamepleGraphFilter;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import sampleGraph.SampleGraph;

import java.util.HashMap;
import java.util.HashSet;

public class SampleGraphFilterTest {

    SampleGraph  sg = new SampleGraph();

    @Before
    public void initeSampleGraph(){
        int id1 = 1;
        int id2 = 2;
        int in3 = 3;
        int id4 = 4;
        String label1 = "student";
        String label2 = "protein";
        String label3 = "professor";
        String label4 = "paper";

        HashMap<String,Object> p1 = new HashMap<>();
        p1.put("age",18);
        p1.put("name","joejoe");

        HashMap<String,Object> p2 = new HashMap<>();
        p2.put("age",19);
        p2.put("name","baby");

        HashMap<String,Object> p3 = new HashMap<>();
        p3.put("age",23);
        p3.put("name","lin");

        HashMap<String,Object> p4 = new HashMap<>();
        p4.put("age",24);
        p4.put("name","cc");


        String type1 = "authorof";
        String type2 = "teach";
        String type3 = "take";
        String type4 = "cover";

        HashMap<String,Object> r1 = new HashMap<>();
        r1.put("time",2004);

        HashMap<String,Object> r2 = new HashMap<>();
        r2.put("time",2901);

        HashMap<String,Object> r3 = new HashMap<>();
        r3.put("time",4567);

        HashMap<String,Object> r4 = new HashMap<>();
        r4.put("time",6723);
        sg.addNode(1,"student",new HashMap<String,Object>(){{put("age",12);put("name","joejoe");}});
        sg.addNode(2,"professor",new HashMap<String,Object>(){{put("age",12);put("name","linlin");}});
        sg.addNode(3,"professor",new HashMap<String,Object>(){{put("age",12);put("name","lili");}});
        sg.addNode(4,"student",new HashMap<String,Object>(){{put("age",12);put("name","cccc");}});
        sg.addRel(Pair.of(1,2),"know",new HashMap<String,Object>(){{put("time",2002);}});
        sg.addRel(Pair.of(4,2),"know",new HashMap<String,Object>(){{put("time",2003);}});
        sg.addRel(Pair.of(2,3),"knows",new HashMap<String,Object>(){{put("time",2004);}});


    }

    @Test
    public void testLabelFilter(){

        int size;
        String res;
        size = sg.getNodes().size();
        res = String.format("before filter node size: %d",size);
        System.out.println(res);

        InnerFilter innf = new LabelFilter("student",true);
        SamepleGraphFilter sgf = new SamepleGraphFilter();
        sgf.setInnerFilter(innf);

        SampleGraph temp  = sgf.filter(this.sg);

        size = temp.getNodes().size();
        res = String.format("after filter node size: %d",size);
        System.out.println(res);

    }


    @Test
    public void testTypeFilter(){

        int size;
        String res;
        size = sg.getRels().size();
        res = String.format("before filter rel size: %d",size);
        System.out.println(res);

        //InnerFilter innf = new LabelFilter("student",true);
        InnerFilter innf = new TypeFilter("knows",true);
        SamepleGraphFilter sgf = new SamepleGraphFilter();
        sgf.setInnerFilter(innf);

        SampleGraph temp  = sgf.filter(this.sg);

        size = temp.getRels().size();
        res = String.format("after filter rel size: %d",size);
        System.out.println(res);

    }

    @Test
    public void testNodePropertyilter(){

        int size;
        String res;
        size = sg.getNodes().size();
        res = String.format("before filter node size: %d",size);
        System.out.println(res);

        //InnerFilter innf = new LabelFilter("student",true);
        BasicFilter bf = new StringFilter("lin","contains");
        InnerFilter innf = new NodePropertyFilter("name",bf);
        SamepleGraphFilter sgf = new SamepleGraphFilter();
        sgf.setInnerFilter(innf);

        SampleGraph temp  = sgf.filter(this.sg);

        size = temp.getNodes().size();
        res = String.format("after filter node size: %d",size);
        System.out.println(res);

    }

    @Test
    public void testNodeFilter(){

        int size;
        String res;
        size = sg.getNodes().size();
        res = String.format("before filter node size: %d",size);
        System.out.println(res);

        LabelFilter innf = new LabelFilter("professor",true);
        BasicFilter bf = new StringFilter("lin","contains");
        NodePropertyFilter npf = new NodePropertyFilter("name",bf);

        NodeFilter nodeFilter = new NodeFilter();
        nodeFilter.setLabelFilter(innf);
        nodeFilter.setPropertyFilter(npf);

        SamepleGraphFilter sgf = new SamepleGraphFilter();
        sgf.setInnerFilter(nodeFilter);

        SampleGraph temp  = sgf.filter(this.sg);

        size = temp.getNodes().size();
        res = String.format("after filter node size: %d",size);
        System.out.println(res);

    }


    @Test
    public void testPathFilter() {


        int size;
        String res;
        size = sg.getNodes().size();
        res = String.format("before filter node size: %d",size);
        System.out.println(res);

        BasicFilter bif = new IntergerFilter(12,"=");
        NodePropertyFilter ageFilter = new NodePropertyFilter("age",bif);
        NodeFilter nodeFilter = new NodeFilter();
        nodeFilter.setPropertyFilter(ageFilter);


        TypeFilter typeFilter = new TypeFilter("knows");
        RelationFilter relationFilter = new RelationFilter();
        relationFilter.setTypeFilter(typeFilter);

        PathFilter startFilter = new PathFilter();
        PathFilter endFilter = new PathFilter();

        startFilter.setCurrentFilter(nodeFilter);
        endFilter.setCurrentFilter(relationFilter);

        startFilter.setLaterFilter(endFilter);

        SampleGraph temp  = startFilter.filter(this.sg);
        size = temp.getNodes().size();
        res = String.format("after filter node size: %d",size);
        System.out.println(res);
    }

}

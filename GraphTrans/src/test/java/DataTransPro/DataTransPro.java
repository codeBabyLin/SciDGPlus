package DataTransPro;

import DataTrans.DataTrans;
import GraphTransPro.GraphTransProImpl;
import GraphTransPro.GraphTransServicePro;
import dataConfig.QueryGenerator;
import dataConfig.QueryTest;
import dataConfig.QueryTestImpl;
import dataProvide.coautor.CoauthorDataStore;
import dataProvide.coautor.CoauthorDataTestConfig;
import dataProvide.dppin.DPPINDataStore;
import dataProvide.dppin.PPINDataTestConfig;
import dataProvide.university.UniversityDataStore;
import dataProvide.university.UniversityDataTestConfig;
import operation.VersionGraphOperation;
import operation.VersionGraphStore;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.gradoop.common.model.impl.properties.Property;
import org.gradoop.temporal.io.impl.csv.TemporalCSVDataSource;
import org.gradoop.temporal.model.impl.TemporalGraph;
import org.gradoop.temporal.model.impl.pojo.TemporalEdge;
import org.gradoop.temporal.model.impl.pojo.TemporalVertex;
import org.gradoop.temporal.util.TemporalGradoopConfig;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public class DataTransPro {

    String coauthorPath = System.getProperty("user.dir")+"\\DynamicGraphStore\\GraphTransPro\\author";
    String ppinPath = System.getProperty("user.dir")+"\\DynamicGraphStore\\GraphTransPro\\ppin";
    String univerPath = System.getProperty("user.dir")+"\\DynamicGraphStore\\GraphTransPro\\univer";
    String bikePath = System.getProperty("user.dir")+"\\DynamicGraphStore\\GraphTransPro\\bike";

    //String path = ppinPath;

    private CoauthorDataStore cds = new CoauthorDataStore();
    private DPPINDataStore dds = new DPPINDataStore();
    private UniversityDataStore uds = new UniversityDataStore(26);
   /* public PerformanceTest(){
        CoauthorDataStore cds = new CoauthorDataStore();
        DPPINDataStore dds = new DPPINDataStore();
        UniversityDataStore uds = new UniversityDataStore(26);
    }*/

    public void storeCoauthor(VersionGraphStore vgs){
        cds.storeVersionGraph(vgs);
    }

    public void storeDPPIN(VersionGraphStore vgs){
        dds.storeVersionGraph(vgs);
    }

    public void storeUniversity(VersionGraphStore vgs){
        uds.storeVersionGraph(vgs);
    }
    //GraphTrans.GraphTransService graphTransService
    public void storeBike(GraphTransServicePro graphTrans) {
        ExecutionEnvironment ENV = ExecutionEnvironment.createLocalEnvironment();
        TemporalGradoopConfig temporalConfig = TemporalGradoopConfig.createConfig(ENV);
        //String path = this.getClass().getClassLoader().getResource("Example").getPath();
        String path = System.getProperty("user.dir") + "\\Citibike-2018-30Stations";
        System.out.println(path);

        AtomicInteger idFactory = new AtomicInteger(1);


        TemporalCSVDataSource source = new TemporalCSVDataSource(path, temporalConfig);
        TemporalGraph graph = source.getTemporalGraph();
        HashMap<String, Long> idsStr = new HashMap<>();
        try{

            for(TemporalVertex vertex: graph.getVertices().collect()){
                String strId = vertex.getId().toString();
                String label = vertex.getLabel();
                long startVersion = vertex.getValidFrom();
                long endVersion = vertex.getValidTo();
                idsStr.put(strId,1L);
                long node = idsStr.size();
                idsStr.put(strId,node);

                graphTrans.addNode_c(node,startVersion);
                graphTrans.deleteNode(node,endVersion);
                graphTrans.setNodeLabel(node,label);
                Iterator<Property> iter = vertex.getProperties().iterator();
                while(iter.hasNext()){
                    Property p = iter.next();
                    graphTrans.updateNodeProperty(node,p.getKey(),p.getValue().getObject(),startVersion);
                }
            }
            for(TemporalEdge edge:graph.getEdges().collect()){
                String strSourceId = edge.getSourceId().toString();
                String strTargetId = edge.getTargetId().toString();
                long startVersion = edge.getValidFrom();
                long endVersion = edge.getValidTo();
                long startNode = idsStr.get(strSourceId);
                long endNode = idsStr.get(strTargetId);
                long relationId = idFactory.getAndIncrement();

                String label = edge.getLabel();

                graphTrans.addRelation_c(relationId,startNode,endNode,startVersion);
                graphTrans.deleteRelation(relationId,startNode,endNode,endVersion);
                graphTrans.setRelationType(relationId,label);
                Iterator<Property> iter = edge.getProperties().iterator();
                while(iter.hasNext()){
                    Property p = iter.next();
                    graphTrans.updateRelationProperty(relationId,p.getKey(),p.getValue().getObject(),startVersion);
                }

            }

            //int vSize = graph.getVertices().collect().size();
            //int eSize = graph.getEdges().collect().size();

            //System.out.println(vSize+ ": " + eSize);
        }catch (Exception e){
            e.printStackTrace();
        }


    }


    public void testAuthor(VersionGraphOperation vgo){
        CoauthorDataTestConfig qgc = new CoauthorDataTestConfig();
        QueryGenerator qg = new QueryGenerator(qgc);
        QueryTest qt = new QueryTestImpl(qg,vgo);
        qt.testAll();
    }
    public void testPPIN(VersionGraphOperation vgo){
        PPINDataTestConfig qgc = new PPINDataTestConfig();
        QueryGenerator qg = new QueryGenerator(qgc);
        QueryTest qt = new QueryTestImpl(qg,vgo);
        qt.testAll();
    }

    public void testUniversity(VersionGraphOperation vgo){
        UniversityDataTestConfig qgc = new UniversityDataTestConfig();
        QueryGenerator qg = new QueryGenerator(qgc);
        QueryTest qt = new QueryTestImpl(qg,vgo);
        qt.testAll();
    }


    //@Test
    public void Dppin_store(){

        GraphTransProStoreImpl graphTransStore = new GraphTransProStoreImpl(this.ppinPath);
        storeDPPIN(graphTransStore);

    }
    // @Test
    public void coauthor(){
        GraphTransProStoreImpl graphTransStore = new GraphTransProStoreImpl(this.coauthorPath);
        storeCoauthor(graphTransStore);
    }

    // @Test
    public void university(){
        GraphTransProStoreImpl graphTransStore = new GraphTransProStoreImpl(this.univerPath);
        storeUniversity(graphTransStore);
    }

    // @Test
    public void citybike(){
        GraphTransServicePro graphTrans = new GraphTransProImpl(this.bikePath);
        storeBike(graphTrans);
    }

    //@Test
    public void testPath(){

    }


  /*  public GraphTrans.GraphTransService city(String path){
        return
    }*/


    public static void main(String[]args){

        DataTransPro dataTrans = new DataTransPro();

       //  dataTrans.coauthor();
       // dataTrans.citybike();
        dataTrans.Dppin_store();
        //dataTrans.university();
       // GraphTransServicePro graphTrans = new GraphTransProImpl(dataTrans.ppinPath);





    }



}

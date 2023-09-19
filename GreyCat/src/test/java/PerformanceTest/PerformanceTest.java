package PerformanceTest;


//import OperationImpl.VersionGraphOperationImplTP;
//import OperationImpl.VersionGraphOperationImplDP;


import OperationImpl.GreycatQueryImpl;
import OperationImpl.GreycatStoreImpl;
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


public class PerformanceTest {

    //String coauthorPath = System.getProperty("user.dir")+"\\DynamicGraphStore\\Dp\\author";
    //String ppinPath = System.getProperty("user.dir")+"\\DynamicGraphStore\\Dp\\ppin";
    //String univerPath = System.getProperty("user.dir")+"\\DynamicGraphStore\\Dp\\univer";

    private CoauthorDataStore cds = new CoauthorDataStore();
    private DPPINDataStore dds = new DPPINDataStore();
    private UniversityDataStore uds = new UniversityDataStore(26);
    public PerformanceTest(){
        CoauthorDataStore cds = new CoauthorDataStore();
        DPPINDataStore dds = new DPPINDataStore();
        UniversityDataStore uds = new UniversityDataStore(26);
    }

    public void storeCoauthor(VersionGraphStore vgs){
        cds.storeVersionGraph(vgs);
    }

    public void storeDPPIN(VersionGraphStore vgs){
        dds.storeVersionGraph(vgs);
    }

    public void storeUniversity(VersionGraphStore vgs){
        uds.storeVersionGraph(vgs);
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



    public static void main(String[] args){
        String coauthorPath = System.getProperty("user.dir")+"\\DynamicGraphStore\\Greycat\\author";
        String ppinPath = System.getProperty("user.dir")+"\\DynamicGraphStore\\Greycat\\ppin";
        String univerPath = System.getProperty("user.dir")+"\\DynamicGraphStore\\Greycat\\univer";

       // String path = coauthorPath;
        String path = ppinPath;
       //String path = univerPath;


        PerformanceTest pft = new PerformanceTest();

        VersionGraphStore vgs = new GreycatStoreImpl(path);
       // pft.storeCoauthor(vgs);
        pft.storeDPPIN(vgs);
        //pft.storeUniversity(vgs);

        //VersionGraphOperation vgo = new GreycatQueryImpl(path);
       // pft.testAuthor(vgo);
       //pft.testPPIN(vgo);
       // pft.testUniversity(vgo);

    }
}

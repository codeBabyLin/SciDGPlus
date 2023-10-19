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
import org.gradoop.temporal.io.impl.csv.TemporalCSVDataSource;
import org.gradoop.temporal.model.impl.TemporalGraph;
import org.gradoop.temporal.util.TemporalGradoopConfig;
import org.junit.Test;

public class DataTrans {


    String coauthorPath = System.getProperty("user.dir")+"\\DynamicGraphStore\\GraphTrans\\author";
    String ppinPath = System.getProperty("user.dir")+"\\DynamicGraphStore\\GraphTrans\\ppin";
    String univerPath = System.getProperty("user.dir")+"\\DynamicGraphStore\\GraphTrans\\univer";

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
    //GraphTransService graphTransService
    public void storeBike() {
        ExecutionEnvironment ENV = ExecutionEnvironment.createLocalEnvironment();
        TemporalGradoopConfig temporalConfig = TemporalGradoopConfig.createConfig(ENV);
        //String path = this.getClass().getClassLoader().getResource("Example").getPath();
        String path = System.getProperty("user.dir") + "\\Citibike-2018-30Stations";
        System.out.println(path);
        TemporalCSVDataSource source = new TemporalCSVDataSource(path, temporalConfig);
        TemporalGraph graph = source.getTemporalGraph();
        try{
        int vSize = graph.getVertices().collect().size();
        int eSize = graph.getEdges().collect().size();
            System.out.println(vSize+ ": " + eSize);
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


    @Test
    public void Dppin_store(){

        GraphTransStoreImpl graphTransStore = new GraphTransStoreImpl(this.ppinPath);
        storeDPPIN(graphTransStore);

    }
    @Test
    public void coauthor(){
        GraphTransStoreImpl graphTransStore = new GraphTransStoreImpl(this.coauthorPath);
        storeCoauthor(graphTransStore);
    }

    @Test
    public void university(){
        GraphTransStoreImpl graphTransStore = new GraphTransStoreImpl(this.univerPath);
        storeUniversity(graphTransStore);
    }

    @Test
    public void citybike(){
        storeBike();
    }



}

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.gradoop.common.model.impl.id.GradoopId;
import org.gradoop.temporal.io.impl.csv.TemporalCSVDataSource;
import org.gradoop.temporal.model.impl.TemporalGraph;
import org.gradoop.temporal.model.impl.pojo.TemporalVertex;
import org.gradoop.temporal.util.TemporalGradoopConfig;
import org.junit.Test;

public class GradoopTest {

    @Test
    public void testHaha(){
        for(int i = 0;i<10;i++){
            String id = GradoopId.get().toString();
            System.out.println(id);
        }

    }

    @Test
    public void testDataRead() throws Exception {

        ExecutionEnvironment ENV = ExecutionEnvironment.createLocalEnvironment();
        TemporalGradoopConfig temporalConfig = TemporalGradoopConfig.createConfig(ENV);



        String path = this.getClass().getClassLoader().getResource("Example").getPath();
        System.out.println(path);


        TemporalCSVDataSource source = new TemporalCSVDataSource(path, temporalConfig);

        TemporalGraph graph = source.getTemporalGraph();

        graph.getEdges().collect().forEach( temporalEdge -> {
            GradoopId sid = temporalEdge.getSourceId();
            GradoopId tid =temporalEdge.getTargetId();
            long txFrom = temporalEdge.getTxFrom();
            long txTo = temporalEdge.getTxTo();

            long vFrom = temporalEdge.getValidFrom();
            long vTo = temporalEdge.getValidTo();
            String res = String.format("%s->%s, tx[%s,%s],valid[%s.%s]",sid.toString(),tid.toString(),txFrom,txTo,vFrom,vTo);
            System.out.println(res);

        });

        graph.getVertices().collect().forEach(temporalVertex -> {
            GradoopId nid = temporalVertex.getId();
            long txFrom = temporalVertex.getTxFrom();
            long txTo = temporalVertex.getTxTo();

            long vFrom = temporalVertex.getValidFrom();
            long vTo = temporalVertex.getValidTo();
            String res = String.format("%s, tx[%s,%s],valid[%s.%s]",nid.toString(),txFrom,txTo,vFrom,vTo);
            System.out.println(res);
        });

       /* String storePath = "./gradoop";

        TemporalCSVDataSink sink = new TemporalCSVDataSink(storePath, temporalConfig);

        graph.writeTo(sink);

        ENV.execute();*/

    }

    @Test
    public void testConstruct(){

        ExecutionEnvironment ENV = ExecutionEnvironment.createLocalEnvironment();
        TemporalGradoopConfig temporalConfig = TemporalGradoopConfig.createConfig(ENV);


        //temporalConfig.getTemporalGraphCollectionFactory().

       /* DataSet<TemporalVertex > temporalVertices = new DataSet<TemporalVertex>() {

        }*/

        DataSet<TemporalVertex> data = ENV.fromCollection(new TemporalVertexSet().getCollection());

        TemporalGraph graph = temporalConfig.getTemporalGraphFactory().createEmptyGraph();

        temporalConfig.getTemporalGraphFactory();
        //DataSet<TemporalVertex> tvs = new DataSource<>();
       // DataSet<Tuple3<String, String, String>> metaData = (new CSVMetaDataSource()).readDistributed(this.getMetaDataPath(), this.getConfig());
       // DataSet<G> graphHeads = this.getConfig().getExecutionEnvironment().readTextFile(this.getGraphHeadCSVPath()).map(csvToGraphHead).withBroadcastSet(metaData, "metadata");
       // DataSet<V> vertices = this.getConfig().getExecutionEnvironment().readTextFile(this.getVertexCSVPath()).map(csvToVertex).withBroadcastSet(metaData, "metadata");
       // DataSet<E> edges = this.getConfig().getExecutionEnvironment().readTextFile(this.getEdgeCSVPath()).map(csvToEdge).withBroadcastSet(metaData, "metadata");
       // return collectionFactory.fromDataSets(graphHeads, vertices, edges);
       /* DataSet<TemporalVertex> tvs = new DataSet<TemporalVertex>() {
        }*/
        //DataSet<>

        //TemporalGraphCollectionFactory collectionFactory = temporalConfig.getTemporalGraphCollectionFactory();
        /*org.apache.flink.api.java.DataSet<TemporalGraphHead> temporalGraphHeads, DataSet<
        TemporalVertex > temporalVertices, DataSet< TemporalEdge > temporalEdges*/

     /*   public TemporalGraph getTemporalGraph() {
            TemporalGraphCollection collection = this.getTemporalGraphCollection();
            return this.getConfig().getTemporalGraphFactory().fromDataSets(collection.getGraphHeads().first(1), collection.getVertices(), collection.getEdges());
        }

        public TemporalGraphCollection getTemporalGraphCollection() {
            TemporalGraphCollectionFactory collectionFactory = this.getConfig().getTemporalGraphCollectionFactory();
            return (TemporalGraphCollection)this.getCollection(new CSVLineToTemporalGraphHead(collectionFactory.getGraphHeadFactory()), new CSVLineToTemporalVertex(collectionFactory.getVertexFactory()), new CSVLineToTemporalEdge(collectionFactory.getEdgeFactory()), collectionFactory);
        }*/
    }


}

import TemporalSet.TemporalEdgeSet;
import TemporalSet.TemporalHeadSet;
import TemporalSet.TemporalVertexSet;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.gradoop.common.model.impl.id.GradoopId;
import org.gradoop.common.model.impl.id.GradoopIdSet;
import org.gradoop.temporal.io.impl.csv.TemporalCSVDataSink;
import org.gradoop.temporal.io.impl.csv.TemporalCSVDataSource;
import org.gradoop.temporal.model.impl.TemporalGraph;
import org.gradoop.temporal.model.impl.pojo.TemporalEdge;
import org.gradoop.temporal.model.impl.pojo.TemporalGraphHead;
import org.gradoop.temporal.model.impl.pojo.TemporalVertex;
import org.gradoop.temporal.util.TemporalGradoopConfig;
import org.junit.Test;

import java.io.IOException;

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
    public void testConstruct() throws Exception {

        ExecutionEnvironment ENV = ExecutionEnvironment.createLocalEnvironment();
        TemporalGradoopConfig temporalConfig = TemporalGradoopConfig.createConfig(ENV);


        String storePath = "./gradoop";
        //temporalConfig.getTemporalGraphCollectionFactory().

       /* DataSet<TemporalVertex > temporalVertices = new DataSet<TemporalVertex>() {

        }*/

       long tx1 = System.currentTimeMillis();
       Thread.sleep(10);
       long tx2 = System.currentTimeMillis();

        GradoopIdSet  gradoopIds = new GradoopIdSet();
        gradoopIds.add(GradoopId.get());

       TemporalVertexSet tvs = new TemporalVertexSet();
        TemporalEdgeSet tes = new TemporalEdgeSet();

       TemporalVertex v1 = new TemporalVertex();
       v1.setId(GradoopId.fromString("000000000000000000000001"));
       v1.setLabel("test");
       v1.setValidFrom(1);
       v1.setValidTo(5);
       v1.setTxFrom(tx1);
       v1.setTxTo(tx2);
       v1.setProperty("age",10);
       v1.setGraphIds(gradoopIds);

        TemporalVertex v2 = new TemporalVertex();
        v2.setId(GradoopId.get());
        v2.setLabel("test");

        v2.setTxFrom(tx1);
        v2.setTxTo(tx2);
        v2.setValidFrom(3);
        v2.setValidTo(8);
        v2.setProperty("age",10);
        v2.setGraphIds(gradoopIds);

        tvs.add(v1);
        tvs.add(v2);

        TemporalEdge e = new TemporalEdge();
        e.setSourceId(v1.getId());
        e.setTargetId(v2.getId());
        e.setId(GradoopId.get());
        e.setLabel("know");
        e.setTxFrom(tx1);
        e.setTxTo(tx2);
        e.setValidFrom(3);
        e.setValidTo(5);
        e.setGraphIds(gradoopIds);

        tes.add(e);


        TemporalGraphHead h = new TemporalGraphHead();
        h.setId(GradoopId.get());
        h.setLabel("g");
        h.setTxFrom(tx1);
        h.setTxTo(tx2);
        h.setValidFrom(1);
        h.setValidTo(10);
        TemporalHeadSet ths = new TemporalHeadSet();
        ths.add(h);


        DataSet<TemporalGraphHead> data0 = ENV.fromCollection(ths.getCollection());

        DataSet<TemporalVertex> data1 = ENV.fromCollection(tvs.getCollection());

        DataSet<TemporalEdge> data2 = ENV.fromCollection(tes.getCollection());

        //data0.map().setSemanticProperties();


        TemporalGraph graph = temporalConfig.getTemporalGraphFactory().fromDataSets(data0,data1,data2);

        TemporalCSVDataSink sink = new TemporalCSVDataSink(storePath, temporalConfig);

        graph.writeTo(sink);

        ENV.execute();

        //TemporalGraph graph = temporalConfig.getTemporalGraphFactory().createEmptyGraph();

        //temporalConfig.getTemporalGraphFactory();
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

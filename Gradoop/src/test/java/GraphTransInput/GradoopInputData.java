package GraphTransInput;

import GraphTrans.GraphTransImpl;
import GraphTrans.GraphTransService;
import GraphTransPro.GraphTransProImpl;
import GraphTransPro.GraphTransServicePro;
import TemporalSet.TemporalEdgeSet;
import TemporalSet.TemporalHeadSet;
import TemporalSet.TemporalVertexSet;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.gradoop.common.model.impl.id.GradoopId;
import org.gradoop.common.model.impl.id.GradoopIdSet;
import org.gradoop.temporal.io.impl.csv.TemporalCSVDataSink;
import org.gradoop.temporal.model.impl.TemporalGraph;
import org.gradoop.temporal.model.impl.pojo.TemporalEdge;
import org.gradoop.temporal.model.impl.pojo.TemporalGraphHead;
import org.gradoop.temporal.model.impl.pojo.TemporalVertex;
import org.gradoop.temporal.util.TemporalGradoopConfig;

import java.util.HashMap;
import java.util.Iterator;

public class GradoopInputData {

    String coauthorPath = System.getProperty("user.dir")+"\\DynamicGraphStore\\GraphTransPro\\author";
    String ppinPath = System.getProperty("user.dir")+"\\DynamicGraphStore\\GraphTransPro\\ppin";
    String univerPath = System.getProperty("user.dir")+"\\DynamicGraphStore\\GraphTransPro\\univer";
    String bikePath = System.getProperty("user.dir")+"\\DynamicGraphStore\\GraphTransPro\\bike";

    private String getGradoopIdFromLong(long id, int limit){
       String value = String.valueOf(id);
       int length = value.length();
       char[] valArray = new char[limit-length];
       for(int i = 0;i< valArray.length;i++){
           valArray[i] = '0';
       }

       return new String(valArray).concat(value);

    }



    private GradoopId getId(long start, long end){
        String res = getGradoopIdFromLong(start,12).concat(getGradoopIdFromLong(end,12));
        return GradoopId.fromString(res);
    }


    private GradoopId getId(long id){
        return GradoopId.fromString(getGradoopIdFromLong(id,24));
    }



    public DataSet<TemporalGraphHead> getGraphSet(ExecutionEnvironment ENV,long graphId,String graphName, long from,long to){
        //ExecutionEnvironment ENV = ExecutionEnvironment.createLocalEnvironment();
        TemporalGraphHead h = new TemporalGraphHead();
        h.setId(getId(graphId));
        h.setLabel(graphName);
        h.setTxFrom(System.currentTimeMillis());
        h.setTxTo(System.currentTimeMillis());
        h.setValidFrom(from);
        h.setValidTo(to);
        TemporalHeadSet ths = new TemporalHeadSet();
        ths.add(h);
        return ENV.fromCollection(ths.getCollection());
    }
    public DataSet<TemporalVertex> getVertexSet(ExecutionEnvironment ENV,GraphTransServicePro service, GradoopIdSet gradoopIds){
        //ExecutionEnvironment ENV = ExecutionEnvironment.createLocalEnvironment();
        TemporalVertexSet nodeSet = new TemporalVertexSet();
        Iterator<Long[]> iter = service.allNodesWithVersions();
        long tx1 = System.currentTimeMillis();
        long tx2 = System.currentTimeMillis();
        while(iter.hasNext()){
            Long[] data = iter.next();
            long node = data[0];
            long startVersion = data[1];
            long endVersion = data[2];
            String label = service.getNodeLabel(node);
            HashMap<String, HashMap<Long,Object>> properties =service.getNodeProperty(node);

            TemporalVertex verTex = new TemporalVertex();
            verTex.setId(getId(node));
            verTex.setTxFrom(tx1);
            verTex.setTxTo(tx2);
            verTex.setValidFrom(startVersion);
            verTex.setValidTo(endVersion);
            verTex.setLabel(label);
            verTex.setGraphIds(gradoopIds);
            for(String key : properties.keySet()){
                Object value = properties.get(key).values().stream().findFirst().get();
                verTex.setProperty(key,value);
            }

            nodeSet.add(verTex);

        }
        return ENV.fromCollection(nodeSet.getCollection());

    }


    public DataSet<TemporalEdge> getEdgeSet(ExecutionEnvironment ENV,GraphTransServicePro service, GradoopIdSet gradoopIds){
        //ExecutionEnvironment ENV = ExecutionEnvironment.createLocalEnvironment();
        TemporalEdgeSet edgeSet = new TemporalEdgeSet();
        Iterator<Long[]> iter = service.allRelationsWithVersions();
        long tx1 = System.currentTimeMillis();
        long tx2 = System.currentTimeMillis();
        while(iter.hasNext()){
            Long[] data = iter.next();
            long relationId = data[0];
            long startNode = data[1];
            long endNode = data[2];
            long startVersion = data[3];
            long endVersion = data[4];
            String label = service.getRelationType(relationId);
            HashMap<String, HashMap<Long,Object>> properties =service.getRelationProperty(relationId);

            TemporalEdge edge = new TemporalEdge();
            edge.setId(getId(startNode,endNode));
            edge.setTxFrom(tx1);
            edge.setTxTo(tx2);
            edge.setSourceId(getId(startNode));
            edge.setTargetId(getId(endNode));
            edge.setValidFrom(startVersion);
            edge.setValidTo(endVersion);
            edge.setLabel(label);
            edge.setGraphIds(gradoopIds);
            for(String key : properties.keySet()){
                Object value = properties.get(key).values().stream().findFirst().get();
                edge.setProperty(key,value);
            }

            edgeSet.add(edge);

        }
        return ENV.fromCollection(edgeSet.getCollection());
    }


    public void storeData(long graphId,String graphName,String storePath, GraphTransServicePro service){

        ExecutionEnvironment ENV = ExecutionEnvironment.createLocalEnvironment();
        TemporalGradoopConfig temporalConfig = TemporalGradoopConfig.createConfig(ENV);


        GradoopIdSet gradoopIds = new GradoopIdSet();
        gradoopIds.add(getId(graphId));

        DataSet<TemporalGraphHead> data0  =  getGraphSet(ENV,graphId,graphName,1,100);
        DataSet<TemporalVertex> data1 = getVertexSet(ENV,service,gradoopIds);
        DataSet<TemporalEdge> data2 = getEdgeSet(ENV,service,gradoopIds);



        TemporalGraph graph = temporalConfig.getTemporalGraphFactory().fromDataSets(data0,data1,data2);

        TemporalCSVDataSink sink = new TemporalCSVDataSink(storePath, temporalConfig);

        try {
            graph.writeTo(sink);

            ENV.execute();
        }catch (Exception e){
            e.printStackTrace();
        }

        //String storePath = "./gradoop";

    }




    public static void main(String[]args){

        GradoopInputData data = new GradoopInputData();
        GraphTransServicePro graphTransService = new GraphTransProImpl(data.ppinPath);

        long graphId = 100;

        String storePath =  System.getProperty("user.dir")+"\\DynamicGraphStore\\Gradoop\\ppin";

        data.storeData(graphId,"ppin",storePath,graphTransService);

        //System.out.println(data.getGradoopIdFromLong(123));
       // String str = 24 * '3';


    }

}

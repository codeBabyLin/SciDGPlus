package DataTransPro;

import GraphTrans.GraphTransImpl;
import GraphTransPro.GraphTransProImpl;
import operation.VersionGraphStore;
import org.apache.commons.lang3.tuple.Pair;
import sampleGraph.SampleGraph;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class GraphTransProStoreImpl implements VersionGraphStore {

    private AtomicInteger idFactory;
    private GraphTransProImpl graphTrans;
    public GraphTransProStoreImpl(String dir){
        this.graphTrans = new GraphTransProImpl(dir);
        this.idFactory = new AtomicInteger(1);
    }


    @Override
    public void begin() {

    }

    @Override
    public void storeGraph(SampleGraph sampleGraph, int version) {
        for(Integer e: sampleGraph.getNodes()){
            String label = sampleGraph.getNodeLabel(e);
            //Vertex n = txGraph.addVertex(T.id,e.toString(),T.label,label);
            HashMap<String,Object> nodeProperty = sampleGraph.getNodeProperties().get(e);
            this.graphTrans.addNode_d(e,version);
            this.graphTrans.setNodeLabel(e,label);
            //this.graphTrans.


            //n.property("nodeId",e);
            for(Map.Entry<String,Object> p: nodeProperty.entrySet()){
                this.graphTrans.updateNodeProperty(e,p.getKey(),p.getValue(),version);
                //n.property(p.getKey(),p.getValue());
            }
        }
        for(Pair<Integer,Integer> pais: sampleGraph.getRels()) {
            //Vertex start = txGraph.vertex(pais.getLeft().toString());
            //Vertex end = txGraph.vertex(pais.getRight().toString());
            long start = pais.getLeft();
            long end = pais.getRight();
            String type = sampleGraph.getRelationType(pais);
            long relationId = this.idFactory.getAndIncrement();
            this.graphTrans.addRelation_d(relationId,start,end,version);
            this.graphTrans.setRelationType(relationId,type);

            HashMap<String,Object> relationProperty =sampleGraph.getRelationProperties().get(pais);
            for(Map.Entry<String,Object> p: relationProperty.entrySet()){
                this.graphTrans.updateRelationProperty(relationId,p.getKey(),p.getValue(),version);
            }

        }

    }

    @Override
    public void finish() {

    }
}


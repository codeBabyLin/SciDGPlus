import operation.VersionGraphStore;
import org.apache.commons.lang3.tuple.Pair;
import sampleGraph.SampleGraph;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GraphTransStoreImpl implements VersionGraphStore {

    private GraphTransImpl graphTrans;
    public GraphTransStoreImpl(String dir){
        this.graphTrans = new GraphTransImpl(dir);
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
            this.graphTrans.addRelation_d(start,end,version);
            this.graphTrans.setRelationType(start,end,type);

            HashMap<String,Object> relationProperty =sampleGraph.getRelationProperties().get(pais);
            for(Map.Entry<String,Object> p: relationProperty.entrySet()){
                this.graphTrans.updateRelationProperty(start,end,p.getKey(),p.getValue(),version);
            }

        }

    }

    @Override
    public void finish() {

    }
}

import kernel.BasicGraph;
import welding.BigContinuousGraph;
import welding.node.NodeDyStore;
import welding.relation.RelationStore;

import java.io.File;
import java.util.Iterator;

public class GraphTransImpl implements GraphTransService {

    private NodeDyStore nodeDyStore;
    private RelationStore relationStore;

    public GraphTransImpl(String dir){
        this.nodeDyStore = new NodeDyStore(new File(dir,"nodeStore").getAbsolutePath());
        this.relationStore = new RelationStore(new File(dir,"relationStore").getAbsolutePath());
    }

    @Override
    public void addNode_d(long node, long version) {
        this.nodeDyStore.addNode_d(node,version);
    }

    @Override
    public void addNode_c(long node, long version) {
        this.nodeDyStore.addNode_C(node,version);
    }

    @Override
    public void deleteNode(long node, long version) {
        this.nodeDyStore.deleteNode(node,version);
    }

    @Override
    public Iterator<Long> getNodesByVersion(long version) {
        return this.nodeDyStore.getNodesByVersion(version);
    }

    @Override
    public Iterator<Long> getNodesByVersion(long start, long end) {
        return this.nodeDyStore.getNodesByVersion(start,end);
    }

    @Override
    public void setNodeLabel(long node, String label) {
        this.nodeDyStore.setNodeLabel(node,label);
    }

    @Override
    public String getNodeLabel(long node) {
        return this.nodeDyStore.getNodeLabel(node);
    }

    @Override
    public void updateNodeProperty(long node, String key, Object value, long time) {
        this.nodeDyStore.updateEntityProperty(node, key, value, time);
    }

    @Override
    public Object getNodeProperty(long node, String key, long time) {
        return this.nodeDyStore.getEntityProperty(node, key, time);
    }

    @Override
    public Object _getNodeProperty(long node, String key, long time) {
        return this.nodeDyStore._getEntityProperty(node, key, time);
    }

    @Override
    public void addRelation_d(long startNode, long endNode, long version) {
        this.relationStore.addRelation_d(startNode, endNode, version);
    }

    @Override
    public void addRelation_c(long startNode, long endNode, long version) {
        this.relationStore.addRelation_c(startNode, endNode, version);
    }

    @Override
    public void deleteRelation(long startNode, long endNode, long version) {
        this.relationStore.deleteRelation(startNode, endNode, version);
    }

    @Override
    public Iterator<Long[]> getRelationsByVersion(long version) {
        return this.relationStore.getRelationsByVersion(version);
    }

    @Override
    public Iterator<Long[]> getRelationsByVersion(long start, long end) {
        return this.relationStore.getRelationsByVersion(start,end);
    }

    @Override
    public void setRelationType(long startNode, long endNode, String type) {
        this.relationStore.setRelationLabel(startNode, endNode, type);
    }

    @Override
    public String getRelationType(long startNode, long endNode) {
        return this.relationStore.getRelationLabel(startNode, endNode);
    }

    @Override
    public void updateRelationProperty(long startNode, long endNode, String key, Object value, long time) {
        this.relationStore.updateEntityProperty(startNode, endNode, key, value, time);
    }

    @Override
    public Object getRelationProperty(long startNode, long endNode, String key, long time) {
        return this.relationStore.getEntityProperty(startNode, endNode, key, time);
    }

    @Override
    public Object _getRelationProperty(long startNode, long endNode, String key, long time) {
        return this.relationStore._getEntityProperty(startNode, endNode, key, time);
    }
}

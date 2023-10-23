package GraphTransPro;

import welding.node.NodeDyStore;
import welding.relation.RelationDyStore;
import welding.relation.RelationStore;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

public class GraphTransProImpl implements GraphTransServicePro{

    private NodeDyStore nodeDyStore;
    private RelationDyStore relationStore;

    public GraphTransProImpl(String dir){
        this.nodeDyStore = new NodeDyStore(new File(dir,"nodeStore").getAbsolutePath());
        this.relationStore = new RelationDyStore(new File(dir,"relationStore").getAbsolutePath());
    }


    @Override
    public void addNode_d(long node, long version) {
        this.nodeDyStore.addNode_d(node,version);
    }

    @Override
    public void addNode_c(long node, long version) {
        this.nodeDyStore.addNode_C(node, version);
    }

    @Override
    public void deleteNode(long node, long version) {
        this.deleteNode(node, version);
    }

    @Override
    public Iterator<Long> getNodesByVersion(long version) {
        return this.nodeDyStore.getNodesByVersion(version);
    }

    @Override
    public Iterator<Long> getNodesByVersion(long start, long end) {
        return this.nodeDyStore.getNodesByVersion(start, end);
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
    public void addRelation_d(long relation, long startNode, long endNode, long version) {
        this.relationStore.addRelation_d(relation, startNode, endNode, version);
    }

    @Override
    public void addRelation_c(long relation, long startNode, long endNode, long version) {
        this.relationStore.addRelation_c(relation, startNode, endNode, version);
    }

    @Override
    public void addRelation_dr(long relation, long version) {
        this.relationStore.addRelation_dr(relation, version);
    }

    @Override
    public void addRelation_cr(long relation, long version) {
        this.relationStore.addRelation_cr(relation, version);
    }

    @Override
    public void deleteRelation(long relation, long startNode, long endNode, long version) {
        this.relationStore.deleteRelation(relation, startNode, endNode, version);
    }

    @Override
    public void deleteRelation_r(long relation, long version) {
        this.relationStore.deleteRelation_r(relation, version);
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
    public void setRelationType(long relation, String type) {
        this.relationStore.setRelationLabel(relation, type);
    }

    @Override
    public String getRelationType(long relation) {
        return this.relationStore.getRelationLabel(relation);
    }

    @Override
    public void updateRelationProperty(long relation, String key, Object value, long time) {
        this.relationStore.updateEntityProperty(relation, key, value, time);
    }

    @Override
    public Object getRelationProperty(long relation, String key, long time) {
        return this.relationStore.getEntityProperty(relation, key, time);
    }

    @Override
    public Object _getRelationProperty(long relation, String key, long time) {
        return this.relationStore._getEntityProperty(relation, key, time);
    }

    @Override
    public Iterator<Long> allNodes() {
        return this.nodeDyStore.AllNodes();
    }

    @Override
    public Iterator<Long[]> allRelations() {
        return this.relationStore.allRelations();
    }

    @Override
    public Iterator<Long[]> allNodesWithVersions() {
        return this.nodeDyStore.AllNodesWithVersions();
    }

    @Override
    public Iterator<Long[]> allRelationsWithVersions() {
        return this.relationStore.allRelationsWithVersions();
    }

    @Override
    public HashMap<String, HashMap<Long, Object>> getNodeProperty(long node) {
        return this.nodeDyStore.getEntityProperty(node);
    }

    @Override
    public HashMap<String, HashMap<Long, Object>> getRelationProperty(long relation) {
        return this.relationStore.getEntityProperty(relation);
    }
}

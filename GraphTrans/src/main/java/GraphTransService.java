import kernel.BasicGraph;

import java.util.Iterator;

public interface GraphTransService {

    //discrete graph

    //void storeGraph(BasicGraph<Long,Long> graph,long version);
    //BasicGraph<Long,Long> getGraph(long version);

    //continuous graph

    void addNode_d(long node,long version);
    void addNode_c(long node,long version);
    void deleteNode(long node,long version);
    Iterator<Long> getNodesByVersion(long version);
    Iterator<Long> getNodesByVersion(long start, long end);
    void setNodeLabel(long node, String label);
    String getNodeLabel(long node);
    void updateNodeProperty(long node, String key, Object value, long time);
    Object getNodeProperty(long node, String key, long time);
    Object _getNodeProperty(long node, String key, long time);

    //Iterator<Long> getNodeOutRelationsByVersion(long node, long version);
    //Iterator<Long> getNodeInRelationsByVersion(long node, long version);
    //Iterator<Long> getNodeAllRelationsByVersion(long node, long version);
    //Iterator<Long[]> getNodeAllRelations(long node);



    void addRelation_d(long startNode,long endNode,long version);
    void addRelation_c(long startNode,long endNode,long version);
    void deleteRelation(long startNode,long endNode,long version);
    Iterator<Long[]> getRelationsByVersion(long version);
    Iterator<Long[]> getRelationsByVersion(long start, long end);
    void setRelationType(long startNode,long endNode, String type);
    String getRelationType(long startNode,long endNode);
    void updateRelationProperty(long startNode,long endNode, String key, Object value, long time);
    Object getRelationProperty(long startNode,long endNode, String key, long time);
    Object _getRelationProperty(long startNode,long endNode, String key, long time);




}

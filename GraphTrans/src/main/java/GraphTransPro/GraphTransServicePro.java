package GraphTransPro;

import java.util.HashMap;
import java.util.Iterator;

public interface GraphTransServicePro {



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




    void addRelation_d(long relation, long startNode,long endNode,long version);
    void addRelation_c(long relation, long startNode,long endNode,long version);
    void addRelation_dr(long relation,long version);
    void addRelation_cr(long relation,long version);

    void deleteRelation(long relation ,long startNode,long endNode,long version);
    void deleteRelation_r(long relation ,long version);


    Iterator<Long[]> getRelationsByVersion(long version);
    Iterator<Long[]> getRelationsByVersion(long start, long end);


    void setRelationType(long relation, String type);
    String getRelationType(long relation);
    void updateRelationProperty(long relation, String key, Object value, long time);
    Object getRelationProperty(long relation, String key, long time);
    Object _getRelationProperty(long relation, String key, long time);



    Iterator<Long> allNodes();
    Iterator<Long[]> allRelations();
    Iterator<Long[]> allNodesWithVersions();
    Iterator<Long[]> allRelationsWithVersions();

    HashMap<String, HashMap<Long,Object>> getNodeProperty(long node);
    HashMap<String,HashMap<Long,Object>> getRelationProperty(long relation);


}

import kernel.BasicGraph;

import java.util.Iterator;

public interface GraphTransService {

    //discrete graph

    void storeGraph(BasicGraph<Long,Long> graph,long version);
    BasicGraph<Long,Long> getGraph(long version);

    //continuous graph

    void addNode(long node,long version);
    void deleteNode(long node,long version);
    void addRelation(long relation,long version);
    void deleteRelation(long relation,long version);

    Iterator<Long> getNodesByVersion(long version);
    Iterator<Long> getRelationsByVersion(long version);
    Iterator<Long> getNodesByVersion(long start, long end);
    Iterator<Long> getRelationsByVersion(long start, long end);

    //property

   // void updateEntityProperty(long id, String key, Object value,long time);
    //Object getEntityProperty(long id, String key, long time);



}

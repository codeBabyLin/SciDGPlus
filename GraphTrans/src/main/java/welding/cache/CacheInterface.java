package welding.cache;


import kernel.BasicGraph;

public interface CacheInterface {

    void cacheGraph(BasicGraph<Long, Long> graph, int version);
    boolean exist(int version);
    BasicGraph<Long,Long> hitGraph(int version);
}

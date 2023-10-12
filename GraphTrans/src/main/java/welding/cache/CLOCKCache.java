package welding.cache;



import kernel.BasicGraph;

import java.util.HashMap;
import java.util.Map;

public class CLOCKCache implements CacheInterface{
    private HashMap<Integer, BasicGraph<Long,Long>> graphPool;
    private int cacheSze;

    private int initScore;
    private HashMap<Integer,Integer> clock;
    public CLOCKCache(int cacheSize){
        this.cacheSze = cacheSize;
        //this.queue = new ArrayDeque<>();
        this.clock = new HashMap<>();
        this.initScore = 2;
    }

    private int findMinValueKey(HashMap<Integer,Integer> clock){
        int flagIndex = -1;
        int MinValue = Integer.MAX_VALUE;
        for(Map.Entry<Integer,Integer> pair: clock.entrySet()){
            int key = pair.getKey();
            int value = pair.getValue();
            if(value<=MinValue){
                flagIndex = key;
                MinValue = value;
            }
        }
        return flagIndex;
    }
    private void setClockTime(){
        for(Integer e: clock.keySet()){
            int value = this.clock.get(e);
            this.clock.put(e,value-1);

        }
    }
    @Override
    public void cacheGraph(BasicGraph<Long, Long> graph, int version) {
        if(graphPool.size()>=this.cacheSze){
            int v = findMinValueKey(this.clock);
            this.clock.remove(v);
            this.graphPool.remove(v);
            setClockTime();

        }
        this.graphPool.put(version,graph);
        this.clock.put(version,initScore);
    }

    @Override
    public boolean exist(int version) {
        return  this.graphPool.containsKey(version);
    }

    @Override
    public BasicGraph<Long, Long> hitGraph(int version) {
        return this.graphPool.get(version);
    }
}

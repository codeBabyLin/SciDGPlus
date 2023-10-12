package TemporalSet;

import org.gradoop.temporal.model.impl.pojo.TemporalEdge;
import org.gradoop.temporal.model.impl.pojo.TemporalVertex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class TemporalVertexSet {

    private ArrayList<TemporalVertex> vertexs;

    public TemporalVertexSet(){
        this.vertexs = new ArrayList<>();
    }
    public void add(TemporalVertex t){
        this.vertexs.add(t);
    }

    public Collection<TemporalVertex> getCollection(){
        return this.vertexs;
    }
    public Iterator<TemporalVertex> getIterator(){
        return this.vertexs.iterator();
    }

}

package TemporalSet;

import org.gradoop.temporal.model.impl.pojo.TemporalGraphHead;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class TemporalHeadSet {
    private ArrayList<TemporalGraphHead> edges;

    public TemporalHeadSet(){
        this.edges = new ArrayList<>();
    }

    public void add(TemporalGraphHead t){
        this.edges.add(t);
    }

    public Collection<TemporalGraphHead> getCollection(){
        return this.edges;
    }
    public Iterator<TemporalGraphHead> getIterator(){
        return this.edges.iterator();
    }
}

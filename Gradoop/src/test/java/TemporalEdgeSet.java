import org.gradoop.temporal.model.impl.pojo.TemporalEdge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class TemporalEdgeSet {

    private ArrayList<TemporalEdge> edges;

    public TemporalEdgeSet(){
        this.edges = new ArrayList<>();
    }

    public void add(TemporalEdge t){
        this.edges.add(t);
    }

    public Collection<TemporalEdge> getCollection(){
        return this.edges;
    }
    public Iterator<TemporalEdge> getIterator(){
        return this.edges.iterator();
    }
}

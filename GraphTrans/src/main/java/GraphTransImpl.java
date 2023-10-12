import kernel.BasicGraph;
import welding.BigContinuousGraph;

import java.util.Iterator;

public class GraphTransImpl implements GraphTransService {


    private BigContinuousGraph graph;
    private String dataPath;

    public GraphTransImpl(String dir){
        this.dataPath = dir;
        this.graph = new BigContinuousGraph(dir);
    }

    @Override
    public void storeGraph(BasicGraph<Long, Long> graphInput, long version) {
        Iterator<Long> iterNode = graphInput.AllNodes();
        while(iterNode.hasNext()){
            long node = iterNode.next();
            if(this.graph.existNode(node)){
                this.graph.deleteNode(node,version);
            }
            else{
                this.graph.addNode(node,version);
                this.graph.deleteNode(node,version);
            }
        }
        Iterator<Long> iterRelation = graphInput.AllRelations();
        while(iterRelation.hasNext()){
            long relation = iterNode.next();
            if(this.graph.existRelation(relation)){
                this.graph.deleteRelation(relation,version);
            }
            else{
                this.graph.addRelation(relation,version);
                this.graph.deleteRelation(relation,version);
            }
        }


    }

    @Override
    public BasicGraph<Long, Long> getGraph(long version) {
        BasicGraph<Long,Long> tempGraph = new BasicGraph<>();
        Iterator<Long> nodeIter = this.getNodesByVersion(version);
        while(nodeIter.hasNext()){
            long node = nodeIter.next();
            tempGraph.addNode(node);
        }

        Iterator<Long> relationIter = this.getNodesByVersion(version);
        while(relationIter.hasNext()){
            long relation = relationIter.next();
            tempGraph.addRelation(relation);
        }

        return tempGraph;
    }

    @Override
    public void addNode(long node, long version) {
        this.graph.addNode(node,version);
    }

    @Override
    public void deleteNode(long node, long version) {
        this.graph.deleteNode(node,version);
    }

    @Override
    public void addRelation(long relation, long version) {
        this.graph.addRelation(relation,version);
    }

    @Override
    public void deleteRelation(long relation, long version) {
        this.graph.deleteRelation(relation,version);
    }

    @Override
    public Iterator<Long> getNodesByVersion(long version) {
        return this.graph.AllNodesByVersion(version);
    }

    @Override
    public Iterator<Long> getRelationsByVersion(long version) {
        return this.graph.AllRelationsByVersion(version);
    }

    @Override
    public Iterator<Long> getNodesByVersion(long start, long end) {
        return this.graph.AllNodesByVersion(start,end);
    }

    @Override
    public Iterator<Long> getRelationsByVersion(long start, long end) {
        return this.graph.AllRelationsByVersion(start,end);
    }


}

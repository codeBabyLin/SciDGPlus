package OperationImpl;

import filters.AtVersionFilter;
import greycat.Callback;
import greycat.Graph;
import greycat.GraphBuilder;
import greycat.Node;
import greycat.leveldb.LevelDBStorage;
import operation.SamepleGraphFilter;
import operation.VersionGraphOperationDefaultImpl;
import org.apache.commons.lang3.tuple.Pair;
import sampleGraph.SampleGraph;

public class GreycatQueryImpl extends VersionGraphOperationDefaultImpl {

    private String dataPath;

    private Graph graph;

    private String relationType;
    public GreycatQueryImpl(String dataPath){
        this.dataPath = dataPath;
        graph = new GraphBuilder()
                .withMemorySize(1000)
                .withStorage(new LevelDBStorage(this.dataPath))
                .build();
    }

    public void setRelationType(String relationType){
        this.relationType = relationType;
    }


    @Override
    public SampleGraph querySingleVersion(SamepleGraphFilter samepleGraphFilter) {


        AtVersionFilter af = (AtVersionFilter) samepleGraphFilter.getOuterFilter();
        int version = af.getVersion();
        long world = version;
        long time = 0;
        SampleGraph sg = new SampleGraph();
        graph.connect(isConnected->{
            graph.index(world,time,"node",nodeIndex -> {
                nodeIndex.find(new Callback<Node[]>() {
                    @Override
                    public void on(Node[] nodes) {
                        for(Node n: nodes){
                            Integer nodeId = new Integer(n.get("nodeId").toString());
                            sg.addNode(nodeId,null,null);
                            n.relation(relationType, new Callback<Node[]>() {
                                @Override
                                public void on(Node[] nodes) {
                                    for(Node e: nodes){
                                        Integer nodeId1 = new Integer(e.get("nodeId").toString());
                                        sg.addRel(Pair.of(nodeId,nodeId1),null,null);
                                    }
                                }
                            });
                        }
                    }
                });
            });
        });
        return sg;
    }
}

package OperationImpl;

import filters.AtVersionFilter;
import operation.SamepleGraphFilter;
import operation.VersionGraphOperationDefaultImpl;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.chronos.chronograph.api.structure.ChronoGraph;
import sampleGraph.SampleGraph;

import java.util.List;

public class ChronoQueryImpl extends VersionGraphOperationDefaultImpl {


    private String dataBasePath;
    private ChronoGraph graph;
    public ChronoQueryImpl(String dataBasePath){
        this.dataBasePath = dataBasePath;
        graph = ChronoGraph.FACTORY.create()
                .exodusGraph(this.dataBasePath)
                .build();
    }

    @Override
    public SampleGraph querySingleVersion(SamepleGraphFilter samepleGraphFilter) {

        AtVersionFilter af = (AtVersionFilter) samepleGraphFilter.getOuterFilter();
        int version = af.getVersion();
        //GraphDatabaseService graphDb = this.graphdbs.get(version);
        //Transaction tx = graphDb.beginTx();
        String versionBranch = String.valueOf(version);

        ChronoGraph txGraph = this.graph.tx().createThreadedTx(versionBranch);
        SampleGraph sg = new SampleGraph();
        List<Vertex> nodes = txGraph.traversal().V().toList();

        nodes.forEach(node -> {
            int id = new Integer(node.property("nodeId").value().toString());
            //int id = new Integer(node.id()
            //String label = node.label();
            sg.addNode(id,null,null);
        });
        List<Edge> edges = txGraph.traversal().E().toList();
        edges.forEach(edge ->{
            Vertex start = edge.outVertex();
            Vertex end = edge.inVertex();
            int startId = new Integer(start.property("nodeId").value().toString());
            int endId = new Integer(end.property("nodeId").value().toString());
            sg.addRel(Pair.of(startId,endId),null,null);
        });

        txGraph.close();

        return sg;
    }


}

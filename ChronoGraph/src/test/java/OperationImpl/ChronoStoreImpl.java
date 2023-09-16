package OperationImpl;

import operation.VersionGraphStore;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.chronos.chronodb.api.BranchManager;
import org.chronos.chronograph.api.branch.ChronoGraphBranchManager;
import org.chronos.chronograph.api.structure.ChronoGraph;
import sampleGraph.SampleGraph;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ChronoStoreImpl implements VersionGraphStore {

    private String storePath;
    private ChronoGraph graph;

    private ChronoGraphBranchManager branchManager;


    public ChronoStoreImpl(String storePath){
        this.storePath = storePath;

        if(!new File(storePath).exists()){
            new File(storePath).mkdirs();
        }

    }

    @Override
    public void begin() {
        graph = ChronoGraph.FACTORY.create()
                .exodusGraph(this.storePath)
                .build();
        branchManager = graph.getBranchManager();

        //branchManager.createBranch("master","1");
       /* ChronoGraphIndexManager indexManager = graph.getIndexManager();
        indexManager.create().stringIndex().onVertexProperyt("type").build();
        indexManager.create().stringIndex().onVertexProperyt("firstName").build();
        indexManager.create().stringIndex().onVertexProperyt("lastName").build();
        indexManager.reindexAll();*/
    }

    @Override
    public void storeGraph(SampleGraph sampleGraph, int version) {
        String versionBranch = String.valueOf(version);
        branchManager.createBranch("master",versionBranch);
        ChronoGraph txGraph = graph.tx().createThreadedTx(versionBranch);
        for(Integer e: sampleGraph.getNodes()){
            String label = sampleGraph.getNodeLabel(e);
            Vertex n = txGraph.addVertex(T.id,e.toString(),T.label,label);
            HashMap<String,Object> nodeProperty = sampleGraph.getNodeProperties().get(e);
            n.property("nodeId",e);
            for(Map.Entry<String,Object> p: nodeProperty.entrySet()){
                n.property(p.getKey(),p.getValue());
            }
        }
        for(Pair<Integer,Integer> pais: sampleGraph.getRels()) {
            Vertex start = txGraph.vertex(pais.getLeft().toString());
            Vertex end = txGraph.vertex(pais.getRight().toString());
            String type = sampleGraph.getRelationType(pais);
            start.addEdge(type,end);
        }
        String msg = String.format("Added version: %d",version);
        txGraph.tx().commit(msg);


    }

    @Override
    public void finish() {
        this.graph.close();
    }
}

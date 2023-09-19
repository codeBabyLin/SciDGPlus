package OperationImpl;

import greycat.Graph;
import greycat.GraphBuilder;
import greycat.Node;
import greycat.Type;
import greycat.leveldb.LevelDBStorage;
import operation.VersionGraphStore;
import org.apache.commons.lang3.tuple.Pair;
import org.neo4j.cypher.internal.frontend.v3_1.ast.functions.Has;
import sampleGraph.SampleGraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class GreycatStoreImpl implements VersionGraphStore {

    private String dataPath;

    private Graph graph;

    public GreycatStoreImpl(String dataPath){
        this.dataPath = dataPath;
    }

    @Override
    public void begin() {
        graph = new GraphBuilder()
                .withMemorySize(1000)
                .withStorage(new LevelDBStorage(this.dataPath))
                .build();
    }

    @Override
    public void storeGraph(SampleGraph sampleGraph, int version) {

        long world = 0;
        long time = version ;
        HashMap<Integer,Node> nodes = new HashMap<>();

        HashSet<Integer> temporalIds = new HashSet<>();
        for(Integer es : sampleGraph.getNodes()) {

            temporalIds.add(es);
            if(temporalIds.size()>=10){
                graph.connect(isConnected-> {
                    HashSet<Node> tempNodes = new HashSet<>();
                    for(Integer e: temporalIds) {
                        String label = sampleGraph.getNodeLabel(e);
                        Node n = graph.newNode(world, time);
                        n.set("nodeId", Type.INT, e);
                        n.set("type", Type.STRING, label);
                        HashMap<String, Object> nodeProperty = sampleGraph.getNodeProperties().get(e);
                        for (Map.Entry<String, Object> p : nodeProperty.entrySet()) {
                            n.set(p.getKey(), Type.STRING, p.getValue().toString());
                        }
                        nodes.put(e,n);
                        tempNodes.add(n);
                        graph.index(world, time, "node", nodeIndex -> {
                            for(Node n1: tempNodes){
                                nodeIndex.addToIndex(n1);
                            }
                        });
                        graph.save(res->{});
                        //tempNodes.clear();
                    }

                });
                //graph.disconnect(res->{});
                temporalIds.clear();
            }

        }

        /*graph.connect(isConnected-> {
            HashSet<Node> tempNodes = new HashSet<>();
            for(Integer e : sampleGraph.getNodes()){
                String label = sampleGraph.getNodeLabel(e);
                Node n = graph.newNode(world,time);
                n.set("nodeId",Type.INT,e);
                n.set("type",Type.STRING,label);
                HashMap<String,Object> nodeProperty = sampleGraph.getNodeProperties().get(e);
                for(Map.Entry<String,Object> p: nodeProperty.entrySet()){
                    n.set(p.getKey(),Type.STRING,p.getValue().toString());
                }
                nodes.put(e,n);
                tempNodes.add(n);
                if(tempNodes.size()>=100){
                    graph.index(world, time, "node", nodeIndex -> {
                        for(Node n1: tempNodes){
                            nodeIndex.addToIndex(n1);
                        }
                    });
                    graph.save(res->{});
                    tempNodes.clear();
                }
                //nodeIndex.addToIndex(n);
            }
            *//*graph.index(world, time, "node", nodeIndex -> {
                for(Integer e : sampleGraph.getNodes()){
                    String label = sampleGraph.getNodeLabel(e);
                    Node n = graph.newNode(world,time);
                    n.set("nodeId",Type.INT,e);
                    n.set("type",Type.STRING,label);
                    HashMap<String,Object> nodeProperty = sampleGraph.getNodeProperties().get(e);
                    for(Map.Entry<String,Object> p: nodeProperty.entrySet()){
                        n.set(p.getKey(),Type.STRING,p.getValue().toString());
                    }
                    nodes.put(e,n);
                    nodeIndex.addToIndex(n);
                }
                for(Pair<Integer,Integer> pais: sampleGraph.getRels()) {
                    Node start = nodes.get(pais.getLeft());
                    Node end = nodes.get(pais.getRight());
                    String type = sampleGraph.getRelationType(pais);
                    start.addToRelation(type,end);
                }
                graph.save(res -> {
                });
            });*//*
        });*/
        //graph.disconnect(fg->{});
    }




    @Override
    public void finish() {

    }
}

package dataProvide.dppin;

import dataConfig.DataStore;
import operation.VersionGraphStore;
import org.apache.commons.lang3.tuple.Pair;
import sampleGraph.SampleGraph;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class DPPINDataStore implements DataStore {

    String path = System.getProperty("user.dir")+"\\DPPIN";
    String Node_Labels_Name = path+"\\Node_Labels.csv";
    String DPPIN_Babu = path+"\\DPPIN-Babu";
    String DPPIN_Breitkreutz = path+"\\DPPIN-Breitkreutz";
    String DPPIN_Gavin= path+"\\DPPIN-Gavin";
    String DPPIN_Hazbun = path+"\\DPPIN-Hazbun";
    String DPPIN_Ho = path+"\\DPPIN-Ho";
    String DPPIN_Ito = path+"\\DPPIN-Ito";
    String DPPIN_Krogan_LCMS = path+"\\DPPIN-Krogan(LCMS)";
    String DPPIN_Krogan_MALDI = path+"\\DPPIN-Krogan(MALDI)";
    String DPPIN_Lambert = path+"\\DPPIN-Lambert";
    String DPPIN_Tarassov = path+"\\DPPIN-Tarassov";
    String DPPIN_Uetz = path+"\\DPPIN-Uetz";
    String DPPIN_Yu = path+"\\DPPIN-Yu";
    String Dynamic_File_Name = "Dynamic_PPIN.txt";
    String Node_Features_Name = "Node_Features.txt";
    String Static_File_name  = "Static_PPIN.txt";

    private String datasetName = DPPIN_Gavin;

    public void SetDatasetName(String datasetName){
        this.datasetName = datasetName;
    }
    public HashMap<String,String> readNodeLabelsName(){
        File file = new File(Node_Labels_Name);
        HashMap<String,String> nodes_labels = new HashMap<>();
        try {
            RandomAccessFile fp = new RandomAccessFile(file, "r");
            String str;
            while ((str = fp.readLine()) != null) {
                if(str.contains("Label")) {
                    continue;
                }
                else{
                    String [] nodelabel = str.split(",");
                    nodes_labels.put(nodelabel[0],nodelabel[1]);
                }
            }
            fp.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return nodes_labels;
    }

    public HashMap<Pair<Integer,Integer>, Edge> readDynamic_PPIN(String baseDir){
        File file = new File(baseDir,Dynamic_File_Name);
        HashMap<Pair<Integer,Integer>, Edge> rels = new HashMap<>();
        String type = "interaction";
        try {
            RandomAccessFile fp = new RandomAccessFile(file, "r");
            String str;
            while ((str = fp.readLine()) != null) {

                String [] edges = str.split(",");
                int startNodeId = new Integer(edges[0]);
                int endNodeId = new Integer(edges[1]);
                int version = new Integer(edges[2]);
                float weight = new Float(edges[3]);
                Edge edge = new Edge();
                edge.setStartNode(startNodeId);
                edge.setEndNode(endNodeId);
                edge.setVersion(version+1);
                edge.setType(type);
                edge.setWeight(weight);
                rels.put(Pair.of(startNodeId,endNodeId),edge);
                str = String.format("startNode:%d endNode:%d version:%d weight:%f",
                        startNodeId,endNodeId,version+1,weight);
                System.out.println(str);
            }
            fp.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  rels;
    }

    public HashMap<Integer, Entity> readNodeFeatures(String baseDir){
        File file = new File(baseDir,Node_Features_Name);
        HashMap<String,String> nodeLabels = readNodeLabelsName();
        HashMap<Integer, Entity> nodes = new HashMap<>();
        try {
            RandomAccessFile fp = new RandomAccessFile(file, "r");
            HashMap<Integer,Float> values = new HashMap<>();
            String str;
            while ((str = fp.readLine()) != null) {
                values.clear();
                String [] edges = str.split(",");
                int nodeId = new Integer(edges[0]);
                String nodeName = edges[1];

                for(int i =1;i<=36;i++){
                    values.put(i,new Float(edges[i+1]));
                }
                Entity node = new Entity();
                node.setNodeId(nodeId);
                node.setNodeName(nodeName);
                node.setValues(values);
                node.setLabel(nodeLabels.get(nodeName));
                nodes.put(nodeId,node);
                str = String.format("nodeId:%d nodeName:%s valuesSize:%d  label:%s",nodeId,nodeName,values.size(),node.getLabel());

                System.out.println(str);

            }
            fp.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return nodes;
    }


    HashMap<Integer, SampleGraph> getVersionGraph(){
        HashMap<Pair<Integer,Integer>, Edge> rels = readDynamic_PPIN(this.datasetName);
        HashMap<Integer, Entity> nodes = readNodeFeatures(this.datasetName);
        HashMap<Integer, HashSet<Pair<Integer,Integer>>> versionRels = new HashMap<>();
        HashMap<Integer, HashMap<Integer,Float>> versionNodes = new HashMap<>();

        rels.forEach((integerIntegerPair, edge) -> {
            int version = edge.getVersion();
            versionRels.computeIfAbsent(version, k -> new HashSet<>());
            versionRels.get(version).add(integerIntegerPair);
        });
        nodes.forEach((integer, entity) -> {
            entity.getValues().forEach((integer1, aFloat) -> {
                if(versionNodes.get(integer1) == null){
                    versionNodes.put(integer1,new HashMap<>());
                }
                versionNodes.get(integer1).put(integer,aFloat);
            });
        });

        HashMap<Integer, SampleGraph> vGraph = new HashMap<>();

        for(HashMap.Entry<Integer, HashMap<Integer,Float>> kv: versionNodes.entrySet()){
            int version = kv.getKey();
            HashMap<Integer,Float> nodesValues  = kv.getValue();
            HashSet<Pair<Integer,Integer>> resIds = versionRels.get(version);
            SampleGraph sg = new SampleGraph();
            for(Integer e: nodesValues.keySet()){
                String label = nodes.get(e).getLabel();
                HashMap<String,Object> p = new HashMap<>();
                float value = nodesValues.get(e);
                p.put("value",value);
                sg.addNode(e,label,p);
            }
            for(Pair<Integer,Integer> rel: resIds){
                String type = rels.get(rel).getType();
                float weight = rels.get(rel).getWeight();
                HashMap<String,Object> p = new HashMap<>();
                p.put("weight",weight);
                sg.addRel(rel,type,p);
            }
            vGraph.put(version,sg);
        }
        return vGraph;
    }

    @Override
    public void storeVersionGraph(VersionGraphStore graphDb) {

        HashMap<Integer, SampleGraph> versionGraph = getVersionGraph();
        graphDb.begin();
        for(Map.Entry<Integer,SampleGraph> kv: versionGraph.entrySet()){
            graphDb.storeGraph(kv.getValue(), kv.getKey());
        }
        graphDb.finish();
    }
}

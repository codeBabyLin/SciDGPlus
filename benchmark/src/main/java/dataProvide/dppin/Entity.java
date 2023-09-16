package dataProvide.dppin;

import java.util.HashMap;

public class Entity {
    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getLabel() {
        if(label == null){
            return "unknow";
        }
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public HashMap<Integer,Float> getValues() {
        return values;
    }

    public void setValues(HashMap<Integer,Float> values) {
        this.values = values;
    }

    private int nodeId;
    private String nodeName;
    private String label;

    public void setVersionValue(int version,float value){
        this.values.put(version,value);
    }

    public float getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(float currentValue) {
        this.currentValue = currentValue;
    }

    private float currentValue;
    private HashMap<Integer,Float> values = new HashMap<>();
    public Entity(){

    }

}

package welding.node;

import Common.Transformer;
import welding.IdVersionStore;
import welding.property.PropertyDyStore;
import welding.type.TypeStore;

import java.io.File;
import java.util.Iterator;

public class NodeDyStore {
    private IdVersionStore nodeIdCStore;
    private IdVersionStore nodeIdDStore;
    private Transformer transformer;
    private TypeStore labelStore;
    private PropertyDyStore nodePropertyStore;
    public NodeDyStore(String dir){
        this.nodeIdCStore = new IdVersionStore(new File(dir,"nodeIdCreate").getAbsolutePath());
        this.nodeIdDStore = new IdVersionStore(new File(dir,"nodeIdDelete").getAbsolutePath());
        this.labelStore = new TypeStore(new File(dir,"nodeLabel").getAbsolutePath());
        this.nodePropertyStore = new PropertyDyStore(new File(dir,"nodeProperty").getAbsolutePath());
        this.transformer = new Transformer();
    }

    public boolean existNode(long node){
        return this.nodeIdCStore.exist(transformer.LongToByte(node));
    }

    public void addNode_d(long node, long version) {

        if(existNode(node)){
            this.deleteNode(node,version);
        }
        else {
            byte[] key = transformer.LongToByte(node);
            byte[] cValue = transformer.LongToByte(version);
            //byte[] dValue = transformer.LongToByte(Long.MAX_VALUE);
            byte[] dValue = transformer.LongToByte(version);
            this.nodeIdCStore.add(key, cValue);
            this.nodeIdDStore.add(key, dValue);
        }
    }
    public void addNode_C(long node, long version) {
        byte[] key = transformer.LongToByte(node);
        byte[] cValue = transformer.LongToByte(version);
        byte[] dValue = transformer.LongToByte(Long.MAX_VALUE);
        //byte[] dValue = transformer.LongToByte(version);
        this.nodeIdCStore.add(key, cValue);
        this.nodeIdDStore.add(key, dValue);
    }

    public Iterator<Long> AllNodes(){
        Iterator<byte[]> iter = this.nodeIdCStore.all();
        return new Iterator<Long>() {
            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public Long next() {
                byte[]data = iter.next();
                long e = transformer.ByteToLong(data);
                return e;
            }
        };
    }


    public void deleteNode(long node,long version){
        byte[]key = transformer.LongToByte(node);
        byte[]dValue = transformer.LongToByte(version);
        this.nodeIdDStore.add(key,dValue);
    }

    public long getNodeCreateVersion(long node){
        byte[]key = transformer.LongToByte(node);
        byte[]value = this.nodeIdCStore.get(key);
        long v = transformer.ByteToLong(value);
        return v;
    }
    public long getNodeDeleteVersion(long node){
        byte[]key = transformer.LongToByte(node);
        byte[]value = this.nodeIdDStore.get(key);
        long v = transformer.ByteToLong(value);
        return v;
    }


    private boolean isOk(long v, long s, long e){
        return s <= v && (v <= e);
    }
    private boolean isOk(long vs,long ve,long ds,long de){
        return ds<=vs && (ve<=de);
    }

    public void setNodeCreateVersion(byte[]key, byte[]value){
        this.nodeIdCStore.add(key,value);
    }
    public void setNodeDeleteVersion(byte[]key, byte[]value){
        this.nodeIdDStore.add(key,value);
    }

    public Iterator<Long> getNodesByVersion(long version) {
        Iterator<byte[]> iter = this.nodeIdCStore.all();
        return new Iterator<Long>() {
            long temp;
            @Override
            public boolean hasNext() {
                if(iter.hasNext()){
                    boolean isCan = false;
                    while(!isCan&& iter.hasNext()){
                        byte[]key = iter.next();
                        byte[]cValue = nodeIdCStore.get(key);
                        byte[]dValue = nodeIdDStore.get(key);
                        long e = transformer.ByteToLong(key);
                        long vs = transformer.ByteToLong(cValue);
                        long ve = transformer.ByteToLong(dValue);
                        if(isOk(version,vs,ve)){
                            temp = e;
                            isCan = true;
                        }
                    }

                    return isCan;

                }
                else{
                    return false;
                }
            }

            @Override
            public Long next() {
                return temp;
            }
        };
    }

    public Iterator<Long> getNodesByVersion(long vStart, long vEnd) {
        Iterator<byte[]> iter = this.nodeIdCStore.all();
        return new Iterator<Long>() {
            long temp;
            @Override
            public boolean hasNext() {
                if(iter.hasNext()){
                    boolean isCan = false;
                    while(!isCan&& iter.hasNext()){
                        byte[]key = iter.next();
                        byte[]cValue = nodeIdCStore.get(key);
                        byte[]dValue = nodeIdDStore.get(key);
                        long e = transformer.ByteToLong(key);
                        long vs = transformer.ByteToLong(cValue);
                        long ve = transformer.ByteToLong(dValue);
                        if(isOk(vStart,vEnd,vs,ve)){
                            temp = e;
                            isCan = true;
                        }
                    }

                    return isCan;

                }
                else{
                    return false;
                }
            }

            @Override
            public Long next() {
                return temp;
            }
        };
    }


    public void setNodeLabel(long node, String label){
        this.labelStore.setEntityType(node,label);
    }

    public String getNodeLabel(long node){
        return this.labelStore.getEntityType(node);
    }

    public void updateEntityProperty(long id, String key, Object value,long time){
        this.nodePropertyStore.updateEntityProperty(id,key,value,time);
    }
    public Object getEntityProperty(long id, String key, long time){
        return this.nodePropertyStore.getEntityProperty(id,key,time);
    }
    public Object _getEntityProperty(long id, String key, long time){
        return this.nodePropertyStore._getEntityProperty(id,key,time);
    }


}

package welding.relation;

import Common.Transformer;
import welding.IdVersionStore;
import welding.property.PropertyDyStore;
import welding.type.TypeStore;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

public class RelationStore {
    private IdVersionStore relationInIdCStore;
    private IdVersionStore relationInIdDStore;

    private IdVersionStore relationOutIdCStore;
    private IdVersionStore relationOutIdDStore;

    private Transformer transformer;
    private TypeStore typeStore;
    private PropertyDyStore relationPropertyStore;

    public RelationStore(String dir){
        this.relationInIdCStore = new IdVersionStore(new File(dir,"relationInIdCreate").getAbsolutePath());
        this.relationInIdDStore = new IdVersionStore(new File(dir,"relationInIdDelete").getAbsolutePath());
        this.relationOutIdCStore = new IdVersionStore(new File(dir,"relationOutIdCreate").getAbsolutePath());
        this.relationOutIdDStore = new IdVersionStore(new File(dir,"relationOutIdDelete").getAbsolutePath());
        this.typeStore = new TypeStore(new File(dir,"relationType").getAbsolutePath());
        this.relationPropertyStore = new PropertyDyStore(new File(dir,"relationProperty").getAbsolutePath());
        this.transformer = new Transformer();
    }

    public boolean existRelation(long start, long end){
        byte[]data1 = transformer.LongToByte(start);
        byte[]data2 = transformer.LongToByte(end);
        byte[]data = transformer.concatByteArray(data1,data2);
        return this.relationOutIdCStore.exist(data);
    }
    public boolean existRelation(byte[]relaiton){
        //byte[]data1 = transformer.LongToByte(start);
        //byte[]data2 = transformer.LongToByte(end);
        //byte[]data = transformer.concatByteArray(data1,data2);
        return this.relationOutIdCStore.exist(relaiton);
    }

    public void addRelation_d(long start, long end,long version){
        byte[]data1 = transformer.LongToByte(start);
        byte[]data2 = transformer.LongToByte(end);
        byte[]outRelation = transformer.concatByteArray(data1,data2);
        byte[]inRelation = transformer.concatByteArray(data2,data1);

        if(existRelation(outRelation)){
            this.deleteRelation(start, end, version);
        }
        else {
            //byte[] key = relation;
            byte[] cValue = transformer.LongToByte(version);
            //byte[] dValue = transformer.LongToByte(Long.MAX_VALUE);
            byte[] dValue = transformer.LongToByte(version);
            this.relationInIdCStore.add(inRelation, cValue);
            this.relationInIdDStore.add(inRelation, dValue);
            this.relationOutIdCStore.add(outRelation, cValue);
            this.relationOutIdDStore.add(outRelation, dValue);
        }
    }

    public void addRelation_c(long start, long end,long version){

        byte[]data1 = transformer.LongToByte(start);
        byte[]data2 = transformer.LongToByte(end);
        byte[]outRelation = transformer.concatByteArray(data1,data2);
        byte[]inRelation = transformer.concatByteArray(data2,data1);
       // byte[]key = transformer.LongToByte(relation);
        byte[]cValue = transformer.LongToByte(version);
        byte[]dValue = transformer.LongToByte(Long.MAX_VALUE);
        this.relationInIdCStore.add(inRelation, cValue);
        this.relationInIdDStore.add(inRelation, dValue);
        this.relationOutIdCStore.add(outRelation, cValue);
        this.relationOutIdDStore.add(outRelation, dValue);
    }
    public void deleteRelation(long start, long end,long version){
        byte[]data1 = transformer.LongToByte(start);
        byte[]data2 = transformer.LongToByte(end);
        byte[]outRelation = transformer.concatByteArray(data1,data2);
        byte[]inRelation = transformer.concatByteArray(data2,data1);

        //byte[]key = transformer.LongToByte(relation);
        byte[]dValue = transformer.LongToByte(version);
        this.relationInIdDStore.add(inRelation,dValue);
        this.relationOutIdDStore.add(outRelation,dValue);
    }
   /* public void deleteRelation( byte[]key ,long version){
        //byte[]key = transformer.LongToByte(relation);
        byte[]dValue = transformer.LongToByte(version);
        this.relationIdDStore.add(key,dValue);
    }*/

    public Iterator<Long[]> AllRelations(){
        Iterator<byte[]> iter = this.relationOutIdCStore.all();
        return new Iterator<Long[]>() {
            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public Long[] next() {
                byte[]data = iter.next();
                Long []e = transformer.ByteToVersionLong(data);
                return e;
            }
        };
    }

    public Iterator<Long[]> AllRelationsWithVersion(){
        Iterator<byte[]> iter = this.relationOutIdCStore.all();
        return new Iterator<Long[]>() {
            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public Long[] next() {
                byte[]data = iter.next();
                Long []e = transformer.ByteToVersionLong(data);
                long start = transformer.ByteToLong(relationOutIdCStore.get(data));
                long end = transformer.ByteToLong(relationOutIdDStore.get(data));
                return new Long[]{e[0],e[1],start,end};
            }
        };
    }

    public long getRelationCreateVersion(long start, long end){
        byte[]data1 = transformer.LongToByte(start);
        byte[]data2 = transformer.LongToByte(end);
        byte[]outRelation = transformer.concatByteArray(data1,data2);
        //byte[]inRelation = transformer.concatByteArray(data2,data1);

        //byte[]key = transformer.LongToByte(relation);
        byte[]value = this.relationOutIdCStore.get(outRelation);
        long v = transformer.ByteToLong(value);
        return v;
    }
    public long getRelationDeleteVersion(long start, long end){
        byte[]data1 = transformer.LongToByte(start);
        byte[]data2 = transformer.LongToByte(end);
        byte[]outRelation = transformer.concatByteArray(data1,data2);
        //byte[]key = transformer.LongToByte(relation);
        byte[]value = this.relationOutIdDStore.get(outRelation);
        long v = transformer.ByteToLong(value);
        return v;
    }

   /* public void setRelationCreateVersion(byte[]key, byte[]value){
        this.relationIdCStore.add(key,value);
    }
    public void setRelationDeleteVersion(byte[]key, byte[]value){
        this.relationIdDStore.add(key,value);
    }*/
    private boolean isOk(long v, long s, long e){
        return s <= v && (v <= e);
    }
    private boolean isOk(long vs,long ve,long ds,long de){
        return ds<=vs && (ve<=de);
    }
    public Iterator<Long[]> getRelationsByVersion(Long version){
        Iterator<byte[]> iter = this.relationOutIdCStore.all();
        return new Iterator<Long[]>() {
            Long[] temp;
            @Override
            public boolean hasNext() {
                if(iter.hasNext()){
                    boolean isCan = false;
                    while(!isCan&& iter.hasNext()) {
                        byte[] key = iter.next();
                        byte[] cValue = relationOutIdCStore.get(key);
                        byte[] dValue = relationOutIdDStore.get(key);
                        Long []e = transformer.ByteToVersionLong(key);
                        long vs = transformer.ByteToLong(cValue);
                        long ve = transformer.ByteToLong(dValue);
                        if (isOk(version, vs, ve)) {
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
            public Long[] next() {
                return temp;
            }
        };
    }

    public Iterator<Long[]> getRelationsByVersion(Long vStart,Long vEnd) {
        Iterator<byte[]> iter = this.relationOutIdCStore.all();
        return new Iterator<Long[]>() {
            Long[] temp;

            @Override
            public boolean hasNext() {
                if (iter.hasNext()) {
                    boolean isCan = false;
                    while (!isCan && iter.hasNext()) {
                        byte[] key = iter.next();
                        byte[] cValue = relationOutIdCStore.get(key);
                        byte[] dValue = relationOutIdDStore.get(key);
                        Long []e = transformer.ByteToVersionLong(key);
                        long vs = transformer.ByteToLong(cValue);
                        long ve = transformer.ByteToLong(dValue);
                        if (isOk(vStart, vEnd, vs, ve)) {
                            temp = e;
                            isCan = true;
                        }
                    }
                    return isCan;
                } else {
                    return false;
                }
            }

            @Override
            public Long[] next() {
                return temp;
            }
        };

    }




    public void setRelationLabel(long start, long end, String label){

        byte[]data1 = transformer.LongToByte(start);
        byte[]data2 = transformer.LongToByte(end);
        byte[]node = transformer.concatByteArray(data1,data2);
        this.typeStore.setEntityType(node,label);
    }

    public String getRelationLabel(long start, long end){
        byte[]data1 = transformer.LongToByte(start);
        byte[]data2 = transformer.LongToByte(end);
        byte[]node = transformer.concatByteArray(data1,data2);
        return this.typeStore.getEntityType(node);
    }

    public void updateEntityProperty(long start, long end, String key, Object value,long time){
        byte[]data1 = transformer.LongToByte(start);
        byte[]data2 = transformer.LongToByte(end);
        byte[]id = transformer.concatByteArray(data1,data2);
        this.relationPropertyStore.updateEntityProperty(id,key,value,time);
    }
    public Object getEntityProperty(long start, long end, String key, long time){
        byte[]data1 = transformer.LongToByte(start);
        byte[]data2 = transformer.LongToByte(end);
        byte[]id = transformer.concatByteArray(data1,data2);
        return this.relationPropertyStore.getEntityProperty(id,key,time);
    }
    public Object _getEntityProperty(long start, long end, String key, long time){
        byte[]data1 = transformer.LongToByte(start);
        byte[]data2 = transformer.LongToByte(end);
        byte[]id = transformer.concatByteArray(data1,data2);
        return this.relationPropertyStore._getEntityProperty(id,key,time);
    }

    public HashMap<String, HashMap<Long,Object>> getEntityProperty(long start, long end){
        byte[]data1 = transformer.LongToByte(start);
        byte[]data2 = transformer.LongToByte(end);
        byte[]id = transformer.concatByteArray(data1,data2);
        return this.relationPropertyStore.getEntityAllProperty(id);
    }

}

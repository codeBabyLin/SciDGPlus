package welding.relation;

import Common.Transformer;
import welding.IdVersionStore;
import welding.property.PropertyDyStore;
import welding.type.TypeStore;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

public class RelationDyStore {

    private IdVersionStore relationIdCStore;// key id start end
    private IdVersionStore relationIdDStore;// key id start end

    private Transformer transformer;
    private TypeStore typeStore;
    private PropertyDyStore relationPropertyStore;

    private IdVersionStore relationIdGroupStore;



    public RelationDyStore(String dir){
        this.relationIdCStore = new IdVersionStore(new File(dir,"relationIdCreate").getAbsolutePath());
        this.relationIdDStore = new IdVersionStore(new File(dir,"relationIdDelete").getAbsolutePath());
        this.relationIdGroupStore = new IdVersionStore(new File(dir,"relationIdGroupStore").getAbsolutePath());
        this.typeStore = new TypeStore(new File(dir,"relationType").getAbsolutePath());
        this.relationPropertyStore = new PropertyDyStore(new File(dir,"relationProperty").getAbsolutePath());
        this.transformer = new Transformer();
    }

    public boolean existRelation(long relation){
        return this.relationIdCStore.exist(transformer.LongToByte(relation));
    }

    public boolean existRelation(long relation, long startNode, long endNode){

        return this.relationIdGroupStore.exist(transformer.LongToByte(relation));
    }

    public void addRelation_dr(long relation,long version){
        if(existRelation(relation)){
            this.deleteRelation_r(relation,version);
        }
        else {
            byte[] key = transformer.LongToByte(relation);
            byte[] cValue = transformer.LongToByte(version);
            //byte[] dValue = transformer.LongToByte(Long.MAX_VALUE);
            byte[] dValue = transformer.LongToByte(version);
            this.relationIdCStore.add(key, cValue);
            this.relationIdDStore.add(key, dValue);
        }
    }


    public void addRelation_d(long relation,long startNode,long endNode, long version){
        if(existRelation(relation,startNode,endNode)){
            this.deleteRelation(relation,startNode,endNode,version);
        }
        else {
            byte[] key = transformer.LongToByte(relation);
            byte[] cValue = transformer.LongToByte(version);
            //byte[] dValue = transformer.LongToByte(Long.MAX_VALUE);
            byte[]data1 = transformer.LongToByte(startNode);
            byte[]data2 = transformer.LongToByte(endNode);
            byte[]data3 = transformer.concatByteArray(data1,data2);


            byte[] dValue = transformer.LongToByte(version);
            this.relationIdCStore.add(key, cValue);
            this.relationIdDStore.add(key, dValue);
            this.relationIdGroupStore.add(key,data3);

           // byte[]data1 = transformer.LongToByte(startNode);
           // byte[]data2 = transformer.LongToByte(endNode);
        }
    }

    public void addRelation_c(long relation,long startNode,long endNode,long version){
        byte[]key = transformer.LongToByte(relation);
        byte[]cValue = transformer.LongToByte(version);
        byte[]dValue = transformer.LongToByte(Long.MAX_VALUE);

        byte[]data1 = transformer.LongToByte(startNode);
        byte[]data2 = transformer.LongToByte(endNode);
        byte[]data3 = transformer.concatByteArray(data1,data2);

        this.relationIdCStore.add(key,cValue);
        this.relationIdDStore.add(key,dValue);
        this.relationIdGroupStore.add(key,data3);
    }
    public void addRelation_cr(long relation,long version){
        byte[]key = transformer.LongToByte(relation);
        byte[]cValue = transformer.LongToByte(version);
        byte[]dValue = transformer.LongToByte(Long.MAX_VALUE);
        this.relationIdCStore.add(key,cValue);
        this.relationIdDStore.add(key,dValue);
    }

    public void deleteRelation(long relation,long start,long end,long version){
        byte[]key = transformer.LongToByte(relation);
        byte[]dValue = transformer.LongToByte(version);
        this.relationIdDStore.add(key,dValue);
    }
    public void deleteRelation_r(long relation,long version){
        byte[]key = transformer.LongToByte(relation);
        byte[]dValue = transformer.LongToByte(version);
        this.relationIdDStore.add(key,dValue);
    }
    public void deleteRelation( byte[]key ,long version){
        //byte[]key = transformer.LongToByte(relation);
        byte[]dValue = transformer.LongToByte(version);
        this.relationIdDStore.add(key,dValue);
    }

    public Iterator<Long> AllRelations(){
        Iterator<byte[]> iter = this.relationIdCStore.all();
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
    public Iterator<Long[]> allRelations(){
        Iterator<byte[]> iter = this.relationIdCStore.all();
        return new Iterator<Long[]>() {
            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public Long[] next() {
                byte[]data = iter.next();
                long e = transformer.ByteToLong(data);
                byte[]ids = relationIdGroupStore.get(data);
                long [] idArray = transformer.ByteToVersion(ids);
                return new Long[]{e,idArray[0],idArray[1]};
            }
        };
    }
    public Iterator<Long[]> allRelationsWithVersions(){
        Iterator<byte[]> iter = this.relationIdCStore.all();
        return new Iterator<Long[]>() {
            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public Long[] next() {
                byte[]data = iter.next();
                long startVersion = transformer.ByteToLong(relationIdCStore.get(data));
                long endVersion = transformer.ByteToLong(relationIdDStore.get(data));
                long e = transformer.ByteToLong(data);
                byte[]ids = relationIdGroupStore.get(data);
                long [] idArray = transformer.ByteToVersion(ids);
                return new Long[]{e,idArray[0],idArray[1],startVersion,endVersion};
            }
        };
    }


    public long getRelationCreateVersion(long relation){
        byte[]key = transformer.LongToByte(relation);
        byte[]value = this.relationIdCStore.get(key);
        long v = transformer.ByteToLong(value);
        return v;
    }
    public long getRelationDeleteVersion(long relation){
        byte[]key = transformer.LongToByte(relation);
        byte[]value = this.relationIdDStore.get(key);
        long v = transformer.ByteToLong(value);
        return v;
    }

    public void setRelationCreateVersion(byte[]key, byte[]value){
        this.relationIdCStore.add(key,value);
    }
    public void setRelationDeleteVersion(byte[]key, byte[]value){
        this.relationIdDStore.add(key,value);
    }
    private boolean isOk(long v, long s, long e){
        return s <= v && (v <= e);
    }
    private boolean isOk(long vs,long ve,long ds,long de){
        return ds<=vs && (ve<=de);
    }
    public Iterator<Long[]> getRelationsByVersion(Long version){
        Iterator<byte[]> iter = this.relationIdCStore.all();
        return new Iterator<Long[]>() {
            Long[] temp;
            @Override
            public boolean hasNext() {
                if(iter.hasNext()){
                    boolean isCan = false;
                    while(!isCan&& iter.hasNext()) {
                        byte[] key = iter.next();
                        byte[] cValue = relationIdCStore.get(key);
                        byte[] dValue = relationIdDStore.get(key);
                        long e = transformer.ByteToLong(key);
                        long vs = transformer.ByteToLong(cValue);
                        long ve = transformer.ByteToLong(dValue);
                        if (isOk(version, vs, ve)) {

                            byte[] nodes = relationIdGroupStore.get(key);
                            long[] nodeIds = transformer.ByteToVersion(nodes);
                            temp = new Long[]{e,nodeIds[0],nodeIds[1]};
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
        Iterator<byte[]> iter = this.relationIdCStore.all();
        return new Iterator<Long[]>() {
            Long[] temp;

            @Override
            public boolean hasNext() {
                if (iter.hasNext()) {
                    boolean isCan = false;
                    while (!isCan && iter.hasNext()) {
                        byte[] key = iter.next();
                        byte[] cValue = relationIdCStore.get(key);
                        byte[] dValue = relationIdDStore.get(key);
                        long e = transformer.ByteToLong(key);
                        long vs = transformer.ByteToLong(cValue);
                        long ve = transformer.ByteToLong(dValue);
                        if (isOk(vStart, vEnd, vs, ve)) {
                            byte[] nodes = relationIdGroupStore.get(key);
                            long[] nodeIds = transformer.ByteToVersion(nodes);
                            temp = new Long[]{e,nodeIds[0],nodeIds[1]};
                            //temp = e;
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




    public void setRelationLabel(long relation, String label){
        this.typeStore.setEntityType(relation,label);
    }

    public String getRelationLabel(long relation){
        return this.typeStore.getEntityType(relation);
    }

    public void updateEntityProperty(long id, String key, Object value,long time){
        this.relationPropertyStore.updateEntityProperty(id,key,value,time);
    }
    public Object getEntityProperty(long id, String key, long time){
        return this.relationPropertyStore.getEntityProperty(id,key,time);
    }
    public Object _getEntityProperty(long id, String key, long time){
        return this.relationPropertyStore._getEntityProperty(id,key,time);
    }
    public HashMap<String, HashMap<Long,Object>> getEntityProperty(long id){
        return this.relationPropertyStore.getEntityAllProperty(transformer.LongToByte(id));
    }



}

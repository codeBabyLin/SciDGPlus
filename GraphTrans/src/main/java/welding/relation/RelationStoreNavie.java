package welding.relation;

import Common.Transformer;
import welding.IdVersionStore;
import welding.property.PropertyDyStore;
import welding.type.TypeStore;

import java.io.File;
import java.util.Iterator;

public class RelationStoreNavie {
    private IdVersionStore relationIdCStore;
    private IdVersionStore relationIdDStore;

    private Transformer transformer;
    private TypeStore typeStore;
    private PropertyDyStore relationPropertyStore;

    public RelationStoreNavie(String dir){
        this.relationIdCStore = new IdVersionStore(new File(dir,"relationIdCreate").getAbsolutePath());
        this.relationIdDStore = new IdVersionStore(new File(dir,"relationIdDelete").getAbsolutePath());
        this.typeStore = new TypeStore(new File(dir,"relationType").getAbsolutePath());
        this.relationPropertyStore = new PropertyDyStore(new File(dir,"relationProperty").getAbsolutePath());
        this.transformer = new Transformer();
    }

    public boolean existRelation(long relation){
        return this.relationIdCStore.exist(transformer.LongToByte(relation));
    }
    public void addRelation_d(long relation,long version){
        if(existRelation(relation)){
            this.deleteRelation(relation,version);
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

    public void addRelation_c(long relation,long version){
        byte[]key = transformer.LongToByte(relation);
        byte[]cValue = transformer.LongToByte(version);
        byte[]dValue = transformer.LongToByte(Long.MAX_VALUE);
        this.relationIdCStore.add(key,cValue);
        this.relationIdDStore.add(key,dValue);
    }
    public void deleteRelation(long relation,long version){
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
    public Iterator<Long> getRelationsByVersion(Long version){
        Iterator<byte[]> iter = this.relationIdCStore.all();
        return new Iterator<Long>() {
            long temp;
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

    public Iterator<Long> getRelationsByVersion(Long vStart,Long vEnd) {
        Iterator<byte[]> iter = this.relationIdCStore.all();
        return new Iterator<Long>() {
            long temp;

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
            public Long next() {
                return temp;
            }
        };

    }




    public void setRelationLabel(long node, String label){
        this.typeStore.setEntityType(node,label);
    }

    public String getRelationLabel(long node){
        return this.typeStore.getEntityType(node);
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



}

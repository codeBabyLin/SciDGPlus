package welding.property;

import KVStore.RocksDBFactory;
import KVStore.RocksDBStorage;
import Common.Transformer;

import java.util.HashMap;

public class PropertyDyStore {
    private NameStore nameStore;
    private RocksDBStorage db;
    private Transformer transformer;
    private String ProtertyStore = "propertyStore";
    private String NameStore = "nameStore";
    public PropertyDyStore(String dataPath){
        this.db = RocksDBFactory.getDB(dataPath+"/"+ProtertyStore);
        this.transformer = new Transformer();
        this.nameStore = new NameStore(dataPath+"/"+NameStore);
    }

    public Object _getEntityProperty(long id,String key, long time){

        this.nameStore.makeSureKeyExist(key);
        long keyId = this.nameStore.getIdByName(key);
        return _getEntityProperty(id,keyId,time);
    }
    public Object _getEntityProperty(long id,long keyId, long time){

        byte[]k1 = transformer.LongToByte(id);
        byte[]k2 = transformer.LongToByte(keyId);
        byte[]k = transformer.concatByteArray(k1,k2);
        HashMap<byte[],byte[]> data = new HashMap<>();
        this.db.seek(k,data);

        byte[] tempkey = new byte[1];
        long tempTime = -1;
        for(byte[] key : data.keySet()){
            long t1 = transformer.readThirdLong(key);
            if(t1 == time) {
                tempkey = key;
                break;
            }
            if(t1<time && t1>tempTime){
                tempkey = key;
                tempTime = t1;
            }
        }
       byte[]value = data.get(tempkey);
        return transformer.byteToObject(value);
    }
    public Object getEntityProperty(long id,String key, long time){

        this.nameStore.makeSureKeyExist(key);
        long keyId = this.nameStore.getIdByName(key);
       return getEntityProperty(id,keyId,time);
    }

    public Object getEntityProperty(long id,long keyId, long time){

        byte[]k1 = transformer.LongToByte(id);
        byte[]k2 = transformer.LongToByte(keyId);
        byte[]k3 = transformer.LongToByte(time);

        byte[]k = transformer.concatByteArray(k1,k2,k3);
        byte[]value = this.db.get(k);
        return transformer.byteToObject(value);
    }

    public void updateEntityProperty(long id, String key, Object value,long time){
        this.nameStore.makeSureKeyExist(key);
        long keyId = this.nameStore.getIdByName(key);
        byte[]k1 = transformer.LongToByte(id);
        byte[]k2 = transformer.LongToByte(keyId);
        byte[]k3 = transformer.LongToByte(time);

        byte[]k = transformer.concatByteArray(k1,k2,k3);
        byte[]v = transformer.objectToByte(value);
        this.db.put(k,v);

    }


    public Object _getEntityProperty(byte[] id,String key, long time){

        this.nameStore.makeSureKeyExist(key);
        long keyId = this.nameStore.getIdByName(key);
        return _getEntityProperty(id,keyId,time);
    }
    public Object _getEntityProperty(byte[] id,long keyId, long time){

        byte[]k1 = id;
        byte[]k2 = transformer.LongToByte(keyId);
        byte[]k = transformer.concatByteArray(k1,k2);
        HashMap<byte[],byte[]> data = new HashMap<>();
        this.db.seek(k,data);

        byte[] tempkey = new byte[1];
        long tempTime = -1;
        for(byte[] key : data.keySet()){
            long t1 = transformer.readThirdLong(key);
            if(t1 == time) {
                tempkey = key;
                break;
            }
            if(t1<time && t1>tempTime){
                tempkey = key;
                tempTime = t1;
            }
        }
        byte[]value = data.get(tempkey);
        return transformer.byteToObject(value);
    }
    public Object getEntityProperty(byte[] id,String key, long time){

        this.nameStore.makeSureKeyExist(key);
        long keyId = this.nameStore.getIdByName(key);
        return getEntityProperty(id,keyId,time);
    }

    public Object getEntityProperty(byte[] id,long keyId, long time){

        byte[]k1 = id;
        byte[]k2 = transformer.LongToByte(keyId);
        byte[]k3 = transformer.LongToByte(time);

        byte[]k = transformer.concatByteArray(k1,k2,k3);
        byte[]value = this.db.get(k);
        return transformer.byteToObject(value);
    }

    public void updateEntityProperty(byte[] id, String key, Object value,long time){
        this.nameStore.makeSureKeyExist(key);
        long keyId = this.nameStore.getIdByName(key);
        byte[]k1 = id;
        byte[]k2 = transformer.LongToByte(keyId);
        byte[]k3 = transformer.LongToByte(time);

        byte[]k = transformer.concatByteArray(k1,k2,k3);
        byte[]v = transformer.objectToByte(value);
        this.db.put(k,v);

    }

    public HashMap<String,HashMap<Long,Object>> getEntityAllProperty(byte[] id){
        HashMap<String,HashMap<Long,Object>> properties = new HashMap<>();
        HashMap<byte[],byte[]> data = new HashMap<>();
        this.db.seek(id,data);

        for(byte[] key : data.keySet()){
            long keyId = transformer.readKeyFromByte(key);
            String propertyName = this.nameStore.getNameById(keyId);
            long t1 = transformer.readTimeFromByte(key);
            byte[]value = data.get(key);
            Object propertyValue = transformer.byteToObject(value);
            if(!properties.containsKey(propertyName)){
                properties.put(propertyName, new HashMap<>());
            }
            properties.get(propertyName).put(t1,propertyValue);
        }
        return properties;

    }


}

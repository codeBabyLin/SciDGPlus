package welding.type;

import Common.Transformer;
import KVStore.RocksDBFactory;
import KVStore.RocksDBStorage;
import welding.property.NameStore;

public class TypeStore {
    private NameStore nameStore;
    private RocksDBStorage db;
    private Transformer transformer;
    private String TypeStore = "typeStore";
    private String TypeNameStore = "typeNameStore";
    public TypeStore(String dataPath){
        this.db = RocksDBFactory.getDB(dataPath+"/"+TypeStore);
        this.transformer = new Transformer();
        this.nameStore = new NameStore(dataPath+"/"+TypeNameStore);
    }

    public void setEntityType(long id, String type){
        this.nameStore.makeSureKeyExist(type);
        long vId = this.nameStore.getIdByName(type);
        byte[]key = transformer.LongToByte(id);
        byte[]value = transformer.LongToByte(vId);
        this.db.put(key,value);
    }
    public String getEntityType(long id){
        byte[]key = transformer.LongToByte(id);
        byte[] value = this.db.get(key);
        long vId = transformer.ByteToLong(value);
        return this.nameStore.getNameById(vId);
    }
    public void setEntityType(byte[] id, String type){
        this.nameStore.makeSureKeyExist(type);
        long vId = this.nameStore.getIdByName(type);
        //byte[]key = transformer.LongToByte(id);
        byte[]value = transformer.LongToByte(vId);
        this.db.put(id,value);
    }
    public String getEntityType(byte[] id){
        //byte[]key = transformer.LongToByte(id);
        byte[] value = this.db.get(id);
        long vId = transformer.ByteToLong(value);
        return this.nameStore.getNameById(vId);
    }


}

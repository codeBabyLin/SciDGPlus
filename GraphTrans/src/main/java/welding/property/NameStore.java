package welding.property;

import KVStore.RocksDBFactory;
import KVStore.RocksDBStorage;
import Common.Transformer;

import java.util.HashMap;
import java.util.Iterator;

public class NameStore {

    private RocksDBStorage db;
    private HashMap<String,Long> nameId;
    private HashMap<Long,String> idName;
    private Transformer transformer;
    private long currentId;
    public NameStore(String dataPath){
        this.db = RocksDBFactory.getDB(dataPath);
        this.nameId = new HashMap<>();
        this.idName = new HashMap<>();
        this.transformer = new Transformer();
        //this.currentId = 0;
        loadAll();
    }
    private void loadAll(){
        Iterator<byte[]> iter = this.db.All();
        while(iter.hasNext()){
            byte[] key = iter.next();
            byte[]value = db.get(key);
            long id = transformer.ByteToLong(key);
            String name = transformer.byteToString(value);
            this.idName.put(id,name);
            this.nameId.put(name,id);
            if(this.currentId<id) this.currentId = id;
        }
    }

    public boolean makeSureKeyExist(String key){
        if(!this.nameId.containsKey(key)){
            this.addName(key);
        }
        return true;
    }
    public boolean existKey(String key){
        return this.nameId.containsKey(key);
    }

    public String getNameById(long id){
        return this.idName.get(id);
    }
    public long getIdByName(String name){
        return this.nameId.get(name);
    }

    public long addName(String name){
        long newId = this.currentId+1;
        this.idName.put(newId,name);
        this.nameId.put(name,newId);
        byte[]key = transformer.LongToByte(newId);
        byte[]value = transformer.StringToByte(name);
        this.db.put(key,value);
        return newId;
    }
    public long deleteName(String name){
        //long id = this.nameId.get(name);
        //this.nameId.remove(name);
        //this.idName.remove(id);
        //byte[]key = transformer.LongToByte(id);
        //this.db.delete(key);
        //return id;
        return this.currentId;
    }

}

package KVStore;

import org.rocksdb.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RocksDBStorage {
    private RocksDB rockDB;
    public RocksDBStorage(RocksDB rockDB){
        this.rockDB = rockDB;
    }

    public boolean exist(byte[]key){
        return rockDB.keyMayExist(key,new Holder<>());
    }

    public void seek(byte[]prefix, Map<byte[],byte[]> data){
        RocksIterator iter = this.rockDB.newIterator();
        iter.seek(prefix);
        //HashMap<byte[],byte[]> data = new HashMap<>();
        while(iter.isValid()){

            byte[] key = iter.key();
            byte[]value = iter.value();
            data.put(key,value);
            iter.next();
        }
    }


    public byte[] get(byte[]key){
        byte[] res = null;
        try {
            res = rockDB.get(key);
        }catch(Exception e){
            e.printStackTrace();
        }
        return res;
    }
    public void put(byte[]key,byte[]value){
        try {
            rockDB.put(key,value);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void delete(byte[]key){
        try {
            rockDB.delete(key);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public Iterator<byte[]> All(){
        RocksIterator iter = rockDB.newIterator();
        iter.seekToFirst();
        return new Iterator<byte[]>() {

            @Override
            public boolean hasNext() {
                return iter.isValid();
            }

            @Override
            public byte[] next() {
                byte[]data = iter.key();
                iter.next();
                return data;
            }
        };
    }
    public void flush(){
        try {
            this.rockDB.flush(new FlushOptions());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}


package dataConfig;

import operation.VersionGraphStore;

import java.io.File;

public interface DataStore {

    //int dataVersion = 27;
    //int startVersion = 1986;
    //int endVersion = 2012;

    //String dataPath = System.getProperty("user.dir")+"\\Coauthor";
    void storeVersionGraph(VersionGraphStore graphDb);

}

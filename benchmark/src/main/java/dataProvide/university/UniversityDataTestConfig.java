package dataProvide.university;

import dataConfig.QueryGeneratorConfig;

import java.io.File;

public class UniversityDataTestConfig implements QueryGeneratorConfig {
    public int dataVersion = 26;
    public int startVersion = 0;
    public int endVersion = 25;

    public int singVersionQuerySize = 10;

    public int multiVersionQuerySize = 10;

    public int sameVersionQuerySize = 10;

    public int diffVersionQuerySize = 10;

    public int deltaVersionQuerySize = 10;

    public String fileName = "UniversityQuery.rtt";
    public String logFileName = "UniversityQueryLog.txt";

    public String getQueryFileName(){
        String pathDir = System.getProperty("user.dir")+"\\Query";
        new File(pathDir).mkdir();
        String queryFileName = new File(pathDir,this.fileName).getAbsolutePath();
        return queryFileName;
    }
    public String getLogFileName(){
        String pathDir = System.getProperty("user.dir")+"\\Query";
        new File(pathDir).mkdir();
        String logFileName = new File(pathDir,this.logFileName).getAbsolutePath();
        return logFileName;
    }

    @Override
    public int getSingleVersionQuerySize() {
        return this.singVersionQuerySize;
    }

    @Override
    public int getMultiVersionQuerySize() {
        return this.multiVersionQuerySize;
    }

    @Override
    public int getSameVersionQuerySize() {
        return this.sameVersionQuerySize;
    }

    @Override
    public int getDiffVersionQuerySize() {
        return this.diffVersionQuerySize;
    }

    @Override
    public int getDeltaVersionQuerySize() {
        return this.deltaVersionQuerySize;
    }

    @Override
    public int getDataVersionSize() {
        return this.dataVersion;
    }

    @Override
    public int getDataStartVersion() {
        return this.startVersion;
    }

    @Override
    public int getDataEndVersion() {
        return this.endVersion;
    }

}

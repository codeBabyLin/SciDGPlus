package OperationImpl;

import operation.VersionGraphStore;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.gradoop.temporal.io.impl.csv.TemporalCSVDataSink;
import org.gradoop.temporal.util.TemporalGradoopConfig;
import sampleGraph.SampleGraph;

public class GradoopStoreImpl implements VersionGraphStore {

    /* String storePath = "./gradoop";

        TemporalCSVDataSink sink = new TemporalCSVDataSink(storePath, temporalConfig);

        graph.writeTo(sink);

        ENV.execute();*/
    //ExecutionEnvironment ENV = ExecutionEnvironment.createLocalEnvironment();
    //TemporalGradoopConfig temporalConfig = TemporalGradoopConfig.createConfig(ENV);
    ExecutionEnvironment ENV;
    TemporalGradoopConfig temporalConfig;
    String dataPath;
    TemporalCSVDataSink sink;
    public GradoopStoreImpl(String dataPath){
        this.dataPath = dataPath;
        this.ENV = ExecutionEnvironment.createLocalEnvironment();
        this.temporalConfig = TemporalGradoopConfig.createConfig(this.ENV);
    }

    @Override
    public void begin() {

    }

    @Override
    public void storeGraph(SampleGraph sampleGraph, int version) {

    }

    @Override
    public void finish() {

    }
}

package operation;

import sampleGraph.SampleGraph;

public interface VersionGraphStore {
    void begin();
    void storeGraph(SampleGraph sampleGraph, int version);
    void finish();
}

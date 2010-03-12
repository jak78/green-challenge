package com.octo.greenchallenge.collect.api;

import java.util.List;

/**
 * TODO doc
 */
public interface CollectCPUService {

    /**
     * TODO doc
     *
     * @param recordedSample
     */
    void recordData(Sample recordedSample);

    String dumpAllRecordedSamples();
}
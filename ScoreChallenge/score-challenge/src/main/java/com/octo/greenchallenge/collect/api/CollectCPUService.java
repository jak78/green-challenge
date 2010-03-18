package com.octo.greenchallenge.collect.api;

/**
 * Handle interaction with Google AppEngine APIs. This interface is used to mock GAE in order to test our code easily.
 */
public interface CollectCPUService {

    /**
     * Store sample in datastore.
     *
     * @param recordedSample sample to store
     */
    void recordData(Sample recordedSample);

    /**
     * Dumps all recorded samples, use this method only for testing purposes.
     *
     * @return all recorded samples in String format in order to display it.
     */
    String dumpAllRecordedSamples();
}
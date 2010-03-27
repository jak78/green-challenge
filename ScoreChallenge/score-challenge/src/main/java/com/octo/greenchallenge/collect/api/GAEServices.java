package com.octo.greenchallenge.collect.api;

import java.util.List;

/**
 * Handle interaction with Google AppEngine APIs. This interface is used to mock GAE in order to test our code easily.
 */
public interface GAEServices {

    /**
     * Store sample in datastore.
     *
     * @param recordedSample sample to store
     */
    void recordData(Sample recordedSample);

    /**
     * Dumps all recorded samples, use this method only for testing purposes.
     *
     * @return all recorded samples.
     */
    List<Sample> dumpAllRecordedSamples();

    /**
     * @return true if user is signed as admin, false otherwise
     */
    boolean isUserAdmin();
}
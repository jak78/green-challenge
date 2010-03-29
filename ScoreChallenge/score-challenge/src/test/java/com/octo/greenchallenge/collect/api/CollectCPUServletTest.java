package com.octo.greenchallenge.collect.api;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static com.octo.greenchallenge.collect.api.SampleSource.GREEN_FOX;
import static org.mockito.Mockito.*;

/**
 * Collect sample HTTP API tests.
 */
@SuppressWarnings("unchecked")
public class CollectCPUServletTest extends ServletTest {

    /**
     * Servlet to test.
     */
    CollectCPUServlet servlet;

    /**
     * Mock, servlet and test data setup.
     *
     * @throws Exception any exception
     */
    @Before
    public void setUp() throws Exception {
        servlet = new CollectCPUServlet();

        servlet.appEngine = appEngine;
        servlet.init(svConfig);

        httpParams.put("challengerID", new String[]{"chuck.norris@gmail.com"});
        httpParams.put("CPUCycles", new String[]{"42"});
        httpParams.put("source", new String[]{"GREEN_FOX"});
    }

    /**
     * When all HTTP params are OK, then record a sample and return 200 OK.
     *
     * @throws Exception any exception
     */
    @Test
    public void ok() throws Exception {
        servlet.doPost(httpRequest, httpResponse);
        httpResponseShouldBe(200, "OK");
        shouldRecordSample(new Sample("chuck.norris@gmail.com", 42, new Date(), GREEN_FOX));
    }

    /**
     * When challengerID HTTP param is missing, then return 400 INVALID_DATA
     * and do not record anything.
     *
     * @throws Exception any exception
     */
    @Test
    public void missingChallenger() throws Exception {
        httpParams.remove("challengerID");
        servlet.doPost(httpRequest, httpResponse);
        httpResponseShouldBe(400, "INVALID_DATA");
        shouldLogSomething();
        shouldNotRecordAnySample();
    }

    /**
     * When challengerID HTTP param is blank, then return 400 INVALID_DATA
     * and do not record anything.
     *
     * @throws Exception any exception
     */
    @Test
    public void blankChallenger() throws Exception {
        httpParams.put("challengerID", new String[]{"  "});
        servlet.doPost(httpRequest, httpResponse);
        httpResponseShouldBe(400, "INVALID_DATA");
        shouldLogSomething();
        shouldNotRecordAnySample();
    }

    /**
     * When CPUCycles HTTP param is missing, then return 400 INVALID_DATA
     * and do not record anything.
     *
     * @throws Exception any exception
     */
    @Test
    public void missingCPUCycles() throws Exception {
        httpParams.remove("CPUCycles");
        servlet.doPost(httpRequest, httpResponse);
        httpResponseShouldBe(400, "INVALID_DATA");
        shouldLogSomething();
        shouldNotRecordAnySample();
    }

    /**
     * When CPUCycles HTTP param is blank, then return 400 INVALID_DATA
     * and do not record anything.
     *
     * @throws Exception any exception
     */
    @Test
    public void blankCPUCycles() throws Exception {
        httpParams.put("CPUCycles", new String[]{"  "});
        servlet.doPost(httpRequest, httpResponse);
        httpResponseShouldBe(400, "INVALID_DATA");
        shouldLogSomething();
        shouldNotRecordAnySample();
    }

    /**
     * When CPUCycles HTTP param is not a number, then return 400 INVALID_DATA
     * and do not record anything.
     *
     * @throws Exception any exception
     */
    @Test
    public void invalidCPUCycles() throws Exception {
        httpParams.put("CPUCycles", new String[]{"-42"});
        servlet.doPost(httpRequest, httpResponse);
        httpResponseShouldBe(400, "INVALID_DATA");
        shouldLogSomething();
        shouldNotRecordAnySample();
    }

    /**
     * When source HTTP param is missing, then return 400 INVALID_DATA
     * and do not record anything.
     *
     * @throws Exception any exception
     */
    @Test
    public void missingSource() throws Exception {
        httpParams.remove("source");
        servlet.doPost(httpRequest, httpResponse);
        httpResponseShouldBe(400, "INVALID_DATA");
        shouldLogSomething();
        shouldNotRecordAnySample();
    }

    /**
     * When source HTTP param is blank, then return 400 INVALID_DATA
     * and do not record anything.
     *
     * @throws Exception any exception
     */
    @Test
    public void blankSource() throws Exception {
        httpParams.put("source", new String[]{"  "});
        servlet.doPost(httpRequest, httpResponse);
        httpResponseShouldBe(400, "INVALID_DATA");
        shouldLogSomething();
        shouldNotRecordAnySample();
    }

    /**
     * When an unexpected exception is thrown, then return 500 TECHNICAL_FAILURE
     * and do not record anything.
     *
     * @throws Exception any exception
     */
    @Test
    public void technicalFailure() throws Exception {
        Throwable err = new NullPointerException();
        doThrow(err).when(persistenceManager).makePersistent((Sample) any());
        servlet.doPost(httpRequest, httpResponse);
        httpResponseShouldBe(500, "TECHNICAL_FAILURE");
        shouldLog(err);
        verify(persistenceManager).makePersistent((Sample) any());
        verify(persistenceManager).close();
        verifyNoMoreInteractions(persistenceManager);
    }

    void shouldRecordSample(Sample sample) {
        verify(persistenceManager, times(1)).makePersistent(sample);
        verify(persistenceManager).close();
        verifyNoMoreInteractions(persistenceManager);
    }

    void shouldNotRecordAnySample() {
        verifyZeroInteractions(persistenceManager);
    }

}

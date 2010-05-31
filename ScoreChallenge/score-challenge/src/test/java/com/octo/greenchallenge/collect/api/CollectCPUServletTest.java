package com.octo.greenchallenge.collect.api;

import com.octo.greenchallenge.collect.api.gae.GAEServices;
import org.junit.Before;
import org.junit.Test;

import javax.jdo.PersistenceManager;
import java.util.Date;
import java.util.List;

import static com.octo.greenchallenge.collect.api.SampleSource.GREEN_FOX;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
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
        shouldRecordOneSample(new Sample("chuck.norris@gmail.com", 42, new Date(), GREEN_FOX));
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

        // Simulates that persistence manager throw an exception:
        Throwable err = new NullPointerException();
        servlet.appEngine = mock(GAEServices.class);
        PersistenceManager persistenceManagerMock = mock(PersistenceManager.class);
        when(servlet.appEngine.getPersistenceManager()).thenReturn(persistenceManagerMock);
        doThrow(err).when(persistenceManagerMock).makePersistent((Sample) any());

        servlet.doPost(httpRequest, httpResponse);

        httpResponseShouldBe(500, "TECHNICAL_FAILURE");
        shouldLog(err);
        verify(persistenceManagerMock).makePersistent((Sample) any());
        verify(persistenceManagerMock).close();
        verifyNoMoreInteractions(persistenceManagerMock);
    }

    void shouldRecordOneSample(Sample sample) {
        List<Sample> samples = (List<Sample>)persistenceManager.newQuery(Sample.class).execute();
        assertThat(samples.size(),equalTo(1));
    }

    void shouldNotRecordAnySample() {
        List<Sample> samples = (List<Sample>)persistenceManager.newQuery(Sample.class).execute();
        assertThat(samples.size(),equalTo(0));
    }

}

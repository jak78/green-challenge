package com.octo.greenchallenge.collect.api;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Date;

import static com.octo.greenchallenge.collect.api.SampleSource.GREEN_FOX;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests de la servlet (API HTTP).
 */
@SuppressWarnings("unchecked")
public class CollectCPUServletTest extends ServletTest {
    CollectCPUServlet servlet;

    GAEServices service;

    @Before
    public void setUp() throws ServletException, IOException {
        servlet = new CollectCPUServlet();
        service = mock(GAEServices.class);

        servlet.service = service;
        servlet.init(svConfig);

        httpParams.put("challengerID", new String[]{"chuck.norris@gmail.com"});
        httpParams.put("CPUCycles", new String[]{"42"});
        httpParams.put("source", new String[]{"GREEN_FOX"});
    }

    @Test
    public void ok() throws IOException, ServletException {
        servlet.doPost(httpRequest, httpResponse);

        httpResponseShouldBe(200, "OK");
        shouldRecordSample(new Sample("chuck.norris@gmail.com", 42, new Date(), GREEN_FOX));
    }

    @Test
    public void missingChallenger() throws IOException, ServletException {
        httpParams.remove("challengerID");
        servlet.doPost(httpRequest, httpResponse);
        httpResponseShouldBe(400, "INVALID_DATA");
        shouldLogSomething();
        shouldNotRecordAnySample();
    }

    @Test
    public void blankChallenger() throws IOException, ServletException {
        httpParams.put("challengerID", new String[]{"  "});
        servlet.doPost(httpRequest, httpResponse);
        httpResponseShouldBe(400, "INVALID_DATA");
        shouldLogSomething();
        shouldNotRecordAnySample();
    }

    @Test
    public void missingCPUCycles() throws IOException, ServletException {
        httpParams.remove("CPUCycles");
        servlet.doPost(httpRequest, httpResponse);
        httpResponseShouldBe(400, "INVALID_DATA");
        shouldLogSomething();
        shouldNotRecordAnySample();
    }

    @Test
    public void blankCPUCycles() throws IOException, ServletException {
        httpParams.put("CPUCycles", new String[]{"  "});
        servlet.doPost(httpRequest, httpResponse);
        httpResponseShouldBe(400, "INVALID_DATA");
        shouldLogSomething();
        shouldNotRecordAnySample();
    }

    @Test
    public void invalidCPUCycles() throws IOException, ServletException {
        httpParams.put("CPUCycles", new String[]{"-42"});
        servlet.doPost(httpRequest, httpResponse);
        httpResponseShouldBe(400, "INVALID_DATA");
        shouldLogSomething();
        shouldNotRecordAnySample();
    }

    @Test
    public void missingSource() throws IOException, ServletException {
        httpParams.remove("source");
        servlet.doPost(httpRequest, httpResponse);
        httpResponseShouldBe(400, "INVALID_DATA");
        shouldLogSomething();
        shouldNotRecordAnySample();
    }

    @Test
    public void blankSource() throws IOException, ServletException {
        httpParams.put("source", new String[]{"  "});
        servlet.doPost(httpRequest, httpResponse);
        httpResponseShouldBe(400, "INVALID_DATA");
        shouldLogSomething();
        shouldNotRecordAnySample();
    }

    @Test
    public void technicalFailure() throws IOException, ServletException {
        Throwable err = new NullPointerException();
        doThrow(err).when(service).recordData((Sample) any());
        servlet.doPost(httpRequest, httpResponse);
        httpResponseShouldBe(500, "TECHNICAL_FAILURE");
        shouldLog(err);
    }

    void shouldRecordSample(Sample sample) {
        verify(service, times(1)).recordData(sample);
    }

    void shouldNotRecordAnySample() {
        verify(service, never()).recordData((Sample) any());
    }

    void httpResponseShouldBe(int expectedHttpStatus, String expectedResponseText) {
        // Check text response :
        assertEquals("response", expectedResponseText, output.toString());

        // API should send the right HTTP status code :
        verify(httpResponse, atLeastOnce()).setStatus(expectedHttpStatus);

        // API should send data in UTF-8 :
        verify(httpResponse, atLeastOnce()).setCharacterEncoding("UTF-8");

        // API should send the right MIME-type :
        verify(httpResponse, atLeastOnce()).setContentType("text/plain");
    }

}

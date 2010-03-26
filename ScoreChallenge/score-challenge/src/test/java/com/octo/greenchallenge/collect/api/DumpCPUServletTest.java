package com.octo.greenchallenge.collect.api;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests of DumpCPUServlet.
 */
public class DumpCPUServletTest extends ServletTest {
    DumpCPUServlet servlet;

    CollectCPUService service;
    private static final String NL = System.getProperty("line.separator") ;

    List<Sample> samples;

    @Before
    public void setUp() throws ServletException, IOException {
        samples = new ArrayList<Sample>();
        samples.add(new Sample("chuck.norris@gmail.com", 1, null, SampleSource.SERVER_APP));
        samples.add(new Sample("chuck.norris@gmail.com", 2, null, SampleSource.SERVER_APP));
        samples.add(new Sample("chuck.norris@gmail.com", 3, null, SampleSource.SERVER_APP));

        servlet = new DumpCPUServlet();
        service = mock(CollectCPUService.class);

        servlet.service = service;
        servlet.init(svConfig);
    }

    @Test
    public void dumpWhenIAmAdmin() throws IOException, ServletException {

        when(service.dumpAllRecordedSamples()).thenReturn(samples);
        when(service.isUserAdmin()).thenReturn(true);

        servlet.doGet(httpRequest, httpResponse);
        
        assertEquals("chuck.norris@gmail.com\t1\tSERVER_APP" + NL +
                "chuck.norris@gmail.com\t2\tSERVER_APP" + NL +
                "chuck.norris@gmail.com\t3\tSERVER_APP" + NL,output.toString());
    }

    @Test
    public void dumpWhenIAmNotAdmin() throws IOException, ServletException {

        when(service.dumpAllRecordedSamples()).thenReturn(samples);
        when(service.isUserAdmin()).thenReturn(false);

        servlet.doGet(httpRequest, httpResponse);

        verify(httpResponse, atLeastOnce()).sendError(eq(403), Matchers.<String>any());
        assertEquals("",output.toString());
    }

}

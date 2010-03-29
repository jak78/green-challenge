package com.octo.greenchallenge.collect.api;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests of DumpCPUServlet.
 */
public class DumpCPUServletTest extends ServletTest {

    private static final String NL = System.getProperty("line.separator");

    /**
     * Servlet to test.
     */
    DumpCPUServlet servlet;

    List<Sample> samples;

    /**
     * Mocks, servlet and test data setup.
     *
     * @throws Exception any problem
     */
    @Before
    public void setUp() throws Exception {
        samples = new ArrayList<Sample>();
        Date date = new SimpleDateFormat("dd-MM-yyyy").parse("01-02-2007");
        samples.add(new Sample("chuck.norris@gmail.com", 1, date, SampleSource.SERVER_APP));
        samples.add(new Sample("chuck.norris@gmail.com", 2, date, SampleSource.SERVER_APP));
        samples.add(new Sample("chuck.norris@gmail.com", 3, date, SampleSource.SERVER_APP));

        servlet = new DumpCPUServlet();
        servlet.appEngine = appEngine;

        servlet.init(svConfig);
    }

    /**
     * When I am admin - or Chuck Norris ;-) - I can dump sample data.
     *
     * @throws Exception any problem
     */
    @Test
    public void dumpWhenIAmAdmin() throws Exception {

        when(query.execute()).thenReturn(samples);
        when(persistenceManager.detachCopyAll(anyCollectionOf(Sample.class))).thenReturn(samples);
        when(userService.isUserLoggedIn()).thenReturn(true);
        when(userService.isUserAdmin()).thenReturn(true);

        servlet.doGet(httpRequest, httpResponse);

        assertEquals("chuck.norris@gmail.com\t1\tSERVER_APP\tThu Feb 01 00:00:00 CET 2007" + NL +
                "chuck.norris@gmail.com\t2\tSERVER_APP\tThu Feb 01 00:00:00 CET 2007" + NL +
                "chuck.norris@gmail.com\t3\tSERVER_APP\tThu Feb 01 00:00:00 CET 2007" + NL, output.toString());
        verify(persistenceManager).close();
    }

    /**
     * When I am not admin, I can't dump sample data.
     *
     * @throws Exception any problem
     */
    @Test
    public void dumpWhenIAmNotAdmin() throws Exception {

        when(query.execute()).thenReturn(samples);
        when(userService.isUserLoggedIn()).thenReturn(true);
        when(userService.isUserAdmin()).thenReturn(false);

        servlet.doGet(httpRequest, httpResponse);

        verify(httpResponse, atLeastOnce()).sendError(eq(403), Matchers.<String>any());
        assertEquals("", output.toString());
    }

    /**
     * When I am not logged, I can't dump sample data.
     *
     * @throws Exception any problem
     */
    @Test
    public void dumpWhenIAmNotLogged() throws Exception {

        when(query.execute()).thenReturn(samples);
        when(userService.isUserLoggedIn()).thenReturn(false);
        when(userService.isUserAdmin()).thenReturn(false);

        servlet.doGet(httpRequest, httpResponse);

        verify(httpResponse, atLeastOnce()).sendError(eq(403), Matchers.<String>any());
        assertEquals("", output.toString());
    }
}

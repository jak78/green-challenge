package com.octo.greenchallenge.collect.api;

import com.google.appengine.api.users.User;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    /**
     * Mocks, servlet and test data setup.
     *
     * @throws Exception any problem
     */
    @Before
    public void setUp() throws Exception {

        when(appEngine.getPersistenceManager()).thenReturn(persistenceManager);
        when(appEngine.getUserService()).thenReturn(userServiceMock);
        
        servlet = new DumpCPUServlet();
        servlet.appEngine = appEngine;

        servlet.init(svConfig);
    }

    /**
     * Chuck Norris sees all data because he's admin.
     *
     * @throws Exception any problem
     */
    @Test
    public void dumpWhenIAmChuckNorris() throws Exception {

        // Insert test data:
        Date date = new SimpleDateFormat("dd-MM-yyyy").parse("01-02-2007");
        persistenceManager.makePersistent(new Sample("chuck.norris@gmail.com", 1, date, SampleSource.SERVER_APP));
        persistenceManager.makePersistent(new Sample("chuck.norris@gmail.com", 2, date, SampleSource.SERVER_APP));
        persistenceManager.makePersistent(new Sample("omer.simpson@gmail.com", 3, date, SampleSource.SERVER_APP));

        // User is logged and admin:
        when(userServiceMock.isUserLoggedIn()).thenReturn(true);
        when(userServiceMock.isUserAdmin()).thenReturn(true);
        // User name is Chuck Norris:
        User curUser = new User("chuck.norris@gmail.com","gmail.com");
        when(userServiceMock.getCurrentUser()).thenReturn(curUser);

        servlet.doGet(httpRequest, httpResponse);

        assertEquals("chuck.norris@gmail.com\t1\tSERVER_APP\t2007-02-01 00:00:00" + NL +
                "chuck.norris@gmail.com\t2\tSERVER_APP\t2007-02-01 00:00:00" + NL +
                "omer.simpson@gmail.com\t3\tSERVER_APP\t2007-02-01 00:00:00" + NL, output.toString());
    }

    /**
     * Omer Simpson sees his sampled data.
     *
     * @throws Exception any problem
     */
    @Test
    public void dumpWhenIAmOmerSimpson() throws Exception {

        // Insert test data:
        Date date = new SimpleDateFormat("dd-MM-yyyy").parse("01-02-2007");
        persistenceManager.makePersistent(new Sample("omer.simpson@gmail.com", 3, date, SampleSource.SERVER_APP));

        // User is logged but not admin:
        when(userServiceMock.isUserLoggedIn()).thenReturn(true);
        when(userServiceMock.isUserAdmin()).thenReturn(false);
        // User name is Omer Simpson:
        User curUser = new User("omer.simpson@gmail.com","gmail.com");
        when(userServiceMock.getCurrentUser()).thenReturn(curUser);

        servlet.doGet(httpRequest, httpResponse);

        assertEquals("omer.simpson@gmail.com\t3\tSERVER_APP\t2007-02-01 00:00:00" + NL, output.toString());
    }

    /**
     * When I am not logged, I can't dump sample data.
     *
     * @throws Exception any problem
     */
    @Test
    public void dumpWhenIAmNobody() throws Exception {

        when(userServiceMock.isUserLoggedIn()).thenReturn(false);
        when(userServiceMock.isUserAdmin()).thenReturn(false);

        servlet.doGet(httpRequest, httpResponse);

        verify(httpResponse, atLeastOnce()).sendError(eq(403), anyString());
        assertEquals("", output.toString());
    }
}

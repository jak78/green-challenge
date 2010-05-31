package com.octo.greenchallenge.collect.api;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
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

    Date date;


    /**
     * Mocks, servlet and test data setup.
     *
     * @throws Exception any problem
     */
    @Before
    public void setUp() throws Exception {

        servlet = new DumpCPUServlet();

        servlet.init(svConfig);

        date = new SimpleDateFormat("dd-MM-yyyy").parse("01-02-2007");
    }

    /**
     * Chuck Norris sees all data because he's admin.
     *
     * @throws Exception any problem
     */
    @Test
    public void dumpWhenIAmChuckNorris() throws Exception {

        // Insert test data:
        persistenceManager.makePersistent(new Sample("chuck.norris@gmail.com", 1, date, SampleSource.SERVER_APP));
        persistenceManager.makePersistent(new Sample("chuck.norris@gmail.com", 2, date, SampleSource.SERVER_APP));
        persistenceManager.makePersistent(new Sample("omer.simpson@gmail.com", 3, date, SampleSource.SERVER_APP));

        // User is logged and admin:
        localUserServiceTestHelper.setEnvIsLoggedIn(true);
        localUserServiceTestHelper.setEnvIsAdmin(true);
        // User name is Chuck Norris:
        localUserServiceTestHelper.setEnvAuthDomain("gmail.com");
        localUserServiceTestHelper.setEnvEmail("chuck.norris@gmail.com");

        servlet.doGet(httpRequest, httpResponse);

        assertThat(output.toString(), equalTo("chuck.norris@gmail.com\t1\tSERVER_APP\t2007-02-01 00:00:00" + NL +
                "chuck.norris@gmail.com\t2\tSERVER_APP\t2007-02-01 00:00:00" + NL +
                "omer.simpson@gmail.com\t3\tSERVER_APP\t2007-02-01 00:00:00" + NL));
    }

    /**
     * Omer Simpson sees his sampled data.
     *
     * @throws Exception any problem
     */
    @Test
    public void dumpWhenIAmOmerSimpson() throws Exception {

        // Insert test data:
        persistenceManager.makePersistent(new Sample("omer.simpson@gmail.com", 3, date, SampleSource.SERVER_APP));

        // User is logged but not admin:
        localUserServiceTestHelper.setEnvIsLoggedIn(true);
        localUserServiceTestHelper.setEnvIsAdmin(false);
        // User name is Omer Simpson:
        localUserServiceTestHelper.setEnvAuthDomain("gmail.com");
        localUserServiceTestHelper.setEnvEmail("omer.simpson@gmail.com");

        servlet.doGet(httpRequest, httpResponse);

        assertThat(output.toString(), equalTo("omer.simpson@gmail.com\t3\tSERVER_APP\t2007-02-01 00:00:00" + NL));
    }

    /**
     * When I am not logged, I can't dump sample data.
     *
     * @throws Exception any problem
     */
    @Test
    public void dumpWhenIAmNobody() throws Exception {

        localUserServiceTestHelper.setEnvIsLoggedIn(false);
        localUserServiceTestHelper.setEnvIsAdmin(false);

        servlet.doGet(httpRequest, httpResponse);

        verify(httpResponse, atLeastOnce()).sendError(eq(403), anyString());
        assertThat(output.toString(),equalTo(""));
    }
}

package com.octo.greenchallenge.collect.api;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

/**
 * PurgeCUPServlet unit tests.
 */
@SuppressWarnings("unchecked")
public class PurgeCPUServletTest extends ServletTest {

    /**
     * Servlet to test.
     */
    PurgeCPUServlet servlet;

    @Before
    public void initTest() {
        servlet = new PurgeCPUServlet();
    }

    @Before
    public void insertTestData() throws ParseException {
        Date date = new SimpleDateFormat("dd-MM-yyyy").parse("01-02-2007");
        persistenceManager.makePersistent(new Sample("chuck.norris@gmail.com", 1, date, SampleSource.SERVER_APP));
        persistenceManager.makePersistent(new Sample("chuck.norris@gmail.com", 2, date, SampleSource.SERVER_APP));
        persistenceManager.makePersistent(new Sample("omer.simpson@gmail.com", 3, date, SampleSource.SERVER_APP));
    }

    @Test
    public void purgeAllSamplesWhenIAmAdmin() throws IOException, ServletException {
        // User is logged and admin:
        localUserServiceTestHelper.setEnvIsLoggedIn(true);
        localUserServiceTestHelper.setEnvIsAdmin(true);
        // User is Chuck Norris:
        localUserServiceTestHelper.setEnvAuthDomain("gmail.com");
        localUserServiceTestHelper.setEnvEmail("chuck.norris@gmail.com");

        servlet.doPost(httpRequest, httpResponse);

        // Should be redirected:
        verify(httpResponse).sendRedirect("/index.jsp");

        // Chuck Norris purges all samples:
        noSampleShouldRemain();
    }

    @Test
    public void purgeOnlyMySamplesWhenIAmNotAdmin() throws IOException, ServletException {
        // User is logged but not admin:
        localUserServiceTestHelper.setEnvIsLoggedIn(true);
        localUserServiceTestHelper.setEnvIsAdmin(false);
        // User is Omer Simpson:
        localUserServiceTestHelper.setEnvAuthDomain("gmail.com");
        localUserServiceTestHelper.setEnvEmail("omer.simpson@gmail.com");

        servlet.doPost(httpRequest, httpResponse);

        // Should be redirected:
        verify(httpResponse).sendRedirect("/index.jsp");

        // Omer can only purge his samples:
        shouldRemainSamples(2);
    }

    @Test
    public void shouldBeRejectedWhenNotLogged() throws IOException, ServletException {
        // User is not logged:
        localUserServiceTestHelper.setEnvIsLoggedIn(false);
        localUserServiceTestHelper.setEnvIsAdmin(false);

        servlet.doPost(httpRequest, httpResponse);

        // Should get an error:
        verify(httpResponse, atLeastOnce()).sendError(eq(403), anyString());
        assertThat(output.toString(), equalTo(""));

        // No sample should have been purged:
        shouldRemainSamples(3);
    }

    private void noSampleShouldRemain() {
        List<Sample> samples = (List<Sample>) persistenceManager.newQuery(Sample.class).execute();
        assertThat("no sample should remain", samples.size(), equalTo(0));
    }

    private void shouldRemainSamples(int nb) {
        List<Sample> samples = (List<Sample>) persistenceManager.newQuery(Sample.class).execute();
        assertThat("remain samples", samples.size(), equalTo(nb));
    }


}

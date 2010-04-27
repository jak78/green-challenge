package com.octo.greenchallenge.collect.api;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.octo.greenchallenge.collect.api.gae.GAEServicesImpl;
import org.junit.After;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Common code for API test classes.
 */
public abstract class ServletTest {

    @Mock
    HttpServletRequest httpRequest;
    @Mock
    HttpServletResponse httpResponse;
    @Mock
    ServletContext svContext;
    @Mock
    ServletConfig svConfig;

    StringWriter output;

    LocalServiceTestHelper localPersistenceServiceTestHelper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    protected LocalServiceTestHelper localUserServiceTestHelper =
            new LocalServiceTestHelper(new LocalUserServiceTestConfig());

    PersistenceManager persistenceManager;
    UserService userService;

    Map<String, String[]> httpParams;

    /**
     * Mocks setup.
     *
     * @throws ServletException in case of servlet problem
     * @throws IOException      in case of IO problem
     */
    @Before
    public void commonSetUp() throws ServletException, IOException {

        MockitoAnnotations.initMocks(this);

        // Servlet container mocking:

        output = new StringWriter();

        when(httpResponse.getWriter()).thenReturn(new PrintWriter(output));
        when(svConfig.getServletContext()).thenReturn(svContext);

        httpParams = new HashMap<String, String[]>();
        when(httpRequest.getParameterMap()).thenReturn(httpParams);

        // Google App Engine mocking:
        localPersistenceServiceTestHelper.setUp();
        localUserServiceTestHelper.setUp();
        persistenceManager = new GAEServicesImpl().getPersistenceManager();
        userService = UserServiceFactory.getUserService();
    }

    @After
    public void commonTearDown() {
        localPersistenceServiceTestHelper.tearDown();
        localUserServiceTestHelper.tearDown();
    }

    void shouldLogSomething() {
        verify(svContext).log(anyString(), (Throwable) any());
    }

    void shouldLog(Throwable err) {
        verify(svContext).log(anyString(), eq(err));
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

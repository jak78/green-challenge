package com.octo.greenchallenge.collect.api;

import org.junit.Before;

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

public abstract class ServletTest {

    HttpServletRequest httpRequest;
    HttpServletResponse httpResponse;
    ServletContext svContext;
    ServletConfig svConfig;

    StringWriter output;

    Map<String, String[]> httpParams;

    @Before
    public void commonSetUp() throws ServletException, IOException {
        httpRequest = mock(HttpServletRequest.class);
        httpResponse = mock(HttpServletResponse.class);
        svContext = mock(ServletContext.class);
        svConfig = mock(ServletConfig.class);
        output = new StringWriter();

        when(httpResponse.getWriter()).thenReturn(new PrintWriter(output));
        when(svConfig.getServletContext()).thenReturn(svContext);

        httpParams = new HashMap<String, String[]>();
        when(httpRequest.getParameterMap()).thenReturn(httpParams);

    }


    void shouldLogSomething() {
        verify(svContext).log(anyString(), (Throwable) any());
    }

    void shouldLog(Throwable err) {
        verify(svContext).log(anyString(), eq(err));
    }

}

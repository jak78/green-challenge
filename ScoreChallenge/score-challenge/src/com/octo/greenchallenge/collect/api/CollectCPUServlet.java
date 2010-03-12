package com.octo.greenchallenge.collect.api;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.octo.greenchallenge.collect.api.StatusCode.*;

/**
 * Servlet that collects a CPU usage measure.
 * TODO doc API HTTP.
 */
public class CollectCPUServlet extends javax.servlet.http.HttpServlet {

    CollectCPUService service = new CollectCPUServiceImpl();

    /**
     * Handle POST HTTP request.
     *
     * @param request  HTTP request
     * @param response HTTP response
     * @throws javax.servlet.ServletException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        Map<String, String[]> httpParams = request.getParameterMap();
        Map<String, String> params = getUniqueParams(httpParams);
        try {
            Sample sample = Sample.build(params);
            service.recordData(sample);
            sendResponse(response, OK, 200);
        } catch (InvalidDataException ex) {
            getServletContext().log("Invalid data with params: " + params + ": " + ex.getMessage(), ex);
            sendResponse(response, INVALID_DATA, 400);
        } catch (Exception ex) {
            getServletContext().log("Technical error with params: " + params, ex);
            sendResponse(response, TECHNICAL_FAILURE, 500);
        }
    }

    private Map<String, String> getUniqueParams(Map<String, String[]> httpParams) {
        Map<String, String> res = new HashMap<String, String>();
        for (String key : httpParams.keySet()) {
            String[] multiValue = httpParams.get(key);
            if (multiValue != null && multiValue.length > 0) {
                res.put(key, multiValue[0]);
            }
        }
        return res;
    }

    private void sendResponse(HttpServletResponse response, StatusCode statusCode, Integer httpStatus) throws IOException {
        response.setStatus(httpStatus);
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(statusCode);
        response.getWriter().flush();
    }

}

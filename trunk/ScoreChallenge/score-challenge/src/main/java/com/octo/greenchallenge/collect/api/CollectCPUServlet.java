package com.octo.greenchallenge.collect.api;

import com.octo.greenchallenge.collect.api.gae.GAEServices;
import com.octo.greenchallenge.collect.api.gae.GAEServicesImpl;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.octo.greenchallenge.collect.api.StatusCode.*;

/**
 * Servlet that collects CPU usage measures.
 *
 * @see #doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
 */
public class CollectCPUServlet extends javax.servlet.http.HttpServlet {

    GAEServices appEngine = new GAEServicesImpl();

    /**
     * <p>Handle POST HTTP request.</p>
     * <p>Accepted HTTP params are:</p>
     * <table>
     * <tr>
     * <th>challengerID</th><td>Challenger ID (email address)</td>
     * </tr>
     * <tr>
     * <th>CPUCycles</th><td>Number of measured CPU Cycles - positive 63 bits integer</td>
     * </tr>
     * <tr>
     * <th>source</th><td>Measure source. Accepted values are SERVER_APP and GREEN_FOX - any other value is rejected.</td>
     * </tr>
     * </table>
     * Any other parameter is ignored.
     * <p/>
     * Response is plain text. Possible responses are :
     * <table>
     * <tr>
     * <th>Response text</th><th>HTTP code</th><th>Explaination</th>
     * </tr>
     * <tr>
     * <th>OK</th><td>200</td><td>Posted measure recorded successfully</td>
     * </tr>
     * <tr>
     * <th>INVALID_DATA</th><td>400</td><td>Data you posted is rejected</td>
     * </tr>
     * <tr>
     * <th>TECHNICAL_FAILURE</th><td>500</td><td>A server error occured.</td>
     * </tr>
     * </table>
     *
     * @param request  HTTP request
     * @param response HTTP response
     * @throws javax.servlet.ServletException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        Map<String, String[]> httpParams = request.getParameterMap();
        Map<String, String> params = getUniqueParams(httpParams);
        log("sample received: " + params);
        try {
            Sample sample = Sample.build(params);
            recordData(sample);
            sendResponse(response, OK, 200);
        } catch (InvalidDataException ex) {
            getServletContext().log("Invalid data with params: " + params + ": " + ex.getMessage(), ex);
            sendResponse(response, INVALID_DATA, 400);
        } catch (Exception ex) {
            getServletContext().log("Technical error with params: " + params, ex);
            sendResponse(response, TECHNICAL_FAILURE, 500);
        }
    }

    /**
     * When asked for request parameters, the servlet container gives us a Map of String arrays. Keys are parameter names,
     * and String arrays contains the multi-valued HTTP params.
     * Our API doesn't handle multi-valued parameters, so this method picks only the first value of each HTTP param and
     * gives a simpler Map structure.
     *
     * @param httpParams multi-valued params map
     * @return mono-valued params map
     */
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

    private void recordData(Sample recordedSample) {
        PersistenceManager pm = appEngine.getPersistenceManager();
        try {
            recordedSample.setTimestamp(new Date());
            pm.makePersistent(recordedSample);
        } finally {
            pm.close();
        }
    }


}

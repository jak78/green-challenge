package com.octo.greenchallenge.collect.api;

import com.google.appengine.api.users.UserService;
import com.octo.greenchallenge.collect.api.gae.GAEServices;
import com.octo.greenchallenge.collect.api.gae.GAEServicesImpl;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Dumps all sample records.
 */
public class DumpCPUServlet extends HttpServlet {

    GAEServices appEngine = new GAEServicesImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        if (!isUserAdmin()) {
            response.sendError(403, "You must be signed as admin to do this");
        } else {
            List<Sample> allSamples = dumpAllRecordedSamples();
            for (Sample s : allSamples) {
                PrintWriter out = response.getWriter();
                writeSample(s, out);
            }
        }
    }

    /**
     * Writes a sample in text format.
     *
     * @param s   sample to write
     * @param out buffer to write into
     */
    private void writeSample(Sample s, PrintWriter out) {
        String formattedTS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(s.getTimestamp());
        out.printf("%s\t%d\t%s\t%s%n", s.getChallengerID(), s.getCpuCycles(), s.getSource(), formattedTS);
    }

    /**
     * Get all recorded samples from database.
     *
     * @return all samples
     */
    @SuppressWarnings("unchecked")
    private List<Sample> dumpAllRecordedSamples() {
        PersistenceManager pm = appEngine.getPersistenceManager();
        try {
            Query q = pm.newQuery("select from " + Sample.class.getName());
            List<Sample> resQ = (List<Sample>) q.execute();
            return (List<Sample>) pm.detachCopyAll(resQ);
        } finally {
            pm.close();
        }
    }

    /**
     * Test if user is admin
     *
     * @return true if current user is logged and is admin
     */
    private boolean isUserAdmin() {
        UserService userService = appEngine.getUserService();
        return userService.isUserLoggedIn() && userService.isUserAdmin();
    }

}

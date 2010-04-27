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

        UserService userService = appEngine.getUserService();
        if (!userService.isUserLoggedIn()) {
            response.sendError(403, "You must be signed to do this");
        } else {
            List<Sample> allSamples;
            if( userService.isUserAdmin() ) {
                // Application administrator can see all samples.
                allSamples = dumpAllRecordedSamples();
            } else {
                // Users can see their samples.
                String challengerID = userService.getCurrentUser().getEmail();
                allSamples = dumpMyRecordedSamples(challengerID);
            }
            // Dumps samples to the browser:
            for (Sample s : allSamples) {
                PrintWriter out = response.getWriter();
                writeSample(s, out);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private List<Sample> dumpMyRecordedSamples(String challengerID) {
        PersistenceManager pm = appEngine.getPersistenceManager();
        try {
            Query q = pm.newQuery(Sample.class);
            q.setFilter("challengerID == me");
            q.declareParameters("String me");
            List<Sample> resQ = (List<Sample>) q.execute(challengerID);
            return (List<Sample>) pm.detachCopyAll(resQ);
        } finally {
            pm.close();
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
            Query q = pm.newQuery(Sample.class);
            List<Sample> resQ = (List<Sample>) q.execute();
            return (List<Sample>) pm.detachCopyAll(resQ);
        } finally {
            pm.close();
        }
    }

}

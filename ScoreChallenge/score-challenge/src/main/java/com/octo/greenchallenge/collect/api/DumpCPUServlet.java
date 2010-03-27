package com.octo.greenchallenge.collect.api;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Dumps all sample records.
 */
public class DumpCPUServlet extends HttpServlet {

    GAEServices service = new GAEServicesImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        if( ! service.isUserAdmin() ) {
            response.sendError(403,"You must be signed as admin to do this");
        } else {
            List<Sample> allSamples = service.dumpAllRecordedSamples();
            // TODO tests avec accents dans samples
            for( Sample s : allSamples ) {
                PrintWriter out = response.getWriter();
                writeSample(s, out);
            }
        }
    }

    private void writeSample(Sample s, PrintWriter out) {
        out.printf("%s\t%d\t%s\t%tc%n",s.getChallengerID(),s.getCpuCycles(),s.getSource(),s.getTimestamp());
    }
}

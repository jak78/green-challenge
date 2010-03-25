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

    CollectCPUService service = new CollectCPUServiceImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Sample> allSamples = service.dumpAllRecordedSamples();
        response.setContentType("text/plain");
        // TODO tests avec accents dans samples
        // TODO tests U
        for( Sample s : allSamples ) {
            PrintWriter out = response.getWriter();
            writeSample(s, out);
        }
    }

    private void writeSample(Sample s, PrintWriter out) {
        out.printf("%s\t%d\t%s%n",s.getChallengerID(),s.getCpuCycles(),s.getSource());
    }
}

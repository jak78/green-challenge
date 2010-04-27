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
import java.util.Collection;

/**
 * Purge sample records.
 */
@SuppressWarnings("unchecked")
public class PurgeCPUServlet extends HttpServlet {

    GAEServices appEngine = new GAEServicesImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        UserService us = appEngine.getUserService();
        PersistenceManager pm = appEngine.getPersistenceManager();

        // Is user logged-in?
        if( ! us.isUserLoggedIn() ) {
            response.sendError(403,"You must be signed to do this");
        } else {
            // Get samples to purge:
            Query q = pm.newQuery(Sample.class);
            Collection<Sample> samplesToPurge;
            if( ! us.isUserAdmin()) {
                String userId = us.getCurrentUser().getEmail();
                q.setFilter("challengerID == me");
                q.declareParameters("String me");
                samplesToPurge = (Collection<Sample>)q.execute(userId);
            } else {
                samplesToPurge = (Collection<Sample>)q.execute();
            }

            // Purge samples:
            pm.deletePersistentAll(samplesToPurge);

            // Redirect:
            response.sendRedirect("/index.jsp");
        }

    }
}

package com.octo.greenchallenge.collect.api;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * {@inheritDoc}
 */
public class GAEServicesImpl implements GAEServices {

    /**
     * {@inheritDoc}
     */
    public void recordData(Sample recordedSample) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            recordedSample.setTimestamp(new Date());
            pm.makePersistent(recordedSample);
        } finally {
            pm.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<Sample> dumpAllRecordedSamples() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            Query q = pm.newQuery("select from " + Sample.class.getName());
            List<Sample> resQ = (List<Sample>) q.execute();
            return (List<Sample>) pm.detachCopyAll(resQ);
        } finally {
            pm.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isUserAdmin() {
        UserService userService = UserServiceFactory.getUserService();
        return userService.isUserLoggedIn() && userService.isUserAdmin();
    }
}

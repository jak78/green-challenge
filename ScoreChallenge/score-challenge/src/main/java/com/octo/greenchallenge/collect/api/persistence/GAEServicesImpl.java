package com.octo.greenchallenge.collect.api.persistence;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

/**
 * {@inheritDoc}
 */
public class GAEServicesImpl implements GAEServices {
    private static final PersistenceManagerFactory pmfInstance =
            JDOHelper.getPersistenceManagerFactory("transactions-optional");

    public PersistenceManager getPersistenceManager() {
        return pmfInstance.getPersistenceManager();
    }

    public UserService getUserService() {
        return UserServiceFactory.getUserService();
    }
}

package com.octo.greenchallenge.collect.api.gae;

import com.google.appengine.api.users.UserService;

import javax.jdo.PersistenceManager;

/**
 * Google APIs acces through an interface in order to easily mock GAE for unit tests.
 */
public interface GAEServices {

    /**
     * Get an instance of JDO PersistenceManager.
     *
     * @return persistence manager
     */
    PersistenceManager getPersistenceManager();

    /**
     * Get an instance of Google User Service.
     * @return user service
     */
    UserService getUserService();
}

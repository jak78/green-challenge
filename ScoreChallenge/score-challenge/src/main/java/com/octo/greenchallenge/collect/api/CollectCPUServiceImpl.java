package com.octo.greenchallenge.collect.api;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import org.datanucleus.jpa.JPAQuery;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * {@inheritDoc}
 */
// FIXME rename
public class CollectCPUServiceImpl implements CollectCPUService {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("transactions-optional");

    /**
     * {@inheritDoc}
     */
    public void recordData(Sample recordedSample) {
        EntityManager em = emf.createEntityManager();
        try {
            recordedSample.setTimestamp(new Date());
            em.persist(recordedSample);
        } finally {
            em.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<Sample> dumpAllRecordedSamples() {
        EntityManager em = emf.createEntityManager();
        try {
            Query q = em.createQuery("select s from Sample s");
            List<Sample> resQ = q.getResultList();
            // FIXME fetch eager
            List<Sample> res = new ArrayList<Sample>();
            for( Sample s : resQ ) {
                res.add( new Sample(s) );
            }
            return res;
//            return resQ;
        } finally {
            em.close();
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

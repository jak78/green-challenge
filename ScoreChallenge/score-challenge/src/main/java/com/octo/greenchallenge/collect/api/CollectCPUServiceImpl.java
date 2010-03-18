package com.octo.greenchallenge.collect.api;

import org.datanucleus.jpa.JPAQuery;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

/**
 * {@inheritDoc}
 */
public class CollectCPUServiceImpl implements CollectCPUService {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("transactions-optional");

    /**
     * {@inheritDoc}
     */
    public void recordData(Sample recordedSample) {
        EntityManager em = emf.createEntityManager();
        try {
            em.persist(recordedSample);
        } finally {
            em.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    public String dumpAllRecordedSamples() {
        EntityManager em = emf.createEntityManager();
        try {
            Query q = em.createQuery("select s from Sample s");
            List<Sample> res = q.getResultList();
            return res.toString();
        } finally {
            em.close();
        }
    }

}

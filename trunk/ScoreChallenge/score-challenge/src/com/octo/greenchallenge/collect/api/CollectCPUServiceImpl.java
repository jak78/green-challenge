package com.octo.greenchallenge.collect.api;

import org.datanucleus.jpa.JPAQuery;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

/**
 * TODO doc
 */
public class CollectCPUServiceImpl implements CollectCPUService {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("transactions-optional");

    /**
     * TODO doc
     *
     * @param recordedSample
     * @return
     */
    public void recordData(Sample recordedSample) {
        EntityManager em = emf.createEntityManager();
        try {
            em.persist(recordedSample);
        } finally {
            em.close();
        }
    }

    public String dumpAllRecordedSamples() {
        EntityManager em = emf.createEntityManager();
        try {
            Query q = em.createQuery("select s from CollectedSample s");
            List<Sample> res = q.getResultList();
            return res.toString();
        } finally {
            em.close();
        }
    }

}

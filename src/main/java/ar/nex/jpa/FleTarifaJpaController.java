/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.jpa;

import ar.nex.entity.FleTarifa;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Renzo
 */
public class FleTarifaJpaController implements Serializable {

    public FleTarifaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(FleTarifa fleTarifa) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(fleTarifa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(FleTarifa fleTarifa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            fleTarifa = em.merge(fleTarifa);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = fleTarifa.getIdTarifa();
                if (findFleTarifa(id) == null) {
                    throw new NonexistentEntityException("The fleTarifa with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FleTarifa fleTarifa;
            try {
                fleTarifa = em.getReference(FleTarifa.class, id);
                fleTarifa.getIdTarifa();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The fleTarifa with id " + id + " no longer exists.", enfe);
            }
            em.remove(fleTarifa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<FleTarifa> findFleTarifaEntities() {
        return findFleTarifaEntities(true, -1, -1);
    }

    public List<FleTarifa> findFleTarifaEntities(int maxResults, int firstResult) {
        return findFleTarifaEntities(false, maxResults, firstResult);
    }

    private List<FleTarifa> findFleTarifaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FleTarifa.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public FleTarifa findFleTarifa(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FleTarifa.class, id);
        } finally {
            em.close();
        }
    }

    public int getFleTarifaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FleTarifa> rt = cq.from(FleTarifa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

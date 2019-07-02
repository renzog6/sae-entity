/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.jpa;

import ar.nex.entity.empleado.EmpleadoPuesto;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import ar.nex.jpa.exceptions.PreexistingEntityException;
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
public class EmpleadoPuestoJpaController implements Serializable {

    public EmpleadoPuestoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EmpleadoPuesto empleadoPuesto) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(empleadoPuesto);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEmpleadoPuesto(empleadoPuesto.getIdPuesto()) != null) {
                throw new PreexistingEntityException("EmpleadoPuesto " + empleadoPuesto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EmpleadoPuesto empleadoPuesto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            empleadoPuesto = em.merge(empleadoPuesto);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = empleadoPuesto.getIdPuesto();
                if (findEmpleadoPuesto(id) == null) {
                    throw new NonexistentEntityException("The empleadoPuesto with id " + id + " no longer exists.");
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
            EmpleadoPuesto empleadoPuesto;
            try {
                empleadoPuesto = em.getReference(EmpleadoPuesto.class, id);
                empleadoPuesto.getIdPuesto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empleadoPuesto with id " + id + " no longer exists.", enfe);
            }
            em.remove(empleadoPuesto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EmpleadoPuesto> findEmpleadoPuestoEntities() {
        return findEmpleadoPuestoEntities(true, -1, -1);
    }

    public List<EmpleadoPuesto> findEmpleadoPuestoEntities(int maxResults, int firstResult) {
        return findEmpleadoPuestoEntities(false, maxResults, firstResult);
    }

    private List<EmpleadoPuesto> findEmpleadoPuestoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EmpleadoPuesto.class));
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

    public EmpleadoPuesto findEmpleadoPuesto(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EmpleadoPuesto.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpleadoPuestoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EmpleadoPuesto> rt = cq.from(EmpleadoPuesto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

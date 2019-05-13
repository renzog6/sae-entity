/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.jpa;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ar.nex.entity.CerEspecie;
import ar.nex.entity.CerVariedad;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class CerVariedadJpaController implements Serializable {

    public CerVariedadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CerVariedad cerVariedad) {
        if (cerVariedad.getCerEspecieList() == null) {
            cerVariedad.setCerEspecieList(new ArrayList<CerEspecie>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<CerEspecie> attachedCerEspecieList = new ArrayList<CerEspecie>();
            for (CerEspecie cerEspecieListCerEspecieToAttach : cerVariedad.getCerEspecieList()) {
                cerEspecieListCerEspecieToAttach = em.getReference(cerEspecieListCerEspecieToAttach.getClass(), cerEspecieListCerEspecieToAttach.getIdEspecie());
                attachedCerEspecieList.add(cerEspecieListCerEspecieToAttach);
            }
            cerVariedad.setCerEspecieList(attachedCerEspecieList);
            em.persist(cerVariedad);
            for (CerEspecie cerEspecieListCerEspecie : cerVariedad.getCerEspecieList()) {
                cerEspecieListCerEspecie.getCerVariedadList().add(cerVariedad);
                cerEspecieListCerEspecie = em.merge(cerEspecieListCerEspecie);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CerVariedad cerVariedad) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CerVariedad persistentCerVariedad = em.find(CerVariedad.class, cerVariedad.getIdVariedead());
            List<CerEspecie> cerEspecieListOld = persistentCerVariedad.getCerEspecieList();
            List<CerEspecie> cerEspecieListNew = cerVariedad.getCerEspecieList();
            List<CerEspecie> attachedCerEspecieListNew = new ArrayList<CerEspecie>();
            for (CerEspecie cerEspecieListNewCerEspecieToAttach : cerEspecieListNew) {
                cerEspecieListNewCerEspecieToAttach = em.getReference(cerEspecieListNewCerEspecieToAttach.getClass(), cerEspecieListNewCerEspecieToAttach.getIdEspecie());
                attachedCerEspecieListNew.add(cerEspecieListNewCerEspecieToAttach);
            }
            cerEspecieListNew = attachedCerEspecieListNew;
            cerVariedad.setCerEspecieList(cerEspecieListNew);
            cerVariedad = em.merge(cerVariedad);
            for (CerEspecie cerEspecieListOldCerEspecie : cerEspecieListOld) {
                if (!cerEspecieListNew.contains(cerEspecieListOldCerEspecie)) {
                    cerEspecieListOldCerEspecie.getCerVariedadList().remove(cerVariedad);
                    cerEspecieListOldCerEspecie = em.merge(cerEspecieListOldCerEspecie);
                }
            }
            for (CerEspecie cerEspecieListNewCerEspecie : cerEspecieListNew) {
                if (!cerEspecieListOld.contains(cerEspecieListNewCerEspecie)) {
                    cerEspecieListNewCerEspecie.getCerVariedadList().add(cerVariedad);
                    cerEspecieListNewCerEspecie = em.merge(cerEspecieListNewCerEspecie);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = cerVariedad.getIdVariedead();
                if (findCerVariedad(id) == null) {
                    throw new NonexistentEntityException("The cerVariedad with id " + id + " no longer exists.");
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
            CerVariedad cerVariedad;
            try {
                cerVariedad = em.getReference(CerVariedad.class, id);
                cerVariedad.getIdVariedead();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cerVariedad with id " + id + " no longer exists.", enfe);
            }
            List<CerEspecie> cerEspecieList = cerVariedad.getCerEspecieList();
            for (CerEspecie cerEspecieListCerEspecie : cerEspecieList) {
                cerEspecieListCerEspecie.getCerVariedadList().remove(cerVariedad);
                cerEspecieListCerEspecie = em.merge(cerEspecieListCerEspecie);
            }
            em.remove(cerVariedad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CerVariedad> findCerVariedadEntities() {
        return findCerVariedadEntities(true, -1, -1);
    }

    public List<CerVariedad> findCerVariedadEntities(int maxResults, int firstResult) {
        return findCerVariedadEntities(false, maxResults, firstResult);
    }

    private List<CerVariedad> findCerVariedadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CerVariedad.class));
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

    public CerVariedad findCerVariedad(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CerVariedad.class, id);
        } finally {
            em.close();
        }
    }

    public int getCerVariedadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CerVariedad> rt = cq.from(CerVariedad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.jpa;

import ar.nex.entity.CerEspecie;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
public class CerEspecieJpaController implements Serializable {

    public CerEspecieJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CerEspecie cerEspecie) {
        if (cerEspecie.getCerVariedadList() == null) {
            cerEspecie.setCerVariedadList(new ArrayList<CerVariedad>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<CerVariedad> attachedCerVariedadList = new ArrayList<CerVariedad>();
            for (CerVariedad cerVariedadListCerVariedadToAttach : cerEspecie.getCerVariedadList()) {
                cerVariedadListCerVariedadToAttach = em.getReference(cerVariedadListCerVariedadToAttach.getClass(), cerVariedadListCerVariedadToAttach.getIdVariedead());
                attachedCerVariedadList.add(cerVariedadListCerVariedadToAttach);
            }
            cerEspecie.setCerVariedadList(attachedCerVariedadList);
            em.persist(cerEspecie);
            for (CerVariedad cerVariedadListCerVariedad : cerEspecie.getCerVariedadList()) {
                cerVariedadListCerVariedad.getCerEspecieList().add(cerEspecie);
                cerVariedadListCerVariedad = em.merge(cerVariedadListCerVariedad);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CerEspecie cerEspecie) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CerEspecie persistentCerEspecie = em.find(CerEspecie.class, cerEspecie.getIdEspecie());
            List<CerVariedad> cerVariedadListOld = persistentCerEspecie.getCerVariedadList();
            List<CerVariedad> cerVariedadListNew = cerEspecie.getCerVariedadList();
            List<CerVariedad> attachedCerVariedadListNew = new ArrayList<CerVariedad>();
            for (CerVariedad cerVariedadListNewCerVariedadToAttach : cerVariedadListNew) {
                cerVariedadListNewCerVariedadToAttach = em.getReference(cerVariedadListNewCerVariedadToAttach.getClass(), cerVariedadListNewCerVariedadToAttach.getIdVariedead());
                attachedCerVariedadListNew.add(cerVariedadListNewCerVariedadToAttach);
            }
            cerVariedadListNew = attachedCerVariedadListNew;
            cerEspecie.setCerVariedadList(cerVariedadListNew);
            cerEspecie = em.merge(cerEspecie);
            for (CerVariedad cerVariedadListOldCerVariedad : cerVariedadListOld) {
                if (!cerVariedadListNew.contains(cerVariedadListOldCerVariedad)) {
                    cerVariedadListOldCerVariedad.getCerEspecieList().remove(cerEspecie);
                    cerVariedadListOldCerVariedad = em.merge(cerVariedadListOldCerVariedad);
                }
            }
            for (CerVariedad cerVariedadListNewCerVariedad : cerVariedadListNew) {
                if (!cerVariedadListOld.contains(cerVariedadListNewCerVariedad)) {
                    cerVariedadListNewCerVariedad.getCerEspecieList().add(cerEspecie);
                    cerVariedadListNewCerVariedad = em.merge(cerVariedadListNewCerVariedad);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = cerEspecie.getIdEspecie();
                if (findCerEspecie(id) == null) {
                    throw new NonexistentEntityException("The cerEspecie with id " + id + " no longer exists.");
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
            CerEspecie cerEspecie;
            try {
                cerEspecie = em.getReference(CerEspecie.class, id);
                cerEspecie.getIdEspecie();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cerEspecie with id " + id + " no longer exists.", enfe);
            }
            List<CerVariedad> cerVariedadList = cerEspecie.getCerVariedadList();
            for (CerVariedad cerVariedadListCerVariedad : cerVariedadList) {
                cerVariedadListCerVariedad.getCerEspecieList().remove(cerEspecie);
                cerVariedadListCerVariedad = em.merge(cerVariedadListCerVariedad);
            }
            em.remove(cerEspecie);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CerEspecie> findCerEspecieEntities() {
        return findCerEspecieEntities(true, -1, -1);
    }

    public List<CerEspecie> findCerEspecieEntities(int maxResults, int firstResult) {
        return findCerEspecieEntities(false, maxResults, firstResult);
    }

    private List<CerEspecie> findCerEspecieEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CerEspecie.class));
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

    public CerEspecie findCerEspecie(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CerEspecie.class, id);
        } finally {
            em.close();
        }
    }

    public int getCerEspecieCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CerEspecie> rt = cq.from(CerEspecie.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

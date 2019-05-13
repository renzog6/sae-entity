/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.jpa;

import ar.nex.entity.AuxItem;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ar.nex.entity.Marca;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class AuxItemJpaController implements Serializable {

    public AuxItemJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AuxItem auxItem) {
        if (auxItem.getMarcaList() == null) {
            auxItem.setMarcaList(new ArrayList<Marca>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Marca> attachedMarcaList = new ArrayList<Marca>();
            for (Marca marcaListMarcaToAttach : auxItem.getMarcaList()) {
                marcaListMarcaToAttach = em.getReference(marcaListMarcaToAttach.getClass(), marcaListMarcaToAttach.getIdMarca());
                attachedMarcaList.add(marcaListMarcaToAttach);
            }
            auxItem.setMarcaList(attachedMarcaList);
            em.persist(auxItem);
            for (Marca marcaListMarca : auxItem.getMarcaList()) {
                marcaListMarca.getAuxItemList().add(auxItem);
                marcaListMarca = em.merge(marcaListMarca);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AuxItem auxItem) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AuxItem persistentAuxItem = em.find(AuxItem.class, auxItem.getIdItem());
            List<Marca> marcaListOld = persistentAuxItem.getMarcaList();
            List<Marca> marcaListNew = auxItem.getMarcaList();
            List<Marca> attachedMarcaListNew = new ArrayList<Marca>();
            for (Marca marcaListNewMarcaToAttach : marcaListNew) {
                marcaListNewMarcaToAttach = em.getReference(marcaListNewMarcaToAttach.getClass(), marcaListNewMarcaToAttach.getIdMarca());
                attachedMarcaListNew.add(marcaListNewMarcaToAttach);
            }
            marcaListNew = attachedMarcaListNew;
            auxItem.setMarcaList(marcaListNew);
            auxItem = em.merge(auxItem);
            for (Marca marcaListOldMarca : marcaListOld) {
                if (!marcaListNew.contains(marcaListOldMarca)) {
                    marcaListOldMarca.getAuxItemList().remove(auxItem);
                    marcaListOldMarca = em.merge(marcaListOldMarca);
                }
            }
            for (Marca marcaListNewMarca : marcaListNew) {
                if (!marcaListOld.contains(marcaListNewMarca)) {
                    marcaListNewMarca.getAuxItemList().add(auxItem);
                    marcaListNewMarca = em.merge(marcaListNewMarca);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = auxItem.getIdItem();
                if (findAuxItem(id) == null) {
                    throw new NonexistentEntityException("The auxItem with id " + id + " no longer exists.");
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
            AuxItem auxItem;
            try {
                auxItem = em.getReference(AuxItem.class, id);
                auxItem.getIdItem();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The auxItem with id " + id + " no longer exists.", enfe);
            }
            List<Marca> marcaList = auxItem.getMarcaList();
            for (Marca marcaListMarca : marcaList) {
                marcaListMarca.getAuxItemList().remove(auxItem);
                marcaListMarca = em.merge(marcaListMarca);
            }
            em.remove(auxItem);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AuxItem> findAuxItemEntities() {
        return findAuxItemEntities(true, -1, -1);
    }

    public List<AuxItem> findAuxItemEntities(int maxResults, int firstResult) {
        return findAuxItemEntities(false, maxResults, firstResult);
    }

    private List<AuxItem> findAuxItemEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AuxItem.class));
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

    public AuxItem findAuxItem(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AuxItem.class, id);
        } finally {
            em.close();
        }
    }

    public int getAuxItemCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AuxItem> rt = cq.from(AuxItem.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

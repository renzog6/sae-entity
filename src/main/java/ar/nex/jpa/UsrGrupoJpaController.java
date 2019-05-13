/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.jpa;

import ar.nex.entity.UsrGrupo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ar.nex.entity.Usuario;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class UsrGrupoJpaController implements Serializable {

    public UsrGrupoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UsrGrupo usrGrupo) {
        if (usrGrupo.getUsuarioList() == null) {
            usrGrupo.setUsuarioList(new ArrayList<Usuario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Usuario> attachedUsuarioList = new ArrayList<Usuario>();
            for (Usuario usuarioListUsuarioToAttach : usrGrupo.getUsuarioList()) {
                usuarioListUsuarioToAttach = em.getReference(usuarioListUsuarioToAttach.getClass(), usuarioListUsuarioToAttach.getIdUsuario());
                attachedUsuarioList.add(usuarioListUsuarioToAttach);
            }
            usrGrupo.setUsuarioList(attachedUsuarioList);
            em.persist(usrGrupo);
            for (Usuario usuarioListUsuario : usrGrupo.getUsuarioList()) {
                UsrGrupo oldGrupoOfUsuarioListUsuario = usuarioListUsuario.getGrupo();
                usuarioListUsuario.setGrupo(usrGrupo);
                usuarioListUsuario = em.merge(usuarioListUsuario);
                if (oldGrupoOfUsuarioListUsuario != null) {
                    oldGrupoOfUsuarioListUsuario.getUsuarioList().remove(usuarioListUsuario);
                    oldGrupoOfUsuarioListUsuario = em.merge(oldGrupoOfUsuarioListUsuario);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UsrGrupo usrGrupo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsrGrupo persistentUsrGrupo = em.find(UsrGrupo.class, usrGrupo.getIdGrupo());
            List<Usuario> usuarioListOld = persistentUsrGrupo.getUsuarioList();
            List<Usuario> usuarioListNew = usrGrupo.getUsuarioList();
            List<Usuario> attachedUsuarioListNew = new ArrayList<Usuario>();
            for (Usuario usuarioListNewUsuarioToAttach : usuarioListNew) {
                usuarioListNewUsuarioToAttach = em.getReference(usuarioListNewUsuarioToAttach.getClass(), usuarioListNewUsuarioToAttach.getIdUsuario());
                attachedUsuarioListNew.add(usuarioListNewUsuarioToAttach);
            }
            usuarioListNew = attachedUsuarioListNew;
            usrGrupo.setUsuarioList(usuarioListNew);
            usrGrupo = em.merge(usrGrupo);
            for (Usuario usuarioListOldUsuario : usuarioListOld) {
                if (!usuarioListNew.contains(usuarioListOldUsuario)) {
                    usuarioListOldUsuario.setGrupo(null);
                    usuarioListOldUsuario = em.merge(usuarioListOldUsuario);
                }
            }
            for (Usuario usuarioListNewUsuario : usuarioListNew) {
                if (!usuarioListOld.contains(usuarioListNewUsuario)) {
                    UsrGrupo oldGrupoOfUsuarioListNewUsuario = usuarioListNewUsuario.getGrupo();
                    usuarioListNewUsuario.setGrupo(usrGrupo);
                    usuarioListNewUsuario = em.merge(usuarioListNewUsuario);
                    if (oldGrupoOfUsuarioListNewUsuario != null && !oldGrupoOfUsuarioListNewUsuario.equals(usrGrupo)) {
                        oldGrupoOfUsuarioListNewUsuario.getUsuarioList().remove(usuarioListNewUsuario);
                        oldGrupoOfUsuarioListNewUsuario = em.merge(oldGrupoOfUsuarioListNewUsuario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = usrGrupo.getIdGrupo();
                if (findUsrGrupo(id) == null) {
                    throw new NonexistentEntityException("The usrGrupo with id " + id + " no longer exists.");
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
            UsrGrupo usrGrupo;
            try {
                usrGrupo = em.getReference(UsrGrupo.class, id);
                usrGrupo.getIdGrupo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usrGrupo with id " + id + " no longer exists.", enfe);
            }
            List<Usuario> usuarioList = usrGrupo.getUsuarioList();
            for (Usuario usuarioListUsuario : usuarioList) {
                usuarioListUsuario.setGrupo(null);
                usuarioListUsuario = em.merge(usuarioListUsuario);
            }
            em.remove(usrGrupo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UsrGrupo> findUsrGrupoEntities() {
        return findUsrGrupoEntities(true, -1, -1);
    }

    public List<UsrGrupo> findUsrGrupoEntities(int maxResults, int firstResult) {
        return findUsrGrupoEntities(false, maxResults, firstResult);
    }

    private List<UsrGrupo> findUsrGrupoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UsrGrupo.class));
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

    public UsrGrupo findUsrGrupo(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UsrGrupo.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsrGrupoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UsrGrupo> rt = cq.from(UsrGrupo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

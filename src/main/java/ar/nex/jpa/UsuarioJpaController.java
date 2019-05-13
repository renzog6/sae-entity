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
import ar.nex.entity.UsrGrupo;
import ar.nex.entity.Persona;
import ar.nex.entity.UsrMenu;
import java.util.ArrayList;
import java.util.List;
import ar.nex.entity.UsrHistorial;
import ar.nex.entity.Usuario;
import ar.nex.jpa.exceptions.IllegalOrphanException;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws IllegalOrphanException {
        if (usuario.getUsrMenuList() == null) {
            usuario.setUsrMenuList(new ArrayList<UsrMenu>());
        }
        if (usuario.getUsrHistorialList() == null) {
            usuario.setUsrHistorialList(new ArrayList<UsrHistorial>());
        }
        List<String> illegalOrphanMessages = null;
        Persona personaOrphanCheck = usuario.getPersona();
        if (personaOrphanCheck != null) {
            Usuario oldUsuarioOfPersona = personaOrphanCheck.getUsuario();
            if (oldUsuarioOfPersona != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Persona " + personaOrphanCheck + " already has an item of type Usuario whose persona column cannot be null. Please make another selection for the persona field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsrGrupo grupo = usuario.getGrupo();
            if (grupo != null) {
                grupo = em.getReference(grupo.getClass(), grupo.getIdGrupo());
                usuario.setGrupo(grupo);
            }
            Persona persona = usuario.getPersona();
            if (persona != null) {
                persona = em.getReference(persona.getClass(), persona.getIdPersona());
                usuario.setPersona(persona);
            }
            List<UsrMenu> attachedUsrMenuList = new ArrayList<UsrMenu>();
            for (UsrMenu usrMenuListUsrMenuToAttach : usuario.getUsrMenuList()) {
                usrMenuListUsrMenuToAttach = em.getReference(usrMenuListUsrMenuToAttach.getClass(), usrMenuListUsrMenuToAttach.getIdMenu());
                attachedUsrMenuList.add(usrMenuListUsrMenuToAttach);
            }
            usuario.setUsrMenuList(attachedUsrMenuList);
            List<UsrHistorial> attachedUsrHistorialList = new ArrayList<UsrHistorial>();
            for (UsrHistorial usrHistorialListUsrHistorialToAttach : usuario.getUsrHistorialList()) {
                usrHistorialListUsrHistorialToAttach = em.getReference(usrHistorialListUsrHistorialToAttach.getClass(), usrHistorialListUsrHistorialToAttach.getIdHistorial());
                attachedUsrHistorialList.add(usrHistorialListUsrHistorialToAttach);
            }
            usuario.setUsrHistorialList(attachedUsrHistorialList);
            em.persist(usuario);
            if (grupo != null) {
                grupo.getUsuarioList().add(usuario);
                grupo = em.merge(grupo);
            }
            if (persona != null) {
                persona.setUsuario(usuario);
                persona = em.merge(persona);
            }
            for (UsrMenu usrMenuListUsrMenu : usuario.getUsrMenuList()) {
                usrMenuListUsrMenu.getUsuarioList().add(usuario);
                usrMenuListUsrMenu = em.merge(usrMenuListUsrMenu);
            }
            for (UsrHistorial usrHistorialListUsrHistorial : usuario.getUsrHistorialList()) {
                Usuario oldUsuarioOfUsrHistorialListUsrHistorial = usrHistorialListUsrHistorial.getUsuario();
                usrHistorialListUsrHistorial.setUsuario(usuario);
                usrHistorialListUsrHistorial = em.merge(usrHistorialListUsrHistorial);
                if (oldUsuarioOfUsrHistorialListUsrHistorial != null) {
                    oldUsuarioOfUsrHistorialListUsrHistorial.getUsrHistorialList().remove(usrHistorialListUsrHistorial);
                    oldUsuarioOfUsrHistorialListUsrHistorial = em.merge(oldUsuarioOfUsrHistorialListUsrHistorial);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdUsuario());
            UsrGrupo grupoOld = persistentUsuario.getGrupo();
            UsrGrupo grupoNew = usuario.getGrupo();
            Persona personaOld = persistentUsuario.getPersona();
            Persona personaNew = usuario.getPersona();
            List<UsrMenu> usrMenuListOld = persistentUsuario.getUsrMenuList();
            List<UsrMenu> usrMenuListNew = usuario.getUsrMenuList();
            List<UsrHistorial> usrHistorialListOld = persistentUsuario.getUsrHistorialList();
            List<UsrHistorial> usrHistorialListNew = usuario.getUsrHistorialList();
            List<String> illegalOrphanMessages = null;
            if (personaNew != null && !personaNew.equals(personaOld)) {
                Usuario oldUsuarioOfPersona = personaNew.getUsuario();
                if (oldUsuarioOfPersona != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Persona " + personaNew + " already has an item of type Usuario whose persona column cannot be null. Please make another selection for the persona field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (grupoNew != null) {
                grupoNew = em.getReference(grupoNew.getClass(), grupoNew.getIdGrupo());
                usuario.setGrupo(grupoNew);
            }
            if (personaNew != null) {
                personaNew = em.getReference(personaNew.getClass(), personaNew.getIdPersona());
                usuario.setPersona(personaNew);
            }
            List<UsrMenu> attachedUsrMenuListNew = new ArrayList<UsrMenu>();
            for (UsrMenu usrMenuListNewUsrMenuToAttach : usrMenuListNew) {
                usrMenuListNewUsrMenuToAttach = em.getReference(usrMenuListNewUsrMenuToAttach.getClass(), usrMenuListNewUsrMenuToAttach.getIdMenu());
                attachedUsrMenuListNew.add(usrMenuListNewUsrMenuToAttach);
            }
            usrMenuListNew = attachedUsrMenuListNew;
            usuario.setUsrMenuList(usrMenuListNew);
            List<UsrHistorial> attachedUsrHistorialListNew = new ArrayList<UsrHistorial>();
            for (UsrHistorial usrHistorialListNewUsrHistorialToAttach : usrHistorialListNew) {
                usrHistorialListNewUsrHistorialToAttach = em.getReference(usrHistorialListNewUsrHistorialToAttach.getClass(), usrHistorialListNewUsrHistorialToAttach.getIdHistorial());
                attachedUsrHistorialListNew.add(usrHistorialListNewUsrHistorialToAttach);
            }
            usrHistorialListNew = attachedUsrHistorialListNew;
            usuario.setUsrHistorialList(usrHistorialListNew);
            usuario = em.merge(usuario);
            if (grupoOld != null && !grupoOld.equals(grupoNew)) {
                grupoOld.getUsuarioList().remove(usuario);
                grupoOld = em.merge(grupoOld);
            }
            if (grupoNew != null && !grupoNew.equals(grupoOld)) {
                grupoNew.getUsuarioList().add(usuario);
                grupoNew = em.merge(grupoNew);
            }
            if (personaOld != null && !personaOld.equals(personaNew)) {
                personaOld.setUsuario(null);
                personaOld = em.merge(personaOld);
            }
            if (personaNew != null && !personaNew.equals(personaOld)) {
                personaNew.setUsuario(usuario);
                personaNew = em.merge(personaNew);
            }
            for (UsrMenu usrMenuListOldUsrMenu : usrMenuListOld) {
                if (!usrMenuListNew.contains(usrMenuListOldUsrMenu)) {
                    usrMenuListOldUsrMenu.getUsuarioList().remove(usuario);
                    usrMenuListOldUsrMenu = em.merge(usrMenuListOldUsrMenu);
                }
            }
            for (UsrMenu usrMenuListNewUsrMenu : usrMenuListNew) {
                if (!usrMenuListOld.contains(usrMenuListNewUsrMenu)) {
                    usrMenuListNewUsrMenu.getUsuarioList().add(usuario);
                    usrMenuListNewUsrMenu = em.merge(usrMenuListNewUsrMenu);
                }
            }
            for (UsrHistorial usrHistorialListOldUsrHistorial : usrHistorialListOld) {
                if (!usrHistorialListNew.contains(usrHistorialListOldUsrHistorial)) {
                    usrHistorialListOldUsrHistorial.setUsuario(null);
                    usrHistorialListOldUsrHistorial = em.merge(usrHistorialListOldUsrHistorial);
                }
            }
            for (UsrHistorial usrHistorialListNewUsrHistorial : usrHistorialListNew) {
                if (!usrHistorialListOld.contains(usrHistorialListNewUsrHistorial)) {
                    Usuario oldUsuarioOfUsrHistorialListNewUsrHistorial = usrHistorialListNewUsrHistorial.getUsuario();
                    usrHistorialListNewUsrHistorial.setUsuario(usuario);
                    usrHistorialListNewUsrHistorial = em.merge(usrHistorialListNewUsrHistorial);
                    if (oldUsuarioOfUsrHistorialListNewUsrHistorial != null && !oldUsuarioOfUsrHistorialListNewUsrHistorial.equals(usuario)) {
                        oldUsuarioOfUsrHistorialListNewUsrHistorial.getUsrHistorialList().remove(usrHistorialListNewUsrHistorial);
                        oldUsuarioOfUsrHistorialListNewUsrHistorial = em.merge(oldUsuarioOfUsrHistorialListNewUsrHistorial);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = usuario.getIdUsuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            UsrGrupo grupo = usuario.getGrupo();
            if (grupo != null) {
                grupo.getUsuarioList().remove(usuario);
                grupo = em.merge(grupo);
            }
            Persona persona = usuario.getPersona();
            if (persona != null) {
                persona.setUsuario(null);
                persona = em.merge(persona);
            }
            List<UsrMenu> usrMenuList = usuario.getUsrMenuList();
            for (UsrMenu usrMenuListUsrMenu : usrMenuList) {
                usrMenuListUsrMenu.getUsuarioList().remove(usuario);
                usrMenuListUsrMenu = em.merge(usrMenuListUsrMenu);
            }
            List<UsrHistorial> usrHistorialList = usuario.getUsrHistorialList();
            for (UsrHistorial usrHistorialListUsrHistorial : usrHistorialList) {
                usrHistorialListUsrHistorial.setUsuario(null);
                usrHistorialListUsrHistorial = em.merge(usrHistorialListUsrHistorial);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

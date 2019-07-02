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
import ar.nex.entity.empleado.Persona;
import ar.nex.entity.empleado.PersonaFamilia;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class PersonaFamiliaJpaController implements Serializable {

    public PersonaFamiliaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PersonaFamilia personaFamilia) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona datos = personaFamilia.getDatos();
            if (datos != null) {
                datos = em.getReference(datos.getClass(), datos.getIdPersona());
                personaFamilia.setDatos(datos);
            }
            Persona persona = personaFamilia.getPersona();
            if (persona != null) {
                persona = em.getReference(persona.getClass(), persona.getIdPersona());
                personaFamilia.setPersona(persona);
            }
            em.persist(personaFamilia);
            if (datos != null) {
                datos.getFamiliaList().add(personaFamilia);
                datos = em.merge(datos);
            }
            if (persona != null) {
                persona.getFamiliaList().add(personaFamilia);
                persona = em.merge(persona);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PersonaFamilia personaFamilia) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PersonaFamilia persistentPersonaFamilia = em.find(PersonaFamilia.class, personaFamilia.getIdFamilia());
            Persona datosOld = persistentPersonaFamilia.getDatos();
            Persona datosNew = personaFamilia.getDatos();
            Persona personaOld = persistentPersonaFamilia.getPersona();
            Persona personaNew = personaFamilia.getPersona();
            if (datosNew != null) {
                datosNew = em.getReference(datosNew.getClass(), datosNew.getIdPersona());
                personaFamilia.setDatos(datosNew);
            }
            if (personaNew != null) {
                personaNew = em.getReference(personaNew.getClass(), personaNew.getIdPersona());
                personaFamilia.setPersona(personaNew);
            }
            personaFamilia = em.merge(personaFamilia);
            if (datosOld != null && !datosOld.equals(datosNew)) {
                datosOld.getFamiliaList().remove(personaFamilia);
                datosOld = em.merge(datosOld);
            }
            if (datosNew != null && !datosNew.equals(datosOld)) {
                datosNew.getFamiliaList().add(personaFamilia);
                datosNew = em.merge(datosNew);
            }
            if (personaOld != null && !personaOld.equals(personaNew)) {
                personaOld.getFamiliaList().remove(personaFamilia);
                personaOld = em.merge(personaOld);
            }
            if (personaNew != null && !personaNew.equals(personaOld)) {
                personaNew.getFamiliaList().add(personaFamilia);
                personaNew = em.merge(personaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = personaFamilia.getIdFamilia();
                if (findPersonaFamilia(id) == null) {
                    throw new NonexistentEntityException("The personaFamilia with id " + id + " no longer exists.");
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
            PersonaFamilia personaFamilia;
            try {
                personaFamilia = em.getReference(PersonaFamilia.class, id);
                personaFamilia.getIdFamilia();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The personaFamilia with id " + id + " no longer exists.", enfe);
            }
            Persona datos = personaFamilia.getDatos();
            if (datos != null) {
                datos.getFamiliaList().remove(personaFamilia);
                datos = em.merge(datos);
            }
            Persona persona = personaFamilia.getPersona();
            if (persona != null) {
                persona.getFamiliaList().remove(personaFamilia);
                persona = em.merge(persona);
            }
            em.remove(personaFamilia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PersonaFamilia> findPersonaFamiliaEntities() {
        return findPersonaFamiliaEntities(true, -1, -1);
    }

    public List<PersonaFamilia> findPersonaFamiliaEntities(int maxResults, int firstResult) {
        return findPersonaFamiliaEntities(false, maxResults, firstResult);
    }

    private List<PersonaFamilia> findPersonaFamiliaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PersonaFamilia.class));
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

    public PersonaFamilia findPersonaFamilia(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PersonaFamilia.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonaFamiliaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PersonaFamilia> rt = cq.from(PersonaFamilia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

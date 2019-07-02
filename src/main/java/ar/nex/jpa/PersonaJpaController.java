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
import ar.nex.entity.empleado.Empleado;
import ar.nex.entity.Usuario;
import ar.nex.entity.empleado.Persona;
import ar.nex.entity.ubicacion.Contacto;
import java.util.ArrayList;
import java.util.List;
import ar.nex.entity.empleado.PersonaFamilia;
import ar.nex.jpa.exceptions.IllegalOrphanException;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class PersonaJpaController implements Serializable {

    public PersonaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Persona persona) {
        if (persona.getContactoList() == null) {
            persona.setContactoList(new ArrayList<Contacto>());
        }
        if (persona.getFamiliaList() == null) {
            persona.setFamiliaList(new ArrayList<PersonaFamilia>());
        }
        if (persona.getFamiliaList1() == null) {
            persona.setFamiliaList1(new ArrayList<PersonaFamilia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado empleado = persona.getEmpleado();
            if (empleado != null) {
                empleado = em.getReference(empleado.getClass(), empleado.getIdEmpleado());
                persona.setEmpleado(empleado);
            }
            Usuario usuario = persona.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getIdUsuario());
                persona.setUsuario(usuario);
            }
            List<Contacto> attachedContactoList = new ArrayList<Contacto>();
            for (Contacto contactoListContactoToAttach : persona.getContactoList()) {
                contactoListContactoToAttach = em.getReference(contactoListContactoToAttach.getClass(), contactoListContactoToAttach.getIdContacto());
                attachedContactoList.add(contactoListContactoToAttach);
            }
            persona.setContactoList(attachedContactoList);
            List<PersonaFamilia> attachedFamiliaList = new ArrayList<PersonaFamilia>();
            for (PersonaFamilia familiaListPersonaFamiliaToAttach : persona.getFamiliaList()) {
                familiaListPersonaFamiliaToAttach = em.getReference(familiaListPersonaFamiliaToAttach.getClass(), familiaListPersonaFamiliaToAttach.getIdFamilia());
                attachedFamiliaList.add(familiaListPersonaFamiliaToAttach);
            }
            persona.setFamiliaList(attachedFamiliaList);
            List<PersonaFamilia> attachedFamiliaList1 = new ArrayList<PersonaFamilia>();
            for (PersonaFamilia familiaList1PersonaFamiliaToAttach : persona.getFamiliaList1()) {
                familiaList1PersonaFamiliaToAttach = em.getReference(familiaList1PersonaFamiliaToAttach.getClass(), familiaList1PersonaFamiliaToAttach.getIdFamilia());
                attachedFamiliaList1.add(familiaList1PersonaFamiliaToAttach);
            }
            persona.setFamiliaList1(attachedFamiliaList1);
            em.persist(persona);
            if (empleado != null) {
                Persona oldPersonaOfEmpleado = empleado.getPersona();
                if (oldPersonaOfEmpleado != null) {
                    oldPersonaOfEmpleado.setEmpleado(null);
                    oldPersonaOfEmpleado = em.merge(oldPersonaOfEmpleado);
                }
                empleado.setPersona(persona);
                empleado = em.merge(empleado);
            }
            if (usuario != null) {
                Persona oldPersonaOfUsuario = usuario.getPersona();
                if (oldPersonaOfUsuario != null) {
                    oldPersonaOfUsuario.setUsuario(null);
                    oldPersonaOfUsuario = em.merge(oldPersonaOfUsuario);
                }
                usuario.setPersona(persona);
                usuario = em.merge(usuario);
            }
            for (Contacto contactoListContacto : persona.getContactoList()) {
                contactoListContacto.getPersonaList().add(persona);
                contactoListContacto = em.merge(contactoListContacto);
            }
            for (PersonaFamilia familiaListPersonaFamilia : persona.getFamiliaList()) {
                Persona oldDatosOfFamiliaListPersonaFamilia = familiaListPersonaFamilia.getDatos();
                familiaListPersonaFamilia.setDatos(persona);
                familiaListPersonaFamilia = em.merge(familiaListPersonaFamilia);
                if (oldDatosOfFamiliaListPersonaFamilia != null) {
                    oldDatosOfFamiliaListPersonaFamilia.getFamiliaList().remove(familiaListPersonaFamilia);
                    oldDatosOfFamiliaListPersonaFamilia = em.merge(oldDatosOfFamiliaListPersonaFamilia);
                }
            }
            for (PersonaFamilia familiaList1PersonaFamilia : persona.getFamiliaList1()) {
                Persona oldPersonaOfFamiliaList1PersonaFamilia = familiaList1PersonaFamilia.getPersona();
                familiaList1PersonaFamilia.setPersona(persona);
                familiaList1PersonaFamilia = em.merge(familiaList1PersonaFamilia);
                if (oldPersonaOfFamiliaList1PersonaFamilia != null) {
                    oldPersonaOfFamiliaList1PersonaFamilia.getFamiliaList1().remove(familiaList1PersonaFamilia);
                    oldPersonaOfFamiliaList1PersonaFamilia = em.merge(oldPersonaOfFamiliaList1PersonaFamilia);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Persona persona) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona persistentPersona = em.find(Persona.class, persona.getIdPersona());
            Empleado empleadoOld = persistentPersona.getEmpleado();
            Empleado empleadoNew = persona.getEmpleado();
            Usuario usuarioOld = persistentPersona.getUsuario();
            Usuario usuarioNew = persona.getUsuario();
            List<Contacto> contactoListOld = persistentPersona.getContactoList();
            List<Contacto> contactoListNew = persona.getContactoList();
            List<PersonaFamilia> familiaListOld = persistentPersona.getFamiliaList();
            List<PersonaFamilia> familiaListNew = persona.getFamiliaList();
            List<PersonaFamilia> familiaList1Old = persistentPersona.getFamiliaList1();
            List<PersonaFamilia> familiaList1New = persona.getFamiliaList1();
            List<String> illegalOrphanMessages = null;
            if (empleadoOld != null && !empleadoOld.equals(empleadoNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Empleado " + empleadoOld + " since its persona field is not nullable.");
            }
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Usuario " + usuarioOld + " since its persona field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (empleadoNew != null) {
                empleadoNew = em.getReference(empleadoNew.getClass(), empleadoNew.getIdEmpleado());
                persona.setEmpleado(empleadoNew);
            }
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getIdUsuario());
                persona.setUsuario(usuarioNew);
            }
            List<Contacto> attachedContactoListNew = new ArrayList<Contacto>();
            for (Contacto contactoListNewContactoToAttach : contactoListNew) {
                contactoListNewContactoToAttach = em.getReference(contactoListNewContactoToAttach.getClass(), contactoListNewContactoToAttach.getIdContacto());
                attachedContactoListNew.add(contactoListNewContactoToAttach);
            }
            contactoListNew = attachedContactoListNew;
            persona.setContactoList(contactoListNew);
            List<PersonaFamilia> attachedFamiliaListNew = new ArrayList<PersonaFamilia>();
            for (PersonaFamilia familiaListNewPersonaFamiliaToAttach : familiaListNew) {
                familiaListNewPersonaFamiliaToAttach = em.getReference(familiaListNewPersonaFamiliaToAttach.getClass(), familiaListNewPersonaFamiliaToAttach.getIdFamilia());
                attachedFamiliaListNew.add(familiaListNewPersonaFamiliaToAttach);
            }
            familiaListNew = attachedFamiliaListNew;
            persona.setFamiliaList(familiaListNew);
            List<PersonaFamilia> attachedFamiliaList1New = new ArrayList<PersonaFamilia>();
            for (PersonaFamilia familiaList1NewPersonaFamiliaToAttach : familiaList1New) {
                familiaList1NewPersonaFamiliaToAttach = em.getReference(familiaList1NewPersonaFamiliaToAttach.getClass(), familiaList1NewPersonaFamiliaToAttach.getIdFamilia());
                attachedFamiliaList1New.add(familiaList1NewPersonaFamiliaToAttach);
            }
            familiaList1New = attachedFamiliaList1New;
            persona.setFamiliaList1(familiaList1New);
            persona = em.merge(persona);
            if (empleadoNew != null && !empleadoNew.equals(empleadoOld)) {
                Persona oldPersonaOfEmpleado = empleadoNew.getPersona();
                if (oldPersonaOfEmpleado != null) {
                    oldPersonaOfEmpleado.setEmpleado(null);
                    oldPersonaOfEmpleado = em.merge(oldPersonaOfEmpleado);
                }
                empleadoNew.setPersona(persona);
                empleadoNew = em.merge(empleadoNew);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                Persona oldPersonaOfUsuario = usuarioNew.getPersona();
                if (oldPersonaOfUsuario != null) {
                    oldPersonaOfUsuario.setUsuario(null);
                    oldPersonaOfUsuario = em.merge(oldPersonaOfUsuario);
                }
                usuarioNew.setPersona(persona);
                usuarioNew = em.merge(usuarioNew);
            }
            for (Contacto contactoListOldContacto : contactoListOld) {
                if (!contactoListNew.contains(contactoListOldContacto)) {
                    contactoListOldContacto.getPersonaList().remove(persona);
                    contactoListOldContacto = em.merge(contactoListOldContacto);
                }
            }
            for (Contacto contactoListNewContacto : contactoListNew) {
                if (!contactoListOld.contains(contactoListNewContacto)) {
                    contactoListNewContacto.getPersonaList().add(persona);
                    contactoListNewContacto = em.merge(contactoListNewContacto);
                }
            }
            for (PersonaFamilia familiaListOldPersonaFamilia : familiaListOld) {
                if (!familiaListNew.contains(familiaListOldPersonaFamilia)) {
                    familiaListOldPersonaFamilia.setDatos(null);
                    familiaListOldPersonaFamilia = em.merge(familiaListOldPersonaFamilia);
                }
            }
            for (PersonaFamilia familiaListNewPersonaFamilia : familiaListNew) {
                if (!familiaListOld.contains(familiaListNewPersonaFamilia)) {
                    Persona oldDatosOfFamiliaListNewPersonaFamilia = familiaListNewPersonaFamilia.getDatos();
                    familiaListNewPersonaFamilia.setDatos(persona);
                    familiaListNewPersonaFamilia = em.merge(familiaListNewPersonaFamilia);
                    if (oldDatosOfFamiliaListNewPersonaFamilia != null && !oldDatosOfFamiliaListNewPersonaFamilia.equals(persona)) {
                        oldDatosOfFamiliaListNewPersonaFamilia.getFamiliaList().remove(familiaListNewPersonaFamilia);
                        oldDatosOfFamiliaListNewPersonaFamilia = em.merge(oldDatosOfFamiliaListNewPersonaFamilia);
                    }
                }
            }
            for (PersonaFamilia familiaList1OldPersonaFamilia : familiaList1Old) {
                if (!familiaList1New.contains(familiaList1OldPersonaFamilia)) {
                    familiaList1OldPersonaFamilia.setPersona(null);
                    familiaList1OldPersonaFamilia = em.merge(familiaList1OldPersonaFamilia);
                }
            }
            for (PersonaFamilia familiaList1NewPersonaFamilia : familiaList1New) {
                if (!familiaList1Old.contains(familiaList1NewPersonaFamilia)) {
                    Persona oldPersonaOfFamiliaList1NewPersonaFamilia = familiaList1NewPersonaFamilia.getPersona();
                    familiaList1NewPersonaFamilia.setPersona(persona);
                    familiaList1NewPersonaFamilia = em.merge(familiaList1NewPersonaFamilia);
                    if (oldPersonaOfFamiliaList1NewPersonaFamilia != null && !oldPersonaOfFamiliaList1NewPersonaFamilia.equals(persona)) {
                        oldPersonaOfFamiliaList1NewPersonaFamilia.getFamiliaList1().remove(familiaList1NewPersonaFamilia);
                        oldPersonaOfFamiliaList1NewPersonaFamilia = em.merge(oldPersonaOfFamiliaList1NewPersonaFamilia);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = persona.getIdPersona();
                if (findPersona(id) == null) {
                    throw new NonexistentEntityException("The persona with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona persona;
            try {
                persona = em.getReference(Persona.class, id);
                persona.getIdPersona();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The persona with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Empleado empleadoOrphanCheck = persona.getEmpleado();
            if (empleadoOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Persona (" + persona + ") cannot be destroyed since the Empleado " + empleadoOrphanCheck + " in its empleado field has a non-nullable persona field.");
            }
            Usuario usuarioOrphanCheck = persona.getUsuario();
            if (usuarioOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Persona (" + persona + ") cannot be destroyed since the Usuario " + usuarioOrphanCheck + " in its usuario field has a non-nullable persona field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Contacto> contactoList = persona.getContactoList();
            for (Contacto contactoListContacto : contactoList) {
                contactoListContacto.getPersonaList().remove(persona);
                contactoListContacto = em.merge(contactoListContacto);
            }
            List<PersonaFamilia> familiaList = persona.getFamiliaList();
            for (PersonaFamilia familiaListPersonaFamilia : familiaList) {
                familiaListPersonaFamilia.setDatos(null);
                familiaListPersonaFamilia = em.merge(familiaListPersonaFamilia);
            }
            List<PersonaFamilia> familiaList1 = persona.getFamiliaList1();
            for (PersonaFamilia familiaList1PersonaFamilia : familiaList1) {
                familiaList1PersonaFamilia.setPersona(null);
                familiaList1PersonaFamilia = em.merge(familiaList1PersonaFamilia);
            }
            em.remove(persona);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Persona> findPersonaEntities() {
        return findPersonaEntities(true, -1, -1);
    }

    public List<Persona> findPersonaEntities(int maxResults, int firstResult) {
        return findPersonaEntities(false, maxResults, firstResult);
    }

    private List<Persona> findPersonaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Persona.class));
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

    public Persona findPersona(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Persona.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Persona> rt = cq.from(Persona.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

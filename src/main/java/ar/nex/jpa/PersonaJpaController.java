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
import ar.nex.entity.Empleado;
import ar.nex.entity.Usuario;
import ar.nex.entity.ubicacion.Contacto;
import java.util.ArrayList;
import java.util.List;
import ar.nex.entity.Familia;
import ar.nex.entity.Persona;
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
            persona.setFamiliaList(new ArrayList<Familia>());
        }
        if (persona.getFamiliaList1() == null) {
            persona.setFamiliaList1(new ArrayList<Familia>());
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
            List<Familia> attachedFamiliaList = new ArrayList<Familia>();
            for (Familia familiaListFamiliaToAttach : persona.getFamiliaList()) {
                familiaListFamiliaToAttach = em.getReference(familiaListFamiliaToAttach.getClass(), familiaListFamiliaToAttach.getIdFamilia());
                attachedFamiliaList.add(familiaListFamiliaToAttach);
            }
            persona.setFamiliaList(attachedFamiliaList);
            List<Familia> attachedFamiliaList1 = new ArrayList<Familia>();
            for (Familia familiaList1FamiliaToAttach : persona.getFamiliaList1()) {
                familiaList1FamiliaToAttach = em.getReference(familiaList1FamiliaToAttach.getClass(), familiaList1FamiliaToAttach.getIdFamilia());
                attachedFamiliaList1.add(familiaList1FamiliaToAttach);
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
            for (Familia familiaListFamilia : persona.getFamiliaList()) {
                Persona oldDatosOfFamiliaListFamilia = familiaListFamilia.getDatos();
                familiaListFamilia.setDatos(persona);
                familiaListFamilia = em.merge(familiaListFamilia);
                if (oldDatosOfFamiliaListFamilia != null) {
                    oldDatosOfFamiliaListFamilia.getFamiliaList().remove(familiaListFamilia);
                    oldDatosOfFamiliaListFamilia = em.merge(oldDatosOfFamiliaListFamilia);
                }
            }
            for (Familia familiaList1Familia : persona.getFamiliaList1()) {
                Persona oldPersonaOfFamiliaList1Familia = familiaList1Familia.getPersona();
                familiaList1Familia.setPersona(persona);
                familiaList1Familia = em.merge(familiaList1Familia);
                if (oldPersonaOfFamiliaList1Familia != null) {
                    oldPersonaOfFamiliaList1Familia.getFamiliaList1().remove(familiaList1Familia);
                    oldPersonaOfFamiliaList1Familia = em.merge(oldPersonaOfFamiliaList1Familia);
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
            List<Familia> familiaListOld = persistentPersona.getFamiliaList();
            List<Familia> familiaListNew = persona.getFamiliaList();
            List<Familia> familiaList1Old = persistentPersona.getFamiliaList1();
            List<Familia> familiaList1New = persona.getFamiliaList1();
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
            List<Familia> attachedFamiliaListNew = new ArrayList<Familia>();
            for (Familia familiaListNewFamiliaToAttach : familiaListNew) {
                familiaListNewFamiliaToAttach = em.getReference(familiaListNewFamiliaToAttach.getClass(), familiaListNewFamiliaToAttach.getIdFamilia());
                attachedFamiliaListNew.add(familiaListNewFamiliaToAttach);
            }
            familiaListNew = attachedFamiliaListNew;
            persona.setFamiliaList(familiaListNew);
            List<Familia> attachedFamiliaList1New = new ArrayList<Familia>();
            for (Familia familiaList1NewFamiliaToAttach : familiaList1New) {
                familiaList1NewFamiliaToAttach = em.getReference(familiaList1NewFamiliaToAttach.getClass(), familiaList1NewFamiliaToAttach.getIdFamilia());
                attachedFamiliaList1New.add(familiaList1NewFamiliaToAttach);
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
            for (Familia familiaListOldFamilia : familiaListOld) {
                if (!familiaListNew.contains(familiaListOldFamilia)) {
                    familiaListOldFamilia.setDatos(null);
                    familiaListOldFamilia = em.merge(familiaListOldFamilia);
                }
            }
            for (Familia familiaListNewFamilia : familiaListNew) {
                if (!familiaListOld.contains(familiaListNewFamilia)) {
                    Persona oldDatosOfFamiliaListNewFamilia = familiaListNewFamilia.getDatos();
                    familiaListNewFamilia.setDatos(persona);
                    familiaListNewFamilia = em.merge(familiaListNewFamilia);
                    if (oldDatosOfFamiliaListNewFamilia != null && !oldDatosOfFamiliaListNewFamilia.equals(persona)) {
                        oldDatosOfFamiliaListNewFamilia.getFamiliaList().remove(familiaListNewFamilia);
                        oldDatosOfFamiliaListNewFamilia = em.merge(oldDatosOfFamiliaListNewFamilia);
                    }
                }
            }
            for (Familia familiaList1OldFamilia : familiaList1Old) {
                if (!familiaList1New.contains(familiaList1OldFamilia)) {
                    familiaList1OldFamilia.setPersona(null);
                    familiaList1OldFamilia = em.merge(familiaList1OldFamilia);
                }
            }
            for (Familia familiaList1NewFamilia : familiaList1New) {
                if (!familiaList1Old.contains(familiaList1NewFamilia)) {
                    Persona oldPersonaOfFamiliaList1NewFamilia = familiaList1NewFamilia.getPersona();
                    familiaList1NewFamilia.setPersona(persona);
                    familiaList1NewFamilia = em.merge(familiaList1NewFamilia);
                    if (oldPersonaOfFamiliaList1NewFamilia != null && !oldPersonaOfFamiliaList1NewFamilia.equals(persona)) {
                        oldPersonaOfFamiliaList1NewFamilia.getFamiliaList1().remove(familiaList1NewFamilia);
                        oldPersonaOfFamiliaList1NewFamilia = em.merge(oldPersonaOfFamiliaList1NewFamilia);
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
            List<Familia> familiaList = persona.getFamiliaList();
            for (Familia familiaListFamilia : familiaList) {
                familiaListFamilia.setDatos(null);
                familiaListFamilia = em.merge(familiaListFamilia);
            }
            List<Familia> familiaList1 = persona.getFamiliaList1();
            for (Familia familiaList1Familia : familiaList1) {
                familiaList1Familia.setPersona(null);
                familiaList1Familia = em.merge(familiaList1Familia);
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

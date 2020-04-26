/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Peticion;
import java.util.ArrayList;
import java.util.List;
import Entidades.Publicacion;
import Entidades.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author marianabojorquez
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(){
        this.emf = Persistence.createEntityManagerFactory("my_persistence_unit");

    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) {
        if (usuario.getPeticionList() == null) {
            usuario.setPeticionList(new ArrayList<Peticion>());
        }
        if (usuario.getPublicacionList() == null) {
            usuario.setPublicacionList(new ArrayList<Publicacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Peticion> attachedPeticionList = new ArrayList<Peticion>();
            for (Peticion peticionListPeticionToAttach : usuario.getPeticionList()) {
                peticionListPeticionToAttach = em.getReference(peticionListPeticionToAttach.getClass(), peticionListPeticionToAttach.getPeticionId());
                attachedPeticionList.add(peticionListPeticionToAttach);
            }
            usuario.setPeticionList(attachedPeticionList);
            List<Publicacion> attachedPublicacionList = new ArrayList<Publicacion>();
            for (Publicacion publicacionListPublicacionToAttach : usuario.getPublicacionList()) {
                publicacionListPublicacionToAttach = em.getReference(publicacionListPublicacionToAttach.getClass(), publicacionListPublicacionToAttach.getPublicacionId());
                attachedPublicacionList.add(publicacionListPublicacionToAttach);
            }
            usuario.setPublicacionList(attachedPublicacionList);
            em.persist(usuario);
            for (Peticion peticionListPeticion : usuario.getPeticionList()) {
                Usuario oldUsuarioFkOfPeticionListPeticion = peticionListPeticion.getUsuarioFk();
                peticionListPeticion.setUsuarioFk(usuario);
                peticionListPeticion = em.merge(peticionListPeticion);
                if (oldUsuarioFkOfPeticionListPeticion != null) {
                    oldUsuarioFkOfPeticionListPeticion.getPeticionList().remove(peticionListPeticion);
                    oldUsuarioFkOfPeticionListPeticion = em.merge(oldUsuarioFkOfPeticionListPeticion);
                }
            }
            for (Publicacion publicacionListPublicacion : usuario.getPublicacionList()) {
                Usuario oldUsuarioFkOfPublicacionListPublicacion = publicacionListPublicacion.getUsuarioFk();
                publicacionListPublicacion.setUsuarioFk(usuario);
                publicacionListPublicacion = em.merge(publicacionListPublicacion);
                if (oldUsuarioFkOfPublicacionListPublicacion != null) {
                    oldUsuarioFkOfPublicacionListPublicacion.getPublicacionList().remove(publicacionListPublicacion);
                    oldUsuarioFkOfPublicacionListPublicacion = em.merge(oldUsuarioFkOfPublicacionListPublicacion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getUsuarioId());
            List<Peticion> peticionListOld = persistentUsuario.getPeticionList();
            List<Peticion> peticionListNew = usuario.getPeticionList();
            List<Publicacion> publicacionListOld = persistentUsuario.getPublicacionList();
            List<Publicacion> publicacionListNew = usuario.getPublicacionList();
            List<Peticion> attachedPeticionListNew = new ArrayList<Peticion>();
            for (Peticion peticionListNewPeticionToAttach : peticionListNew) {
                peticionListNewPeticionToAttach = em.getReference(peticionListNewPeticionToAttach.getClass(), peticionListNewPeticionToAttach.getPeticionId());
                attachedPeticionListNew.add(peticionListNewPeticionToAttach);
            }
            peticionListNew = attachedPeticionListNew;
            usuario.setPeticionList(peticionListNew);
            List<Publicacion> attachedPublicacionListNew = new ArrayList<Publicacion>();
            for (Publicacion publicacionListNewPublicacionToAttach : publicacionListNew) {
                publicacionListNewPublicacionToAttach = em.getReference(publicacionListNewPublicacionToAttach.getClass(), publicacionListNewPublicacionToAttach.getPublicacionId());
                attachedPublicacionListNew.add(publicacionListNewPublicacionToAttach);
            }
            publicacionListNew = attachedPublicacionListNew;
            usuario.setPublicacionList(publicacionListNew);
            usuario = em.merge(usuario);
            for (Peticion peticionListOldPeticion : peticionListOld) {
                if (!peticionListNew.contains(peticionListOldPeticion)) {
                    peticionListOldPeticion.setUsuarioFk(null);
                    peticionListOldPeticion = em.merge(peticionListOldPeticion);
                }
            }
            for (Peticion peticionListNewPeticion : peticionListNew) {
                if (!peticionListOld.contains(peticionListNewPeticion)) {
                    Usuario oldUsuarioFkOfPeticionListNewPeticion = peticionListNewPeticion.getUsuarioFk();
                    peticionListNewPeticion.setUsuarioFk(usuario);
                    peticionListNewPeticion = em.merge(peticionListNewPeticion);
                    if (oldUsuarioFkOfPeticionListNewPeticion != null && !oldUsuarioFkOfPeticionListNewPeticion.equals(usuario)) {
                        oldUsuarioFkOfPeticionListNewPeticion.getPeticionList().remove(peticionListNewPeticion);
                        oldUsuarioFkOfPeticionListNewPeticion = em.merge(oldUsuarioFkOfPeticionListNewPeticion);
                    }
                }
            }
            for (Publicacion publicacionListOldPublicacion : publicacionListOld) {
                if (!publicacionListNew.contains(publicacionListOldPublicacion)) {
                    publicacionListOldPublicacion.setUsuarioFk(null);
                    publicacionListOldPublicacion = em.merge(publicacionListOldPublicacion);
                }
            }
            for (Publicacion publicacionListNewPublicacion : publicacionListNew) {
                if (!publicacionListOld.contains(publicacionListNewPublicacion)) {
                    Usuario oldUsuarioFkOfPublicacionListNewPublicacion = publicacionListNewPublicacion.getUsuarioFk();
                    publicacionListNewPublicacion.setUsuarioFk(usuario);
                    publicacionListNewPublicacion = em.merge(publicacionListNewPublicacion);
                    if (oldUsuarioFkOfPublicacionListNewPublicacion != null && !oldUsuarioFkOfPublicacionListNewPublicacion.equals(usuario)) {
                        oldUsuarioFkOfPublicacionListNewPublicacion.getPublicacionList().remove(publicacionListNewPublicacion);
                        oldUsuarioFkOfPublicacionListNewPublicacion = em.merge(oldUsuarioFkOfPublicacionListNewPublicacion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getUsuarioId();
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

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getUsuarioId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<Peticion> peticionList = usuario.getPeticionList();
            for (Peticion peticionListPeticion : peticionList) {
                peticionListPeticion.setUsuarioFk(null);
                peticionListPeticion = em.merge(peticionListPeticion);
            }
            List<Publicacion> publicacionList = usuario.getPublicacionList();
            for (Publicacion publicacionListPublicacion : publicacionList) {
                publicacionListPublicacion.setUsuarioFk(null);
                publicacionListPublicacion = em.merge(publicacionListPublicacion);
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

    public Usuario findUsuario(Integer id) {
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

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
import Entidades.Ubicacion;
import Entidades.Raza;
import Entidades.Usuario;
import Entidades.Peticion;
import Entidades.Publicacion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author marianabojorquez
 */
public class PublicacionJpaController implements Serializable {

    public PublicacionJpaController(){
        this.emf = Persistence.createEntityManagerFactory("my_persistence_unit");

    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Publicacion publicacion) {
        if (publicacion.getPeticionList() == null) {
            publicacion.setPeticionList(new ArrayList<Peticion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ubicacion ubicacionFk = publicacion.getUbicacionFk();
            if (ubicacionFk != null) {
                ubicacionFk = em.getReference(ubicacionFk.getClass(), ubicacionFk.getUbicacionId());
                publicacion.setUbicacionFk(ubicacionFk);
            }
            Raza razaFk = publicacion.getRazaFk();
            if (razaFk != null) {
                razaFk = em.getReference(razaFk.getClass(), razaFk.getRazaId());
                publicacion.setRazaFk(razaFk);
            }
            Usuario usuarioFk = publicacion.getUsuarioFk();
            if (usuarioFk != null) {
                usuarioFk = em.getReference(usuarioFk.getClass(), usuarioFk.getUsuarioId());
                publicacion.setUsuarioFk(usuarioFk);
            }
            List<Peticion> attachedPeticionList = new ArrayList<Peticion>();
            for (Peticion peticionListPeticionToAttach : publicacion.getPeticionList()) {
                peticionListPeticionToAttach = em.getReference(peticionListPeticionToAttach.getClass(), peticionListPeticionToAttach.getPeticionId());
                attachedPeticionList.add(peticionListPeticionToAttach);
            }
            publicacion.setPeticionList(attachedPeticionList);
            em.persist(publicacion);
            if (ubicacionFk != null) {
                ubicacionFk.getPublicacionList().add(publicacion);
                ubicacionFk = em.merge(ubicacionFk);
            }
            if (razaFk != null) {
                razaFk.getPublicacionList().add(publicacion);
                razaFk = em.merge(razaFk);
            }
            if (usuarioFk != null) {
                usuarioFk.getPublicacionList().add(publicacion);
                usuarioFk = em.merge(usuarioFk);
            }
            for (Peticion peticionListPeticion : publicacion.getPeticionList()) {
                Publicacion oldPublicacionFkOfPeticionListPeticion = peticionListPeticion.getPublicacionFk();
                peticionListPeticion.setPublicacionFk(publicacion);
                peticionListPeticion = em.merge(peticionListPeticion);
                if (oldPublicacionFkOfPeticionListPeticion != null) {
                    oldPublicacionFkOfPeticionListPeticion.getPeticionList().remove(peticionListPeticion);
                    oldPublicacionFkOfPeticionListPeticion = em.merge(oldPublicacionFkOfPeticionListPeticion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Publicacion publicacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Publicacion persistentPublicacion = em.find(Publicacion.class, publicacion.getPublicacionId());
            Ubicacion ubicacionFkOld = persistentPublicacion.getUbicacionFk();
            Ubicacion ubicacionFkNew = publicacion.getUbicacionFk();
            Raza razaFkOld = persistentPublicacion.getRazaFk();
            Raza razaFkNew = publicacion.getRazaFk();
            Usuario usuarioFkOld = persistentPublicacion.getUsuarioFk();
            Usuario usuarioFkNew = publicacion.getUsuarioFk();
            List<Peticion> peticionListOld = persistentPublicacion.getPeticionList();
            List<Peticion> peticionListNew = publicacion.getPeticionList();
            if (ubicacionFkNew != null) {
                ubicacionFkNew = em.getReference(ubicacionFkNew.getClass(), ubicacionFkNew.getUbicacionId());
                publicacion.setUbicacionFk(ubicacionFkNew);
            }
            if (razaFkNew != null) {
                razaFkNew = em.getReference(razaFkNew.getClass(), razaFkNew.getRazaId());
                publicacion.setRazaFk(razaFkNew);
            }
            if (usuarioFkNew != null) {
                usuarioFkNew = em.getReference(usuarioFkNew.getClass(), usuarioFkNew.getUsuarioId());
                publicacion.setUsuarioFk(usuarioFkNew);
            }
            List<Peticion> attachedPeticionListNew = new ArrayList<Peticion>();
            for (Peticion peticionListNewPeticionToAttach : peticionListNew) {
                peticionListNewPeticionToAttach = em.getReference(peticionListNewPeticionToAttach.getClass(), peticionListNewPeticionToAttach.getPeticionId());
                attachedPeticionListNew.add(peticionListNewPeticionToAttach);
            }
            peticionListNew = attachedPeticionListNew;
            publicacion.setPeticionList(peticionListNew);
            publicacion = em.merge(publicacion);
            if (ubicacionFkOld != null && !ubicacionFkOld.equals(ubicacionFkNew)) {
                ubicacionFkOld.getPublicacionList().remove(publicacion);
                ubicacionFkOld = em.merge(ubicacionFkOld);
            }
            if (ubicacionFkNew != null && !ubicacionFkNew.equals(ubicacionFkOld)) {
                ubicacionFkNew.getPublicacionList().add(publicacion);
                ubicacionFkNew = em.merge(ubicacionFkNew);
            }
            if (razaFkOld != null && !razaFkOld.equals(razaFkNew)) {
                razaFkOld.getPublicacionList().remove(publicacion);
                razaFkOld = em.merge(razaFkOld);
            }
            if (razaFkNew != null && !razaFkNew.equals(razaFkOld)) {
                razaFkNew.getPublicacionList().add(publicacion);
                razaFkNew = em.merge(razaFkNew);
            }
            if (usuarioFkOld != null && !usuarioFkOld.equals(usuarioFkNew)) {
                usuarioFkOld.getPublicacionList().remove(publicacion);
                usuarioFkOld = em.merge(usuarioFkOld);
            }
            if (usuarioFkNew != null && !usuarioFkNew.equals(usuarioFkOld)) {
                usuarioFkNew.getPublicacionList().add(publicacion);
                usuarioFkNew = em.merge(usuarioFkNew);
            }
            for (Peticion peticionListOldPeticion : peticionListOld) {
                if (!peticionListNew.contains(peticionListOldPeticion)) {
                    peticionListOldPeticion.setPublicacionFk(null);
                    peticionListOldPeticion = em.merge(peticionListOldPeticion);
                }
            }
            for (Peticion peticionListNewPeticion : peticionListNew) {
                if (!peticionListOld.contains(peticionListNewPeticion)) {
                    Publicacion oldPublicacionFkOfPeticionListNewPeticion = peticionListNewPeticion.getPublicacionFk();
                    peticionListNewPeticion.setPublicacionFk(publicacion);
                    peticionListNewPeticion = em.merge(peticionListNewPeticion);
                    if (oldPublicacionFkOfPeticionListNewPeticion != null && !oldPublicacionFkOfPeticionListNewPeticion.equals(publicacion)) {
                        oldPublicacionFkOfPeticionListNewPeticion.getPeticionList().remove(peticionListNewPeticion);
                        oldPublicacionFkOfPeticionListNewPeticion = em.merge(oldPublicacionFkOfPeticionListNewPeticion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = publicacion.getPublicacionId();
                if (findPublicacion(id) == null) {
                    throw new NonexistentEntityException("The publicacion with id " + id + " no longer exists.");
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
            Publicacion publicacion;
            try {
                publicacion = em.getReference(Publicacion.class, id);
                publicacion.getPublicacionId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The publicacion with id " + id + " no longer exists.", enfe);
            }
            Ubicacion ubicacionFk = publicacion.getUbicacionFk();
            if (ubicacionFk != null) {
                ubicacionFk.getPublicacionList().remove(publicacion);
                ubicacionFk = em.merge(ubicacionFk);
            }
            Raza razaFk = publicacion.getRazaFk();
            if (razaFk != null) {
                razaFk.getPublicacionList().remove(publicacion);
                razaFk = em.merge(razaFk);
            }
            Usuario usuarioFk = publicacion.getUsuarioFk();
            if (usuarioFk != null) {
                usuarioFk.getPublicacionList().remove(publicacion);
                usuarioFk = em.merge(usuarioFk);
            }
            List<Peticion> peticionList = publicacion.getPeticionList();
            for (Peticion peticionListPeticion : peticionList) {
                peticionListPeticion.setPublicacionFk(null);
                peticionListPeticion = em.merge(peticionListPeticion);
            }
            em.remove(publicacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Publicacion> findPublicacionEntities() {
        return findPublicacionEntities(true, -1, -1);
    }

    public List<Publicacion> findPublicacionEntities(int maxResults, int firstResult) {
        return findPublicacionEntities(false, maxResults, firstResult);
    }

    private List<Publicacion> findPublicacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Publicacion.class));
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

    public Publicacion findPublicacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Publicacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getPublicacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Publicacion> rt = cq.from(Publicacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

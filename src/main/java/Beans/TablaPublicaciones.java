/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Controladores.PublicacionJpaController;
import Entidades.Publicacion;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import static java.util.Locale.filter;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;
import javax.persistence.criteria.Predicate;
import org.primefaces.PrimeFaces;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author alanlomeli
 */
@Named
@ApplicationScoped
public class TablaPublicaciones implements Serializable {

    @Inject
    private Sesion sesion;
    @Inject
    private DarAdopcion darAdopcion;

    private List<Publicacion> publicaciones;
    private PublicacionJpaController publicacionController;
    private Publicacion edicionPublicacion;

    public List<Publicacion> getPublicaciones() {
        publicacionController = new PublicacionJpaController();
        if (FacesContext.getCurrentInstance().getViewRoot().getViewId().equals("/mis_publicaciones.xhtml")) {

            publicaciones = publicacionController.getEntityManager().createQuery("select a from Publicacion a WHERE a.usuarioFk.usuarioId = " + sesion.getUsuario_id(),
                    Publicacion.class).getResultList();
        } else {
            publicaciones = publicacionController.findPublicacionEntities();
        }
        return publicaciones;

    }

    public void setPublicaciones(List<Publicacion> publicaciones) {
        this.publicaciones = publicaciones;
    }

    public void editarPublicacion(int id) {
        edicionPublicacion = publicacionController.findPublicacion(id);
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("editar_adopcion.xhtml");  //Redireccionamos al home
        } catch (IOException ex) {
            System.out.println("Error redireccionando");
        }
    }

    public Publicacion getEdicionPublicacion() {
        return edicionPublicacion;
    }

    public void setEdicionPublicacion(Publicacion edicionPublicacion) {
        this.edicionPublicacion = edicionPublicacion;
    }

    public void editar() {
        try {
            publicacionController.edit(edicionPublicacion);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        FacesMessage message = new FacesMessage("Se han actualizado los datos");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void activarDesactivarPublicacion(int id, Boolean activo) {
        Publicacion publicacion = publicacionController.findPublicacion(id);
        if (publicacion.getEstadoAdopcion()) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("No se pueden activar publicaciones de gatos adoptados."));
        } else {
            publicacion.setActivo(activo);
            PrimeFaces.current().executeScript("location.reload();");

            try {
                publicacionController.edit(publicacion);
            } catch (Exception ex) {
            }
        }
    }

}

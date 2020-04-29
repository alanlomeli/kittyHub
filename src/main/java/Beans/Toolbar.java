/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author marianabojorquez
 */
@Named
@RequestScoped
public class Toolbar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Sesion sesion;

    private String paginaActual;
    private String iconoActual;

    @PostConstruct
    public void init() {
        setOpcionesSesion();
    }

    public void setOpcionesSesion() {
        //Config para obtener el nombre que aparece en el menu
        if (FacesContext.getCurrentInstance().getViewRoot().getViewId().equals("/index.xhtml")) {
            paginaActual = "Home";
            iconoActual = "fa fa-rocket";
        } else if (FacesContext.getCurrentInstance().getViewRoot().getViewId().equals("/mis_solicitudes.xhtml")) {
            paginaActual = "Mis solicitudes";
            iconoActual = "fa fa-share";
        } else if (FacesContext.getCurrentInstance().getViewRoot().getViewId().equals("/peticiones.xhtml")) {
            paginaActual = "Peticiones";
            iconoActual = "fa fa-question";
        } else if (FacesContext.getCurrentInstance().getViewRoot().getViewId().equals("/mis_publicaciones.xhtml")) {
            paginaActual = "Mis publicaciones";
            iconoActual = "fa fa-list-ol";
        } else if (FacesContext.getCurrentInstance().getViewRoot().getViewId().equals("/mis_gatos.xhtml")) {
            paginaActual = "Mis gatos";
            iconoActual = "fa fa-heart";
        } else if (FacesContext.getCurrentInstance().getViewRoot().getViewId().equals("/posts.xhtml")) {
            paginaActual = "Posts";
            iconoActual = "fa fa-pencil-square-o";
        } else if (FacesContext.getCurrentInstance().getViewRoot().getViewId().equals("/usuarios.xhtml")) {
            paginaActual = "Usuarios";
            iconoActual = "fa fa-reddit";
        } else {
            paginaActual = "Creando";
            iconoActual = "fa fa-edit";

        }
        paginaActual = "⠀" + paginaActual;
        paginaActual += "⠀▾";
    }

    public void cerrarSesion() {
        sesion.setIniciado(false);
        sesion.setUsuario("");
        sesion.setTipo(0);
        setOpcionesSesion();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");  //Redireccionamos al home
            System.out.println(sesion.getTipo());
        } catch (IOException ex) {
            System.out.println("Error redireccionando");
        }
    }

    public Sesion getSesion() {
        return sesion;
    }

    public void setSesion(Sesion sesion) {
        this.sesion = sesion;
    }

    public String getPaginaActual() {
        return paginaActual;
    }

    public void setPaginaActual(String paginaActual) {
        this.paginaActual = paginaActual;
    }

    public String getIconoActual() {
        return iconoActual;
    }

    public void setIconoActual(String iconoActual) {
        this.iconoActual = iconoActual;
    }

}

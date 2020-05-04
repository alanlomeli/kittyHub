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
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author marianabojorquez
 */
@Named
@ViewScoped
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
            checkTrasspass();
            paginaActual = "Mis solicitudes";
            iconoActual = "fa fa-share";
        } else if (FacesContext.getCurrentInstance().getViewRoot().getViewId().equals("/peticiones.xhtml")) {
            checkTrasspass();
            paginaActual = "Peticiones";
            iconoActual = "fa fa-question";
        } else if (FacesContext.getCurrentInstance().getViewRoot().getViewId().equals("/mis_publicaciones.xhtml")) {
            checkTrasspass();
            paginaActual = "Mis publicaciones";
            iconoActual = "fa fa-list-ol";
        } else if (FacesContext.getCurrentInstance().getViewRoot().getViewId().equals("/mis_gatos.xhtml")) {
            checkTrasspass();
            paginaActual = "Mis gatos";
            iconoActual = "fa fa-heart";
        } else if (FacesContext.getCurrentInstance().getViewRoot().getViewId().equals("/posts.xhtml")) {
            checkTrasspass();
            paginaActual = "Posts";
            iconoActual = "fa fa-pencil-square-o";
        } else if (FacesContext.getCurrentInstance().getViewRoot().getViewId().equals("/usuarios.xhtml")) {
            checkTrasspass();
            paginaActual = "Usuarios";
            iconoActual = "fa fa-reddit";
        } else {
            paginaActual = "Creando";
            iconoActual = "fa fa-edit";

        }
        paginaActual = "⠀" + paginaActual;
        paginaActual += "⠀▾";
    }

    public void checkTrasspass() {
        if (!sesion.isIniciado()) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("404.xhtml");  //Redireccionamos a un error
            } catch (IOException ex) {
                System.out.println("Error redireccionando");
            }
        } else if (FacesContext.getCurrentInstance().getViewRoot().getViewId().equals("/usuarios.xhtml") && sesion.getTipo() != 3) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("404.xhtml");  //Redireccionamos a un error
            } catch (IOException ex) {
                System.out.println("Error redireccionando");
            }
        } else if (FacesContext.getCurrentInstance().getViewRoot().getViewId().equals("/posts.xhtml") && (sesion.getTipo() != 2 && sesion.getTipo() != 3)) {
            System.out.println(sesion.getTipo());
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("404.xhtml");  //Redireccionamos a un error
            } catch (IOException ex) {
                System.out.println("Error redireccionando");
            }
        }

    }

    public void cerrarSesion() {
        sesion.setIniciado(false);
        sesion.setUsuario("");
        sesion.setTipo(0);
        sesion.setUsuario_id(0);
        setOpcionesSesion();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");  //Redireccionamos al home
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

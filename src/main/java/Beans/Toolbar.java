/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Controladores.UsuarioJpaController;
import Entidades.Usuario;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

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

    private MenuModel modelo;
    private String paginaActual;

    @PostConstruct
    public void init() {
        setOpcionesSesion();
    }

    public void setOpcionesSesion() {
        //Config para obtener el nombre que aparece en el menu
        if (FacesContext.getCurrentInstance().getViewRoot().getViewId().equals("/index.xhtml")) {
            paginaActual = "Home";
        } else if (FacesContext.getCurrentInstance().getViewRoot().getViewId().equals("/mis_solicitudes.xhtml")) {
            paginaActual = "Mis solicitudes";
        } else if (FacesContext.getCurrentInstance().getViewRoot().getViewId().equals("/peticiones.xhtml")) {
            paginaActual = "Peticiones";
        } else if (FacesContext.getCurrentInstance().getViewRoot().getViewId().equals("/mis_publicaciones.xhtml")) {
            paginaActual = "Mis publicaciones";
        } else if (FacesContext.getCurrentInstance().getViewRoot().getViewId().equals("/mis_gatos.xhtml")) {
            paginaActual = "Mis gatos";
        }

        modelo = new DefaultMenuModel();
        DefaultMenuItem opcionHome = new DefaultMenuItem("Home");
        opcionHome.setOutcome("index");
        opcionHome.setIcon("pi pi-plus-circle");
        modelo.getElements().add(opcionHome);

        DefaultMenuItem opcionMisSolicitudes = new DefaultMenuItem("Mis solicitudes");
        opcionMisSolicitudes.setOutcome("index");
        opcionMisSolicitudes.setIcon("pi pi-plus-circle");
        modelo.getElements().add(opcionMisSolicitudes);

        DefaultMenuItem opcionPeticiones = new DefaultMenuItem("Mis peticiones");
        opcionPeticiones.setOutcome("index");
        opcionPeticiones.setIcon("pi pi-plus-circle");
        modelo.getElements().add(opcionPeticiones);

        DefaultMenuItem opcionMisPublicaciones = new DefaultMenuItem("Mis publicaciones");
        opcionMisPublicaciones.setOutcome("index");
        opcionMisPublicaciones.setIcon("pi pi-plus-circle");
        modelo.getElements().add(opcionMisPublicaciones);

        DefaultMenuItem opcionMisGatos = new DefaultMenuItem("Mis gatos");
        opcionMisGatos.setOutcome("index");
        opcionMisGatos.setIcon("pi pi-plus-circle");
        modelo.getElements().add(opcionMisGatos);

        if (sesion.getTipo() == 2 || sesion.getTipo() == 3) { //Si el usuario es un moderador o administrador
            DefaultMenuItem opcionPosts = new DefaultMenuItem("Posts");
            opcionPosts.setOutcome("index");
            opcionPosts.setIcon("pi pi-plus-circle");
            modelo.getElements().add(opcionPosts);
            System.out.println("claro" + sesion.getTipo());
        }
        if (sesion.getTipo() == 3) { //Si el usuario es un moderador 
            DefaultMenuItem opcionUsuarios = new DefaultMenuItem("Usuarios");
            opcionUsuarios.setOutcome("index");
            opcionUsuarios.setIcon("pi pi-plus-circle");
            modelo.getElements().add(opcionUsuarios);
        }
    }

    public void debug() {
        System.out.println(sesion.isIniciado());
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

    public MenuModel getModelo() {
        return modelo;
    }

    public void setModelo(MenuModel modelo) {
        this.modelo = modelo;
    }

    public String getPaginaActual() {
        return paginaActual;
    }

    public void setPaginaActual(String paginaActual) {
        this.paginaActual = paginaActual;
    }

}

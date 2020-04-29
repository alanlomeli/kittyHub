/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Controladores.AES;
import Controladores.UsuarioJpaController;
import Entidades.Usuario;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

/**
 *
 * @author alanlomeli
 */
@Named
@RequestScoped
public class Registro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Sesion sesion;

    private String campoUsuario;
    private String campoNombre;
    private String campoApellido;
    private Boolean genero;
    private String contra;
    private UsuarioJpaController usuarioController;
    private final short tipo = 1;

    @PostConstruct
    public void init() {
        usuarioController = new UsuarioJpaController();
    }

    public void clickRegistro() {
        //Verificamos que el usuario que se va a registrar no existe ya en la base
        Query query = usuarioController.getEntityManager().createNamedQuery("Usuario.findByUsuario");
        query.setParameter("usuario", campoUsuario);

        if (query.getResultList().size() > 0) { //Sí existe
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "El usuario ingresado ya existe", null));
        } else {
            Usuario usuarioTemp = new Usuario();
            usuarioTemp.setUsuario(campoUsuario);
            usuarioTemp.setNombre(campoNombre);
            usuarioTemp.setApellido(campoApellido);
            usuarioTemp.setGenero(genero);
            usuarioTemp.setContrasena(AES.encrypt(contra).getBytes());
            usuarioTemp.setActivo(true);
            usuarioTemp.setTipo(tipo);
            usuarioController.create(usuarioTemp);
             try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");  //Redireccionamos al home
                } catch (IOException ex) {
                    System.out.println("Error redireccionando");
                }
        }
    }

    public Sesion getSesion() {
        return sesion;
    }

    public void setSesion(Sesion sesion) {
        this.sesion = sesion;
    }

    public String getCampoUsuario() {
        return campoUsuario;
    }

    public void setCampoUsuario(String campoUsuario) {
        this.campoUsuario = campoUsuario;
    }

    public String getCampoNombre() {
        return campoNombre;
    }

    public void setCampoNombre(String campoNombre) {
        this.campoNombre = campoNombre;
    }

    public String getCampoApellido() {
        return campoApellido;
    }

    public void setCampoApellido(String campoApellido) {
        this.campoApellido = campoApellido;
    }

    public Boolean getGenero() {
        return genero;
    }

    public void setGenero(Boolean genero) {
        this.genero = genero;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

}

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
 * @author marianabojorquez
 */
@Named
@RequestScoped
public class IniciarSesion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Sesion sesion;

    private String campoUsuario;
    private String campoContra;
    private UsuarioJpaController usuarioController;

    @PostConstruct
    public void init() {
        usuarioController = new UsuarioJpaController();
    }

    public void clickIniciarsesion() {
        usuarioController = new UsuarioJpaController();

        Query query = usuarioController.getEntityManager().createNamedQuery("Usuario.findByUsuario");
        query.setParameter("usuario", campoUsuario);

        if (query.getResultList().size() > 0) {
            Usuario usuarioTemp = (Usuario) query.getSingleResult();
            if (AES.decrypt(new String(usuarioTemp.getContrasena())).equals(campoContra)) {
                // FacesContext.getCurrentInstance().addMessage(null,
                //new FacesMessage("Bienvenido "+campoUsuario));
                sesion.setIniciado(true); //Ponemos el usuario al bean para que se inicie sesión
                sesion.setUsuario(campoUsuario);
                sesion.setTipo(usuarioTemp.getTipo());
                System.out.println(sesion.getTipo());
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");  //Redireccionamos al home
                } catch (IOException ex) {
                    System.out.println("Error redireccionando");
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "El usuario o contraseña son incorrectos", null));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "El usuario o contraseña son incorrectos", null));
        }
        usuarioController = new UsuarioJpaController();

    }

    public String getCampoUsuario() {
        return campoUsuario;
    }

    public void setCampoUsuario(String campoUsuario) {
        this.campoUsuario = campoUsuario;
    }

    public String getCampoContra() {
        return campoContra;
    }

    public void setCampoContra(String campoContra) {
        this.campoContra = campoContra;
    }

    public Sesion getSesion() {
        return sesion;
    }

    public void setSesion(Sesion sesion) {
        this.sesion = sesion;
    }

}

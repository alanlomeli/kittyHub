/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

//import Controladores.UsuarioJpaController;
import Entidades.Usuario;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author alanlomeli
 */
@Named
@RequestScoped
public class IniciarSesion implements Serializable{
    private static final long serialVersionUID = 1L;

    @Inject
    Usuario usuario;
    
    private String campoUsuario;
    private String campoContra;

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

   // UsuarioJpaController usuarioController;

    @PostConstruct
    public void init() {
       // usuarioController = new UsuarioJpaController();
    }

    public void clickIniciarsesion() {
        System.out.println(campoUsuario+" "+campoContra);
    }

}

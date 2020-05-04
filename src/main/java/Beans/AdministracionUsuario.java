/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Controladores.AES;
import Entidades.Usuario;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import Controladores.UsuarioJpaController;
import java.io.IOException;
import javax.faces.context.FacesContext;

/**
 *
 * @author alanlomeli
 */
@Named
@ApplicationScoped
public class AdministracionUsuario {

    private Usuario usuario;
    private String contra;
    private Boolean edicion;

    @PostConstruct
    public void init() {
    
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;

    }

    public void registrar() {
        System.out.println(edicion);

        UsuarioJpaController usuarioController = new UsuarioJpaController();
        if (edicion) {
            if (!contra.equals("FAKEPASSWORD")) {
                usuario.setContrasena(AES.encrypt(contra).getBytes());
            }
            try {
                usuarioController.edit(usuario);
            } catch (Exception ex) {

            }
        } else {
            usuario.setContrasena(AES.encrypt(contra).getBytes());
            usuarioController.create(usuario);

        }
         try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("usuarios.xhtml");
        } catch (IOException ex) {
            System.out.println("Error redireccionando");
        }
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    public Boolean getEdicion() {
        return edicion;
    }

    public void setEdicion(Boolean edicion) {
        this.edicion = edicion;
    }

}

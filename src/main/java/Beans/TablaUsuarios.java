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
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author
 */
@Named
@ViewScoped
public class TablaUsuarios implements Serializable {

    @Inject
    private Sesion sesion;

    @Inject
    AdministracionUsuario adminUsuario;
    private List<Usuario> listaUsuarios;

    public List<Usuario> getListaUsuarios() {
        UsuarioJpaController usuarioController = new UsuarioJpaController();
        listaUsuarios = usuarioController.findUsuarioEntities();
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public void irRegistro() {
        Usuario usuario = new Usuario();

        adminUsuario.setUsuario(usuario);
        adminUsuario.setEdicion(false);

        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("editar_usuario.xhtml");
        } catch (IOException ex) {
            System.out.println("Error redireccionando");
        }
    }

    public void irEditar(int id) {
        UsuarioJpaController usuarioController = new UsuarioJpaController();
        Usuario usuario = usuarioController.findUsuario(id);

        adminUsuario.setUsuario(usuario);
        adminUsuario.setEdicion(true);
        adminUsuario.setContra("FAKEPASSWORD");

        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("editar_usuario.xhtml");
        } catch (IOException ex) {
            System.out.println("Error redireccionando");
        }
    }

    public void activarDesactivar(int id, Boolean activar) throws Exception {
        UsuarioJpaController usuarioController = new UsuarioJpaController();
        Usuario usuario = usuarioController.findUsuario(id);
        usuario.setActivo(activar);
        usuarioController.edit(usuario);
        PrimeFaces.current().executeScript("location.reload();");

    }
}

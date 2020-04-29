/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Controladores.PublicacionJpaController;
import Controladores.UsuarioJpaController;
import Entidades.Publicacion;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author alanlomeli
 */
@Named
@RequestScoped
public class Publicaciones implements Serializable {

    private static final long serialVersionUID = 1L;

    private PublicacionJpaController publicacionController;
    private List<Publicacion> publicaciones;

    @PostConstruct
    public void init() {

    }

    public List<Publicacion> getPublicaciones() {
        return publicaciones;
    }

    public void setPublicaciones(List<Publicacion> publicaciones) {
        this.publicaciones = publicaciones;
    }

    public List<Publicacion> obtenerPublicaciones() {
        publicacionController = new PublicacionJpaController();
        return publicacionController.findPublicacionEntities();

    }

    public String obtenerNombreDueno(int id) {
    UsuarioJpaController usuario= new UsuarioJpaController();
    
    return usuario.findUsuario(id).getNombre();
    }
}

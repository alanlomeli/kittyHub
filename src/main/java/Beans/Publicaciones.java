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
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author marianabojorquez
 */
@Named
@ViewScoped
public class Publicaciones implements Serializable {

    private static final long serialVersionUID = 1L;

    private PublicacionJpaController publicacionController;
    private List<Publicacion> publicaciones;

    @PostConstruct
    public void init() {
         publicacionController = new PublicacionJpaController();
         publicacionController.getEntityManager().clear();
        List<Publicacion> listaVieja = publicacionController.findPublicacionEntities();
        List<Publicacion> listaNueva =  new ArrayList<>();        
        for (int i = listaVieja.size(); i > 0; i--) {
            if(listaVieja.get(i-1).getActivo()&&!listaVieja.get(i-1).getEstadoAdopcion()){
           
            listaNueva.add(listaVieja.get(i-1));
            }
        }
        publicaciones=listaNueva;
    }

    public List<Publicacion> getPublicaciones() {
        return publicaciones;
    }

    public void setPublicaciones(List<Publicacion> publicaciones) {
        this.publicaciones = publicaciones;
    }

    public List<Publicacion> obtenerPublicaciones() {
        
        publicacionController = new PublicacionJpaController();
        List<Publicacion> listaVieja = publicacionController.findPublicacionEntities();
        List<Publicacion> listaNueva =  new ArrayList<>();        
        for (int i = listaVieja.size(); i > 0; i--) {
            if(listaVieja.get(i-1).getActivo()&&!listaVieja.get(i-1).getEstadoAdopcion()){
           
            listaNueva.add(listaVieja.get(i-1));
            }
        }
        return listaNueva;
    }

    public String obtenerNombreDueno(int id) {
        UsuarioJpaController usuario = new UsuarioJpaController();

        return usuario.findUsuario(id).getNombre();
    }
}

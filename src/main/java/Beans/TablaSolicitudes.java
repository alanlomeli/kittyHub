/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Controladores.PeticionJpaController;
import Controladores.PublicacionJpaController;
import Entidades.Peticion;
import Entidades.Publicacion;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
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
public class TablaSolicitudes implements Serializable {

    @Inject
    private Sesion sesion;

    private List<Peticion> misSolicitudes;
    private PeticionJpaController peticionController;
    private List<Peticion> peticionesRecibidas;
    private List<Peticion> gatosAdoptados;

    private int seleccionado;
    private String respuesta;

    public List<Peticion> getMisSolicitudes() {
        peticionController = new PeticionJpaController();
        misSolicitudes = peticionController.getEntityManager().createQuery("select a from Peticion a WHERE a.usuarioFk.usuarioId = " + sesion.getUsuario_id(),
                Peticion.class).getResultList();
        return misSolicitudes;
    }

    public void setMisSolicitudes(List<Peticion> misSolicitudes) {

        this.misSolicitudes = misSolicitudes;
    }

    public List<Peticion> getPeticionesRecibidas() {

        peticionController = new PeticionJpaController();
        peticionesRecibidas = peticionController.getEntityManager().createQuery("select a from Peticion a WHERE  a.estado=0 AND a.publicacionFk.usuarioFk.usuarioId = " + sesion.getUsuario_id(),
                Peticion.class).getResultList();
        return peticionesRecibidas;
    }

    public void setPeticionesRecibidas(List<Peticion> peticionesRecibidas) {
        this.peticionesRecibidas = peticionesRecibidas;
    }

    public void responderPeticion(Boolean estatus) {
        Peticion peticion = new Peticion();
        PeticionJpaController peticionController = new PeticionJpaController();
        peticion = peticionController.findPeticion(seleccionado);
        peticion.setComentarioRespuesta(respuesta);
        Short estado = (estatus) ? (short) 1 : (short) 2;
        peticion.setEstado(estado);
        
        if (estatus) {
            PublicacionJpaController publicacionController = new PublicacionJpaController();
            Publicacion publicacion = publicacionController.findPublicacion(peticion.getPublicacionFk().getPublicacionId());
            publicacion.setEstadoAdopcion(true);
            publicacion.setActivo(false);
            try {
                publicacionController.edit(publicacion);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        try {
            peticionController.edit(peticion);
            List<Peticion> peticiones = peticionController.findPeticionEntities();
            for (int i = 0; i < peticiones.size(); i++) {
                if (peticiones.get(i).getPublicacionFk().getPublicacionId().equals(peticion.getPublicacionFk().getPublicacionId())
                        && !peticiones.get(i).getPeticionId().equals(seleccionado)) { //Buscamos otra peticion con el mismo id de publicacion, pero diferente al seleccionado para cancelarlas
                    peticiones.get(i).setEstado((short) 2);
                    peticiones.get(i).setComentarioRespuesta("Se ha seleccionado a otra persona");
                    peticionController.edit(peticiones.get(i));
                }
            }

            PrimeFaces.current().executeScript("PF('dlg1').hide();");
            PrimeFaces.current().executeScript("location.reload();");

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public List<Peticion> getGatosAdoptados() {
        peticionController = new PeticionJpaController();
        gatosAdoptados = peticionController.getEntityManager().createQuery("select a from Peticion a WHERE  a.estado=1 AND a.usuarioFk.usuarioId = " + sesion.getUsuario_id(),
                Peticion.class).getResultList();
        return gatosAdoptados;
    }

    public void setGatosAdoptados(List<Peticion> gatosAdoptados) {
        this.gatosAdoptados = gatosAdoptados;
    }

    public int getSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(int seleccionado) {
        this.seleccionado = seleccionado;
    }

    public void seleccionarPeticion(int peticion_id) {
        this.seleccionado = peticion_id;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}

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
import Entidades.Usuario;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.PrimeFaces;

/**
 *
 * @author marianabojorquez
 */
@Named
@ApplicationScoped
public class VerPost implements Serializable {

    private int id;
    private Publicacion datosPublicacion;
    private String sexo;
    private String comentario;
    @Inject
    private Sesion sesion;

    @PostConstruct
    public void init() {
        comentario = "";
        if (!sesion.isIniciado()) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");  //Redireccionamos al home
            } catch (IOException ex) {
                System.out.println("Error redireccionando");
            }
        }
        getParam();
        PublicacionJpaController publicacionController = new PublicacionJpaController();
        datosPublicacion = publicacionController.findPublicacion(id);
        if (datosPublicacion.getSexo()) {
            sexo = "Femenino";
        } else {
            sexo = "Masculino";

        }

    }

    public void crearPeticion() {
        if (!datosPublicacion.getUsuarioFk().getUsuarioId().equals(sesion.getUsuario_id())) {
            PeticionJpaController peticionController = new PeticionJpaController();
            Peticion nuevaPeticion = new Peticion();
            nuevaPeticion.setPublicacionFk(datosPublicacion);
            nuevaPeticion.setUsuarioFk(new Usuario(sesion.getUsuario_id()));
            nuevaPeticion.setComentarioPeticion(comentario);
            nuevaPeticion.setEstado((short) 0);
            peticionController.create(nuevaPeticion);
            FacesMessage message = new FacesMessage("La petición se ha enviado");
            PrimeFaces.current().executeScript("PF('dlg1').hide();");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Tu petición se envió", null));
        } else {
            PrimeFaces.current().executeScript("PF('dlg1').hide();");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "¡No puedes adoptar a tu propio gato!", null));
        }
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public void getParam() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if (req.getParameter("id") != null) {
            String val = (String) req.getParameter("id");
            id = Integer.parseInt(val);
            System.out.println(id);
        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private void mostrarInfo() {

    }

    public Publicacion getDatosPublicacion() {
        return datosPublicacion;
    }

    public void setDatosPublicacion(Publicacion datosPublicacion) {
        this.datosPublicacion = datosPublicacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}

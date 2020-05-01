/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Controladores.PublicacionJpaController;
import Controladores.RazaJpaController;
import Controladores.UbicacionJpaController;
import Controladores.UsuarioJpaController;
import Entidades.Publicacion;
import Entidades.Raza;
import Entidades.Ubicacion;
import Entidades.Usuario;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.primefaces.shaded.commons.io.FilenameUtils;

/**
 *
 * @author alanlomeli
 */
@Named
@RequestScoped
public class DarAdopcion implements Serializable {

    @Inject
    private Sesion sesion;

    private String campoTitulo;
    private String campoDescripcion;
    private String campoNombre;
    private int campoEdad;
    private int campoRaza;
    private int campoUbicacion;
    private boolean campoSexo;
    private UploadedFile campoFoto;
    private StreamedContent image;

    private Raza raza;
    private UbicacionJpaController estadoController;
    private RazaJpaController razaController;
    private PublicacionJpaController publicacionController;
    private List<Raza> razas;

    @PostConstruct
    public void init() {
        if (!sesion.isIniciado()) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");  //Redireccionamos al home
            } catch (IOException ex) {
                System.out.println("Error redireccionando");
            }
        }
    }

    public void publicarAdopcion() {
        if (campoFoto.getSize() > 0) {

            publicacionController = new PublicacionJpaController();
            Publicacion publicacion= new Publicacion();
            publicacion.setUbicacionFk(estadoController.findUbicacion(campoUbicacion));
            publicacion.setUsuarioFk(new Usuario(sesion.getUsuario_id()));
            publicacion.setRazaFk(raza);
            publicacion.setTitulo(campoTitulo);
            publicacion.setDescripcion(campoDescripcion);
            publicacion.setNombre(campoNombre);
            publicacion.setSexo(campoSexo);
            publicacion.setActivo(true);
            publicacion.setEstadoAdopcion(false);
            publicacion.setEdad((short)campoEdad);
            
            
            FacesMessage message = new FacesMessage("Successful", campoFoto.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            Path folder = Paths.get("/Volumes/1TB Homework/kittyhub");
            String filename = FilenameUtils.getBaseName(campoFoto.getFileName());
            String extension = FilenameUtils.getExtension(campoFoto.getFileName());

            try {
                Path file = Files.createTempFile(folder, filename + "-", "." + extension);
                InputStream input = campoFoto.getInputstream();
                Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
                publicacion.setFoto(file.getFileName().toString());
                publicacionController.create(publicacion);
                System.out.println("Uploaded file successfully saved in " + file);

            } catch (Exception ex) {
                System.out.println(ex);
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debes adjuntar una foto", null));
        }

    }

    public StreamedContent getImage() {
        return image;
    }

    public String getCampoTitulo() {
        return campoTitulo;
    }

    public void setCampoTitulo(String campoTitulo) {
        this.campoTitulo = campoTitulo;
    }

    public String getCampoDescripcion() {
        return campoDescripcion;
    }

    public void setCampoDescripcion(String campoDescripcion) {
        this.campoDescripcion = campoDescripcion;
    }

    public String getCampoNombre() {
        return campoNombre;
    }

    public void setCampoNombre(String campoNombre) {
        this.campoNombre = campoNombre;
    }

    public int getCampoEdad() {
        return campoEdad;
    }

    public void setCampoEdad(int campoEdad) {
        this.campoEdad = campoEdad;
    }

    public int getCampoRaza() {
        return campoRaza;
    }

    public void setCampoRaza(int campoRaza) {
        this.campoRaza = campoRaza;
    }

    public int getCampoUbicacion() {
        return campoUbicacion;
    }

    public void setCampoUbicacion(int campoUbicacion) {
        this.campoUbicacion = campoUbicacion;
    }

    public boolean isCampoSexo() {
        return campoSexo;
    }

    public void setCampoSexo(boolean campoSexo) {
        this.campoSexo = campoSexo;
    }

    public UploadedFile getCampoFoto() {
        return campoFoto;
    }

    public void setCampoFoto(UploadedFile campoFoto) {
        this.campoFoto = campoFoto;
    }

    public List<Raza> obtenerRazas() {
        raza = new Raza();
        razaController = new RazaJpaController();
        return  razaController.findRazaEntities();
    }

    public Raza getRaza() {
        return raza;
    }

    public void setRaza(Raza raza) {
        this.raza = raza;
    }

    public List<Ubicacion> obtenerEstados() {

        estadoController = new UbicacionJpaController();
        return estadoController.findUbicacionEntities();
    }

    public List<Raza> getRazas() {
        return razas;
    }

    public void setRazas(List<Raza> razas) {
        this.razas = razas;
    }

}

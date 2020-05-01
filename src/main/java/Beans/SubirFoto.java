/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Named;
import static jdk.nashorn.tools.ShellFunctions.input;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.primefaces.shaded.commons.io.FilenameUtils;
import org.primefaces.shaded.commons.io.IOUtils;

/**
 *
 * @author alanlomeli
 */
@Named
@RequestScoped
public class SubirFoto {

    private UploadedFile foto;
    private StreamedContent image;

    @PostConstruct 
        public void init(){
            
        }
    public void upload() {
        if (foto != null) {
            FacesMessage message = new FacesMessage("Successful", foto.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            Path folder = Paths.get("/Volumes/1TB Homework/kittyhub");
            String filename = FilenameUtils.getBaseName(foto.getFileName());
            String extension = FilenameUtils.getExtension(foto.getFileName());

            try {
                Path file = Files.createTempFile(folder, filename + "-", "." + extension);
                InputStream input = foto.getInputstream();
                Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Uploaded file successfully saved in " + file);

            } catch (Exception ex) {
                System.out.println(ex);
            }
        }

    }

    public StreamedContent getImage() {
        return image;
    }

 

    public UploadedFile getFoto() {
        return foto;
    }

    public void setFoto(UploadedFile foto) {
        this.foto = foto;
    }

}

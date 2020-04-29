/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Controladores.UsuarioJpaController;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author alanlomeli
 */
@Named
@RequestScoped
public class DarAdopcion implements Serializable {

    @Inject
    private Sesion sesion;

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
}

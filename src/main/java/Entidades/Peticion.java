/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author alanlomeli
 */
@Entity
@Table(name = "peticion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Peticion.findAll", query = "SELECT p FROM Peticion p"),
    @NamedQuery(name = "Peticion.findByPeticionId", query = "SELECT p FROM Peticion p WHERE p.peticionId = :peticionId"),
    @NamedQuery(name = "Peticion.findByComentarioPeticion", query = "SELECT p FROM Peticion p WHERE p.comentarioPeticion = :comentarioPeticion"),
    @NamedQuery(name = "Peticion.findByComentarioRespuesta", query = "SELECT p FROM Peticion p WHERE p.comentarioRespuesta = :comentarioRespuesta"),
    @NamedQuery(name = "Peticion.findByEstado", query = "SELECT p FROM Peticion p WHERE p.estado = :estado")})
public class Peticion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "peticion_id")
    private Integer peticionId;
    @Size(max = 255)
    @Column(name = "comentario_peticion")
    private String comentarioPeticion;
    @Size(max = 255)
    @Column(name = "comentario_respuesta")
    private String comentarioRespuesta;
    @Column(name = "estado")
    private Short estado;
    @JoinColumn(name = "publicacion_fk", referencedColumnName = "publicacion_Id")
    @ManyToOne
    private Publicacion publicacionFk;
    @JoinColumn(name = "usuario_fk", referencedColumnName = "usuario")
    @ManyToOne
    private Usuario usuarioFk;

    public Peticion() {
    }

    public Peticion(Integer peticionId) {
        this.peticionId = peticionId;
    }

    public Integer getPeticionId() {
        return peticionId;
    }

    public void setPeticionId(Integer peticionId) {
        this.peticionId = peticionId;
    }

    public String getComentarioPeticion() {
        return comentarioPeticion;
    }

    public void setComentarioPeticion(String comentarioPeticion) {
        this.comentarioPeticion = comentarioPeticion;
    }

    public String getComentarioRespuesta() {
        return comentarioRespuesta;
    }

    public void setComentarioRespuesta(String comentarioRespuesta) {
        this.comentarioRespuesta = comentarioRespuesta;
    }

    public Short getEstado() {
        return estado;
    }

    public void setEstado(Short estado) {
        this.estado = estado;
    }

    public Publicacion getPublicacionFk() {
        return publicacionFk;
    }

    public void setPublicacionFk(Publicacion publicacionFk) {
        this.publicacionFk = publicacionFk;
    }

    public Usuario getUsuarioFk() {
        return usuarioFk;
    }

    public void setUsuarioFk(Usuario usuarioFk) {
        this.usuarioFk = usuarioFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (peticionId != null ? peticionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Peticion)) {
            return false;
        }
        Peticion other = (Peticion) object;
        if ((this.peticionId == null && other.peticionId != null) || (this.peticionId != null && !this.peticionId.equals(other.peticionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Peticion[ peticionId=" + peticionId + " ]";
    }
    
}

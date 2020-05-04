/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author marianabojorquez
 */
@Entity
@Table(name = "raza")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Raza.findAll", query = "SELECT r FROM Raza r"),
    @NamedQuery(name = "Raza.findByRazaId", query = "SELECT r FROM Raza r WHERE r.razaId = :razaId"),
    @NamedQuery(name = "Raza.findByNombre", query = "SELECT r FROM Raza r WHERE r.nombre = :nombre"),
    @NamedQuery(name = "Raza.findByFoto", query = "SELECT r FROM Raza r WHERE r.foto = :foto")})
public class Raza implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "raza_id")
    private Integer razaId;
    @Size(max = 30)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 30)
    @Column(name = "foto")
    private String foto;
    @OneToMany(mappedBy = "razaFk")
    private List<Publicacion> publicacionList;

    public Raza() {
    }

    public Raza(Integer razaId,String nombre,String foto) {
        this.razaId=razaId;
        this.nombre=nombre;
        this.foto=foto;
    }

    public Raza(Integer razaId) {
        this.razaId = razaId;
    }

    public Integer getRazaId() {
        return razaId;
    }

    public void setRazaId(Integer razaId) {
        this.razaId = razaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @XmlTransient
    public List<Publicacion> getPublicacionList() {
        return publicacionList;
    }

    public void setPublicacionList(List<Publicacion> publicacionList) {
        this.publicacionList = publicacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (razaId != null ? razaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Raza)) {
            return false;
        }
        Raza other = (Raza) object;
        if ((this.razaId == null && other.razaId != null) || (this.razaId != null && !this.razaId.equals(other.razaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Raza[ razaId=" + razaId + " ]";
    }

}

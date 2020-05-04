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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "publicacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Publicacion.findAll", query = "SELECT p FROM Publicacion p"),
    @NamedQuery(name = "Publicacion.findByPublicacionId", query = "SELECT p FROM Publicacion p WHERE p.publicacionId = :publicacionId"),
    @NamedQuery(name = "Publicacion.findByTitulo", query = "SELECT p FROM Publicacion p WHERE p.titulo = :titulo"),
    @NamedQuery(name = "Publicacion.findByDescripcion", query = "SELECT p FROM Publicacion p WHERE p.descripcion = :descripcion"),
    @NamedQuery(name = "Publicacion.findByNombre", query = "SELECT p FROM Publicacion p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Publicacion.findBySexo", query = "SELECT p FROM Publicacion p WHERE p.sexo = :sexo"),
    @NamedQuery(name = "Publicacion.findByActivo", query = "SELECT p FROM Publicacion p WHERE p.activo = :activo"),
    @NamedQuery(name = "Publicacion.findByEstadoAdopcion", query = "SELECT p FROM Publicacion p WHERE p.estadoAdopcion = :estadoAdopcion"),
    @NamedQuery(name = "Publicacion.findByEdad", query = "SELECT p FROM Publicacion p WHERE p.edad = :edad"),
    @NamedQuery(name = "Publicacion.findByFoto", query = "SELECT p FROM Publicacion p WHERE p.foto = :foto")})
public class Publicacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "publicacion_Id")
    private Integer publicacionId;
    @Size(max = 100)
    @Column(name = "titulo")
    private String titulo;
    @Size(max = 255)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 40)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "sexo")
    private Boolean sexo;
    @Column(name = "activo")
    private Boolean activo;
    @Column(name = "estado_adopcion")
    private Boolean estadoAdopcion;
    @Column(name = "edad")
    private Short edad;
    @Size(max = 255)
    @Column(name = "foto")
    private String foto;
    @OneToMany(mappedBy = "publicacionFk")
    private List<Peticion> peticionList;
    @JoinColumn(name = "ubicacion_fk", referencedColumnName = "ubicacion_id")
    @ManyToOne
    private Ubicacion ubicacionFk;
    @JoinColumn(name = "raza_fk", referencedColumnName = "raza_id")
    @ManyToOne
    private Raza razaFk;
    @JoinColumn(name = "usuario_fk", referencedColumnName = "usuario_id")
    @ManyToOne
    private Usuario usuarioFk;

    public Publicacion() {
    }

    public Publicacion(Integer publicacionId) {
        this.publicacionId = publicacionId;
    }

    public Integer getPublicacionId() {
        return publicacionId;
    }

    public void setPublicacionId(Integer publicacionId) {
        this.publicacionId = publicacionId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getSexo() {
        return sexo;
    }

    public void setSexo(Boolean sexo) {
        this.sexo = sexo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Boolean getEstadoAdopcion() {
        return estadoAdopcion;
    }

    public void setEstadoAdopcion(Boolean estadoAdopcion) {
        this.estadoAdopcion = estadoAdopcion;
    }

    public Short getEdad() {
        return edad;
    }

    public void setEdad(Short edad) {
        this.edad = edad;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @XmlTransient
    public List<Peticion> getPeticionList() {
        return peticionList;
    }

    public void setPeticionList(List<Peticion> peticionList) {
        this.peticionList = peticionList;
    }

    public Ubicacion getUbicacionFk() {
        return ubicacionFk;
    }

    public void setUbicacionFk(Ubicacion ubicacionFk) {
        this.ubicacionFk = ubicacionFk;
    }

    public Raza getRazaFk() {
        return razaFk;
    }

    public void setRazaFk(Raza razaFk) {
        this.razaFk = razaFk;
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
        hash += (publicacionId != null ? publicacionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Publicacion)) {
            return false;
        }
        Publicacion other = (Publicacion) object;
        if ((this.publicacionId == null && other.publicacionId != null) || (this.publicacionId != null && !this.publicacionId.equals(other.publicacionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Publicacion[ publicacionId=" + publicacionId + " ]";
    }
    
}

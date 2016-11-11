/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author aganius
 */
@Entity
@Table(name = "preguntas_encuesta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PreguntasEncuesta.findAll", query = "SELECT p FROM PreguntasEncuesta p"),
    @NamedQuery(name = "PreguntasEncuesta.findByTNombre", query = "SELECT p FROM PreguntasEncuesta p WHERE p.preguntasEncuestaPK.tNombre = :tNombre"),
    @NamedQuery(name = "PreguntasEncuesta.findByTCi", query = "SELECT p FROM PreguntasEncuesta p WHERE p.preguntasEncuestaPK.tTelefono = :tTelefono"),
    @NamedQuery(name = "PreguntasEncuesta.findByTRif", query = "SELECT p FROM PreguntasEncuesta p WHERE p.preguntasEncuestaPK.tRif = :tRif"),
    @NamedQuery(name = "PreguntasEncuesta.findById", query = "SELECT p FROM PreguntasEncuesta p WHERE p.preguntasEncuestaPK.id = :id"),
    @NamedQuery(name = "PreguntasEncuesta.findByPregunta", query = "SELECT p FROM PreguntasEncuesta p WHERE p.pregunta = :pregunta")})
public class PreguntasEncuesta implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "preguntasEncuesta", orphanRemoval=true)
    private Collection<OpcionesPregunta> opcionesPreguntaCollection;

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PreguntasEncuestaPK preguntasEncuestaPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "pregunta")
    private String pregunta;

    @JoinColumns({
        @JoinColumn(name = "t_nombre", referencedColumnName = "nombre", insertable = false, updatable = false),
        @JoinColumn(name = "t_telefono", referencedColumnName = "telefono", insertable = false, updatable = false),
        @JoinColumn(name = "t_rif", referencedColumnName = "rif_empresa", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private TEncuesta tEncuesta;

    public PreguntasEncuesta() {
    }

    public PreguntasEncuesta(PreguntasEncuestaPK preguntasEncuestaPK) {
        this.preguntasEncuestaPK = preguntasEncuestaPK;
    }



    public PreguntasEncuesta(String tNombre, String tTelefono, String tRif, int id) {
        this.preguntasEncuestaPK = new PreguntasEncuestaPK(tNombre, tTelefono, tRif, id);
    }

    public PreguntasEncuestaPK getPreguntasEncuestaPK() {
        return preguntasEncuestaPK;
    }

    public void setPreguntasEncuestaPK(PreguntasEncuestaPK preguntasEncuestaPK) {
        this.preguntasEncuestaPK = preguntasEncuestaPK;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public TEncuesta getTEncuesta() {
        return tEncuesta;
    }

    public void setTEncuesta(TEncuesta tEncuesta) {
        this.tEncuesta = tEncuesta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (preguntasEncuestaPK != null ? preguntasEncuestaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PreguntasEncuesta)) {
            return false;
        }
        PreguntasEncuesta other = (PreguntasEncuesta) object;
        if ((this.preguntasEncuestaPK == null && other.preguntasEncuestaPK != null) || (this.preguntasEncuestaPK != null && !this.preguntasEncuestaPK.equals(other.preguntasEncuestaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String salida = "{"
                + preguntasEncuestaPK.toString() + ","
                + "\"pregunta\":\"" + pregunta + "\""
                + "}";
        return salida;
    }

    @XmlTransient
    public Collection<OpcionesPregunta> getOpcionesPreguntaCollection() {
        return opcionesPreguntaCollection;
    }

    public void setOpcionesPreguntaCollection(Collection<OpcionesPregunta> opcionesPreguntaCollection) {
        this.opcionesPreguntaCollection = opcionesPreguntaCollection;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author aganius
 */
@Entity
@Table(name = "opciones_pregunta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OpcionesPregunta.findAll", query = "SELECT o FROM OpcionesPregunta o"),
    @NamedQuery(name = "OpcionesPregunta.findByTNombre", query = "SELECT o FROM OpcionesPregunta o WHERE o.opcionesPreguntaPK.tNombre = :tNombre"),
    @NamedQuery(name = "OpcionesPregunta.findByTCi", query = "SELECT o FROM OpcionesPregunta o WHERE o.opcionesPreguntaPK.tTelefono = :tTelefono"),
    @NamedQuery(name = "OpcionesPregunta.findByTRif", query = "SELECT o FROM OpcionesPregunta o WHERE o.opcionesPreguntaPK.tRif = :tRif"),
    @NamedQuery(name = "OpcionesPregunta.findByPreguntaId", query = "SELECT o FROM OpcionesPregunta o WHERE o.opcionesPreguntaPK.preguntaId = :preguntaId"),
    @NamedQuery(name = "OpcionesPregunta.findById", query = "SELECT o FROM OpcionesPregunta o WHERE o.opcionesPreguntaPK.id = :id"),
    @NamedQuery(name = "OpcionesPregunta.findByOpcion", query = "SELECT o FROM OpcionesPregunta o WHERE o.opcion = :opcion")})
public class OpcionesPregunta implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected OpcionesPreguntaPK opcionesPreguntaPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "opcion")
    private String opcion;
    @JoinColumns({
        @JoinColumn(name = "t_nombre", referencedColumnName = "t_nombre", insertable = false, updatable = false),
        @JoinColumn(name = "t_telefono", referencedColumnName = "t_telefono", insertable = false, updatable = false),
        @JoinColumn(name = "t_rif", referencedColumnName = "t_rif", insertable = false, updatable = false),
        @JoinColumn(name = "pregunta_id", referencedColumnName = "id", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private PreguntasEncuesta preguntasEncuesta;

    public OpcionesPregunta() {
    }

    public OpcionesPregunta(OpcionesPreguntaPK opcionesPreguntaPK) {
        this.opcionesPreguntaPK = opcionesPreguntaPK;
    }

    public OpcionesPregunta(OpcionesPreguntaPK opcionesPreguntaPK, String opcion) {
        this.opcionesPreguntaPK = opcionesPreguntaPK;
        this.opcion = opcion;
    }

    public OpcionesPregunta(String tNombre, String t_telefono, String tRif, int preguntaId, int id) {
        this.opcionesPreguntaPK = new OpcionesPreguntaPK(tNombre, t_telefono, tRif, preguntaId, id);
    }

    public OpcionesPreguntaPK getOpcionesPreguntaPK() {
        return opcionesPreguntaPK;
    }

    public void setOpcionesPreguntaPK(OpcionesPreguntaPK opcionesPreguntaPK) {
        this.opcionesPreguntaPK = opcionesPreguntaPK;
    }

    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }

    public PreguntasEncuesta getPreguntasEncuesta() {
        return preguntasEncuesta;
    }

    public void setPreguntasEncuesta(PreguntasEncuesta preguntasEncuesta) {
        this.preguntasEncuesta = preguntasEncuesta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (opcionesPreguntaPK != null ? opcionesPreguntaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OpcionesPregunta)) {
            return false;
        }
        OpcionesPregunta other = (OpcionesPregunta) object;
        if ((this.opcionesPreguntaPK == null && other.opcionesPreguntaPK != null) || (this.opcionesPreguntaPK != null && !this.opcionesPreguntaPK.equals(other.opcionesPreguntaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String salida = "{"
                + opcionesPreguntaPK.toString() + ","
                + "\"opcion\":\"" + opcion + "\""
                + "}";
        return salida;
    }
    
}

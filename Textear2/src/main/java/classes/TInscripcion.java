/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author aganius
 */
@Entity
@Table(name = "t_inscripcion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TInscripcion.findAll", query = "SELECT t FROM TInscripcion t"),
    @NamedQuery(name = "TInscripcion.findByRifEmpresa", query = "SELECT t FROM TInscripcion t WHERE t.tInscripcionPK.rifEmpresa = :rifEmpresa"),
    @NamedQuery(name = "TInscripcion.findByTelefono", query = "SELECT t FROM TInscripcion t WHERE t.tInscripcionPK.telefono = :telefono"),
    @NamedQuery(name = "TInscripcion.findByNombre", query = "SELECT t FROM TInscripcion t WHERE t.tInscripcionPK.nombre = :nombre"),
    @NamedQuery(name = "TInscripcion.findByEstado", query = "SELECT t FROM TInscripcion t WHERE t.estado = :estado"),
    @NamedQuery(name = "TInscripcion.findByFechaEnvio", query = "SELECT t FROM TInscripcion t WHERE t.fechaEnvio = :fechaEnvio"),
    @NamedQuery(name = "TInscripcion.findByFechaCreacion", query = "SELECT t FROM TInscripcion t WHERE t.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "TInscripcion.findByFechaExpiracion", query = "SELECT t FROM TInscripcion t WHERE t.fechaExpiracion = :fechaExpiracion"),
    @NamedQuery(name = "TInscripcion.findByRespuesta", query = "SELECT t FROM TInscripcion t WHERE t.respuesta = :respuesta"),
    @NamedQuery(name = "TInscripcion.findByBandeja", query = "SELECT t FROM TInscripcion t WHERE t.bandeja = :bandeja")})
public class TInscripcion implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TInscripcionPK tInscripcionPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "estado")
    private String estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_envio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEnvio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_expiracion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaExpiracion;
    @Basic(optional = true)
    @Size(min = 1, max = 2147483647)
    @Column(name = "respuesta")
    private String respuesta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "bandeja")
    private String bandeja;
    @JoinColumns({
        @JoinColumn(name = "telefono", referencedColumnName = "telefono", insertable = false, updatable = false),
        @JoinColumn(name = "rif_empresa", referencedColumnName = "rif_empresa", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Abonado abonado;
    @JoinColumn(name = "codigo_canal", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Canal codigoCanal;

    public TInscripcion() {
    }

    public TInscripcion(TInscripcionPK tInscripcionPK) {
        this.tInscripcionPK = tInscripcionPK;
    }

    public TInscripcion(TInscripcionPK tInscripcionPK, String estado, Date fechaEnvio, Date fechaCreacion, Date fechaExpiracion, String respuesta, String bandeja) {
        this.tInscripcionPK = tInscripcionPK;
        this.estado = estado;
        this.fechaEnvio = fechaEnvio;
        this.fechaCreacion = fechaCreacion;
        this.fechaExpiracion = fechaExpiracion;
        this.respuesta = respuesta;
        this.bandeja = bandeja;
    }

    public TInscripcion(String rifEmpresa, String telefono, String nombre) {
        this.tInscripcionPK = new TInscripcionPK(rifEmpresa, telefono, nombre);
    }

    public TInscripcionPK getTInscripcionPK() {
        return tInscripcionPK;
    }

    public void setTInscripcionPK(TInscripcionPK tInscripcionPK) {
        this.tInscripcionPK = tInscripcionPK;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(Date fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getBandeja() {
        return bandeja;
    }

    public void setBandeja(String bandeja) {
        this.bandeja = bandeja;
    }

    public Abonado getAbonado() {
        return abonado;
    }

    public void setAbonado(Abonado abonado) {
        this.abonado = abonado;
    }

    public Canal getCodigoCanal() {
        return codigoCanal;
    }

    public void setCodigoCanal(Canal codigoCanal) {
        this.codigoCanal = codigoCanal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tInscripcionPK != null ? tInscripcionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TInscripcion)) {
            return false;
        }
        TInscripcion other = (TInscripcion) object;
        if ((this.tInscripcionPK == null && other.tInscripcionPK != null) || (this.tInscripcionPK != null && !this.tInscripcionPK.equals(other.tInscripcionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
                String salida = "{"
                + tInscripcionPK.toString() + ","
                + "\"canal\":\"" + codigoCanal.toString() + "\","
                + "\"abonado\":\"" + abonado.toString() + "\","
                + "\"estado\":\"" + estado + "\","
                + "\"fechaEnvio\":\"" + fechaEnvio + "\","
                + "\"fechaCreacion\":\"" + fechaCreacion + "\","
                + "\"fechaExpiracion\":\"" + fechaExpiracion + "\","
                + "\"bandeja\":\"" + bandeja + "\","
                + "\"respuesta\":\"" + respuesta + "\""
                + "}";
        return salida;
    }
    
}
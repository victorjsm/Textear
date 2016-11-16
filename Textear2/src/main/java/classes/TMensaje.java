/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "t_mensaje")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TMensaje.findAll", query = "SELECT t FROM TMensaje t"),
    @NamedQuery(name = "TMensaje.findByRifEmpresa", query = "SELECT t FROM TMensaje t WHERE t.tMensajePK.rifEmpresa = :rifEmpresa"),
    @NamedQuery(name = "TMensaje.findByTelefono", query = "SELECT t FROM TMensaje t WHERE t.tMensajePK.telefono = :telefono"),
    @NamedQuery(name = "TMensaje.findByNombre", query = "SELECT t FROM TMensaje t WHERE t.tMensajePK.nombre = :nombre"),
    @NamedQuery(name = "TMensaje.findByEstado", query = "SELECT t FROM TMensaje t WHERE t.estado = :estado"),
    @NamedQuery(name = "TMensaje.findByFechaEnvio", query = "SELECT t FROM TMensaje t WHERE t.fechaEnvio = :fechaEnvio"),
    @NamedQuery(name = "TMensaje.findByFechaCreacion", query = "SELECT t FROM TMensaje t WHERE t.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "TMensaje.findByFechaExpiracion", query = "SELECT t FROM TMensaje t WHERE t.fechaExpiracion = :fechaExpiracion"),
    @NamedQuery(name = "TMensaje.findByMensaje", query = "SELECT t FROM TMensaje t WHERE t.mensaje = :mensaje"),
    @NamedQuery(name = "TMensaje.findByBandeja", query = "SELECT t FROM TMensaje t WHERE t.bandeja = :bandeja")})
public class TMensaje implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TMensajePK tMensajePK;
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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "mensaje")
    private String mensaje;
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

    public TMensaje() {
    }

    public TMensaje(TMensajePK tMensajePK) {
        this.tMensajePK = tMensajePK;
    }

    public TMensaje(TMensajePK tMensajePK, String estado, Date fechaEnvio, Date fechaCreacion, Date fechaExpiracion, String mensaje, String bandeja, BigDecimal precioTotal) {
        this.tMensajePK = tMensajePK;
        this.estado = estado;
        this.fechaEnvio = fechaEnvio;
        this.fechaCreacion = fechaCreacion;
        this.fechaExpiracion = fechaExpiracion;
        this.mensaje = mensaje;
        this.bandeja = bandeja;
    }

    public TMensaje(String rifEmpresa, String telefono, String nombre) {
        this.tMensajePK = new TMensajePK(rifEmpresa, telefono, nombre);
    }

    public TMensajePK getTMensajePK() {
        return tMensajePK;
    }

    public void setTMensajePK(TMensajePK tMensajePK) {
        this.tMensajePK = tMensajePK;
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

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
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
        hash += (tMensajePK != null ? tMensajePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TMensaje)) {
            return false;
        }
        TMensaje other = (TMensaje) object;
        if ((this.tMensajePK == null && other.tMensajePK != null) || (this.tMensajePK != null && !this.tMensajePK.equals(other.tMensajePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String salida = "{"
                + tMensajePK.toString() + ","
                + "\"canal\":\"" + codigoCanal.toString() + "\","
                + "\"abonado\":\"" + abonado.toString() + "\","
                + "\"estado\":\"" + estado + "\","
                + "\"fechaEnvio\":\"" + fechaEnvio + "\","
                + "\"fechaCreacion\":\"" + fechaCreacion + "\","
                + "\"fechaExpiracion\":\"" + fechaExpiracion + "\","
                + "\"bandeja\":\"" + bandeja + "\","
                + "\"mensaje\":\"" + mensaje + "\""
                + "}";
        return salida;
    }
    
}

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
@Table(name = "t_sistema")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TSistema.findAll", query = "SELECT t FROM TSistema t"),
    @NamedQuery(name = "TSistema.findByRifEmpresa", query = "SELECT t FROM TSistema t WHERE t.tSistemaPK.rifEmpresa = :rifEmpresa"),
    @NamedQuery(name = "TSistema.findByTelefono", query = "SELECT t FROM TSistema t WHERE t.tSistemaPK.telefono = :telefono"),
    @NamedQuery(name = "TSistema.findByNombre", query = "SELECT t FROM TSistema t WHERE t.tSistemaPK.nombre = :nombre"),
    @NamedQuery(name = "TSistema.findByEstado", query = "SELECT t FROM TSistema t WHERE t.estado = :estado"),
    @NamedQuery(name = "TSistema.findByFechaEnvio", query = "SELECT t FROM TSistema t WHERE t.fechaEnvio = :fechaEnvio"),
    @NamedQuery(name = "TSistema.findByFechaCreacion", query = "SELECT t FROM TSistema t WHERE t.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "TSistema.findByFechaExpiracion", query = "SELECT t FROM TSistema t WHERE t.fechaExpiracion = :fechaExpiracion"),
    @NamedQuery(name = "TSistema.findByBandeja", query = "SELECT t FROM TSistema t WHERE t.bandeja = :bandeja")})
public class TSistema implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TSistemaPK tSistemaPK;
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

    public TSistema() {
    }

    public TSistema(TSistemaPK tSistemaPK) {
        this.tSistemaPK = tSistemaPK;
    }

    public TSistema(TSistemaPK tSistemaPK, String estado, Date fechaEnvio, Date fechaCreacion, Date fechaExpiracion, String bandeja) {
        this.tSistemaPK = tSistemaPK;
        this.estado = estado;
        this.fechaEnvio = fechaEnvio;
        this.fechaCreacion = fechaCreacion;
        this.fechaExpiracion = fechaExpiracion;
        this.bandeja = bandeja;
    }

    public TSistema(String rifEmpresa, String telefono, String nombre) {
        this.tSistemaPK = new TSistemaPK(rifEmpresa, telefono, nombre);
    }

    public TSistemaPK getTSistemaPK() {
        return tSistemaPK;
    }

    public void setTSistemaPK(TSistemaPK tSistemaPK) {
        this.tSistemaPK = tSistemaPK;
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
        hash += (tSistemaPK != null ? tSistemaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TSistema)) {
            return false;
        }
        TSistema other = (TSistema) object;
        if ((this.tSistemaPK == null && other.tSistemaPK != null) || (this.tSistemaPK != null && !this.tSistemaPK.equals(other.tSistemaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String salida = "{"
                + tSistemaPK.toString() + ","
                + "\"canal\":\"" + codigoCanal.toString() + "\","
                + "\"abonado\":\"" + abonado.toString() + "\","
                + "\"estado\":\"" + estado + "\","
                + "\"fechaEnvio\":\"" + fechaEnvio + "\","
                + "\"fechaCreacion\":\"" + fechaCreacion + "\","
                + "\"fechaExpiracion\":\"" + fechaExpiracion + "\","
                + "\"bandeja\":\"" + bandeja + "\""
                + "}";
        return salida;
    }


}
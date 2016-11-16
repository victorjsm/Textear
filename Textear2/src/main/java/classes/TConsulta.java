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
@Table(name = "t_consulta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TConsulta.findAll", query = "SELECT t FROM TConsulta t"),
    @NamedQuery(name = "TConsulta.findByRifEmpresa", query = "SELECT t FROM TConsulta t WHERE t.tConsultaPK.rifEmpresa = :rifEmpresa"),
    @NamedQuery(name = "TConsulta.findByCi", query = "SELECT t FROM TConsulta t WHERE t.tConsultaPK.telefono = :telefono"),
    @NamedQuery(name = "TConsulta.findByNombre", query = "SELECT t FROM TConsulta t WHERE t.tConsultaPK.nombre = :nombre"),
    @NamedQuery(name = "TConsulta.findByEstado", query = "SELECT t FROM TConsulta t WHERE t.estado = :estado"),
    @NamedQuery(name = "TConsulta.findByFechaEnvio", query = "SELECT t FROM TConsulta t WHERE t.fechaEnvio = :fechaEnvio"),
    @NamedQuery(name = "TConsulta.findByFechaCreacion", query = "SELECT t FROM TConsulta t WHERE t.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "TConsulta.findByFechaExpiracion", query = "SELECT t FROM TConsulta t WHERE t.fechaExpiracion = :fechaExpiracion"),
    @NamedQuery(name = "TConsulta.findByBienvenida", query = "SELECT t FROM TConsulta t WHERE t.bienvenida = :bienvenida"),
    @NamedQuery(name = "TConsulta.findByAyuda", query = "SELECT t FROM TConsulta t WHERE t.ayuda = :ayuda"),
    @NamedQuery(name = "TConsulta.findByRespuesta", query = "SELECT t FROM TConsulta t WHERE t.respuesta = :respuesta"),
    @NamedQuery(name = "TConsulta.findByBandeja", query = "SELECT t FROM TConsulta t WHERE t.bandeja = :bandeja"),
    @NamedQuery(name = "TConsulta.findByPregunta", query = "SELECT t FROM TConsulta t WHERE t.pregunta = :pregunta")})
public class TConsulta implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TConsultaPK tConsultaPK;
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
    @Column(name = "bienvenida")
    private String bienvenida;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "ayuda")
    private String ayuda;
    @Basic(optional = true)
    @Size(min = 1, max = 2147483647)
    @Column(name = "respuesta")
    private String respuesta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "bandeja")
    private String bandeja;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "pregunta")
    private String pregunta;
    @JoinColumns({
        @JoinColumn(name = "telefono", referencedColumnName = "telefono", insertable = false, updatable = false),
        @JoinColumn(name = "rif_empresa", referencedColumnName = "rif_empresa", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Abonado abonado;
    @JoinColumn(name = "codigo_canal", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Canal codigoCanal;

    public TConsulta() {
    }

    public TConsulta(TConsultaPK tConsultaPK) {
        this.tConsultaPK = tConsultaPK;
    }

    public TConsulta(TConsultaPK tConsultaPK, String estado, Date fechaEnvio, Date fechaCreacion, Date fechaExpiracion, String bienvenida, String ayuda, String respuesta, String bandeja, String pregunta) {
        this.tConsultaPK = tConsultaPK;
        this.estado = estado;
        this.fechaEnvio = fechaEnvio;
        this.fechaCreacion = fechaCreacion;
        this.fechaExpiracion = fechaExpiracion;
        this.bienvenida = bienvenida;
        this.ayuda = ayuda;
        this.respuesta = respuesta;
        this.bandeja = bandeja;
        this.pregunta = pregunta;
    }

    public TConsulta(String rifEmpresa, String ci, String nombre) {
        this.tConsultaPK = new TConsultaPK(rifEmpresa, ci, nombre);
    }

    public TConsultaPK getTConsultaPK() {
        return tConsultaPK;
    }

    public void setTConsultaPK(TConsultaPK tConsultaPK) {
        this.tConsultaPK = tConsultaPK;
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

    public String getBienvenida() {
        return bienvenida;
    }

    public void setBienvenida(String bienvenida) {
        this.bienvenida = bienvenida;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void setAyuda(String ayuda) {
        this.ayuda = ayuda;
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

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
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
        hash += (tConsultaPK != null ? tConsultaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TConsulta)) {
            return false;
        }
        TConsulta other = (TConsulta) object;
        if ((this.tConsultaPK == null && other.tConsultaPK != null) || (this.tConsultaPK != null && !this.tConsultaPK.equals(other.tConsultaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String salida = "{"
                + tConsultaPK.toString() + ","
                + "\"canal\":\"" + codigoCanal.toString() + "\","
                + "\"abonado\":\"" + abonado.toString() + "\","
                + "\"estado\":\"" + estado + "\","
                + "\"fechaEnvio\":\"" + fechaEnvio + "\","
                + "\"fechaCreacion\":\"" + fechaCreacion + "\","
                + "\"fechaExpiracion\":\"" + fechaExpiracion + "\","
                + "\"bandeja\":\"" + bandeja + "\","
                + "\"ayuda\":\"" + ayuda + "\","
                + "\"respuesta\":\"" + respuesta + "\","
                + "\"pregunta\":\"" + pregunta + "\","
                + "\"bienvenida\":\"" + bienvenida + "\""
                + "}";
        return salida;
    }
    
}
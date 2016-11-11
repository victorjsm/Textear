/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author aganius
 */
@Entity
@Table(name = "t_encuesta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TEncuesta.findAll", query = "SELECT t FROM TEncuesta t"),
    @NamedQuery(name = "TEncuesta.findByRifEmpresa", query = "SELECT t FROM TEncuesta t WHERE t.tEncuestaPK.rifEmpresa = :rifEmpresa"),
    @NamedQuery(name = "TEncuesta.findByCi", query = "SELECT t FROM TEncuesta t WHERE t.tEncuestaPK.telefono = :telefono"),
    @NamedQuery(name = "TEncuesta.findByNombre", query = "SELECT t FROM TEncuesta t WHERE t.tEncuestaPK.nombre = :nombre"),
    @NamedQuery(name = "TEncuesta.findByEstado", query = "SELECT t FROM TEncuesta t WHERE t.estado = :estado"),
    @NamedQuery(name = "TEncuesta.findByFechaEnvio", query = "SELECT t FROM TEncuesta t WHERE t.fechaEnvio = :fechaEnvio"),
    @NamedQuery(name = "TEncuesta.findByFechaCreacion", query = "SELECT t FROM TEncuesta t WHERE t.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "TEncuesta.findByFechaExpiracion", query = "SELECT t FROM TEncuesta t WHERE t.fechaExpiracion = :fechaExpiracion"),
    @NamedQuery(name = "TEncuesta.findByBienvenida", query = "SELECT t FROM TEncuesta t WHERE t.bienvenida = :bienvenida"),
    @NamedQuery(name = "TEncuesta.findByAyuda", query = "SELECT t FROM TEncuesta t WHERE t.ayuda = :ayuda"),
    @NamedQuery(name = "TEncuesta.findByRespuesta", query = "SELECT t FROM TEncuesta t WHERE t.respuesta = :respuesta"),
    @NamedQuery(name = "TEncuesta.findByBandeja", query = "SELECT t FROM TEncuesta t WHERE t.bandeja = :bandeja"),
    @NamedQuery(name = "TEncuesta.findByPrecioTotal", query = "SELECT t FROM TEncuesta t WHERE t.precioTotal = :precioTotal")})
public class TEncuesta implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TEncuestaPK tEncuestaPK;
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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "respuesta")
    private String respuesta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "bandeja")
    private String bandeja;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio_total")
    private BigDecimal precioTotal;
    @JoinColumns({
        @JoinColumn(name = "telefono", referencedColumnName = "telefono", insertable = false, updatable = false),
        @JoinColumn(name = "rif_empresa", referencedColumnName = "rif_empresa", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Abonado abonado;
    @JoinColumn(name = "codigo_canal", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Canal codigoCanal;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tEncuesta", orphanRemoval=true)
    private Collection<PreguntasEncuesta> preguntasEncuestaCollection;

    public TEncuesta() {
    }

    public TEncuesta(TEncuestaPK tEncuestaPK) {
        this.tEncuestaPK = tEncuestaPK;
    }

    public TEncuesta(TEncuestaPK tEncuestaPK, String estado, Date fechaEnvio, Date fechaCreacion, Date fechaExpiracion, String bienvenida, String ayuda, String respuesta, String bandeja, BigDecimal precioTotal) {
        this.tEncuestaPK = tEncuestaPK;
        this.estado = estado;
        this.fechaEnvio = fechaEnvio;
        this.fechaCreacion = fechaCreacion;
        this.fechaExpiracion = fechaExpiracion;
        this.bienvenida = bienvenida;
        this.ayuda = ayuda;
        this.respuesta = respuesta;
        this.bandeja = bandeja;
        this.precioTotal = precioTotal;
    }

    public TEncuesta(String rifEmpresa, String ci, String nombre) {
        this.tEncuestaPK = new TEncuestaPK(rifEmpresa, ci, nombre);
    }

    public TEncuestaPK getTEncuestaPK() {
        return tEncuestaPK;
    }

    public void setTEncuestaPK(TEncuestaPK tEncuestaPK) {
        this.tEncuestaPK = tEncuestaPK;
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

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
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

    @XmlTransient
    public Collection<PreguntasEncuesta> getPreguntasEncuestaCollection() {
        return preguntasEncuestaCollection;
    }

    public void setPreguntasEncuestaCollection(Collection<PreguntasEncuesta> preguntasEncuestaCollection) {
        this.preguntasEncuestaCollection = preguntasEncuestaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tEncuestaPK != null ? tEncuestaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TEncuesta)) {
            return false;
        }
        TEncuesta other = (TEncuesta) object;
        if ((this.tEncuestaPK == null && other.tEncuestaPK != null) || (this.tEncuestaPK != null && !this.tEncuestaPK.equals(other.tEncuestaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String salida = "{"
                + tEncuestaPK.toString() + ","
                + "\"precio\":" + precioTotal + "\","
                + "\"canal\":\"" + codigoCanal.toString() + "\","
                + "\"abonado\":\"" + abonado.toString() + "\","
                + "\"estado\":\"" + estado + "\","
                + "\"fechaEnvio\":\"" + fechaEnvio + "\","
                + "\"fechaCreacion\":\"" + fechaCreacion + "\","
                + "\"fechaExpiracion\":\"" + fechaExpiracion + "\","
                + "\"bandeja\":\"" + bandeja + "\","
                + "\"ayuda\":\"" + ayuda + "\","
                + "\"respuesta\":\"" + respuesta + "\","
                + "\"bienvenida\":\"" + bienvenida + "\""
                + "}";
        return salida;
    }
    
}

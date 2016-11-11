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
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "mensaje_rec")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MensajeRec.findAll", query = "SELECT m FROM MensajeRec m"),
    @NamedQuery(name = "MensajeRec.findById", query = "SELECT m FROM MensajeRec m WHERE m.id = :id"),
    @NamedQuery(name = "MensajeRec.findByCanal", query = "SELECT m FROM MensajeRec m WHERE m.canal = :canal"),
    @NamedQuery(name = "MensajeRec.findByTelefonoEmisor", query = "SELECT m FROM MensajeRec m WHERE m.telefonoEmisor = :telefonoEmisor"),
    @NamedQuery(name = "MensajeRec.findByTelefonoReceptor", query = "SELECT m FROM MensajeRec m WHERE m.telefonoReceptor = :telefonoReceptor"),
    @NamedQuery(name = "MensajeRec.findByMensaje", query = "SELECT m FROM MensajeRec m WHERE m.mensaje = :mensaje"),
    @NamedQuery(name = "MensajeRec.findByBandeja", query = "SELECT m FROM MensajeRec m WHERE m.bandeja = :bandeja"),
    @NamedQuery(name = "MensajeRec.findByFecha", query = "SELECT m FROM MensajeRec m WHERE m.fecha = :fecha")})
public class MensajeRec implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "canal")
    private int canal;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "telefono_emisor")
    private String telefonoEmisor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "telefono_receptor")
    private String telefonoReceptor;
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    public MensajeRec() {
    }

    public MensajeRec(Integer id) {
        this.id = id;
    }

    public MensajeRec(Integer id, int canal, String telefonoEmisor, String telefonoReceptor, String mensaje, String bandeja, Date fecha) {
        this.id = id;
        this.canal = canal;
        this.telefonoEmisor = telefonoEmisor;
        this.telefonoReceptor = telefonoReceptor;
        this.mensaje = mensaje;
        this.bandeja = bandeja;
        this.fecha = fecha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCanal() {
        return canal;
    }

    public void setCanal(int canal) {
        this.canal = canal;
    }

    public String getTelefonoEmisor() {
        return telefonoEmisor;
    }

    public void setTelefonoEmisor(String telefonoEmisor) {
        this.telefonoEmisor = telefonoEmisor;
    }

    public String getTelefonoReceptor() {
        return telefonoReceptor;
    }

    public void setTelefonoReceptor(String telefonoReceptor) {
        this.telefonoReceptor = telefonoReceptor;
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MensajeRec)) {
            return false;
        }
        MensajeRec other = (MensajeRec) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String salida = "{"
                + "\"ID\":\"" + id + "\","
                + "\"canal\":\"" + canal + "\","
                + "\"Telefono_emisor\":\"" + telefonoEmisor + "\","
                + "\"Telefono_receptor\":\"" + telefonoReceptor + "\","
                + "\"Mensaje\":\"" + mensaje + "\","
                + "\"Bandeja\":\"" + bandeja + "\","
                + "\"Fecha\":\"" + fecha + "\""
                + "}";
        return salida;
    }
    
}

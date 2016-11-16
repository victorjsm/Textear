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
@Table(name = "mensaje_rec")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MensajeRec.findAll", query = "SELECT m FROM MensajeRec m"),
    @NamedQuery(name = "MensajeRec.findByAcronimo", query = "SELECT m FROM MensajeRec m WHERE m.mensajeRecPK.acronimo = :acronimo"),
    @NamedQuery(name = "MensajeRec.findByClave", query = "SELECT m FROM MensajeRec m WHERE m.mensajeRecPK.clave = :clave"),
    @NamedQuery(name = "MensajeRec.findByCanal", query = "SELECT m FROM MensajeRec m WHERE m.canal = :canal"),
    @NamedQuery(name = "MensajeRec.findByMensaje", query = "SELECT m FROM MensajeRec m WHERE m.mensaje = :mensaje"),
    @NamedQuery(name = "MensajeRec.findByTelefono", query = "SELECT m FROM MensajeRec m WHERE m.mensajeRecPK.telefono = :telefono")})
public class MensajeRec implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MensajeRecPK mensajeRecPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "canal")
    private String canal;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "mensaje")
    private String mensaje;

    public MensajeRec() {
    }

    public MensajeRec(MensajeRecPK mensajeRecPK) {
        this.mensajeRecPK = mensajeRecPK;
    }

    public MensajeRec(MensajeRecPK mensajeRecPK, String canal, String mensaje) {
        this.mensajeRecPK = mensajeRecPK;
        this.canal = canal;
        this.mensaje = mensaje;
    }

    public MensajeRec(String acronimo, String clave, String telefono) {
        this.mensajeRecPK = new MensajeRecPK(acronimo, clave, telefono);
    }

    public MensajeRecPK getMensajeRecPK() {
        return mensajeRecPK;
    }

    public void setMensajeRecPK(MensajeRecPK mensajeRecPK) {
        this.mensajeRecPK = mensajeRecPK;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mensajeRecPK != null ? mensajeRecPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MensajeRec)) {
            return false;
        }
        MensajeRec other = (MensajeRec) object;
        if ((this.mensajeRecPK == null && other.mensajeRecPK != null) || (this.mensajeRecPK != null && !this.mensajeRecPK.equals(other.mensajeRecPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String salida = "{"
                + "\"canal\":\"" + canal + "\","
                + "\"mensaje\":\"" + mensaje + "\","
                + mensajeRecPK.toString()
                + "}";
        return salida;
    }
    
}

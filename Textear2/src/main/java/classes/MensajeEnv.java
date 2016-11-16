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
@Table(name = "mensaje_env")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MensajeEnv.findAll", query = "SELECT m FROM MensajeEnv m"),
    @NamedQuery(name = "MensajeEnv.findByAcronimo", query = "SELECT m FROM MensajeEnv m WHERE m.mensajeEnvPK.acronimo = :acronimo"),
    @NamedQuery(name = "MensajeEnv.findByClave", query = "SELECT m FROM MensajeEnv m WHERE m.mensajeEnvPK.clave = :clave"),
    @NamedQuery(name = "MensajeEnv.findByCanal", query = "SELECT m FROM MensajeEnv m WHERE m.canal = :canal"),
    @NamedQuery(name = "MensajeEnv.findByMensaje", query = "SELECT m FROM MensajeEnv m WHERE m.mensaje = :mensaje"),
    @NamedQuery(name = "MensajeEnv.findByTelefono", query = "SELECT m FROM MensajeEnv m WHERE m.mensajeEnvPK.telefono = :telefono")})
public class MensajeEnv implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MensajeEnvPK mensajeEnvPK;
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

    public MensajeEnv() {
    }

    public MensajeEnv(MensajeEnvPK mensajeEnvPK) {
        this.mensajeEnvPK = mensajeEnvPK;
    }

    public MensajeEnv(MensajeEnvPK mensajeEnvPK, String canal, String mensaje) {
        this.mensajeEnvPK = mensajeEnvPK;
        this.canal = canal;
        this.mensaje = mensaje;
    }

    public MensajeEnv(String acronimo, String clave, String telefono) {
        this.mensajeEnvPK = new MensajeEnvPK(acronimo, clave, telefono);
    }

    public MensajeEnvPK getMensajeEnvPK() {
        return mensajeEnvPK;
    }

    public void setMensajeEnvPK(MensajeEnvPK mensajeEnvPK) {
        this.mensajeEnvPK = mensajeEnvPK;
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
        hash += (mensajeEnvPK != null ? mensajeEnvPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MensajeEnv)) {
            return false;
        }
        MensajeEnv other = (MensajeEnv) object;
        if ((this.mensajeEnvPK == null && other.mensajeEnvPK != null) || (this.mensajeEnvPK != null && !this.mensajeEnvPK.equals(other.mensajeEnvPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String salida = "{"
                + "\"canal\":\"" + canal + "\","
                + "\"mensaje\":\"" + mensaje + "\","
                + mensajeEnvPK.toString()
                + "}";
        return salida;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author aganius
 */
@Embeddable
public class MensajeRecPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "acronimo")
    private String acronimo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "clave")
    private String clave;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "telefono")
    private String telefono;

    public MensajeRecPK() {
    }

    public MensajeRecPK(String acronimo, String clave, String telefono) {
        this.acronimo = acronimo;
        this.clave = clave;
        this.telefono = telefono;
    }

    public String getAcronimo() {
        return acronimo;
    }

    public void setAcronimo(String acronimo) {
        this.acronimo = acronimo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (acronimo != null ? acronimo.hashCode() : 0);
        hash += (clave != null ? clave.hashCode() : 0);
        hash += (telefono != null ? telefono.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MensajeRecPK)) {
            return false;
        }
        MensajeRecPK other = (MensajeRecPK) object;
        if ((this.acronimo == null && other.acronimo != null) || (this.acronimo != null && !this.acronimo.equals(other.acronimo))) {
            return false;
        }
        if ((this.clave == null && other.clave != null) || (this.clave != null && !this.clave.equals(other.clave))) {
            return false;
        }
        if ((this.telefono == null && other.telefono != null) || (this.telefono != null && !this.telefono.equals(other.telefono))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String salida =
                "\"acronimo\":\"" + acronimo + "\","
                + "\"telefono\":\"" + telefono + "\","
                + "\"clave\":\"" + clave + "\"";
        return salida;
    }
    
}

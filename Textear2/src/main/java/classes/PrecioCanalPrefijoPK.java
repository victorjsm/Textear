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
public class PrecioCanalPrefijoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "codigo_prefijo")
    private String codigoPrefijo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "codigo_canal")
    private String codigoCanal;

    public PrecioCanalPrefijoPK() {
    }

    public PrecioCanalPrefijoPK(String codigoPrefijo, String codigoCanal) {
        this.codigoPrefijo = codigoPrefijo;
        this.codigoCanal = codigoCanal;
    }

    public String getCodigoPrefijo() {
        return codigoPrefijo;
    }

    public void setCodigoPrefijo(String codigoPrefijo) {
        this.codigoPrefijo = codigoPrefijo;
    }

    public String getCodigoCanal() {
        return codigoCanal;
    }

    public void setCodigoCanal(String codigoCanal) {
        this.codigoCanal = codigoCanal;
    }



    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrecioCanalPrefijoPK)) {
            return false;
        }
        PrecioCanalPrefijoPK other = (PrecioCanalPrefijoPK) object;
        if (this.codigoPrefijo != other.codigoPrefijo) {
            return false;
        }
        if (this.codigoCanal != other.codigoCanal) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String salida
                = "\"codigoPrefijo\":\"" + codigoPrefijo + "\","
                + "\"codigoCanal\":\"" + codigoCanal + "\"";
        return salida;
    }
    
}

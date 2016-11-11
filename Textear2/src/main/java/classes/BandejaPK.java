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
public class BandejaPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "rif_empresa")
    private String rifEmpresa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nombre")
    private String nombre;

    public BandejaPK() {
    }

    public BandejaPK(String rifEmpresa, String nombre) {
        this.rifEmpresa = rifEmpresa;
        this.nombre = nombre;
    }

    public String getRifEmpresa() {
        return rifEmpresa;
    }

    public void setRifEmpresa(String rifEmpresa) {
        this.rifEmpresa = rifEmpresa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rifEmpresa != null ? rifEmpresa.hashCode() : 0);
        hash += (nombre != null ? nombre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BandejaPK)) {
            return false;
        }
        BandejaPK other = (BandejaPK) object;
        if ((this.rifEmpresa == null && other.rifEmpresa != null) || (this.rifEmpresa != null && !this.rifEmpresa.equals(other.rifEmpresa))) {
            return false;
        }
        if ((this.nombre == null && other.nombre != null) || (this.nombre != null && !this.nombre.equals(other.nombre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String salida = "\"rifEmpresa\":\"" + rifEmpresa + "\","
                + "\"nombre\":\"" + nombre + "\"";
        return salida;
    }
    
}

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
public class OpcionesPreguntaPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "t_nombre")
    private String tNombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "t_telefono")
    private String tTelefono;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "t_rif")
    private String tRif;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pregunta_id")
    private int preguntaId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private int id;

    public OpcionesPreguntaPK() {
    }

    public OpcionesPreguntaPK(String tNombre, String tTelefono, String tRif, int preguntaId, int id) {
        this.tNombre = tNombre;
        this.tTelefono = tTelefono;
        this.tRif = tRif;
        this.preguntaId = preguntaId;
        this.id = id;
    }

    public String getTNombre() {
        return tNombre;
    }

    public void setTNombre(String tNombre) {
        this.tNombre = tNombre;
    }

    public String getTTelefono() {
        return tTelefono;
    }

    public void setTTelefono(String tTelefono) {
        this.tTelefono = tTelefono;
    }

    public String getTRif() {
        return tRif;
    }

    public void setTRif(String tRif) {
        this.tRif = tRif;
    }

    public int getPreguntaId() {
        return preguntaId;
    }

    public void setPreguntaId(int preguntaId) {
        this.preguntaId = preguntaId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tNombre != null ? tNombre.hashCode() : 0);
        hash += (tTelefono != null ? tTelefono.hashCode() : 0);
        hash += (tRif != null ? tRif.hashCode() : 0);
        hash += (int) preguntaId;
        hash += (int) id;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OpcionesPreguntaPK)) {
            return false;
        }
        OpcionesPreguntaPK other = (OpcionesPreguntaPK) object;
        if ((this.tNombre == null && other.tNombre != null) || (this.tNombre != null && !this.tNombre.equals(other.tNombre))) {
            return false;
        }
        if ((this.tTelefono == null && other.tTelefono != null) || (this.tTelefono != null && !this.tTelefono.equals(other.tTelefono))) {
            return false;
        }
        if ((this.tRif == null && other.tRif != null) || (this.tRif != null && !this.tRif.equals(other.tRif))) {
            return false;
        }
        if (this.preguntaId != other.preguntaId) {
            return false;
        }
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String salida
                = "\"telefono\":\"" + tTelefono + "\","
                + "\"nombre\":\"" + tNombre + "\","
                + "\"id\":\"" + id + "\","
                + "\"pregunta\":\"" + preguntaId + "\","
                + "\"rifEmpresa\":\"" + tRif + "\"";
        return salida;
    }
    
}

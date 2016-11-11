/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author aganius
 */
@Entity
@Table(name = "bandeja")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bandeja.findAll", query = "SELECT b FROM Bandeja b"),
    @NamedQuery(name = "Bandeja.findByRifEmpresa", query = "SELECT b FROM Bandeja b WHERE b.bandejaPK.rifEmpresa = :rifEmpresa"),
    @NamedQuery(name = "Bandeja.findByNombre", query = "SELECT b FROM Bandeja b WHERE b.bandejaPK.nombre = :nombre"),
    @NamedQuery(name = "Bandeja.findByTipo", query = "SELECT b FROM Bandeja b WHERE b.tipo = :tipo")})
public class Bandeja implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BandejaPK bandejaPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "tipo")
    private String tipo;
    @JoinColumn(name = "rif_empresa", referencedColumnName = "rif", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Empresa empresa;

    public Bandeja() {
    }

    public Bandeja(BandejaPK bandejaPK) {
        this.bandejaPK = bandejaPK;
    }

    public Bandeja(BandejaPK bandejaPK, String tipo) {
        this.bandejaPK = bandejaPK;
        this.tipo = tipo;
    }

    public Bandeja(String rifEmpresa, String nombre) {
        this.bandejaPK = new BandejaPK(rifEmpresa, nombre);
    }

    public BandejaPK getBandejaPK() {
        return bandejaPK;
    }

    public void setBandejaPK(BandejaPK bandejaPK) {
        this.bandejaPK = bandejaPK;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bandejaPK != null ? bandejaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bandeja)) {
            return false;
        }
        Bandeja other = (Bandeja) object;
        if ((this.bandejaPK == null && other.bandejaPK != null) || (this.bandejaPK != null && !this.bandejaPK.equals(other.bandejaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "{"+ bandejaPK.toString() + ", \"tipo\":\"" + tipo + "\"}";
    }
    
}

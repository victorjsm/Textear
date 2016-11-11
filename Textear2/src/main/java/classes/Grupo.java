/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author aganius
 */
@Entity
@Table(name = "grupo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Grupo.findAll", query = "SELECT g FROM Grupo g"),
    @NamedQuery(name = "Grupo.findByRifEmpresa", query = "SELECT g FROM Grupo g WHERE g.grupoPK.rifEmpresa = :rifEmpresa"),
    @NamedQuery(name = "Grupo.findByNombre", query = "SELECT g FROM Grupo g WHERE g.grupoPK.nombre = :nombre"),
    @NamedQuery(name = "Grupo.findByCi", query = "SELECT g FROM Grupo g WHERE g.grupoPK.telefono = :telefono")})
public class Grupo implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GrupoPK grupoPK;
    @JoinColumns({
        @JoinColumn(name = "telefono", referencedColumnName = "telefono", insertable = false, updatable = false),
        @JoinColumn(name = "rif_empresa", referencedColumnName = "rif_empresa", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Abonado abonado;

    public Grupo() {
    }

    public Grupo(GrupoPK grupoPK) {
        this.grupoPK = grupoPK;
    }

    public Grupo(String rifEmpresa, String nombre, String ci) {
        this.grupoPK = new GrupoPK(rifEmpresa, nombre, ci);
    }

    public GrupoPK getGrupoPK() {
        return grupoPK;
    }

    public void setGrupoPK(GrupoPK grupoPK) {
        this.grupoPK = grupoPK;
    }

    public Abonado getAbonado() {
        return abonado;
    }

    public void setAbonado(Abonado abonado) {
        this.abonado = abonado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (grupoPK != null ? grupoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Grupo)) {
            return false;
        }
        Grupo other = (Grupo) object;
        if ((this.grupoPK == null && other.grupoPK != null) || (this.grupoPK != null && !this.grupoPK.equals(other.grupoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String salida = "{"
                + grupoPK.toString() 
                + "}";
        return salida;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "prefijo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Prefijo.findAll", query = "SELECT p FROM Prefijo p"),
    @NamedQuery(name = "Prefijo.findByCodigo", query = "SELECT p FROM Prefijo p WHERE p.codigo = :codigo"),
    @NamedQuery(name = "Prefijo.findByCostomt", query = "SELECT p FROM Prefijo p WHERE p.costomt = :costomt")})
public class Prefijo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "codigo")
    private String codigo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "costomt")
    private BigDecimal costomt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "prefijo")
    private Collection<PrecioCanalPrefijo> precioCanalPrefijoCollection;

    
    public Prefijo() {
    }

    public Prefijo(String codigo) {
        this.codigo = codigo;
    }

    public Prefijo(String codigo, BigDecimal costomt) {
        this.codigo = codigo;
        this.costomt = costomt;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public BigDecimal getCostomt() {
        return costomt;
    }

    public void setCostomt(BigDecimal costomt) {
        this.costomt = costomt;
    }

    @XmlTransient
    public Collection<PrecioCanalPrefijo> getPrecioCanalPrefijoCollection() {
        return precioCanalPrefijoCollection;
    }

    public void setPrecioCanalPrefijoCollection(Collection<PrecioCanalPrefijo> precioCanalPrefijoCollection) {
        this.precioCanalPrefijoCollection = precioCanalPrefijoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Prefijo)) {
            return false;
        }
        Prefijo other = (Prefijo) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String salida = "{"
                + "\"codigo\": \"" + codigo + "\","
                + "\"costoMT\":" + costomt  + "}";
        return salida;
    }
    
}

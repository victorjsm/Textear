/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author aganius
 */
@Entity
@Table(name = "precio_canal_prefijo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PrecioCanalPrefijo.findAll", query = "SELECT p FROM PrecioCanalPrefijo p"),
    @NamedQuery(name = "PrecioCanalPrefijo.findByCodigoPrefijo", query = "SELECT p FROM PrecioCanalPrefijo p WHERE p.precioCanalPrefijoPK.codigoPrefijo = :codigoPrefijo"),
    @NamedQuery(name = "PrecioCanalPrefijo.findByCodigoCanal", query = "SELECT p FROM PrecioCanalPrefijo p WHERE p.precioCanalPrefijoPK.codigoCanal = :codigoCanal"),
    @NamedQuery(name = "PrecioCanalPrefijo.findByPrecio", query = "SELECT p FROM PrecioCanalPrefijo p WHERE p.precio = :precio")})
public class PrecioCanalPrefijo implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PrecioCanalPrefijoPK precioCanalPrefijoPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio")
    private BigDecimal precio;
    @JoinColumn(name = "codigo_canal", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Canal canal;
    @JoinColumn(name = "codigo_prefijo", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Prefijo prefijo;

    public PrecioCanalPrefijo() {
    }

    public PrecioCanalPrefijo(PrecioCanalPrefijoPK precioCanalPrefijoPK) {
        this.precioCanalPrefijoPK = precioCanalPrefijoPK;
    }

    public PrecioCanalPrefijo(PrecioCanalPrefijoPK precioCanalPrefijoPK, BigDecimal precio) {
        this.precioCanalPrefijoPK = precioCanalPrefijoPK;
        this.precio = precio;
    }

    public PrecioCanalPrefijo(String codigoPrefijo, String codigoCanal) {
        this.precioCanalPrefijoPK = new PrecioCanalPrefijoPK(codigoPrefijo, codigoCanal);
    }

    public PrecioCanalPrefijoPK getPrecioCanalPrefijoPK() {
        return precioCanalPrefijoPK;
    }

    public void setPrecioCanalPrefijoPK(PrecioCanalPrefijoPK precioCanalPrefijoPK) {
        this.precioCanalPrefijoPK = precioCanalPrefijoPK;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Canal getCanal() {
        return canal;
    }

    public void setCanal(Canal canal) {
        this.canal = canal;
    }

    public Prefijo getPrefijo() {
        return prefijo;
    }

    public void setPrefijo(Prefijo prefijo) {
        this.prefijo = prefijo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (precioCanalPrefijoPK != null ? precioCanalPrefijoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrecioCanalPrefijo)) {
            return false;
        }
        PrecioCanalPrefijo other = (PrecioCanalPrefijo) object;
        if ((this.precioCanalPrefijoPK == null && other.precioCanalPrefijoPK != null) || (this.precioCanalPrefijoPK != null && !this.precioCanalPrefijoPK.equals(other.precioCanalPrefijoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String salida = "{"
                + "\"precio\":" + precio + ","
                + precioCanalPrefijoPK.toString() + ","
                + "\"prefijo\":" + prefijo.toString()
                + "}";
        return salida;
    }
    
}

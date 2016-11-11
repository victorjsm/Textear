/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
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
@Table(name = "canal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Canal.findAll", query = "SELECT c FROM Canal c"),
    @NamedQuery(name = "Canal.findByCodigo", query = "SELECT c FROM Canal c WHERE c.codigo = :codigo"),
    @NamedQuery(name = "Canal.findByDescripcion", query = "SELECT c FROM Canal c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "Canal.findByLongitud", query = "SELECT c FROM Canal c WHERE c.longitud = :longitud"),
    @NamedQuery(name = "Canal.findByPrecioRecibir", query = "SELECT c FROM Canal c WHERE c.precioRecibir = :precioRecibir"),
    @NamedQuery(name = "Canal.findByPrecioEnviar", query = "SELECT c FROM Canal c WHERE c.precioEnviar = :precioEnviar")})
public class Canal implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codigoCanal")
    private Collection<TSistema> tSistemaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codigoCanal")
    private Collection<TInscripcion> tInscripcionCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codigoCanal")
    private List<TConsulta> tConsultaList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codigoCanal")
    private Collection<TEncuesta> tEncuestaCollection;



    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "longitud")
    private int longitud;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio_recibir")
    private BigDecimal precioRecibir;
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio_enviar")
    private BigDecimal precioEnviar;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codigoCanal")
    private Collection<TMensaje> tMensajeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "canal")
    private Collection<PrecioCanalPrefijo> precioCanalPrefijoCollection;

    public Canal() {
    }

    public Canal(String codigo) {
        this.codigo = codigo;
    }

    public Canal(String codigo, String descripcion, int longitud, BigDecimal precioRecibir, BigDecimal precioEnviar) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.longitud = longitud;
        this.precioRecibir = precioRecibir;
        this.precioEnviar = precioEnviar;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getLongitud() {
        return longitud;
    }

    public void setLongitud(int longitud) {
        this.longitud = longitud;
    }

    public BigDecimal getPrecioRecibir() {
        return precioRecibir;
    }

    public void setPrecioRecibir(BigDecimal precioRecibir) {
        this.precioRecibir = precioRecibir;
    }

    public BigDecimal getPrecioEnviar() {
        return precioEnviar;
    }

    public void setPrecioEnviar(BigDecimal precioEnviar) {
        this.precioEnviar = precioEnviar;
    }

    @XmlTransient
    public Collection<TMensaje> getTMensajeCollection() {
        return tMensajeCollection;
    }

    public void setTMensajeCollection(Collection<TMensaje> tMensajeCollection) {
        this.tMensajeCollection = tMensajeCollection;
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
        if (!(object instanceof Canal)) {
            return false;
        }
        Canal other = (Canal) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        List<PrecioCanalPrefijo> precios = (List<PrecioCanalPrefijo>) precioCanalPrefijoCollection;
        String mientras = "[";
        for (int i = 0; i < precios.size(); ++i){
            mientras = mientras + precios.get(i).toString();
            if (i < precios.size()-1){
                mientras = mientras + ",";
            }
        }
        mientras = mientras + "]";
        String salida = "{"
                + "\"codigo\": \"" + codigo + "\","
                + "\"descripcion\":\"" + descripcion + "\","
                + "\"longitud\":" + longitud + ","
                + "\"precioRecibir\":" + precioRecibir + ","
                + "\"precioEnviar\":" + precioEnviar + ","
                + "\"Prefijos\":"+ mientras
                + "}";
        return salida;
    }

    @XmlTransient
    public Collection<TEncuesta> getTEncuestaCollection() {
        return tEncuestaCollection;
    }

    public void setTEncuestaCollection(Collection<TEncuesta> tEncuestaCollection) {
        this.tEncuestaCollection = tEncuestaCollection;
    }

    public List<TConsulta> getTConsultaList() {
        return tConsultaList;
    }

    public void setTConsultaList(List<TConsulta> tConsultaList) {
        this.tConsultaList = tConsultaList;
    }

    @XmlTransient
    public Collection<TSistema> getTSistemaCollection() {
        return tSistemaCollection;
    }

    public void setTSistemaCollection(Collection<TSistema> tSistemaCollection) {
        this.tSistemaCollection = tSistemaCollection;
    }

    @XmlTransient
    public Collection<TInscripcion> getTInscripcionCollection() {
        return tInscripcionCollection;
    }

    public void setTInscripcionCollection(Collection<TInscripcion> tInscripcionCollection) {
        this.tInscripcionCollection = tInscripcionCollection;
    }


}
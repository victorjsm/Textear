/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
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
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author aganius
 */
@Entity
@Table(name = "abonado")
@NamedQueries({
    @NamedQuery(name = "Abonado.findAll", query = "SELECT a FROM Abonado a")})
public class Abonado implements Serializable {

    @Column(name = "negra")
    private Boolean negra;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "abonado")
    private Collection<TSistema> tSistemaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "abonado")
    private Collection<TInscripcion> tInscripcionCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "abonado")
    private List<TEncuesta> tEncuestaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "abonado")
    private List<TMensaje> tMensajeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "abonado")
    private List<Grupo> grupoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "abonado")
    private List<TConsulta> tConsultaList;

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AbonadoPK abonadoPK;
    @Basic(optional = false)
    @Size(min = 1, max = 255)
    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;
    @JoinColumn(name = "rif_empresa", referencedColumnName = "rif", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Empresa empresa;
    @Basic(optional = false)
    @Size(min = 1, max = 25)
    @Column(name = "ci", nullable = false, length = 25)
    private String ci;

    public Abonado() {
    }

    public Abonado(AbonadoPK abonadoPK) {
        this.abonadoPK = abonadoPK;
    }

    public Abonado(AbonadoPK abonadoPK, String nombre, String ci) {
        this.abonadoPK = abonadoPK;
        this.nombre = nombre;
        this.ci = ci;
    }

    public Abonado(String rifEmpresa, String telefono) {
        this.abonadoPK = new AbonadoPK(rifEmpresa, telefono);
    }

    public AbonadoPK getAbonadoPK() {
        return abonadoPK;
    }

    public void setAbonadoPK(AbonadoPK abonadoPK) {
        this.abonadoPK = abonadoPK;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
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
        hash += (abonadoPK != null ? abonadoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Abonado)) {
            return false;
        }
        Abonado other = (Abonado) object;
        if ((this.abonadoPK == null && other.abonadoPK != null) || (this.abonadoPK != null && !this.abonadoPK.equals(other.abonadoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String salida = "{"
                + abonadoPK.toString() + ","
                + "\"nombre\":\"" + nombre + "\","
                + "\"negra\":" + negra + ","
                + "\"ci\":\"" + ci + "\""
                + "}";
        return salida;
    }

    public List<TEncuesta> getTEncuestaList() {
        return tEncuestaList;
    }

    public void setTEncuestaList(List<TEncuesta> tEncuestaList) {
        this.tEncuestaList = tEncuestaList;
    }

    public List<TMensaje> getTMensajeList() {
        return tMensajeList;
    }

    public void setTMensajeList(List<TMensaje> tMensajeList) {
        this.tMensajeList = tMensajeList;
    }

    public List<Grupo> getGrupoList() {
        return grupoList;
    }

    public void setGrupoList(List<Grupo> grupoList) {
        this.grupoList = grupoList;
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

    public Boolean getNegra() {
        return negra;
    }

    public void setNegra(Boolean negra) {
        this.negra = negra;
    }
    
}

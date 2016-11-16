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
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "empresa")
@NamedQueries({
    @NamedQuery(name = "Empresa.findAll", query = "SELECT e FROM Empresa e")})
public class Empresa implements Serializable {

    @Size(max = 4)
    @Column(name = "acronimo")
    private String acronimo;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresa")
    private List<Bandeja> bandejaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rifEmpresa")
    private List<Usuario> usuarioList;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "rif", nullable = false, length = 255)
    private String rif;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nit", nullable = false, length = 255)
    private String nit;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "direccion", nullable = false, length = 2147483647)
    private String direccion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "telefono", nullable = false, length = 20)
    private String telefono;
    @Size(max = 40)
    @Column(name = "paginaweb", length = 40)
    private String paginaweb;
    @Basic(optional = false)
    @NotNull
    @Column(name = "saldo", nullable = false)
    private float saldo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresa")
    private List<Abonado> abonadoList;

    public Empresa() {
    }

    public Empresa(String rif) {
        this.rif = rif;
    }

    public Empresa(String rif, String nit, String nombre, String direccion, String telefono, float saldo) {
        this.rif = rif;
        this.nit = nit;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.saldo = saldo;
    }

    public String getRif() {
        return rif;
    }

    public void setRif(String rif) {
        this.rif = rif;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPaginaweb() {
        return paginaweb;
    }

    public void setPaginaweb(String paginaweb) {
        this.paginaweb = paginaweb;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    public List<Abonado> getAbonadoList() {
        return abonadoList;
    }

    public void setAbonadoList(List<Abonado> abonadoList) {
        this.abonadoList = abonadoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rif != null ? rif.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empresa)) {
            return false;
        }
        Empresa other = (Empresa) object;
        if ((this.rif == null && other.rif != null) || (this.rif != null && !this.rif.equals(other.rif))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String salida = "{"
                + "\"nit\":\"" + nit + "\","
                + "\"nombre\":\"" + nombre + "\","
                + "\"direccion\":\"" + direccion + "\","
                + "\"telefono\":\"" + telefono + "\","
                + "\"rif\":\"" + rif + "\","
                + "\"acronimo\":\"" + acronimo + "\","
                + "\"saldo\":" + saldo  + "}";
        return salida;
    }

    public List<Bandeja> getBandejaList() {
        return bandejaList;
    }

    public void setBandejaList(List<Bandeja> bandejaList) {
        this.bandejaList = bandejaList;
    }

    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    public String getAcronimo() {
        return acronimo;
    }

    public void setAcronimo(String acronimo) {
        this.acronimo = acronimo;
    }

    
}

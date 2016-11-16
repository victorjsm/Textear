/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package textearback;

/**
 *
 * @author aganius
 */
public class Mensaje  implements java.io.Serializable{
    
    private String Acronimo;
    private String Clave;
    private String Telefono;
    private String Mensaje;
    private String Canal;
    
    public Mensaje() {
        this.Acronimo = null;
        this.Clave = null;
        this.Telefono = null;
        this.Mensaje = null;
        this.Canal = null;
        
    }
    
    public String getClave() {
        return Clave;
    }

    public void setClave(String clave) {
        this.Clave = clave;
    }
    
    public String getAcronimo() {
        return Acronimo;
    }

    public void setAcronimo(String acronimo) {
        this.Acronimo = acronimo;
    }
    
    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        this.Telefono = telefono;
    }
    
    public String getMensaje() {
        return Mensaje;
    }

    public void setMensaje(String mensaje) {
        this.Mensaje = mensaje;
    }
    
    public String getCanal() {
        return Canal;
    }

    public void setCanal(String canal) {
        this.Canal = canal;
    }
    
    @Override
    public String toString(){
        String result = "{"
                + "\"header\":\"" + this.Acronimo + "." + this.Clave + "\","
                + "\"canal\":\"" + this.Canal + "\","
                + "\"mensaje\":\"" + this.Mensaje + "\","
                + "\"telefono\":\"" + this.Telefono + "\"}";
        return result;
    }
    
}

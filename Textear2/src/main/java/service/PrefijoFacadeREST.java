/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import classes.Prefijo;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author aganius
 */
@Stateless
@Path("classes.prefijo")
public class PrefijoFacadeREST extends AbstractFacade<Prefijo> {

    @PersistenceContext(unitName = "com.mycompany_Textear2_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public PrefijoFacadeREST() {
        super(Prefijo.class);
    }
    
    // GET prefijos
    // Procedimento por el cual el REST envia todos los prefijos almacenados en el
    // sistema.
    
    @GET
    @Path("prefijosGET/")
    @Produces({MediaType.APPLICATION_JSON})
    public String find_canal() {
        
        // Con un QUERY se obtienen todos los prefijos almacenados en el sistema,
        // se coloca en formato arreglo de JSON y se envia al servicio front-end.
        List<Prefijo> temp = super.findWithQuery("SELECT p FROM Prefijo p");
        for (int k = 0; k < temp.size(); k++) {

            em.refresh(temp.get(k));
        }

        String json = List_to_JSONString(temp);
        return json;
    }
    
    // GET prefijo
    // Procedimiento por el cual el REST envia la informacion completa de un prefijo
    // en particular. El REST recibe el codigo del prefijo deseado y con un JSON
    // envia la informacion del mismo
    
    @GET
    @Path("prefijoGET/{cod}")
    @Produces({MediaType.APPLICATION_JSON})
    public String find_abonados(@PathParam("cod") String cod) {
        // Se toma el codigo y usando el entity manager se encuentra el objeto
        // se coloca en formato JSON y se envia devuelta al servio front-end.
        
        Prefijo temp = super.find(cod);
        em.refresh(temp);
        return temp.toString();
    }
    
    // CREATE Prefijo
    // Procedimiento por el cual el REST recibe la informacion de un prefijo nuevo.
    // y lo agrega a la base de datos.
    // El REST recibe la informacion atravez de un objeto JSON, la consume, crea
    // el nuevo objeto en base de datos y envia un jSON de respuesta al servicio
    // front con el resultado de la operacion.
    
    @POST
    @Path("/prefijoPOST")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createprefijo(String temp) {
        
        // Se crea un nuevo objeto prefijo del entity manager.
        
        Prefijo pre = new Prefijo();
        try {
            // obj. Objeto JSON que contendra la informacion enviada adesde el sistema
            // fornt.
            // mensaje. String que contendra la respuesta enviada al front.
            // falla. Boolean que indicara al front si ocurrio algun problema durante
            // el procedimiento.

            JSONObject obj = new JSONObject(temp);

            String mensaje;
            Boolean falla;
            
            // Se agrega la informacion del prefijo necesitada y se prueba si existe
            // ya en la base de datos. Si no existe duplicado se agrega en la base
            // de datos, si existe se genera un mensaje de error.
            
            pre.setCodigo(obj.getString("codigo"));
            pre.setCostomt(BigDecimal.valueOf(obj.getDouble("costoMT")));

            if (super.find(pre.getCodigo()) == null) {
                super.create(pre);
                mensaje = "se creo el prefijo exitosamente";
                falla = false;
            } else {
                mensaje = "Ya existe un prefijo con ese Codigo";
                falla = true;
            }
            
            // token. Objeto JSON de respuesta al servicio front.
            // cont. Objeto JSON contenido en token.
            // Se crea un objeto con la fecha actual para la respuesta y para el LOG.
            // Ademas de un formato con el que se desee guardar la fecha.

            JSONObject token = new JSONObject();
            JSONObject cont = new JSONObject();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            
            // Se crea un objeto respuesta al servicio front y finalmente se envia.

            cont.put("Logtime", dateFormat.format(date));
            cont.put("mensaje", mensaje);
            cont.put("falla", falla);
            token.put("token", cont);

            return token.toString();

        } catch (JSONException ex) {
            return "{\"token\":{\"mensaje\":\"No se pudo crear el prefijo\",\"falla\":true}}";
//            java.util.logging.Logger.getLogger(WebController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // EDIT Prefijo
    // Procedimiento por el cual el REST recibe informacion actualizada de un prefijo
    // actualemnte almacenado en la base de datos y lo actualiza.
    // El REST recibe la informacion del prefijo en un JSON. Actualiza la informacion
    // del prefijo actualmente almacenado y responde al servicio front.

    
    
    @POST
    @Path("/prefijoEDIT")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_JSON)
    public String edit_prefijo(String temp) {
        try {
            
            // json. Objeto JSON que contiene la informacion enviada desde el servicio front.
            // mensaje. String que contendra el mensaje de respuesta al servicio front.
            // falla. Boolean que indica al servicio front si ocurrio alguna falla en el
            // procedimiento.

            JSONObject json = new JSONObject(temp);

            String mensaje;
            Boolean falla;
            
            // pre. Objeto Prefijo que contendra el prefijo existente en la base de datos, si
            // lo consigue edita la informacion con la que se envio desde el front, y lo guarda.
            // Si no existe, se envia mensaje de error al servicio front.

            Prefijo pre = super.find(json.getString("codigo"));

            if (pre == null) {
                mensaje = "No existe un prefijo con ese Codigo";
                falla = true;

            } else {

                pre.setCostomt(BigDecimal.valueOf(json.getDouble("costoMT")));

                super.edit(pre);

                mensaje = "se actualizo el prefijo exitosamente";
                falla = false;

            }
            
            // token. Objeto JSON de respuesta al servicio front.
            // cont. Objeto JSON contenido en token.
            // Se toma la fecha actual mas el formato en el que se desea
            // guardar para el LOG y la respuesta al servicio front.

            JSONObject token = new JSONObject();
            JSONObject cont = new JSONObject();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            
            // Se crea el obejto de respuesta y se envia al servicio front.

            cont.put("Logtime", dateFormat.format(date));
            cont.put("mensaje", mensaje);
            cont.put("falla", falla);
            token.put("token", cont);

            return token.toString();

        } catch (JSONException ex) {
            return "{\"token\":{\"mensaje\":\"No se pudo actualizar el prefijo\",\"falla\":true}}";
//            java.util.logging.Logger.getLogger(WebController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // DELETE Prefijo
    // Procedimiento por el cual el REST recibe informacion de un prefijo que se desea eliminar
    // actualemnte almacenado en la base de datos.
    // El REST recibe la informacion del prefijo en un JSON. Busca el elemento en la base de datos
    // y lo elimina.
    
    @POST
    @Path("/prefijoDELETE")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_JSON)
    public String delete_prefijo(String temp) {
        
        // falla. Boolean que indica al front si ocurrio algun error durante el procedimiento
        // json. Objeto JSON que contendra la informacion enviada desde el front-end
        // token. Objeto JSON que contendra la respuesta al servicio front.
        // cont. Objeto JSON contenido en token.
        
        Boolean falla = false;
        JSONObject json;
        JSONObject token = new JSONObject();
        JSONObject cont = new JSONObject();
        try {
            
            // Se obtiene la informacion enviada desde el front y la fecha actual.
            // ademas de proveer un formato en el que se quiere guardar en el LOG
            // y enviar de respuesta al front.
            json = new JSONObject(temp);
            JSONArray arreglo = json.getJSONArray("arreglo");

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            
            // Por cada elemento en el arreglo recibido desde el front se revisa si existe
            // un prefijo en la base de datos. Si no existe se da falla pero se sige
            // revisando el arreglo intentando eliminar los elementos que queden.
            

            for (int i = 0; i < arreglo.length(); ++i) {
                json = arreglo.getJSONObject(i);

                if (super.find(json.getString("codigo")) == null) {
                    cont.put("mensaje", "Alguno de los prefijos que intento eliminar no existe. Actualize la pagina");
                    falla = true;

                } else {
                    super.remove(super.find(json.getString("codigo")));
                }
            }
            
            // Se crea el objeto de respuesta al servicio front y se envia la respuesta.

            cont.put("Logtime", dateFormat.format(date));
            cont.put("falla", falla);
            if (!falla) {
                cont.put("mensaje", "Se elimino exitosamente el prefijo");
            }

            token.put("token", cont);

            return token.toString();

        } catch (JSONException ex) {
            return "{\"token\":{\"mensaje\":\"No se pudo eliminar el prefijo\",\"falla\":true}}";
//            java.util.logging.Logger.getLogger(WebController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}

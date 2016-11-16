/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import classes.MensajeRec;
import classes.MensajeRecPK;
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
import javax.ws.rs.core.PathSegment;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author aganius
 */
@Stateless
@Path("classes.mensajerec")
public class MensajeRecFacadeREST extends AbstractFacade<MensajeRec> {

    @PersistenceContext(unitName = "com.mycompany_Textear2_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    private MensajeRecPK getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;acronimo=acronimoValue;clave=claveValue;telefono=telefonoValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        classes.MensajeRecPK key = new classes.MensajeRecPK();
        javax.ws.rs.core.MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
        java.util.List<String> acronimo = map.get("acronimo");
        if (acronimo != null && !acronimo.isEmpty()) {
            key.setAcronimo(acronimo.get(0));
        }
        java.util.List<String> clave = map.get("clave");
        if (clave != null && !clave.isEmpty()) {
            key.setClave(clave.get(0));
        }
        java.util.List<String> telefono = map.get("telefono");
        if (telefono != null && !telefono.isEmpty()) {
            key.setTelefono(telefono.get(0));
        }
        return key;
    }

    public MensajeRecFacadeREST() {
        super(MensajeRec.class);
    }

    // OBTENER MENSAJES
    // Procedimiento por el cual el REST recibe el Acronimo de una empresa en particular,
    // y retorna todos los mensaes recibidos de empresa.
    // El REST recibe el rif via una llamada GET
    @GET
    @Path("mensajesGET/{acro}")
    @Produces({MediaType.APPLICATION_JSON})
    public String find_mensajes(@PathParam("acro") String acro) {

        // Se utiliza un query asoaciado al entitymanager para obtener todos
        // los abonados asociados a ese RIF en particular
        List<MensajeRec> temp = super.findWithQuery(
                "SELECT m FROM MensajeRec m WHERE m.mensajeRecPK.acronimo = \"" + acro + "\" ");

        for (int k = 0; k < temp.size(); k++) {

            em.refresh(temp.get(k));
        }

        // Se vuelve un arreglo de STRINGS con formato JSON y se envia nuevamente al front-end.
        String json = List_to_JSONString(temp);
        return json;
    }
    
    
    // ELIMINAR MENSAJE
    // Procedimiento por el cual el REST recibe informacion de uno o mas mensajes 
    // y los elimina en la base de datos.
    // El REST recibe una llamada POST con un JSON que sera comsumido para obtener la informacion deseada.
    // Si alguno de lso mensajes no existe en la base de datos la llamada
    // termina y se envia un error al front-end
    @POST
    @Path("/mensajesDELETE")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_JSON)
    public String delete_mensajes(String temp) {

        // falla. Booleano usado para determinar si el procedimineto fue
        // exitoso o no.
        // json. Contiene la informacion enviada desde el front-end para crear 
        // el nuevo abonado.
        // token, sera la respuesta de la llamada. Con esta variable
        // nos comunicamos con el Front-End para informar si se realizo exitosamente
        // el procedimiento.
        // cont. Contiene el contenido que llevara token. Esto incluye el dia en el
        // que se realizo el procedimiento, un booleano si se realizo exitosamente
        // o no y un mensaje de exito o falla dependiendo del resultado.
        Boolean falla = false;
        JSONObject json;
        JSONObject token = new JSONObject();
        JSONObject cont = new JSONObject();

        try {
            // Se obtiene el JSON con el arreglo conteniendo todos los abonados a eliminar
            json = new JSONObject(temp);
            JSONArray arreglo = json.getJSONArray("arreglo");

            // Fecha del dia actual, para el LOG y para el mensaje de respuesta al front-end
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();

            // Ciclo atravez de cada elemento que se desea eliminar, se obtiene el key de cada
            // abonado, se busca en la base de datos y se elimina.
            // Si no se encuentra algun abonado, la llamada falla y se envia mensaje de error,
            // al front-end
            for (int i = 0; i < arreglo.length(); ++i) {
                json = arreglo.getJSONObject(i);
                MensajeRecPK menpk = new MensajeRecPK();
                menpk.setAcronimo(json.getString("acronimo"));
                menpk.setClave(json.getString("clave"));
                menpk.setTelefono(json.getString("telefono"));

                if (super.find(menpk) == null) {
                    cont.put("mensaje", "Alguno de los mensajes que intento eliminar no existe. Actualize la pagina");
                    falla = true;

                } else {
                    super.remove(super.find(menpk));
                }
            }

            // Se crea el mensaje de respuesta y posteriormente se envia
            cont.put("Logtime", dateFormat.format(date));
            cont.put("falla", falla);
            if (!falla) {
                cont.put("mensaje", "Se elimino exitosamente el mensaje");
            }

            token.put("token", cont);

            return token.toString();

        } catch (JSONException ex) {
            return "{\"token\":{\"mensaje\":\"No se pudo eliminar el mensaje\",\"falla\":true}}";
//            java.util.logging.Logger.getLogger(WebController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}

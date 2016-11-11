/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import classes.Canal;
import classes.PrecioCanalPrefijo;
import classes.PrecioCanalPrefijoPK;
import classes.Prefijo;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
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
@Path("classes.canal")
public class CanalFacadeREST extends AbstractFacade<Canal> {

    @PersistenceContext(unitName = "com.mycompany_Textear2_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public CanalFacadeREST() {
        super(Canal.class);
    }
    
    
    // Obtener CANAL
    // Procedimiento por el cual el REST envia al front todos los canales almacenados
    // en el sistema.
    @GET
    @Path("canalGET/")
    @Produces({MediaType.APPLICATION_JSON})
    public String find_canal() {
        // Se consiguen todos los canales, se vuelven un string con formato JSON,
        // y se envia al front-end
        List<Canal> temp = super.findWithQuery("SELECT c FROM Canal c");
        String json = List_to_JSONString(temp);
        return json;
    }
    
    // CREAR CANAL
    // Procedimiento por el cual el REST recibe informacion de un canal 
    // y lo agrega en la base de datos.
    // El REST recibe una llamada POST con un JSON que sera comsumido para obtener la informacion deseada.
    // Si alguno de lso abonados no existe en la base de datos la llamada
    // termina y se envia un error al front-end

    @POST
    @Path("/canalPOST")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createcanal(String temp) {
        
        // can. Objeto Canal que se guardara en el sistema.
        // precios. Lista de prefijos y sus precios dentro del canal.
        Canal can = new Canal();
        List<PrecioCanalPrefijo> precios = new ArrayList<PrecioCanalPrefijo>();

        try {
            
            // Objecto JSON que se utilizara para recibir la informacion del front-end

            JSONObject obj = new JSONObject(temp);
            
            // Se obtiene el canal y el arreglo de prefijos con sus precios dentro del
            // canal

            JSONObject json = obj.getJSONObject("canal");
            JSONArray arreglo = obj.getJSONArray("arreglo");

            String mensaje;
            Boolean falla;
            
            // Se agregan los valores determinados para el objeto canal, y luego
            // se le agrega una lista de los prefijos y sus precios asociados al canal.

            can.setCodigo(json.getString("codigo"));
            can.setDescripcion(json.getString("descripcion"));
            can.setLongitud(json.getInt("longitud"));
            can.setPrecioEnviar(BigDecimal.valueOf(json.getDouble("precioEnviar")));
            can.setPrecioRecibir(BigDecimal.valueOf(json.getDouble("precioRecibir")));

            for (int i = 0; i < arreglo.length(); ++i) {
                
                
                // por cada objeto del arreglo, se crea un prefijo y su objeto llave,
                // estos objetos si existen en la base de datos solo se asocian, si no
                // existen con anterioridad se crean.

                json = arreglo.getJSONObject(i);

                JSONObject jsonPrefijo = json.getJSONObject("prefijo");
                BigDecimal precio = BigDecimal.valueOf(json.getDouble("precio"));

                Prefijo prefijo = new Prefijo();
                prefijo.setCodigo(jsonPrefijo.getString("codigo"));
                prefijo.setCostomt(BigDecimal.valueOf(jsonPrefijo.getDouble("costoMT")));

                PrecioCanalPrefijo pcp = new PrecioCanalPrefijo();
                PrecioCanalPrefijoPK pcpPK = new PrecioCanalPrefijoPK();

                pcpPK.setCodigoCanal(can.getCodigo());
                pcpPK.setCodigoPrefijo(jsonPrefijo.getString("codigo"));

                pcp.setPrecio(precio);
                pcp.setPrecioCanalPrefijoPK(pcpPK);
                pcp.setCanal(can);
                pcp.setPrefijo(prefijo);
                precios.add(pcp);

    }

            can.setPrecioCanalPrefijoCollection(precios);
            
            // se verifica que el canal no exista previamente y se crea. Si existia previamente
            // se envia mensaje de error al front-end
            if (super.find(can.getCodigo()) == null) {
                super.create(can);
                mensaje = "se creo el canal exitosamente";
                falla = false;
            } else {
                mensaje = "Ya existe un canal con ese Codigo";
                falla = true;
    }
            
            // Finalmente se crea el objeto de respuesta al front-end

            JSONObject token = new JSONObject();
            JSONObject cont = new JSONObject();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();

            cont.put("Logtime", dateFormat.format(date));
            cont.put("mensaje", mensaje);
            cont.put("falla", falla);
            token.put("token", cont);

            return token.toString();

        } catch (JSONException ex) {
            return "{\"token\":{\"mensaje\":\"No se pudo crear abonado\",\"falla\":true}}";
//            java.util.logging.Logger.getLogger(WebController.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    
    
    // DELETE CANAL
    // Procedimiento por el cual el REST recibe informacion de uno o varios canales 
    // y los elimina de la base de datos.
    // El REST recibe una llamada POST con un JSON que sera comsumido para obtener la informacion deseada.

    
    // Cabe destacar que si alguno de los canales no existe se envia mensaje de error pero se siguen chequeando
    // los demas elementos enviados a eliminar hasta culminar la lista
    @POST
    @Path("/canalesDELETE")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_JSON)
    public String delete_canales(String temp) {
        
        // falla. Booleano que se usa para verificar si ocurrio una falla o no al efectuar una operacion
        // en el sistema.
        // json. JSON para obtener la informacion que envia el front-end para efectuar la operacion.
        // token. Objeto que se usara para enviar la informacion de respuesta al front-end
        // cont. Ojecto que contiene la real informacion de la respuesta, va dentro del objeto token.
        
        Boolean falla = false;
        JSONObject json;
        JSONObject token = new JSONObject();
        JSONObject cont = new JSONObject();
        
        try {
            
            //  Se obtiene la informacion enviada desde el front-end y se saca el arreglo
            // con todos los canales a eliminar
            json = new JSONObject(temp);
            JSONArray arreglo = json.getJSONArray("arreglo");

            // Fecha para el LOG y para la respuesta al front-end
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();

            // Arreglo que toma cada elemento del arreglo y elimina los canales, si alguno no existiA
            // en la base de datos con anterioridad se envia mensaje de error, sin embargo se continuara
            // revisando los demas elementos en la lista y se intentara eliminarlos.
            for (int i = 0; i < arreglo.length(); ++i) {
                json = arreglo.getJSONObject(i);

                if (super.find(json.getString("codigo")) == null) {
                    cont.put("mensaje", "Alguno de los abonados que intento eliminar no existe. Actualize la pagina");
                    falla = true;

                } else {
                    super.remove(super.find(json.getString("codigo")));
    }
            }
            
            // Se coloca la informacion en el objeto respuesta y finalmente se envia

            cont.put("Logtime", dateFormat.format(date));
            cont.put("falla", falla);
            if (!falla) {
                cont.put("mensaje", "Se elimino exitosamente el abonado");
    }

            token.put("token", cont);

            return token.toString();

        } catch (JSONException ex) {
            return "{\"token\":{\"mensaje\":\"No se pudo eliminar abonado\",\"falla\":true}}";
//            java.util.logging.Logger.getLogger(WebController.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    
    // EDITAR CANAL
    // Procedimiento por el cual el REST recibe informacion de un canal 
    // y lo edita en la base de datos.
    // El REST recibe una llamada POST con un JSON que sera comsumido para obtener la informacion deseada.

    @POST
    @Path("/canalEDIT")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_JSON)
    public String edit_canales(String temp) {

        // precios. Lista de precios dentro del canal, incluye los prefijos y el precio para cada uno
        List<PrecioCanalPrefijo> precios = new ArrayList<PrecioCanalPrefijo>();

        try {
            
            // Se obtiene la informacion enviada desde el front-end y un arreglo con los prefijos y precios.
            
            JSONObject json = new JSONObject(temp);

            JSONArray arreglo = json.getJSONArray("prefijos");
            
            String mensaje;
            Boolean falla;
            
            // Se obtiene el canal, si no existe se define una falla.

            Canal can = super.find(json.getString("codigo"));

            if (can == null) {
                mensaje = "No existe un canal con ese Codigo";
                falla = true;

            } else {
                
                // Al canal se le agrega la informacion nueva y los prefijos con su precio nuevo

                can.setDescripcion(json.getString("descripcion"));
                can.setLongitud(json.getInt("longitud"));
                can.setPrecioEnviar(BigDecimal.valueOf(json.getDouble("precioEnviar")));
                can.setPrecioRecibir(BigDecimal.valueOf(json.getDouble("precioRecibir")));

                for (int i = 0; i < arreglo.length(); ++i) {

                    json = arreglo.getJSONObject(i);

                    JSONObject jsonPrefijo = json.getJSONObject("prefijo");
                    BigDecimal precio = BigDecimal.valueOf(json.getDouble("precio"));

                    Prefijo prefijo = new Prefijo();
                    prefijo.setCodigo(jsonPrefijo.getString("codigo"));
                    prefijo.setCostomt(BigDecimal.valueOf(jsonPrefijo.getDouble("costoMT")));

                    PrecioCanalPrefijo pcp = new PrecioCanalPrefijo();
                    PrecioCanalPrefijoPK pcpPK = new PrecioCanalPrefijoPK();

                    pcpPK.setCodigoCanal(can.getCodigo());
                    pcpPK.setCodigoPrefijo(jsonPrefijo.getString("codigo"));

                    pcp.setPrecio(precio);
                    pcp.setPrecioCanalPrefijoPK(pcpPK);
                    pcp.setCanal(can);
                    pcp.setPrefijo(prefijo);
                    precios.add(pcp);

                }
                
                
                can.setPrecioCanalPrefijoCollection(precios);

                // y se edita el canal finalmente
                super.edit(can);
                
                mensaje = "se actualizo el canal exitosamente";
                falla = false;
                
            }

            // Se crea el objeto respuesta y se coloca la informacion de respuesta.
            // Finalmente se envia el token.
            JSONObject token = new JSONObject();
            JSONObject cont = new JSONObject();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();

            cont.put("Logtime", dateFormat.format(date));
            cont.put("mensaje", mensaje);
            cont.put("falla", falla);
            token.put("token", cont);

            return token.toString();

        } catch (JSONException ex) {
            return "{\"token\":{\"mensaje\":\"No se pudo actualizar el canal\",\"falla\":true}}";
//            java.util.logging.Logger.getLogger(WebController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}

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
@Path("classes.preciocanalprefijo")
public class PrecioCanalPrefijoFacadeREST extends AbstractFacade<PrecioCanalPrefijo> {

    @PersistenceContext(unitName = "com.mycompany_Textear2_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    private PrecioCanalPrefijoPK getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;codigoPrefijo=codigoPrefijoValue;codigoCanal=codigoCanalValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        classes.PrecioCanalPrefijoPK key = new classes.PrecioCanalPrefijoPK();
        javax.ws.rs.core.MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
        java.util.List<String> codigoPrefijo = map.get("codigoPrefijo");
        if (codigoPrefijo != null && !codigoPrefijo.isEmpty()) {
            key.setCodigoPrefijo(new java.lang.String(codigoPrefijo.get(0)));
        }
        java.util.List<String> codigoCanal = map.get("codigoCanal");
        if (codigoCanal != null && !codigoCanal.isEmpty()) {
            key.setCodigoCanal(new java.lang.String(codigoCanal.get(0)));
        }
        return key;
    }

    public PrecioCanalPrefijoFacadeREST() {
        super(PrecioCanalPrefijo.class);
    }
    
    // Create Precio-Canal-Prefijo
    // Procedimiento por el cual el REST recibe informacion de precios para cada prefijo
    // de un canal determinado.
    // Posteriormente se envia un JSON de respuesta al front-end.


    @POST
    @Path("/preciocanalprefijoPOST")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String create_precio_canal_prefijo(String temp) {
        
        
        //json. Objeto JSON que contendra la informacion eviada desde el front-end
        // token. Objeto JSON que contendra la respuesta que se enviara al front-end
        // cont. Objeto JSON contenido en token.
        // mensaje. String de respuesta si fue una oprecion exitosa o no.
        // falla. Booleano que indica al front-end si ocurrio una falla en el
        // procedimiento.
        
        JSONObject json;
        JSONObject token = new JSONObject();
        JSONObject cont = new JSONObject();
        String mensaje = "Se crearon los precios de los prefijos exitosamente";
        Boolean falla = false;
        
        try {
            
            // Se obtiene la informacion enviada desde el front. Un arreglo con cada uno
            // de los precios para los prefijos y la informacion del canal.
            json = new JSONObject(temp);
            JSONArray arreglo = json.getJSONArray("arreglo");
            JSONObject jsonCanal = json.getJSONObject("canal");
            
            // Se crea el objeto canal asociado con la informacion enviada desde el front.
            // ademas se toma la fecha actual y el formato deseado para la respuesta al
            // front y el LOG.

            Canal canal = new Canal();

            canal.setCodigo(jsonCanal.getString("codigo"));
            canal.setDescripcion(jsonCanal.getString("descripcion"));
            canal.setLongitud(jsonCanal.getInt("longitud"));
            
            canal.setPrecioEnviar(BigDecimal.valueOf(jsonCanal.getDouble("precioEnviar")));
            canal.setPrecioRecibir(BigDecimal.valueOf(jsonCanal.getDouble("precioRecibir")));
            
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            
            
            // Por cada elemento del arreglo, se obtiene la informacion del prefijo
            // y el precio asociado a cada uno, se crea el objeto prefijo y se le
            // asocia el precio ademas del codigo del canal.
            
            // Finalmente si se consigue una entrada igual en la base de datos se
            // reporta error, si no se crea el nuevo elemento en la base de datos.
            for (int i = 0; i < arreglo.length(); ++i) {

                json = arreglo.getJSONObject(i);

                JSONObject jsonPrefijo = json.getJSONObject("prefijo");
                BigDecimal precio = BigDecimal.valueOf(json.getDouble("precio"));

                Prefijo prefijo = new Prefijo();
                prefijo.setCodigo(jsonPrefijo.getString("codigo"));
                prefijo.setCostomt(BigDecimal.valueOf(jsonPrefijo.getDouble("costoMT")));

                PrecioCanalPrefijo pcp = new PrecioCanalPrefijo();
                PrecioCanalPrefijoPK pcpPK = new PrecioCanalPrefijoPK();

                pcpPK.setCodigoCanal(jsonCanal.getString("codigo"));
                pcpPK.setCodigoPrefijo(jsonPrefijo.getString("codigo"));
                
                
                if (super.find(pcpPK) == null) {
                    pcp.setPrecio(precio);
                    pcp.setPrecioCanalPrefijoPK(pcpPK);
                    pcp.setCanal(canal);
                    pcp.setPrefijo(prefijo);
                    System.out.println(pcp.toString());
                    super.create(pcp);
                    
                } else {
                    falla = true;
                    mensaje = "Hubo un error creando los precios de los prefijos";
                    i = arreglo.length();
                }

            }
            
            // Se crea el objeto con la respuesta al front y se envia.
            
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

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}

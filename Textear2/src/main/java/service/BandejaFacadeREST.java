/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import classes.Bandeja;
import classes.BandejaPK;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author aganius
 */
@Stateless
@Path("classes.bandeja")
public class BandejaFacadeREST extends AbstractFacade<Bandeja> {

    @PersistenceContext(unitName = "com.mycompany_Textear2_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    private BandejaPK getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;rifEmpresa=rifEmpresaValue;nombre=nombreValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        classes.BandejaPK key = new classes.BandejaPK();
        javax.ws.rs.core.MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
        java.util.List<String> rifEmpresa = map.get("rifEmpresa");
        if (rifEmpresa != null && !rifEmpresa.isEmpty()) {
            key.setRifEmpresa(rifEmpresa.get(0));
        }
        java.util.List<String> nombre = map.get("nombre");
        if (nombre != null && !nombre.isEmpty()) {
            key.setNombre(nombre.get(0));
        }
        return key;
    }

    public BandejaFacadeREST() {
        super(Bandeja.class);
    }
    
    // Obtener Bandeja
    // Procedimiento por el cual el REST envia al front todos los canales almacenados
    // en el sistema.
    @GET
    @Path("bandejas/{rif}/{tipo}")
    @Produces({MediaType.APPLICATION_JSON})
    public String find_bandeja(@PathParam("rif") String rif, @PathParam("tipo") String tipo) {
        // Se consiguen todos los canales, se vuelven un string con formato JSON,
        // y se envia al front-end
        List<Bandeja> temp = super.findWithQuery(
                "SELECT b FROM Bandeja b WHERE b.bandejaPK.rifEmpresa = \""+ rif + "\" AND  b.tipo = \"" + tipo + "\"");
        for (int k = 0; k < temp.size(); k++) {
            em.refresh(temp.get(k));
        }
        String json = List_to_JSONString(temp);
        return json;
    }
    
    
    

    // CREAR BANDEJA
    // Procedimiento por el cual el REST recibe informacion de una bandeja de mensajes
    // que quiera crear en su sistema.
    // El REST recibe una llamada POST con un JSON que sera comsumido para obtener la informacion deseada.
    @POST
    @Path("/createbandeja")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createbandeja(String temp) {

        // band. Nueva bandeja a ser creada en el sistema.
        // bandpk. Objeto llave del objeto bandeja.
        // json. Objeto JSON que se usa para obtener la informacion de la
        // llamada POST.
        // falla. Booleano que indica si hubo una falla o no en la creacion de una nueva bandeja.
        // mensaje. Mensaje de respuesta al front-end
        Bandeja band = new Bandeja();
        BandejaPK bandpk = new BandejaPK();
        JSONObject json;
        Boolean falla = false;
        String mensaje = "Se creo exitosamente la bandeja";

        try {
            //Se obtiene la informacion enviada desde el front-end, y se crea el objeto llave
            // de la bandeja deseada.
            json = new JSONObject(temp);
            bandpk.setRifEmpresa(json.getString("rif"));
            bandpk.setNombre(json.getString("nombre"));

            // Si no existe una bandeja con la misma llave en la base de datos, se crea la nueva
            // bandeja. Si ya existe una igual, se produce un error y se envia al front-end como
            // respuesta.
            if (super.find(bandpk) == null) {
                band.setBandejaPK(bandpk);
                band.setTipo(json.getString("tipo"));
                super.create(band);
            } else {
                falla = true;
                mensaje = "Ya existe una bandeja con ese nombre";
            }

            // Se crean los objetos de respuesta al servidor
            JSONObject token = new JSONObject();
            JSONObject cont = new JSONObject();
            
            // Se crea una fecha con el dia de hoy para el LOG y para el mensaje de respuesta, y se
            // envia posteriormente.
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            
            cont.put("Logtime", dateFormat.format(date));
            cont.put("mensaje", mensaje);
            cont.put("falla", falla);
            token.put("token", cont);

//                Logger.getLogger(UsuarioFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return token.toString();

        } catch (JSONException ex) {
            return "\"token\":{\"mensaje\":\"No se pudo crear la bandeja\"}";
//            java.util.logging.Logger.getLogger(WebController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}

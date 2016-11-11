/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import classes.Abonado;
import classes.AbonadoPK;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author aganius
 */
@Stateless
@Path("classes.abonado")
public class AbonadoFacadeREST extends AbstractFacade<Abonado> {

    @PersistenceContext(unitName = "com.mycompany_Textear2_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    private AbonadoPK getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;rifEmpresa=rifEmpresaValue;telefono=telefonoValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        classes.AbonadoPK key = new classes.AbonadoPK();
        javax.ws.rs.core.MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
        java.util.List<String> rifEmpresa = map.get("rifEmpresa");
        if (rifEmpresa != null && !rifEmpresa.isEmpty()) {
            key.setRifEmpresa(rifEmpresa.get(0));
        }
        java.util.List<String> telefono = map.get("telefono");
        if (telefono != null && !telefono.isEmpty()) {
            key.setTelefono(telefono.get(0));
        }
        return key;
    }

    public AbonadoFacadeREST() {
        super(Abonado.class);
    }

    // EDITAR ABONADO
    // Procedimiento por el cual el REST recibe informacion editada de un abonado
    // y actualiza ese abonado en la base de datos.
    // El REST recibe la informacion nueva del abonado via una llamada POST, que
    // ademas contiene un JSON que sera comsumido para obtener la informacion deseada.
    @POST
    @Path("/abonadosUPDATE")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String updateabonado(String temp) {

        // token, sera la respuesta de la llamada. Con esta variable
        // nos comunicamos con el Front-End para informar si se realizo exitosamente
        // el procedimiento.
        // cont. Contiene el contenido que llevara token. Esto incluye el dia en el
        // que se realizo el procedimiento, un booleano si se realizo exitosamente
        // o no y un mensaje de exito o falla dependiendo del resultado.
        //falla. Es el booleano que nos dice si ocurrio una falla en el procedimiento
        // o no.
        JSONObject token = new JSONObject();
        JSONObject cont = new JSONObject();
        Boolean falla = false;

        try {

            // Se crea una fecha actual y un formato en el que se desea guardar.
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();

            // json contiene la informacion que viene desde el front-end en formato
            // JSON.
            JSONObject json = new JSONObject(temp);

            // Se crea el objeto llave de la clase abonados, y se intenta encontrar el
            // abonado que se desea editar, si no se encuentra ocurre una falla y se 
            // reporta al front-end
            AbonadoPK abopk = new AbonadoPK();

            abopk.setRifEmpresa(json.getString("rifEmpresa"));
            abopk.setTelefono(json.getString("telefono"));

            if (super.find(abopk) == null) {
                cont.put("mensaje", "El abonado a editar ya no existe, actualize la pagina");
                falla = true;

                // Si se encuentra, se edita la informacion del mismo y se guarda nuevamente
                // en la base de datos.
            } else {

                Abonado prueba = super.find(abopk);
                prueba.setNombre(json.getString("nombre"));
                prueba.setCi(json.getString("ci"));
                prueba.setNegra(json.getBoolean("negra"));

                super.edit(prueba);
            }

            // Se coloca el contenido del mensaje de salida en cont
            cont.put("Logtime", dateFormat.format(date));
            cont.put("falla", falla);
            if (!falla) {
                cont.put("mensaje", "Se actualizo exitosamente el abonado");
            }

            token.put("token", cont);
//                Logger.getLogger(UsuarioFacadeREST.class.getName()).log(Level.SEVERE, null, ex);

            // Se envia la respuesta al front-end
            return token.toString();

            // Si ocurre una excepcion al momento de crear alguno de los JSON se reporta una
            // falla al front-end
        } catch (JSONException ex) {
            return "{\"token\":{\"mensaje\":\"No se pudo crear abonado\",\"falla\":true}}";
//            java.util.logging.Logger.getLogger(WebController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // CREAR ABONADO
    // Procedimiento por el cual el REST recibe informacion de uno o mas abonados 
    // y los crea en la base de datos.
    // El REST recibe la informacion nueva del abonado via una llamada POST, que
    // ademas contiene un JSON que sera comsumido para obtener la informacion deseada.
    // Cabe destacar que chequea por duplicados pero no presenta error ante ello, es decir
    // si existe un abonado con el mismo numero de telefono en a base de datos, solo lo salta,
    // no crea el duplicado en la base de datos y no envia error alguno, esto es debido a la
    // naturaleza de como se crean los abonados en el front.
    @POST
    @Path("/abonadosPOST")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createabonado(String temp) {

        // json. Contiene la informacion enviada desde el front-end para crear 
        // el nuevo abonado.
        // token, sera la respuesta de la llamada. Con esta variable
        // nos comunicamos con el Front-End para informar si se realizo exitosamente
        // el procedimiento.
        // cont. Contiene el contenido que llevara token. Esto incluye el dia en el
        // que se realizo el procedimiento, un booleano si se realizo exitosamente
        // o no y un mensaje de exito o falla dependiendo del resultado.
        JSONObject json;
        JSONObject token = new JSONObject();
        JSONObject cont = new JSONObject();
        System.out.println(temp);

        try {

            // Se obtiene la informacion del front-end procesando el JSON correspondiente.
            // y del arreglo de abonados que tiene en su interior.
            json = new JSONObject(temp);
            JSONArray arreglo = json.getJSONArray("arreglo");

            // Se crea la fecha de hoy para el Log y para el mensaje de respuesta.
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();

            // Y finalmente en un ciclo se va revisando cada uno de los objetos y se crea
            // el abonado correspondiente si no existia uno con el mismo telefono en la 
            // base de datos.
            for (int i = 0; i < arreglo.length(); ++i) {
                json = arreglo.getJSONObject(i);

                Abonado abo = new Abonado();
                AbonadoPK abopk = new AbonadoPK();

                abopk.setRifEmpresa(json.getString("rifEmpresa"));
                abopk.setTelefono(json.getString("telefono"));

                if (super.find(abopk) == null) {
                    abo.setNombre(json.getString("nombre"));
                    abo.setCi(json.getString("ci"));
                    abo.setNegra(false);
                    abo.setAbonadoPK(abopk);
                    super.create(abo);
                }
            }

            // Se crea el mensaje de repsuesta y se envia posteriormente
            cont.put("Logtime", dateFormat.format(date));
            cont.put("mensaje", "Se creo exitosamente el abonado");
            cont.put("falla", false);

            token.put("token", cont);

            return token.toString();

            // Si ocurre algun error de formato en la entrada de algun JSON se envia 
            // error al fronte-end
        } catch (JSONException ex) {
            return "{\"token\":{\"mensaje\":\"No se pudo crear abonado\",\"falla\":true}}";
//            java.util.logging.Logger.getLogger(WebController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // OBTENER ABONADOS
    // Procedimiento por el cual el REST recibe el RIF de una empresa en particular,
    // y retorna todos los abonados asociados a esa empresa.
    // El REST recibe el rif via una llamada GET
    @GET
    @Path("abonadosGET/{rif}")
    @Produces({MediaType.APPLICATION_JSON})
    public String find_abonados(@PathParam("rif") String rif) {

        // Se utiliza un query asoaciado al entitymanager para obtener todos
        // los abonados asociados a ese RIF en particular
        List<Abonado> temp = super.findWithQuery(
                "SELECT a FROM Abonado a WHERE a.abonadoPK.rifEmpresa = \"" + rif + "\" "
                + "AND a.negra = false");

        // Se vuelve un arreglo de STRINGS con formato JSON y se envia nuevamente al front-end.
        String json = List_to_JSONString(temp);
        return json;
    }

    // ELIMINAR ABONADO
    // Procedimiento por el cual el REST recibe informacion de uno o mas abonados 
    // y los elimina en la base de datos.
    // El REST recibe una llamada POST con un JSON que sera comsumido para obtener la informacion deseada.
    // Si alguno de lso abonados no existe en la base de datos la llamada
    // termina y se envia un error al front-end
    @POST
    @Path("/abonadosDELETE")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_JSON)
    public String delete_abonados(String temp) {

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
                AbonadoPK abopk = new AbonadoPK();
                abopk.setRifEmpresa(json.getString("rifEmpresa"));
                abopk.setTelefono(json.getString("telefono"));

                if (super.find(abopk) == null) {
                    cont.put("mensaje", "Alguno de los abonados que intento eliminar no existe. Actualize la pagina");
                    falla = true;

                } else {
                    super.remove(super.find(abopk));
                }
            }

            // Se crea el mensaje de respuesta y posteriormente se envia
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

    // OBTENER LISTA NEGRA
    // Procedimiento por el cual el REST recibe el RIF de una empresa en particular,
    // y retorna todos los abonados dentro de la lista negra asociados a esa empresa.
    // El REST recibe el rif via una llamada GET
    @GET
    @Path("listanegraGET/{rif}")
    @Produces({MediaType.APPLICATION_JSON})
    public String find_lista(@PathParam("rif") String rif) {

        // Se utiliza un query asoaciado al entitymanager para obtener todos
        // los abonados asociados a ese RIF en particular
        List<Abonado> temp = super.findWithQuery(
                "SELECT a FROM Abonado a WHERE a.abonadoPK.rifEmpresa = \"" + rif + "\" "
                + "AND a.negra = true");

        // Se vuelve un arreglo de STRINGS con formato JSON y se envia nuevamente al front-end.
        String json = List_to_JSONString(temp);
        return json;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}

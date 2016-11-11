/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import classes.Abonado;
import classes.AbonadoPK;
import classes.Canal;
import classes.TInscripcion;
import classes.TInscripcionPK;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
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
@Path("classes.tinscripcion")
public class TInscripcionFacadeREST extends AbstractFacade<TInscripcion> {

    @PersistenceContext(unitName = "com.mycompany_Textear2_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    private TInscripcionPK getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;rifEmpresa=rifEmpresaValue;telefono=telefonoValue;nombre=nombreValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        classes.TInscripcionPK key = new classes.TInscripcionPK();
        javax.ws.rs.core.MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
        java.util.List<String> rifEmpresa = map.get("rifEmpresa");
        if (rifEmpresa != null && !rifEmpresa.isEmpty()) {
            key.setRifEmpresa(rifEmpresa.get(0));
        }
        java.util.List<String> telefono = map.get("telefono");
        if (telefono != null && !telefono.isEmpty()) {
            key.setTelefono(telefono.get(0));
        }
        java.util.List<String> nombre = map.get("nombre");
        if (nombre != null && !nombre.isEmpty()) {
            key.setNombre(nombre.get(0));
        }
        return key;
    }

    public TInscripcionFacadeREST() {
        super(TInscripcion.class);
    }
    
    // GET Tarea Inscripcion
    // Procedimiento por el cual el REST recibe el rif de la empresa a travez de
    // una llamada GET y con el devuelve todas las tareas de inscripcion asociadas 
    // a ese rif en un arreglo JSON.

    @GET
    @Path("/TinscripcionGET/{rif}")
    @Produces({MediaType.APPLICATION_JSON})
    public String find_inscripcion(@PathParam("rif") String rif) {
        
        // tarea. Objeto JSON destinado a guardar la informacion de cada tarea para
        //      despues ser llevado al arreglo de respuesta.
        // canal. Objeto JSON destinado a guardar la informacion del canal de cada tarea.
        // abonado. Objeto JSON destinado a guardar la informacion de los abonados destinatarios
        //      de cada tarea.
        // tareas. Array JSON que contendra las tareas relacionadas con el rif enviado.
        // abonados. Array JSON que contendra los abonados destinatarios de cada tarea


        JSONObject tarea, canal, abonado;
        JSONArray tareas, abonados;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {

            // Se ejecuta un QUERY para encontrar todos los nombre de tarea existentes
            // asociados al rif.
            
            List lista = em.createNativeQuery("SELECT DISTINCT Nombre FROM t_inscripcion WHERE "
                    + "RIF_Empresa = \'" + rif + "\' ").getResultList();

            tareas = new JSONArray();

            // Por cada nombre en la lista se busca todas las entradas en la base de datos respectivas
            // a ese nombre.
            // Se toma la informacion asociada a la tarea en el primer ciclo y
            // se va tomando la informacion de cada abonado destinatario, cuando se tienen todos,
            // se agrega al objeto tarea la lista de abonados.

            
            for (int i = 0; i < lista.size(); i++) {

                tarea = new JSONObject();

                abonados = new JSONArray();

                List<TInscripcion> temp = super.findWithQuery(
                        "SELECT t FROM TInscripcion t WHERE t.tInscripcionPK.nombre = \"" + lista.get(i).toString() + "\"");

                for (int k = 0; k < temp.size(); k++) {

                    abonado = new JSONObject();

                    if (k == 0) {
                        canal = new JSONObject(temp.get(k).getCodigoCanal().toString());
                        tarea.put("canal", canal);
                        tarea.put("nombre", temp.get(k).getTInscripcionPK().getNombre());
                        tarea.put("bandeja", temp.get(k).getBandeja());
                        tarea.put("fechaCreacion", dateFormat.format(temp.get(k).getFechaCreacion()));
                        tarea.put("fechaExpiracion", dateFormat.format(temp.get(k).getFechaExpiracion()));
                        tarea.put("fechaEnvio", dateFormat.format(temp.get(k).getFechaEnvio()));
                        tarea.put("estado", temp.get(k).getEstado());
                        tarea.put("respuesta", temp.get(k).getRespuesta());
                        
                    }

                    Abonado j = temp.get(k).getAbonado();
                    abonado.put("telefono", temp.get(k).getTInscripcionPK().getTelefono());
                    abonado.put("nombre", j.getNombre());
                    abonado.put("ci", j.getCi());

                    abonados.put(abonado);
                }
                tarea.put("abonados", abonados);
                tareas.put(tarea);

            }
            return tareas.toString();
        } catch (JSONException ex) {
            return "{\"token\":{\"mensaje\":\"No se pudo crear la tarea\",\"falla\":true}}";
            //            java.util.logging.Logger.getLogger(WebController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
        // CREATE Tarea Inscripcion
    // Procedimiento por el cual el REST recibe la informacion de una tarea nueva
    // mediante un JSON y la crea en base de datos. Finalmente envia el resultado
    // de la operacion en un json respuesta.
    
    @POST
    @Path("/tinscripcionPOST")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createtinscripcion(String temp) throws ParseException, JSONException {
        
        // obj. Objeto JSON que contendra la informacion enviada desde el front.
        // json. Objeto JSON variable que contendra informacion de cada abonado
        //      destinatario de la consulta.
        // canal. Objeto JSON que contendra la informacion del canal en el que se
        //      enviara la tarea.
        // men. Objeto JSON que contendra la informacion del mensaje como tal de la
        //      tarea.
        // toke. Objeto JSON respuesta al servicio front.
        // cont. Objeto JSON contenido en el objeto token.
        // mensaje. String con el mensaje de respuesta al servicio front con el resultado
        //      del procedimiento.
        // falla. Boolean que informa al front si hubo alfun error durante el procedimiento.
        
        JSONObject json, obj, canal, men;
        JSONObject token = new JSONObject();
        JSONObject cont = new JSONObject();
        String mensaje = "Se creo exitosamente la tarea";
        Boolean falla = false;

        try {
            
            // Primero se obtiene la informacion enviada desde el front. Que
            // incluye toda la informacion necesaria de la tarea a crear. Al mismo
            // tiempo se coloca la fecha actual de creacion del objeto por control.
            
            // Luego se crea un objeto Canal por el cual se transmite la tarea y se
            // le asignan sus datos respectivos.
            
            obj = new JSONObject(temp);

            JSONArray arreglo = obj.getJSONArray("arreglo");

            men = obj.getJSONObject("mensaje");
            canal = men.getJSONObject("canal");

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date today = new Date();
            Date envio = dateFormat.parse(men.getString("fechaEnvio"));
            Date expiro = dateFormat.parse(men.getString("fechaExpiracion"));
            today = dateFormat.parse(dateFormat.format(today));

            Canal can = new Canal();

            can.setCodigo(canal.getString("codigo"));
            can.setDescripcion(canal.getString("descripcion"));
            can.setLongitud(canal.getInt("longitud"));
            can.setPrecioEnviar(BigDecimal.valueOf(canal.getDouble("precioEnviar")));
            can.setPrecioRecibir(BigDecimal.valueOf(canal.getDouble("precioRecibir")));

            // Por cada destinatario se crea una tarea asociada, se toma la informacion
            // proveniente del front y se va creando uno a uno con el estado de
            // pendiente.
            
            // Si existe ya una tarea con la misma llave, asociada al mismo abonado
            // se genera un mensaje de error y se interrumpe la rutina.
            
            for (int i = 0; i < arreglo.length(); ++i) {
                json = arreglo.getJSONObject(i);

                TInscripcion tins = new TInscripcion();
                TInscripcionPK tinspk = new TInscripcionPK();

                // ----- DATOS DEL ABONDADO
                Abonado abo = new Abonado();
                AbonadoPK abopk = new AbonadoPK();

                abopk.setRifEmpresa(json.getString("rif"));
                abopk.setTelefono(json.getString("telefono"));

                abo.setNombre(json.getString("nombre"));
                abo.setCi(json.getString("ci"));
                abo.setAbonadoPK(abopk);

                //------ DATOS DE LA TAREA
                tins.setBandeja(men.getString("bandeja"));
                tins.setCodigoCanal(can);

                tins.setEstado("PENDIENTE");

                tinspk.setTelefono(json.getString("telefono"));
                tinspk.setNombre(men.getString("nombre"));
                tinspk.setRifEmpresa(json.getString("rif"));

                tins.setFechaCreacion(today);
                tins.setFechaExpiracion(expiro);
                tins.setFechaEnvio(envio);
                tins.setTInscripcionPK(tinspk);

                tins.setRespuesta(men.getString("respuesta"));
                tins.setAbonado(abo);


                if (super.find(tinspk) == null) {
                    super.create(tins);
                } else {
                    mensaje = "Ya existe una tarea con ese nombre";
                    falla = true;
                    i = arreglo.length();
                }
            }

            // Finalmente se crea el mensaje de respuesta al servicio front y
            // se envia el resultado del procedimiento.
            cont.put("mensaje", mensaje);
            cont.put("falla", falla);
            cont.put("Logtime", today);
            token.put("token", cont);

            return token.toString();

        } catch (JSONException | ParseException ex) {
            return "{\"token\":{\"mensaje\":\"No se pudo crear la tarea\",\"falla\":true}}";
//            java.util.logging.Logger.getLogger(WebController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // EDIT Tarea Inscripcion
    // Procedimiento por el cual el REST recibe la informacion actualizada de una tarea
    // existente mediante un JSON y la actualiza en base de datos. Finalmente envia el resultado
    // de la operacion en un json respuesta.
    
    @POST
    @Path("/tinscripcionEditPOST")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String edittinscripcion(String temp) throws ParseException, JSONException {
        
        // obj. Objeto JSON que contendra la informacion enviada desde el front.
        // json. Objeto JSON variable que contendra informacion de cada abonado
        //      destinatario de la consulta.
        // canal. Objeto JSON que contendra la informacion del canal en el que se
        //      enviara la tarea.
        // men. Objeto JSON que contendra la informacion del mensaje como tal de la
        //      tarea.
        // toke. Objeto JSON respuesta al servicio front.
        // cont. Objeto JSON contenido en el objeto token.
        // mensaje. String con el mensaje de respuesta al servicio front con el resultado
        //      del procedimiento.
        // falla. Boolean que informa al front si hubo alfun error durante el procedimiento.
        
        JSONObject json, obj, canal, men;
        JSONObject token = new JSONObject();
        JSONObject cont = new JSONObject();
        String mensaje = "Se actualizo exitosamente la tarea";
        Boolean falla = false;

        try {
            
            // Primero se obtiene la informacion enviada desde el front. Que
            // incluye toda la informacion necesaria de la tarea a crear. Al mismo
            // tiempo se coloca la fecha actual de creacion del objeto por control.
            
            // Luego se crea un objeto Canal por el cual se transmite la tarea y se
            // le asignan sus datos respectivos.
            
            obj = new JSONObject(temp);

            JSONArray arreglo = obj.getJSONArray("arreglo");
            men = obj.getJSONObject("mensaje");
            canal = men.getJSONObject("canal");

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date today = new Date();
            Date envio = dateFormat.parse(men.getString("fechaEnvio"));
            Date expiro = dateFormat.parse(men.getString("fechaExpiracion"));
            today = dateFormat.parse(dateFormat.format(today));

            Canal can = new Canal();

            can.setCodigo(canal.getString("codigo"));
            can.setDescripcion(canal.getString("descripcion"));
            can.setLongitud(canal.getInt("longitud"));
            can.setPrecioEnviar(BigDecimal.valueOf(canal.getDouble("precioEnviar")));
            can.setPrecioRecibir(BigDecimal.valueOf(canal.getDouble("precioRecibir")));

            // Se revisa la lista de destinatarios, por cada destinatario se intenta
            // conseguir una tarea asociada en la base de datos, si no existe, se crea
            // una nueva, si existe se edita la informacion antigua con la nueva y se
            // guarda la informacion
            
            for (int i = 0; i < arreglo.length(); ++i) {

                json = arreglo.getJSONObject(i);

                TInscripcion tins = new TInscripcion();
                TInscripcionPK tinspk = new TInscripcionPK();

                tinspk.setTelefono(json.getString("telefono"));
                tinspk.setNombre(men.getString("nombre"));
                tinspk.setRifEmpresa(json.getString("rifEmpresa"));

                if (super.find(tinspk) == null) {

                    Abonado abo = new Abonado();
                    AbonadoPK abopk = new AbonadoPK();

                    abopk.setRifEmpresa(json.getString("rifEmpresa"));
                    abopk.setTelefono(json.getString("telefono"));

                    abo.setNombre(json.getString("nombre"));
                    abo.setCi(json.getString("ci"));
                    abo.setAbonadoPK(abopk);

                    //------ DATOS DE LA TAREA
                    tins.setBandeja(men.getString("bandeja"));
                    tins.setCodigoCanal(can);

                    tins.setEstado("PENDIENTE");

                    tins.setFechaCreacion(today);
                    tins.setFechaExpiracion(expiro);
                    tins.setFechaEnvio(envio);
                    tins.setTInscripcionPK(tinspk);

                    tins.setRespuesta(men.getString("respuesta"));
                    
                    tins.setAbonado(abo);

                    super.create(tins);
                } else {

                    tins = super.find(tinspk);

                    tins.setBandeja(men.getString("bandeja"));
                    tins.setCodigoCanal(can);

                    tins.setEstado("PENDIENTE");

                    tins.setFechaExpiracion(expiro);
                    tins.setFechaEnvio(envio);
                    tins.setTInscripcionPK(tinspk);

                    tins.setRespuesta(men.getString("respuesta"));
                    
                    super.edit(tins);
                }
            }

            // Finalmente se crea el mensaje de respuesta al servicio front y
            // se envia el resultado del procedimiento.
            
            cont.put("mensaje", mensaje);
            cont.put("falla", falla);
            cont.put("Logtime", today);
            token.put("token", cont);

            return token.toString();

        } catch (JSONException | ParseException ex) {
            return "{\"token\":{\"mensaje\":\"No se pudo actualizar la tarea\",\"falla\":true}}";
//            java.util.logging.Logger.getLogger(WebController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // ELIMINAR Tarea Inscripcion
    // Procedimiento por el cual el REST recibe la informacion de una tarea que se
    // desea eliminar, busca las entidades necesarias en la base de datos y las elimina.
    // Si no existe semejante entidad produce un error y envia un mensaje al front-end.

    @POST
    @Path("/tinscripcionElimPOST")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String eliminartinscripcion(String temp) throws JSONException, ParseException {
        
        // obj. Objeto JSON que contendra la informacion enviada desde el front.
        // json. Objeto JSON variable que contendra informacion de cada abonado
        //      destinatario de la consulta.
        // canal. Objeto JSON que contendra la informacion del canal en el que se
        //      enviara la tarea.
        // men. Objeto JSON que contendra la informacion del mensaje como tal de la
        //      tarea.
        // toke. Objeto JSON respuesta al servicio front.
        // cont. Objeto JSON contenido en el objeto token.
        // mensaje. String con el mensaje de respuesta al servicio front con el resultado
        //      del procedimiento.
        // falla. Boolean que informa al front si hubo alfun error durante el procedimiento.
        
        
        JSONObject json, obj, men;
        JSONArray tareas;
        JSONObject token = new JSONObject();
        JSONObject cont = new JSONObject();
        String mensaje = "Se elimino exitosamente la tarea";
        Boolean falla = false;

        try {

            // Primero se obtiene la informacion enviada desde el front. Que
            // incluye toda la informacion necesaria de la tarea a crear. Al mismo
            // tiempo se coloca la fecha actual de creacion del objeto por control.
            
            obj = new JSONObject(temp);
            tareas = obj.getJSONArray("tareas");
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date today = new Date();
            today = dateFormat.parse(dateFormat.format(today));
            
            // Por cada una de las tareas enviadas a eliminar, se obtiene la informacion
            // de todos los destinatarios y la informacion del mensaje como tal.
            for (int i = 0; i < tareas.length(); ++i) {

                JSONObject tarea = tareas.getJSONObject(i);
                JSONArray arreglo = tarea.getJSONArray("arreglo");

                men = tarea.getJSONObject("mensaje");
                
                // Luego por cada destinatario se crea un objeto dummy que nos servira para
                // la busqueda, si se consigue el elemento se elimina, si no se produce
                // un error que luego sera enviado al front-end.

                for (int j = 0; j < arreglo.length(); ++j) {
                    json = arreglo.getJSONObject(j);

                    TInscripcionPK tinspk = new TInscripcionPK();

                    tinspk.setTelefono(json.getString("telefono"));
                    tinspk.setNombre(men.getString("nombre"));
                    tinspk.setRifEmpresa(json.getString("rifEmpresa"));

                    if (super.find(tinspk) == null) {
                        falla = true;
                        mensaje = "La tarea que intento eliminar no existe, actualize la pagina";
                        j = arreglo.length();
                    } else {
                        super.remove(super.find(tinspk));
                    }
                }
            }
            
            // Se crea el objeto de respuesta, con el mensaje y con la fecha
            // actual y se envia al front-end.
            
            cont.put("mensaje", mensaje);
            cont.put("falla", falla);
            cont.put("Logtime", today);
            token.put("token", cont);

            return token.toString();

        } catch (JSONException | ParseException ex) {
            return "{\"token\":{\"mensaje\":\"No se pudo actualizar la tarea\",\"falla\":true}}";
//            java.util.logging.Logger.getLogger(WebController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}

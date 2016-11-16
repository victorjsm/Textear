/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import classes.Abonado;
import classes.AbonadoPK;
import classes.Canal;
import classes.TMensaje;
import classes.TMensajePK;
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
@Path("classes.tmensaje")
public class TMensajeFacadeREST extends AbstractFacade<TMensaje> {

    @PersistenceContext(unitName = "com.mycompany_Textear2_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    private TMensajePK getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;rifEmpresa=rifEmpresaValue;ci=ciValue;nombre=nombreValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        classes.TMensajePK key = new classes.TMensajePK();
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

    public TMensajeFacadeREST() {
        super(TMensaje.class);
    }
    
    // GET Tarea Mensaje
    // Procedimiento por el cual el REST recibe el rif de la empresa a travez de
    // una llamada GET y con el devuelve todas las tareas de mensajes asociadas 
    // a ese rif en un arreglo JSON.

    @GET
    @Path("/tmensajeGET/{rif}")
    @Produces({MediaType.APPLICATION_JSON})
    public String find_mensaje(@PathParam("rif") String rif) {
        
        
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

            List lista = em.createNativeQuery("SELECT DISTINCT Nombre FROM t_mensaje WHERE "
                    + "RIF_Empresa = \'" + rif + "\' ").getResultList();

            tareas = new JSONArray();

            for (int i = 0; i < lista.size(); i++) {

                tarea = new JSONObject();

                abonados = new JSONArray();

                List<TMensaje> temp = super.findWithQuery(
                        "SELECT t FROM TMensaje t WHERE t.tMensajePK.nombre = \"" + lista.get(i).toString() + "\"");

                for (int k = 0; k < temp.size(); k++) {
                    
                    em.refresh(temp.get(k));

                    abonado = new JSONObject();

                    if (k == 0) {
                        canal = new JSONObject(temp.get(k).getCodigoCanal().toString());
                        tarea.put("canal", canal);
                        tarea.put("nombre", temp.get(k).getTMensajePK().getNombre());
                        tarea.put("bandeja", temp.get(k).getBandeja());
                        tarea.put("mensaje", temp.get(k).getMensaje());
                        tarea.put("fechaCreacion", dateFormat.format(temp.get(k).getFechaCreacion()));
                        tarea.put("fechaExpiracion", dateFormat.format(temp.get(k).getFechaExpiracion()));
                        tarea.put("fechaEnvio", dateFormat.format(temp.get(k).getFechaEnvio()));
                        tarea.put("estado", temp.get(k).getEstado());
                    }

                    Abonado j = temp.get(k).getAbonado();
                    abonado.put("telefono", j.getAbonadoPK().getTelefono());
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
    
    // CREATE Tarea Mensaje
    // Procedimiento por el cual el REST recibe la informacion de una tarea nueva
    // mediante un JSON y la crea en base de datos. Finalmente envia el resultado
    // de la operacion en un json respuesta.

    @POST
    @Path("/tmensajePOST")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createtmensaje(String temp) throws ParseException, JSONException {
        
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
            Date envio = dateFormat.parse(men.getString("fecha"));
            Date expiro = new Date(envio.getTime() + (1000 * 60 * 60 * 24));
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

                TMensaje tmen = new TMensaje();
                TMensajePK tmenpk = new TMensajePK();

                // ----- DATOS DEL ABONDADO
                Abonado abo = new Abonado();
                AbonadoPK abopk = new AbonadoPK();

                abopk.setRifEmpresa(json.getString("rif"));
                abopk.setTelefono(json.getString("telefono"));

                abo.setNombre(json.getString("nombre"));
                abo.setCi(json.getString("ci"));
                abo.setAbonadoPK(abopk);

                //------ DATOS DE LA TAREA
                tmen.setBandeja(men.getString("bandeja"));
                tmen.setCodigoCanal(can);

                tmen.setEstado("PENDIENTE");

                tmenpk.setTelefono(json.getString("telefono"));
                tmenpk.setNombre(men.getString("nombre"));
                tmenpk.setRifEmpresa(json.getString("rif"));

                tmen.setFechaCreacion(today);
                tmen.setFechaExpiracion(expiro);
                tmen.setFechaEnvio(envio);
                tmen.setTMensajePK(tmenpk);

                tmen.setMensaje(men.getString("mensaje"));
                tmen.setAbonado(abo);


                if (super.find(tmenpk) == null) {
                    super.create(tmen);
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
    
    // EDIT Tarea Mensaje
    // Procedimiento por el cual el REST recibe la informacion actualizada de una tarea
    // existente mediante un JSON y la actualiza en base de datos. Finalmente envia el resultado
    // de la operacion en un json respuesta.

    @POST
    @Path("/tmensajeEditPOST")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String edittmensaje(String temp) throws ParseException, JSONException {
        
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
            Date expiro = dateFormat.parse(men.getString("fechaExpiracion"));;
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

                TMensaje tmen = new TMensaje();
                TMensajePK tmenpk = new TMensajePK();

                tmenpk.setTelefono(json.getString("telefono"));
                tmenpk.setNombre(men.getString("nombre"));
                tmenpk.setRifEmpresa(json.getString("rifEmpresa"));

                if (super.find(tmenpk) == null) {

                    Abonado abo = new Abonado();
                    AbonadoPK abopk = new AbonadoPK();

                    abopk.setRifEmpresa(json.getString("rifEmpresa"));
                    abopk.setTelefono(json.getString("telefono"));

                    abo.setNombre(json.getString("nombre"));
                    abo.setCi(json.getString("ci"));
                    abo.setAbonadoPK(abopk);

                    //------ DATOS DE LA TAREA
                    tmen.setBandeja(men.getString("bandeja"));
                    tmen.setCodigoCanal(can);

                    tmen.setEstado("PENDIENTE");

                    tmen.setFechaCreacion(today);
                    tmen.setFechaExpiracion(expiro);
                    tmen.setFechaEnvio(envio);
                    tmen.setTMensajePK(tmenpk);

                    tmen.setMensaje(men.getString("mensaje"));
                    tmen.setAbonado(abo);

                    super.create(tmen);
                } else {

                    tmen = super.find(tmenpk);

                    tmen.setBandeja(men.getString("bandeja"));
                    tmen.setCodigoCanal(can);

                    tmen.setEstado("PENDIENTE");

                    tmen.setFechaExpiracion(expiro);
                    tmen.setFechaEnvio(envio);
                    tmen.setTMensajePK(tmenpk);

                    tmen.setMensaje(men.getString("mensaje"));

                    super.edit(tmen);
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
    
    // ELIMINAR Tarea Mensaje
    // Procedimiento por el cual el REST recibe la informacion de una tarea que se
    // desea eliminar, busca las entidades necesarias en la base de datos y las elimina.
    // Si no existe semejante entidad produce un error y envia un mensaje al front-end.

    @POST
    @Path("/tmensajeElimPOST")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String eliminartmensaje(String temp) throws JSONException, ParseException {
        
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

                    TMensajePK tmenpk = new TMensajePK();

                    tmenpk.setTelefono(json.getString("telefono"));
                    tmenpk.setNombre(men.getString("nombre"));
                    tmenpk.setRifEmpresa(json.getString("rifEmpresa"));

                    if (super.find(tmenpk) == null) {
                        falla = true;
                        mensaje = "La tarea que intento eliminar no existe, actualize la pagina";
                        j = arreglo.length();
                    } else {
                        super.remove(super.find(tmenpk));
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

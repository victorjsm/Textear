/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import classes.Abonado;
import classes.AbonadoPK;
import classes.Canal;
import classes.OpcionesPregunta;
import classes.OpcionesPreguntaPK;
import classes.PreguntasEncuesta;
import classes.PreguntasEncuestaPK;
import classes.TEncuesta;
import classes.TEncuestaPK;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
@Path("classes.tencuesta")
public class TEncuestaFacadeREST extends AbstractFacade<TEncuesta> {

    @PersistenceContext(unitName = "com.mycompany_Textear2_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    private TEncuestaPK getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;rifEmpresa=rifEmpresaValue;ci=ciValue;nombre=nombreValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        classes.TEncuestaPK key = new classes.TEncuestaPK();
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

    public TEncuestaFacadeREST() {
        super(TEncuesta.class);
    }
    
    // GET Tarea Encuesta
    // Procedimiento por el cual el REST recibe el rif de la empresa a travez de
    // una llamada GET y con el devuelve todas las tareas de encuesta asociadas 
    // a ese rif en un arreglo JSON.

    @GET
    @Path("/TencuestaGET/{rif}")
    @Produces({MediaType.APPLICATION_JSON})
    public String find_encuestas(@PathParam("rif") String rif) {

        // tarea. Objeto JSON destinado a guardar la informacion de cada tarea para
        //      despues ser llevado al arreglo de respuesta.
        // canal. Objeto JSON destinado a guardar la informacion del canal de cada tarea.
        // abonado. Objeto JSON destinado a guardar la informacion de los abonados destinatarios
        //      de cada tarea.
        // pregunta. Objeto JSON que contendra la informacion asociada a cada pregunta de la encuesta.
        // opcion. Objeto JSON que contendra la informacion asociada a cada opcion de cada pregunta
        //      de la encuesta
        // tareas. Array JSON que contendra las tareas relacionadas con el rif enviado.
        // abonados. Array JSON que contendra los abonados destinatarios de cada tarea.
        // preguntas. Array JSON que contendra las preguntas de cada encuesta,
        // opciones. Array JSON que contendra las opciones de cada pregunta.

        JSONObject tarea, canal, abonado, pregunta, opcion;
        JSONArray tareas, abonados, preguntas, opciones;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            
            // Se ejecuta un QUERY para encontrar todos los nombre de tarea existentes
            // asociados al rif.

            List lista = em.createNativeQuery("SELECT DISTINCT Nombre FROM t_encuesta WHERE "
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

                List<TEncuesta> temp = super.findWithQuery(
                        "SELECT t FROM TEncuesta t WHERE t.tEncuestaPK.nombre = \"" + lista.get(i).toString() + "\"");

                for (int k = 0; k < temp.size(); k++) {
                    
                    em.refresh(temp.get(k));

                    abonado = new JSONObject();

                    if (k == 0) {
                        canal = new JSONObject(temp.get(k).getCodigoCanal().toString());
                        tarea.put("canal", canal);
                        tarea.put("nombre", temp.get(k).getTEncuestaPK().getNombre());
                        tarea.put("bandeja", temp.get(k).getBandeja());
                        tarea.put("bienvenida", temp.get(k).getBienvenida());
                        tarea.put("ayuda", temp.get(k).getAyuda());
                        tarea.put("fechaCreacion", dateFormat.format(temp.get(k).getFechaCreacion()));
                        tarea.put("fechaExpiracion", dateFormat.format(temp.get(k).getFechaExpiracion()));
                        tarea.put("fechaEnvio", dateFormat.format(temp.get(k).getFechaEnvio()));
                        tarea.put("estado", temp.get(k).getEstado());

                        // Luego por cada tarea se toma un arreglo de las preguntas asociadas
                        // y sucesivamente de las opciones por cada pregunta, se colocan en un arreglo
                        // y finalmente se le agregan a la tarea.
                        preguntas = new JSONArray();
                        List<PreguntasEncuesta> preg = new ArrayList<>(temp.get(k).getPreguntasEncuestaCollection());

                        for (int j = 0; j < preg.size(); j++) {
                            PreguntasEncuesta p = preg.get(j);
                            pregunta = new JSONObject(p.toString());
                            opciones = new JSONArray();

                            List<OpcionesPregunta> opc = new ArrayList<>(p.getOpcionesPreguntaCollection());
                            for (int l = 0; l < opc.size(); l++) {
                                OpcionesPregunta o = opc.get(l);
                                opcion = new JSONObject(o.toString());
                                opciones.put(opcion);
                            }
                            pregunta.put("opciones", opciones);
                            preguntas.put(pregunta);
                        }

                        tarea.put("preguntas", preguntas);
                    }

                    Abonado j = temp.get(k).getAbonado();
                    abonado.put("telefono", temp.get(k).getTEncuestaPK().getTelefono());
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
    
    // CREATE Tarea Encuesta
    // Procedimiento por el cual el REST recibe la informacion de una tarea nueva
    // mediante un JSON y la crea en base de datos. Finalmente envia el resultado
    // de la operacion en un json respuesta.

    @POST
    @Path("/tencuestaPOST")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createtencuesta(String temp) throws ParseException, JSONException {
        
        // obj. Objeto JSON que contendra la informacion enviada desde el front.
        // json. Objeto JSON variable que contendra informacion de cada abonado
        //      destinatario de la consulta.
        // json2. Objeto JSON variable que contendra la informacion de cada pregunta
        //      y luego de cada opcion dentro de la encuesta.
        // canal. Objeto JSON que contendra la informacion del canal en el que se
        //      enviara la tarea.
        // men. Objeto JSON que contendra la informacion del mensaje como tal de la
        //      tarea.
        // toke. Objeto JSON respuesta al servicio front.
        // cont. Objeto JSON contenido en el objeto token.
        // mensaje. String con el mensaje de respuesta al servicio front con el resultado
        //      del procedimiento.
        // falla. Boolean que informa al front si hubo alfun error durante el procedimiento.
        
        JSONObject json, json2, obj, canal, men;
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

                TEncuesta tenc = new TEncuesta();
                TEncuestaPK tencpk = new TEncuestaPK();

                Abonado abo = new Abonado();
                AbonadoPK abopk = new AbonadoPK();

                abopk.setRifEmpresa(json.getString("rif"));
                abopk.setTelefono(json.getString("telefono"));

                abo.setNombre(json.getString("nombre"));
                abo.setCi(json.getString("ci"));
                abo.setAbonadoPK(abopk);

                tenc.setBandeja(men.getString("bandeja"));
                tenc.setCodigoCanal(can);

                tenc.setEstado("PENDIENTE");

                tencpk.setTelefono(json.getString("telefono"));
                tencpk.setNombre(men.getString("nombre"));
                tencpk.setRifEmpresa(json.getString("rif"));

                tenc.setFechaCreacion(today);
                tenc.setFechaExpiracion(expiro);
                tenc.setFechaEnvio(envio);
                tenc.setTEncuestaPK(tencpk);

                tenc.setBienvenida(men.getString("bienvenida"));
                tenc.setAyuda(men.getString("ayuda"));
                tenc.setAbonado(abo);

                JSONArray jsonPreguntas = men.getJSONArray("preguntas");

                List<PreguntasEncuesta> preguntas = new ArrayList<>();

                // Por cada tarea se genera una coleccion de preguntas asociadas
                // y cada pregunta tiene una coleccion de opciones asociada.
                
                // Se toma la informacion provista desde el front y se crean
                // los objetos y sus objetos llaves requeridos, finalmente
                // se le atribuyen a la tarea.
                
                for (int j = 0; j < jsonPreguntas.length(); ++j) {
                    json2 = jsonPreguntas.getJSONObject(j);

                    PreguntasEncuesta pregunta = new PreguntasEncuesta();
                    PreguntasEncuestaPK preguntaPK = new PreguntasEncuestaPK();

                    preguntaPK.setId(j);
                    preguntaPK.setTTelefono(json.getString("telefono"));
                    preguntaPK.setTNombre(men.getString("nombre"));
                    preguntaPK.setTRif(json.getString("rif"));

                    pregunta.setPregunta(json2.getString("pregunta"));
                    pregunta.setRespuesta("");
                    pregunta.setPreguntasEncuestaPK(preguntaPK);

                    JSONArray jsonOpciones = json2.getJSONArray("opciones");
                    List<OpcionesPregunta> opciones = new ArrayList<>();

                    for (int k = 0; k < jsonOpciones.length(); ++k) {
                        json2 = jsonOpciones.getJSONObject(k);
                        OpcionesPregunta opcion = new OpcionesPregunta();
                        OpcionesPreguntaPK opcionPK = new OpcionesPreguntaPK();

                        opcionPK.setId(k);
                        opcionPK.setPreguntaId(j);
                        opcionPK.setTTelefono(json.getString("telefono"));
                        opcionPK.setTNombre(men.getString("nombre"));
                        opcionPK.setTRif(json.getString("rif"));

                        opcion.setOpcion(json2.getString("opcion"));
                        opcion.setOpcionesPreguntaPK(opcionPK);

                        opciones.add(opcion);

                    }
                    pregunta.setOpcionesPreguntaCollection(opciones);
                    preguntas.add(pregunta);

                }

                tenc.setPreguntasEncuestaCollection(preguntas);

                if (super.find(tencpk) == null) {
                    super.create(tenc);
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
    
    // EDIT Tarea Encuesta
    // Procedimiento por el cual el REST recibe la informacion actualizada de una tarea
    // existente mediante un JSON y la actualiza en base de datos. Finalmente envia el resultado
    // de la operacion en un json respuesta.

    @POST
    @Path("/tencuestaEditPOST")
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

            // --------- FECHAS EN LA TAREA
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date today = new Date();
            Date envio = dateFormat.parse(men.getString("fechaEnvio"));
            Date expiro = dateFormat.parse(men.getString("fechaExpiracion"));
            today = dateFormat.parse(dateFormat.format(today));

            // --------  DATOS DEL CANAL
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


                TEncuesta tmen = new TEncuesta();
                TEncuestaPK tmenpk = new TEncuestaPK();
                    
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
                    
                    if (men.has("estado")){
                        tmen.setEstado(men.getString("estado"));
                    } else {
                        tmen.setEstado("PENDIENTE");
                    }

                    

                    tmen.setFechaCreacion(today);
                    tmen.setFechaExpiracion(expiro);
                    tmen.setFechaEnvio(envio);
                    tmen.setTEncuestaPK(tmenpk);

                    tmen.setBienvenida(men.getString("bienvenida"));
                    tmen.setAyuda(men.getString("ayuda"));

                    tmen.setAbonado(abo);


                    JSONArray jsonPreguntas = men.getJSONArray("preguntas");

                    List<PreguntasEncuesta> preguntas = new ArrayList<>();


                    for (int j = 0; j < jsonPreguntas.length(); ++j) {
                        JSONObject json2 = jsonPreguntas.getJSONObject(j);

                        PreguntasEncuesta pregunta = new PreguntasEncuesta();
                        PreguntasEncuestaPK preguntaPK = new PreguntasEncuestaPK();

                        preguntaPK.setId(j);
                        preguntaPK.setTTelefono(json.getString("telefono"));
                        preguntaPK.setTNombre(men.getString("nombre"));
                        preguntaPK.setTRif(json.getString("rif"));

                        pregunta.setPregunta(json2.getString("pregunta"));
                        pregunta.setPreguntasEncuestaPK(preguntaPK);

                        JSONArray jsonOpciones = json2.getJSONArray("opciones");
                        List<OpcionesPregunta> opciones = new ArrayList<>();

                        for (int k = 0; k < jsonOpciones.length(); ++k) {
                            json2 = jsonOpciones.getJSONObject(k);
                            OpcionesPregunta opcion = new OpcionesPregunta();
                            OpcionesPreguntaPK opcionPK = new OpcionesPreguntaPK();

                            opcionPK.setId(k);
                            opcionPK.setPreguntaId(j);
                            opcionPK.setTTelefono(json.getString("telefono"));
                            opcionPK.setTNombre(men.getString("nombre"));
                            opcionPK.setTRif(json.getString("rif"));

                            opcion.setOpcion(json2.getString("opcion"));
                            opcion.setOpcionesPreguntaPK(opcionPK);

                            
                            opciones.add(opcion);

                        }
                        pregunta.setOpcionesPreguntaCollection(opciones);
                        preguntas.add(pregunta);

                    }

                    tmen.setPreguntasEncuestaCollection(preguntas);

                    super.create(tmen);
                } else {
                    
                    tmen = super.find(tmenpk);

                    tmen.setBandeja(men.getString("bandeja"));
                    tmen.setCodigoCanal(can);

                    if (men.has("estado")){
                        tmen.setEstado(men.getString("estado"));
                    } else {
                        tmen.setEstado("PENDIENTE");
                    }

                    tmen.setFechaExpiracion(expiro);
                    tmen.setFechaEnvio(envio);
                    tmen.setTEncuestaPK(tmenpk);

                    tmen.setBienvenida(men.getString("bienvenida"));
                    tmen.setAyuda(men.getString("ayuda"));
                    
                    JSONArray jsonPreguntas = men.getJSONArray("preguntas");
                   
                    
                    List<PreguntasEncuesta> preguntas = new ArrayList<>();

                    for (int j = 0; j < jsonPreguntas.length(); ++j) {
                        JSONObject json2 = jsonPreguntas.getJSONObject(j);

                        PreguntasEncuesta pregunta = new PreguntasEncuesta();
                        PreguntasEncuestaPK preguntaPK = new PreguntasEncuestaPK();

                        preguntaPK.setId(j);
                        preguntaPK.setTTelefono(json.getString("telefono"));
                        preguntaPK.setTNombre(men.getString("nombre"));
                        
                        preguntaPK.setTRif(json.getString("rifEmpresa"));

                        pregunta.setPregunta(json2.getString("pregunta"));
                        pregunta.setPreguntasEncuestaPK(preguntaPK);

                        JSONArray jsonOpciones = json2.getJSONArray("opciones");
                        List<OpcionesPregunta> opciones = new ArrayList<>();
                        
                        for (int k = 0; k < jsonOpciones.length(); ++k) {
                            json2 = jsonOpciones.getJSONObject(k);
                            OpcionesPregunta opcion = new OpcionesPregunta();
                            OpcionesPreguntaPK opcionPK = new OpcionesPreguntaPK();

                            opcionPK.setId(k);
                            opcionPK.setPreguntaId(j);
                            opcionPK.setTTelefono(json.getString("telefono"));
                            opcionPK.setTNombre(men.getString("nombre"));
                            opcionPK.setTRif(json.getString("rifEmpresa"));
                            
                            opcion.setOpcion(json2.getString("opcion"));
                            opcion.setOpcionesPreguntaPK(opcionPK);

                            opciones.add(opcion);

            }
                        pregunta.setOpcionesPreguntaCollection(opciones);
                        preguntas.add(pregunta);

                    }
                    

                    tmen.getPreguntasEncuestaCollection().clear();
                    tmen.setPreguntasEncuestaCollection(preguntas);
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
    
    // ELIMINAR Tarea Encuesta
    // Procedimiento por el cual el REST recibe la informacion de una tarea que se
    // desea eliminar, busca las entidades necesarias en la base de datos y las elimina.
    // Si no existe semejante entidad produce un error y envia un mensaje al front-end.

    @POST
    @Path("/tencuestaElimPOST")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String encuestatconsulta(String temp) throws JSONException, ParseException {
        
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

                    TEncuestaPK tencpk = new TEncuestaPK();

                    tencpk.setTelefono(json.getString("telefono"));
                    tencpk.setNombre(men.getString("nombre"));
                    tencpk.setRifEmpresa(json.getString("rifEmpresa"));

                    if (super.find(tencpk) == null) {
                        falla = true;
                        mensaje = "La tarea que intento eliminar no existe, actualize la pagina";
                        j = arreglo.length();
                    } else {
                        super.remove(super.find(tencpk));
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
            return "{\"token\":{\"mensaje\":\"No se pudo eliminar la tarea\",\"falla\":true}}";
//            java.util.logging.Logger.getLogger(WebController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}

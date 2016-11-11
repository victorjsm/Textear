/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import classes.Abonado;
import classes.AbonadoPK;
import classes.Grupo;
import classes.GrupoPK;
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
@Path("classes.grupo")
public class GrupoFacadeREST extends AbstractFacade<Grupo> {

    @PersistenceContext(unitName = "com.mycompany_Textear2_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    private GrupoPK getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;rifEmpresa=rifEmpresaValue;nombre=nombreValue;ci=ciValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        classes.GrupoPK key = new classes.GrupoPK();
        javax.ws.rs.core.MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
        java.util.List<String> rifEmpresa = map.get("rifEmpresa");
        if (rifEmpresa != null && !rifEmpresa.isEmpty()) {
            key.setRifEmpresa(rifEmpresa.get(0));
        }
        java.util.List<String> nombre = map.get("nombre");
        if (nombre != null && !nombre.isEmpty()) {
            key.setNombre(nombre.get(0));
        }
        java.util.List<String> telefono = map.get("telefono");
        if (telefono != null && !telefono.isEmpty()) {
            key.setTelefono(telefono.get(0));
        }
        return key;
    }

    public GrupoFacadeREST() {
        super(Grupo.class);
    }
    
    // OBTENER GRUPOS
    // Procedimiento por el cual el REST recibe el RIF de una empresa en particular,
    // y retorna todos los grupos de abonados asociados a esa empresa y el numero
    // de miembros pertenecientes a cada uno.
    // El REST recibe el rif via una llamada GET y retorna un JSON.


    @GET
    @Path("gruposGETALL/{rif}")
    @Produces({MediaType.APPLICATION_JSON})
    public String find_grupos(@PathParam("rif") String rif) {
        
        // json. Objecto JSON providisonal para almacenar la informacion deseada
        // de cada grupo que se enviara como respuesta.
        // token. Objeto JSON que se enviara como respuesta al servicio front-end
        // cont. Objeto JSON contenido dentro de token.
        // arreglo. Arreglo JSON para almacenar los elementos que se almacenaran
        // en json con la informacion de cada grupo.
        // numero. Int donde se almacenara el tamano de cada grupo.
        // temp2. Contendra una lista con todos los nombres de los grupos diferentes 
        // en la base de datos.
        
        
        JSONObject json;
        JSONObject token = new JSONObject();
        JSONObject cont = new JSONObject();
        JSONArray arreglo = new JSONArray();
        int numero;
        List temp2 = em.createNativeQuery("SELECT DISTINCT Nombre FROM Grupo WHERE "
                + "RIF_Empresa = \'" + rif + "\' ").getResultList();

        try {
            
            // Se obtiene la fecha actual para el mensaje de respuesta y para el LOG
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            
            // Por cada nombre en la lista temp2, se cuenta cuantos miembros existen 
            // en dicho grupo y se guarda en numero, luego se crea el objeto respuesta
            // deseado, con nombre del grupo y numero de miembros y se almacena en el
            // arreglo de respuesta

            for (int i = 0; i < temp2.size(); ++i) {
                numero = ((Long) em.createNativeQuery("SELECT count(1) FROM Grupo WHERE "
                        + "RIF_Empresa = \'" + rif + "\' AND "
                        + "Nombre = \'" + temp2.get(i).toString() + "\'").getSingleResult()).intValue();
                json = new JSONObject();
                json.put("numero", numero);
                json.put("nombre", temp2.get(i).toString());
                arreglo.put(json);
            }
            
            // Finalmente se crea el objeto de respuesta deseado y se envia al front-end

            cont.put("Logtime", dateFormat.format(date));
            cont.put("arreglo", arreglo);
            cont.put("falla", false);

            token.put("token", cont);

            return (arreglo.toString());

        } catch (JSONException ex) {
            return "{\"token\":{\"mensaje\":\"No se pudieron enviar los grupos\",\"falla\":true}}";
//            java.util.logging.Logger.getLogger(WebController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // OBTENER GRUPO
    // Procedimiento por el cual el REST recibe el RIF de una empresa en particular,
    // y el nombre de un grupo y retorna toda la informacion asociada a ese grupo.
    // El REST recibe el rif y el nombre via una llamada GET y devuelve con un JSON.


    @GET
    @Path("gruposGETabo/{rif}/{nombre}")
    @Produces({MediaType.APPLICATION_JSON})
    public String find_grupo(@PathParam("rif") String rif, @PathParam("nombre") String nombre) {
        
        // Con un QUERY orientado al entitymanager se obtiene la entidad de la base de datos
        // asociada a ese nombre y a esa empresa
        List<Grupo> temp = super.findWithQuery(
                "SELECT g FROM Grupo g WHERE g.grupoPK.rifEmpresa = \"" + rif + "\""
                + "AND g.grupoPK.nombre = \"" + nombre + "\""
        );
        
        // El procedimiento toString, me devuelve la informacion del abonado perteneciente al grupo
        // en formato JSON, lo concateno a una lista conservando el formato JSON deseado
        // de un arreglo y devuelvo la lista al front-end con todos los abonados pertenecientes
        // al grupo.
        String lista = "[";
        for (int i = 0; i < temp.size(); i++) {
            lista = lista + temp.get(i).getAbonado().toString();
            if (i != temp.size() - 1) {
                lista = lista + ',';
            }
        }
        lista = lista + "]";
        return lista;
    }
    
    // CREAR GRUPO
    // Procedimiento por el cual el REST recibe informacion de un nuevo grupo 
    // y lo crea en la base de datos.
    // El REST recibe la informacion nueva del grupo via una llamada POST dentro
    // de un JSON, que sera consumido para crear la instancia en la base de datos
    // y posteriormente envia un JSON de respuesta al front-end

    @POST
    @Path("/grupoPOST")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String creategrupo(String temp) {
        
        // json. Objeto JSON que contendra la informacion enviada desde el front-end
        // token. Objeto JSON respuesta para el servicio front-end
        // cont. Objeto JSON contenido en token.
        // arreglo. Arreglo JSON que contendra la informacion de los abonados pertenecientes
        // al grupo por crear.
        JSONObject json;
        JSONObject token;
        JSONObject cont;
        JSONArray arreglo;

        try {
            
            // Se incializan los JSON y se obtiene la informacion enviada desde el 
            // front end en la llamada. Ademas se toma la fecha de hoy y se crea
            // el formato deseado para la respuesta y el LOG.
            json = new JSONObject(temp);
            token = new JSONObject();
            cont = new JSONObject();
            arreglo = json.getJSONArray("arreglo");
            String nombre = json.getString("nombre");
            
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            
            
            // Por cada objeto del arreglo, se toma la informacion necesaria para
            // el grupo nuevo y por cada abonado que pertenecera al grupo.
            // Despues se busca si existe dicho grupo, el grupo esta completamente
            // asociado a los abonados que son parte de el, sin abonados no hay grupo.
            // por lo cual si existe ya la instancia que se intenta crear, no se 
            // produce error, simplemente se ignora y no se agrega el duplicado.
            for (int i = 0; i < arreglo.length(); ++i) {
                json = arreglo.getJSONObject(i);

                GrupoPK grupoPK = new GrupoPK();

                grupoPK.setRifEmpresa(json.getString("rif"));
                grupoPK.setNombre(nombre);
                grupoPK.setTelefono(json.getString("telefono"));

                Abonado abo = new Abonado();
                AbonadoPK abopk = new AbonadoPK();

                abopk.setRifEmpresa(json.getString("rif"));
                abopk.setTelefono(json.getString("telefono"));

                abo.setNombre(json.getString("nombre"));
                abo.setCi(json.getString("ci"));
                abo.setAbonadoPK(abopk);
                
                Grupo grupo = new Grupo();

                if (super.find(grupoPK) == null) {
                    grupo.setGrupoPK(grupoPK);
                    grupo.setAbonado(abo);
                    super.create(grupo);
                }
            }
            
            // Se crea el mensaje a enviar de respuesta al front-end y se envia.
            cont.put("Logtime", dateFormat.format(date));
            cont.put("mensaje", "Se creo exitosamente el grupo");
            cont.put("falla", false);

            token.put("token", cont);

            return token.toString();

        } catch (JSONException ex) {
            return "{\"token\":{\"mensaje\":\"No se pudo crear abonado\",\"falla\":true}}";
//            java.util.logging.Logger.getLogger(WebController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // DELETE Abonados de Grupo
    // Procedimiento por el cual el REST recibe informacion de diferentes abonados
    // que se quieran eliminar de un grupo en particular.
    // El REST recibe la informacion del grupo y los abonados que se desean eliminar
    // dentro de un JSON, que sera consumido para eliminar los datos deseados de la
    // base de datos. Posteriormente se envia un JSON de respuesta al front-end.
    
    @POST
    @Path("/gruposABODELETE")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_JSON)
    public String delete_gruposAbo(String temp) {
        
        // falla. Boolean que indica si ocurrio algun error en el procedimiento al
        // front-end.
        // json. Objeto JSON que contendra la informacion provista desde el front-end
        // para realizar la operacion.
        // token. Objeto JSON de respuesta al servicio front-end
        // cont. Objeto JSON que contendra el objeto json.
        
        Boolean falla = false;
        JSONObject json;
        JSONObject token = new JSONObject();
        JSONObject cont = new JSONObject();
        try {
            
            // Se obtiene la informacion enviada desde el front-end y se crea un objeto
            // con la fecha actual y un formato para esa fecha.
            
            
            json = new JSONObject(temp);
            JSONArray arreglo = json.getJSONArray("arreglo");

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            
            // Por cada elemento en el arreglo enviado desde el front, se toma la informacion
            // del abonado a eliminar, si no existe ese abonado asociado al grupo, se produce
            // un error, se termina el ciclo y se envia la respuesta. Si existe, se elimina
            // la entrada de la base de datos y se continua el ciclo.

            for (int i = 0; i < arreglo.length(); ++i) {
                json = arreglo.getJSONObject(i);

                GrupoPK grupk = new GrupoPK();
                grupk.setTelefono(json.getString("telefono"));
                grupk.setNombre(json.getString("nombre"));
                grupk.setRifEmpresa(json.getString("rif"));

                if (super.find(grupk) == null) {
                    cont.put("mensaje",
                            "El abonado con cedula" + json.getString("ci") + " no existe"
                            + "en el grupo" + json.getString("nombre") + ". Actualize la pagina");
                    falla = true;

                } else {
                    super.remove(super.find(grupk));
                }
            }
            
            // Se crea el elemento de respuesta al servicio front-end y se envia.

            cont.put("Logtime", dateFormat.format(date));
            cont.put("falla", falla);
            if (!falla) {
                cont.put("mensaje", "Se eliminaron exitosamente los abonados del grupo");
            }

            token.put("token", cont);

            return token.toString();

        } catch (JSONException ex) {
            return "{\"token\":{\"mensaje\":\"No se pudo eliminar los abonados del grupo grupo\",\"falla\":true}}";
//            java.util.logging.Logger.getLogger(WebController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // DELETE Grupo
    // Procedimiento por el cual el REST recibe informacion de un grupo o varios que quiera
    // eliminar.
    // El REST recibe la informacion dentro de un JSON, que sera consumido 
    // para buscar el objeto en la base de datos y eliminarlo. Posteriormente 
    // se envia un JSON de respuesta al front-end.
    
    @POST
    @Path("/gruposDELETE")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_JSON)
    public String delete_grupos(String temp) {
        
        // falla. Boolean que se utilizara para indicarle al front si ocurrio algun
        // error en el procedimiento.
        // json. Objeto JSON que contendra la informacion enviada desde el front-end
        // token. Objeto JSON que contendra la respuesta al servicio front-end.
        // cont. Objeto JSON contenido en el objeto token.
        
        Boolean falla = false;
        JSONObject json;
        JSONObject token = new JSONObject();
        JSONObject cont = new JSONObject();
        
        try {
            
            // Se obtiene la informacion enviada desde el servidor que contiene
            // un arreglo de grupos a eliminar. Tambien se obtiene la fecha actual
            // y el formato deseado para el LOG y la respuesta al front-end.
            
            json = new JSONObject(temp);
            JSONArray arreglo = json.getJSONArray("arreglo");

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            
            // Por cada elemento del arreglo se busca ese elemento en la base de datos
            // y se elimina.
            
            for (int i = 0; i < arreglo.length(); ++i) {
                json = arreglo.getJSONObject(i);
                
                
                List<Grupo> temp2 = super.findWithQuery(
                        "SELECT g FROM Grupo g WHERE g.grupoPK.rifEmpresa = \"" + json.getString("rif") + "\""
                        + "AND g.grupoPK.nombre = \"" + json.getString("nombre") + "\"");


                for (int j = 0; j < temp2.size(); j++) {
                    super.remove(temp2.get(j));
                }
            }
            
            // Se crea el mensaje de respuesta al servidor y se envia la respuesta.
            

            cont.put("Logtime", dateFormat.format(date));
            cont.put("falla", falla);
            if (!falla) {
                cont.put("mensaje", "Se elimino exitosamente el grupo");
            }

            token.put("token", cont);

            return token.toString();

        } catch (JSONException ex) {
            return "{\"token\":{\"mensaje\":\"No se pudo eliminar el grupo\",\"falla\":true}}";
//            java.util.logging.Logger.getLogger(WebController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}

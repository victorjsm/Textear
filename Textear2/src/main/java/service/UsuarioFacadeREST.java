/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import classes.Empresa;
import classes.Usuario;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@Path("classes.usuario")
public class UsuarioFacadeREST extends AbstractFacade<Usuario> {

    @PersistenceContext(unitName = "com.mycompany_Textear2_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public UsuarioFacadeREST() {
        super(Usuario.class);
    }

    // CREATE Usuario
    // Procedimiento por el cual el REST recibe mediante una llamada POST, un
    // JSON con la informacion de un nuevo usuario a ser creado.
    // El procedimiento termina enviando un JSON de respuesta al servicio front
    // con el resultado del mismo.
    @POST
    @Path("/createuser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createuser(String temp) {

        // user. Objeto Usuario destinado a guardar la informacion del usuario
        //      nuevo en la base de datos.
        // emp. Objeto Empresa destinado a guardar la informacion de la empresa
        //      asoaciada al usuario nuevo.
        // json. Objeto JSON que contendra la informacion enviada desde el
        //      servicio front.
        // json2. Objeto JSON que contendra la informacion de la empresa enviada
        //      desde el servicio front.
        // mensaje. String que contendra el mensaje de respuesta al servicio front.
        // falla. Boolean que indicara al servicio front si ocurrio algun error
        //      al momento de ejecutar el procedimiento.
        Usuario user = new Usuario();
        Empresa emp = new Empresa();
        JSONObject json, json2;
        String mensaje;
        Boolean falla;

        try {

            // Se obtiene la informacion enviada desde el front y se almacena
            // en json y json2.
            // Despues se le asigna la informacion necesaria a los objetos emp y
            // user respectivamente.
            json = new JSONObject(temp);
            json2 = json.getJSONObject("empresa");

            if (json2.has("direccion")) {
                emp.setDireccion(json2.getString("direccion"));
            }
            if (json2.has("nit")) {
                emp.setNit(json2.getString("nit"));
            }
            emp.setRif(json2.getString("rif"));
            emp.setNombre(json2.getString("nombre"));
            emp.setAcronimo(json2.getString("acronimo"));
            if (json2.has("pagina")) {
                emp.setPaginaweb(json2.getString("pagina"));
            }
            emp.setTelefono(json2.getString("telefono"));

            user.setId(json.getString("id"));
            if (json.has("email")) {
            user.setEmail(json.getString("email"));
            }
            if (json.has("fax")) {
            user.setFax(json.getString("fax"));
            }
            user.setNombre(json.getString("nombre"));
            user.setPassword(json.getString("password"));
            user.setTelefono(json.getString("telefono"));
            user.setTipo(json.getString("tipo"));
            user.setRifEmpresa(emp);

            // Se verifica que no exista un usuario igual almacenado en base de
            // datos, si existe uno igual, se crea un mensaje de error y se envia
            // al servicio post. Si no existe uno igual, se crea la nueva instancia
            // en la base de datos y se envia mensaje de exito al front.
            if (super.find(json.getString("id")) == null) {
                super.create(user);
                mensaje = "se creo el usuario exitosamente";
                falla = false;
            } else {
                mensaje = "Ya existe un usuario con ese ID";
                falla = true;
            }

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
            return "{\"token\":{\"mensaje\":\"No se pudo crear el usuario\",\"falla\":true}}";
//            java.util.logging.Logger.getLogger(WebController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // CHECK Usuario
    // Procedimeinto por el cual el REST recibe mediante una llamada POST, un
    // JSON con el usuario y la contrasena de un intento de inicio.
    // El procedimiento verifica la informacion y envia un mensaje de exito
    // o fallo para indicar si se puede proceder con el inicio de sesion en
    // el sistema.
    @POST
    @Path("/authenticate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String authenticade(String temp) {

        // json. Objeto JSON, contendra la informacion enviada desde el servicio
        //      front para el incio de sesion.
        JSONObject json;
        try {

            // Se adquiere la informacion del usuario y la contrasena, se intenta
            // conseguir el usuario y se verifica que la contrasena se igual, si
            // alguno de estos falla se envia mensaje de error al servicio.
            // Para que este niegue el acceso a la aplicacion por contrasena erronea.
            json = new JSONObject(temp);
            String username = json.getString("username");
            String password = json.getString("password");

            Usuario user = super.find(username);

            if (!(user == null) && user.getPassword().equals(password)) {
                JSONObject token = new JSONObject();
                JSONObject cont = new JSONObject();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();

                JSONObject temp2 = new JSONObject(user.toString());
                cont.put("Logtime", dateFormat.format(date));
                cont.put("Telefono", user.getTelefono());
                cont.put("usuario", temp2);
                token.put("token", cont);

                return token.toString();
            }
            return "";

        } catch (JSONException ex) {
            return "";
//            java.util.logging.Logger.getLogger(WebController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // OBTENER USUARIOS
    // Procedimiento por el cual el REST recibe el RIF de una empresa en particular,
    // y retorna todos los abonados asociados a esa empresa.
    // El REST recibe el rif via una llamada GET
    @GET
    @Path("usuariosGET/{rif}")
    @Produces({MediaType.APPLICATION_JSON})
    public String find_usuarios(@PathParam("rif") String rif) {

        // Se utiliza un query asoaciado al entitymanager para obtener todos
        // los usuarios asociados a ese RIF en particular
        List<Usuario> temp = super.findWithQuery(
                "SELECT u FROM Usuario u WHERE u.rifEmpresa.rif = \"" + rif + "\" AND"
                + " u.tipo != 'cliente'");
        
        for (int k = 0; k < temp.size(); k++) {

            em.refresh(temp.get(k));
        }

        // Se vuelve un arreglo de STRINGS con formato JSON y se envia nuevamente al front-end.
        String json = List_to_JSONString(temp);
        return json;
    }

    // OBTENER USUARIOS ADMIN
    // Procedimiento por el cual el REST envia al front una lista con todos los
    // usuarios tipo cliente almacenados en el sistema, procedimiento valido unicamente para
    // administradores que inicien session en la pagina.
    // El REST recibe el rif via una llamada GET
    @GET
    @Path("usuariosadminGET/")
    @Produces({MediaType.APPLICATION_JSON})
    @SuppressWarnings("empty-statement")
    public String find_usuarios_admin() {

        // lista. Lista de usuarios donde estaran todos los usuarios tipo cliente que se encuentren
        //      en el sistema.
        // lista2.  Lista de los usuarios pertenecientes a esa cuenta cliente.
        // arreglo. JSONArray que contendra todos los usuarios clientes en formato JSON
        //      para ser enviados al front-end
        // arreglo2. JSONArray que contendra por cada usuario admin un arreglo con todos los
        //      usuarios pertenecientes a esa cuenta.
        List<Usuario> lista, lista2;
        JSONArray arreglo, arreglo2;

        // Primero obtenemos todos los usuarios por cada cliente y inicializamos nuestro primer
        // arreglo JSON
        lista = super.findWithQuery("SELECT u FROM Usuario u WHERE u.tipo = 'cliente'");
        try {
            arreglo = new JSONArray();
            // Por cada elemento en lista, se toma el elemento y se coloca en formato JSON,
            // luego se busca todos los usuarios asociados a ese mismo rif de empresa y se
            // inicializa el segundo arreglo

            for (int i = 0; i < lista.size(); i++) {

                Usuario user = lista.get(i);
                em.refresh(user);
                JSONObject temp = new JSONObject(user.toString());
                lista2 = super.findWithQuery("SELECT u FROM Usuario u WHERE u.rifEmpresa.rif = \"" + user.getRifEmpresa() + "\"");
                arreglo2 = new JSONArray();

                // Ahora cada uno de estos usuarios es colocado en formato JSON y se agrega al arreglo2
                // finalmente arreglo 2 es insertado en el json del cliente y finalmente se envia el
                // resultado al servicio front.
                for (Usuario user2 : lista2) {
                    JSONObject temp2 = new JSONObject(user2.toString());
                    arreglo2.put(temp2);
                }
                temp.put("usuarios", arreglo2);
                arreglo.put(temp);
            }
            return arreglo.toString();

        } catch (JSONException ex) {
            return "";
//            java.util.logging.Logger.getLogger(WebController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // EDIT Usuario
    // Procedimiento por el cual el REST recibe informacion actualizada de un usuario
    // actualemnte almacenado en la base de datos y lo actualiza.
    // El REST recibe la informacion del usuario en un JSON. Actualiza la informacion
    // del usuario actualmente almacenado y responde al servicio front.
    @POST
    @Path("/usuarioEDIT")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_JSON)
    public String edit_usuario(String temp) {
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
            Usuario pre = super.find(json.getString("id"));

            if (pre == null) {
                mensaje = "No existe un prefijo con ese Codigo";
                falla = true;

            } else {

                pre.setEmail(json.getString("email"));
                if (json.has("fax")) {
                    pre.setFax(json.getString("fax"));
                };

                pre.setNombre(json.getString("nombre"));
                pre.setPassword(json.getString("password"));
                pre.setTelefono(json.getString("telefono"));
                pre.setTipo(json.getString("tipo"));

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
    
    
    
    // DELETE Usuario
    // Procedimiento por el cual el REST recibe informacion de un usuario que se desea eliminar
    // actualemnte almacenado en la base de datos.
    // El REST recibe la informacion del usuario en un JSON. Busca el elemento en la base de datos
    // y lo elimina.
    
    @POST
    @Path("/usuarioDELETE")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_JSON)
    public String delete_usuario(String temp) {
        
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

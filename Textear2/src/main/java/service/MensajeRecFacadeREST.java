/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import classes.MensajeRec;
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

/**
 *
 * @author aganius
 */
@Stateless
@Path("classes.mensajerec")
public class MensajeRecFacadeREST extends AbstractFacade<MensajeRec> {

    
    
    @PersistenceContext(unitName = "com.mycompany_Textear2_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public MensajeRecFacadeREST() {
        super(MensajeRec.class);
    }


    
    @GET
    @Path("/rec_messages/{telefono}")
    @Produces({MediaType.APPLICATION_JSON})
    public String find_user_rec_messages(@PathParam("telefono") String telefono) {
        List<MensajeRec> temp = super.findWithQuery("SELECT m FROM MensajeRec m WHERE m.telefonoReceptor = \"" + telefono+"\"");
        
        String json = List_to_JSONString(temp);
        
        return json;
    }
    
    @GET
    @Path("/meh/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<MensajeRec> findTest(@PathParam("id") String id) {
        return super.findWithQuery("SELECT m FROM MensajeRec m WHERE m.id =" + id);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}

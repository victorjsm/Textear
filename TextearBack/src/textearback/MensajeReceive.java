/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package textearback;

import com.tedexis.commons.db.ConfigurationDB;
import com.tedexis.commons.db.DBSinglePool;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import tedexis.utils.log.LogUtils;
import static textearback.TextearBack.pool;

/**
 *
 * @author yecheverria
 */
public class MensajeReceive implements MessageListener {
    
    public static DBSinglePool pool;

    @Override
    public void onMessage(Message msg) {
        
        System.out.println("recibiendo objecto de queue...");
        
        Properties p;
        Mensajes_DataManage tmSingle;
        ObjectMessage om = (ObjectMessage) msg;
        try {
            p = LogUtils.loadProperty("/conf/texteardb.properties");
            pool = new DBSinglePool(ConfigurationDB.getInstance(p));
            
            tmSingle = new Mensajes_DataManage(pool);
            
            Mensaje men = (Mensaje) om.getObject();

            String respuesta = men.getMensaje();
            String telefono = men.getTelefono();
            String clave = men.getClave();
            String acronimo = men.getAcronimo();
            String canal = men.getCanal();
            Boolean paso;
            
            paso = tmSingle.checkConsulta(respuesta,telefono,clave,acronimo,canal);
            if (!paso) {
                paso = tmSingle.checkEncuesta(respuesta, telefono, clave, acronimo,canal);
            }
            if (!paso) {
                paso = tmSingle.checkInscripcion(respuesta, telefono, clave, acronimo,canal);
            }
            if (!paso){
                System.out.println("no es de ninguno");
            }
            
        } catch (IOException | ClassNotFoundException | JMSException ex ) {
            Logger.getLogger(MensajeReceive.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        

    }

}

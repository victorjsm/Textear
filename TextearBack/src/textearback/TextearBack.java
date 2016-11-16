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
import tedexis.utils.log.LogUtils;

import com.tedeixs.utils.jms.QueueHelper;
import javax.jms.JMSException;

/**
 *
 * @author aganius
 */
public class TextearBack {

    public static DBSinglePool pool;

    public static void main(String[] args) {
        // TODO code application logic here
        Tareas_DataManage tmSingle;

        com.sun.messaging.QueueConnectionFactory qConnectionFactory = new com.sun.messaging.QueueConnectionFactory();
        QueueHelper qHelper;

        Properties p;
        try {
            p = LogUtils.loadProperty(args[0]);
            qConnectionFactory.setProperty("imqAddressList", p.getProperty("imqAddressList"));
            qHelper = new QueueHelper(qConnectionFactory, p.getProperty("imqName"));

            pool = new DBSinglePool(ConfigurationDB.getInstance(p));

            tmSingle = new Tareas_DataManage(pool);

            qHelper.start();
            qHelper.setMessageListener(new ExampleMessageListener());

//            tmSingle.checkExpired(qHelper);

            tmSingle.checkMensaje(qHelper);
//
//            tmSingle.checkConsulta(qHelper);
//
//            tmSingle.checkInscripcion(qHelper);
//
//            tmSingle.checkSistema(qHelper);

            qHelper.stop();

        } catch (IOException | ClassNotFoundException | JMSException ex) {
            Logger.getLogger(TextearBack.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

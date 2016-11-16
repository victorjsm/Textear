/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package textearback;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 *
 * @author yecheverria
 */
public class ExampleMessageListener implements MessageListener {

    @Override
    public void onMessage(Message msg) {
        System.out.println("recibiendo objecto de queue...");
        ObjectMessage om = (ObjectMessage) msg;
        try {
            String e = om.getObject().toString();
            System.out.println(e);
        } catch (JMSException ex) {
            Logger.getLogger(ExampleMessageListener.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

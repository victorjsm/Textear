/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tedeixs.utils.jms;

import com.sun.messaging.jmq.jmsclient.QueueConnectionImpl;
import com.sun.messaging.jms.notification.Event;
import com.sun.messaging.jms.notification.EventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Escucha los eventos del MQ.
 *
 * @author yecheverria
 */
public class MQEventListener implements EventListener {

    private NotifyMQListener notifyMQ;

    public static Logger LOG = LogManager.getLogger(MQEventListener.class);
    
    public MQEventListener(NotifyMQListener notifyMQ) {
        this.notifyMQ = notifyMQ;
    }

    /**
     * Genera un MQEventListener con el NotifyMQListener vacio por defecto.
     */
    public MQEventListener() {
        this.notifyMQ = new NotifyMQListener() {
            @Override
            public void onFailedReconnection(Event event) {
            }

            @Override
            public void onSuccessfulReconnection(Event event) {
            }
        };
    }

    @Override
    public void onEvent(Event event) {
        LOG.debug(event);
        // Reconexion Fallida. 
        if ("E401".equalsIgnoreCase(event.getEventCode())) {
            LOG.info("Reconexion fallida al MQ. Reintentando Conexion.");
            LOG.info((QueueConnectionImpl) event.getSource());
            notifyMQ.onFailedReconnection(event);
        } // Exito en Reconexion. 
        else if ("E301".equalsIgnoreCase(event.getEventCode())) {
            LOG.info("Reconexión al MQ realizada con exito.");
            LOG.info((QueueConnectionImpl) event.getSource());
            notifyMQ.onSuccessfulReconnection(event);
        }
    }
}

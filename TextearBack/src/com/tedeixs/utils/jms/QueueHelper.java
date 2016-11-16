/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tedeixs.utils.jms;

import com.sun.messaging.jms.notification.EventListener;
import java.io.Serializable;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;

/**
 * Representa una conexión al queue.
 * A este queue se le genera su QueueSender y su QueueReceiver.
 * @author yecheverria
 */
public class QueueHelper {

    private final QueueConnection qConnection;
    private final QueueSession qSession;
    private final QueueSender qSender;
    private final QueueReceiver qReceiver;
    private final Queue queue;

    /**
     * Se crea la cola. Se genera su QueueSender y su QueueReceiver.
     * @param qConnectionFactory Objeto del cual se va a crear la sesion.
     * @param qName nombre de la cola a crear.
     * @throws JMSException
     */
    public QueueHelper(QueueConnectionFactory qConnectionFactory, String qName) throws JMSException {
        qConnection = qConnectionFactory.createQueueConnection();
        qSession = qConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        queue = new com.sun.messaging.Queue(qName);
        qSender = qSession.createSender(queue);
        qReceiver = qSession.createReceiver(queue);
    }

    /**
     * Asigna el listener para el QueueReceiver.
     *
     * @param messageListener puede ser null para desactivar el listener.
     * @throws JMSException
     */
    public void setMessageListener(MessageListener messageListener) throws JMSException {
        qReceiver.setMessageListener(messageListener);
    }

    /**
     * Envia un mensaje a la cola con el QueueSender.
     *
     * @param mensaje
     * @throws JMSException
     */
    public void send(Serializable mensaje) throws JMSException {
        ObjectMessage om = qSession.createObjectMessage();
        om.setObject(mensaje);
        qSender.send(om);
    }

    /**
     * Asigna el ExceptionListener al QueueConnection. Este listeners sera
     * llamado si ocurre algun erro de desconexion del Mq.
     *
     * @param el
     * @throws JMSException
     */
    public void setExceptionListener(ExceptionListener el) throws JMSException {
        qConnection.setExceptionListener(el);
    }

    /**
     * Asigna el EventListener al ((com.sun.messaging.jms.Connection)
     * QueueConnection)
     *
     * @param el
     * @throws JMSException
     */
    public void setEventListener(EventListener el) throws JMSException {
        ((com.sun.messaging.jms.Connection) qConnection).setEventListener(el);
    }

    public void start() throws JMSException {
        qConnection.start();
    }

    public void stop() throws JMSException {
        try {
//     Dejo de recibir mensajes
            setMessageListener(null);
        } catch (Exception e) {

        }

        try {
            qReceiver.close();
        } catch (Exception e) {

        }
        try {
            qSender.close();
        } catch (Exception e) {

        }
        try {
            qSession.close();
        } catch (Exception e) {

        }
        try {
            qConnection.close();
        } catch (Exception e) {

        }
    }

}

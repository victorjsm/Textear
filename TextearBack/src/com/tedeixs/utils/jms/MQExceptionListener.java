/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tedeixs.utils.jms;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author yecheverria
 */
public class MQExceptionListener implements ExceptionListener {

    NotifyMQExceptionListener notifyException;

    public static Logger LOG = LogManager.getLogger(MQExceptionListener.class);

    public MQExceptionListener(NotifyMQExceptionListener notifyException) {
        this.notifyException = notifyException;
    }

    public MQExceptionListener() {
        this(new NotifyMQExceptionListener() {
            @Override
            public void onException(JMSException jmse) {
            }
        });
    }

    @Override
    public void onException(JMSException jmse) {
        LOG.error("Se ha desconectado permanentemente del MQ. Verifique conexión y reinicie Capa de Conexion. ", jmse);
        notifyException.onException(jmse);
    }
}

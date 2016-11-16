/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tedeixs.utils.jms;

import com.sun.messaging.jms.notification.Event;

/**
 *
 * @author yecheverria
 */
public interface NotifyMQListener {

    /**
     * Es llamado cada vez que ocurre un evento en el MQ tipo "E401".
     *
     * @param event
     */
    public void onFailedReconnection(Event event);

    /**
     * Es llamado cada vez que ocurre un evento en el MQ tipo "E301".
     *
     * @param event
     */
    public void onSuccessfulReconnection(Event event);
}

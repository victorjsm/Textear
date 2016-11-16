/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tedeixs.utils.jms;

import javax.jms.JMSException;

/**
 *
 * @author yecheverria
 */
public interface NotifyMQExceptionListener {
    
    public void onException(JMSException jmse);
    
}

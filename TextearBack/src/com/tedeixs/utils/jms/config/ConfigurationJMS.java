/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tedeixs.utils.jms.config;

import java.util.Properties;
import javax.jms.Session;

/**
 * Genera solo una instancia asi el getInstance sea invocado con un properties
 * diferente.
 * @author yecheverria
 */
public class ConfigurationJMS {

    private static ConfigurationJMS instance;
    public final static String IMQ_ADDRESS_LIST_PROPERTY_NAME = "imqAddressList";
    public final static String IMQ_NAME_PROPERTY_NAME = "imqName";
    public final static String IMQ_ACK_ON_ACKNOWLEDGE_PROPERTY_NAME = "imqAckOnAcknowledge";
    public final static String IMQ_RECONNECT_ENABLED_PROPERTY_NAME = "imqReconnectEnabled";
    public final static String IMQ_RECONNECT_ATTEMPTS_PROPERTY_NAME = "imqReconnectAttempts";
    public final static String IMQ_RECONNECT_INTERVAL_PROPERTY_NAME = "imqReconnectInterval";

    private String imqAddressList;
    private String imqName;
    private String imqAckOnAcknowledge;
    private String imqReconnectEnabled;
    private String imqReconnectAttempts;
    private String imqReconnectInterval;

    public ConfigurationJMS(Properties propertyfile) {
        ini(propertyfile);
    }

    public static ConfigurationJMS getInstance(Properties propertyfile) {
        if (instance == null) {
            instance = new ConfigurationJMS(propertyfile);
        }
        return instance;
    }

    /**
     * Pasa los valores del properties a variables.
     *
     * @param propertyfile
     */
    private void ini(Properties propertyfile) {
        imqAddressList = propertyfile.getProperty(IMQ_ADDRESS_LIST_PROPERTY_NAME);
        imqName = propertyfile.getProperty(IMQ_NAME_PROPERTY_NAME);
        imqAckOnAcknowledge = propertyfile.getProperty(IMQ_ACK_ON_ACKNOWLEDGE_PROPERTY_NAME, "false");
        imqReconnectEnabled = propertyfile.getProperty(IMQ_RECONNECT_ENABLED_PROPERTY_NAME, "true");
        imqReconnectAttempts = propertyfile.getProperty(IMQ_RECONNECT_ATTEMPTS_PROPERTY_NAME, "-1");
        imqReconnectInterval = propertyfile.getProperty(IMQ_RECONNECT_INTERVAL_PROPERTY_NAME, "30000");
    }

    public String getImqAddressList() {
        return imqAddressList;
    }

    public String getImqName() {
        return imqName;
    }

    public String getImqAckOnAcknowledge() {
        return imqAckOnAcknowledge;
    }

    public String getImqReconnectEnabled() {
        return imqReconnectEnabled;
    }

    public String getImqReconnectAttempts() {
        return imqReconnectAttempts;
    }

    public String getImqReconnectInterval() {
        return imqReconnectInterval;
    }

}

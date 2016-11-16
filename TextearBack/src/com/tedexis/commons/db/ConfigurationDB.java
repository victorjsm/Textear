/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tedexis.commons.db;

import java.util.Properties;

/**
 *
 * @author yecheverria
 */
public class ConfigurationDB {

    public final static String DB_URL = "DB_URL";
    public final static String DB_USER = "DB_USER";
    public final static String DB_PASSWORD = "DB_PASSWORD";
    public final static String DB_DRIVER = "DB_DRIVER";
    public final static String DB_POOL_INIT_SIZE = "DB_POOL_INIT_SIZE";
    public final static String DB_POOL_MAX_ACT = "DB_POOL_MAX_ACT";
    public final static String DB_POOL_MAX_IDLE = "DB_POOL_MAX_IDLE";
    public final static String DB_POOL_MAX_WAIT = "DB_POOL_MAX_WAIT";

    private static ConfigurationDB instance = null;
    public final String driverClass;
    public final String url;
    public final String user;
    public final String password;
    public final int poolSize;
    public final int poolMaxAct;
    public final int poolMaxIdle;
    public final int poolWait;

    private ConfigurationDB(Properties prop) {
        /* se usa el trim para generar un nullpointerexception en caso de venir vacio */
        driverClass = prop.getProperty(DB_DRIVER).trim();
        url = prop.getProperty(DB_URL).trim();
        user = prop.getProperty(DB_USER).trim();
        password = prop.getProperty(DB_PASSWORD);
        poolSize = Integer.parseInt(prop.getProperty(DB_POOL_INIT_SIZE, "10"));
        poolMaxAct = Integer.parseInt(prop.getProperty(DB_POOL_MAX_ACT, "10"));
        poolMaxIdle = Integer.parseInt(prop.getProperty(DB_POOL_MAX_IDLE, "5"));
        poolWait = Integer.parseInt(prop.getProperty(DB_POOL_MAX_WAIT, "3000"));
    }

    public static ConfigurationDB getInstance(Properties prop) {
        if (instance == null) {
            instance = new ConfigurationDB(prop);
        }
        return instance;
    }
}

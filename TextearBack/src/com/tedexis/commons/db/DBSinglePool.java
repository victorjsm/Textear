/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tedexis.commons.db;

import java.sql.Connection;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Representa el pool de una sola conexión.
 *
 * @author yecheverria
 */
public final class DBSinglePool {

    public final static Logger LOG = LogManager.getLogger(DBSinglePool.class);
    private static DBSinglePool instance = null;
    private static ConfigurationDB configurationDB;

    /**
     * El pool de conexiones.
     */
    private static BasicDataSource dataSource = null;

    public DBSinglePool(ConfigurationDB configurationDB) throws ClassNotFoundException {
        this.configurationDB = configurationDB;
        dataSource = getBasicDataSource(configurationDB);
    }

    /**
     * Crea el pool de conexiones
     *
     * @param configurationDB
     * @return
     * @throws ClassNotFoundException
     */
    private BasicDataSource getBasicDataSource(ConfigurationDB configurationDB) throws ClassNotFoundException {
        BasicDataSource ds = null;
        if (dataSource == null) {

            ds = new BasicDataSource();

            Class.forName(configurationDB.driverClass);

            ds.setUrl(configurationDB.url);
            ds.setDriverClassName(configurationDB.driverClass);
            ds.setUsername(configurationDB.user);
            ds.setPassword(configurationDB.password);
            ds.setInitialSize(configurationDB.poolSize);
            ds.setMaxActive(configurationDB.poolMaxAct);
            ds.setMaxIdle(configurationDB.poolMaxIdle);
            ds.setMaxWait(configurationDB.poolWait);

            return ds;
        }
        return dataSource;
    }

    /**
     * Cierra el DataSource.
     */
    synchronized void closeDataSource() {
        try {
            dataSource.close();
            dataSource = null;
        } catch (Exception ex) {
            LOG.warn("problema cerrando el datasource", ex);
        }
    }

    /**
     * Obtiene la conexion del pool de conexiones. Si el datasource se ha
     * cerrado anteriormente este metodo lo re conecta automaticamente.
     *
     * @return
     */
    synchronized Connection getConnection() throws Exception {
        Connection con = null;
        try {
            if (dataSource == null) {
                dataSource = getBasicDataSource(configurationDB);
            }
            con = dataSource.getConnection();
        } catch (Exception exc) {
            LOG.error("No pudo obtener la conexion.", exc);
            throw exc;
        }
        return con;
    }
}

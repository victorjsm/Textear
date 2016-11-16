/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tedexis.commons.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author yecheverria
 */
public abstract class DBSingleManager {

    private DBSinglePool dbPool = null;

    public DBSingleManager(DBSinglePool dbPool) {
        this.dbPool = dbPool;
    }

    /**
     * Ejecuta una sentencia SELECT a traves del objeto DBExcecuterObject
     *
     * @param o
     * @return Los datos obtenidos de base de datos.
     * @throws Exception
     */
    protected ResultSet execute(final DBExecuterObject o) throws Exception {
        return o.executeQuery(getConnection());
    }

    /**
     * Obtiene una conexion del DataSource.
     *
     * @return
     * @throws SQLException
     */
    protected Connection getConnection() throws SQLException, Exception {
        return dbPool.getConnection();
    }

    /**
     * Crea un ExecuterObject.
     *
     * @return
     * @throws SQLException
     */
    protected DBExecuterObject getBDExecutor(String sql) throws SQLException {
        return new DBExecuterObject(sql);
    }

    protected void close(AutoCloseable objCloseable) {
        if (objCloseable != null) {
            try {
                objCloseable.close();
                objCloseable = null;
            } catch (Exception ex) {
            }
        }
    }

    /**
     * Clase que representa lo necesario para realizar la execucion de una
     * consulta.
     *
     * @author yecheverria
     */
    protected class DBExecuterObject {

        private Statement statement;
        /**
         * Conexion a la base de datos
         */
        private Connection connection;
        /**
         * Sentencia a ejecutar.
         */
        private final String sql;

        DBExecuterObject(String sql) {
            this.sql = sql;
        }

        /**
         * Realiza la execucion(executeQuery) de la base de datos. Crea el
         * statement asi: .createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
         * java.sql.ResultSet.CONCUR_READ_ONLY);. Por defecto el fetchSize es
         * 1000.
         *
         * @param connection conexión a la DB
         * @return Resultset
         * @throws SQLException
         */
        public ResultSet executeQuery(Connection connection) throws SQLException {
            return executeQuery(connection, 1000);
        }

        /**
         * Realiza la execucion(executeQuery) de la base de datos. Crea el
         * statement asi: .createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
         * java.sql.ResultSet.CONCUR_READ_ONLY);
         *
         * @param connection conexión a la DB
         * @param fetchSize tamano del fetchSize del statement.
         * @return Resultset
         * @throws SQLException
         */
        public ResultSet executeQuery(Connection connection, int fetchSize) throws SQLException {
            this.connection = connection;
            /*solo lecutra y solo hacia adelante.*/
            statement = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);
            statement.setFetchSize(fetchSize);
            return statement.executeQuery(sql);
        }
        public int executeUpdate(Connection connection, int fetchSize) throws SQLException {
            this.connection = connection;
            /*solo lecutra y solo hacia adelante.*/
            statement = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);
            statement.setFetchSize(fetchSize);
            return statement.executeUpdate(sql);
        }

        /**
         * Retorna el valor para ser cerrado luego de su uso.
         *
         * @return
         */
        public Statement getStatement() {
            return statement;
        }

        /**
         * Retorna el valor para ser cerrado luego de su uso.
         *
         * @return
         */
        public Connection getConnection() {
            return connection;
        }

    }
}

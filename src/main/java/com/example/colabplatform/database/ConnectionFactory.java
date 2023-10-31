package com.example.colabplatform.database;


import com.example.colabplatform.exceptions.ConnectionPoolException;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionFactory {
    public static final ConnectionFactory factory = new ConnectionFactory();
    private final ConnectionPool connectionPool;
    private static final int MAX_CONNECTIONS = 10;
    private final ThreadLocal<Connection> threadLocalConnection = new ThreadLocal<>();
    private ConnectionFactory() {
        this.connectionPool = new ConnectionPool(MAX_CONNECTIONS);
    }

    public static ConnectionFactory instance(){
        return factory;
    }

    public synchronized Connection getConnection() throws ConnectionPoolException {
//        return connectionPool.getConnection();
        if(threadLocalConnection.get()==null){
            threadLocalConnection.set(connectionPool.getConnection());
        }
        return threadLocalConnection.get();
    }

    public void releaseConnection() throws SQLException, ConnectionPoolException {
        connectionPool.releaseConnection(threadLocalConnection.get());
        threadLocalConnection.set(null);
    }

}
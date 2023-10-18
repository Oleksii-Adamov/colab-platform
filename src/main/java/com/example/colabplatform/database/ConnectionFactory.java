package com.example.colabplatform.database;


import com.example.colabplatform.exceptions.ConnectionPoolException;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionFactory {
    public static final ConnectionFactory factory = new ConnectionFactory();
    private final ConnectionPool connectionPool;
    private static final int MAX_CONNECTIONS = 10;
    private ConnectionFactory() {
        this.connectionPool = new ConnectionPool(MAX_CONNECTIONS);
    }

    public static ConnectionFactory instance(){
        return factory;
    }

    public synchronized Connection getConnection() throws ConnectionPoolException {
        return connectionPool.getConnection();
        //        if(threadLocalTransactionConnection.get()!=null){
//            return threadLocalTransactionConnection.get();
//        }
//        Connection con = connectionFactory.getConnection();
//        con.setAutoCommit(false);
//        return new TransactionConnection(con);
    }

    public void releaseConnection(Connection connection) throws SQLException, ConnectionPoolException {
        connectionPool.releaseConnection(connection);
    }

}
/*
 * Copyright 2012 FuseSource
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.camel.component.sjms.pool;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;

/**
 * TODO Add Class documentation for ConnectionPool
 * 
 */
public class ConnectionPool extends ObjectPool<Connection> {
    private ConnectionFactory connectionFactory;
    private String username;
    private String password;

    /**
     * TODO Add Constructor Javadoc
     * 
     * @param poolSize
     * @param connectionFactory
     */
    public ConnectionPool(int poolSize, ConnectionFactory connectionFactory) {
    	this(poolSize, connectionFactory, null, null);
    }

    /**
     * TODO Add Constructor Javadoc
     * 
     * @param poolSize
     * @param connectionFactory
     * @param username
     * @param password
     */
    public ConnectionPool(int poolSize, ConnectionFactory connectionFactory, String username, String password) {
        super(poolSize);
        this.connectionFactory = connectionFactory;
        this.username = username;
        this.password = password;
    }

    @Override
    protected Connection createObject() throws Exception {
        Connection connection = null;
        if(connectionFactory != null) {
            if(getUsername() != null && getPassword() != null){
                connection = connectionFactory.createConnection(getUsername(), getPassword());
            } else {
                connection = connectionFactory.createConnection();
            }
        }
        if(connection != null) {
            connection.start();
        }
        return connection;
    }
    
    @Override
    protected void destroyObject(Connection connection) throws Exception {
    	if (connection != null) {
            connection.stop();
            connection.close();
        }
    	
    }

    /**
     * Sets the ConnectionFactory value of connectionFactory for this instance
     * of ConnectionPool.
     * 
     * @param connectionFactory
     *            Sets ConnectionFactory, default is null
     */
    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    /**
     * Gets the ConnectionFactory value of connectionFactory for this instance
     * of ConnectionPool.
     * 
     * @return the connectionFactory
     */
    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    /**
     * Gets the String value of username for this instance of ConnectionPool.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the String value of password for this instance of ConnectionPool.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }
}

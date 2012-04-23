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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * TODO Add Class documentation for ConnectionPoolTest
 * 
 */
public class ConnectionPoolTest {
    private ActiveMQConnectionFactory connectionFactory;

    @Before
    public void setup() {
        connectionFactory = new ActiveMQConnectionFactory(
                "vm://broker?broker.persistent=false");
    }

    @After
    public void teardown() {
        connectionFactory = null;
    }

    /**
     * Test method for
     * {@link org.apache.camel.component.sjms.pool.ConnectionPoolTest#createObject()}
     * .
     * 
     * @throws Exception
     */
    @Test
    public void testCreateObject() throws Exception {
        ConnectionPool pool = new ConnectionPool(1, connectionFactory);
        assertNotNull(pool);
        ActiveMQConnection connection = (ActiveMQConnection) pool
                .borrowObject();
        assertNotNull(connection);
        assertTrue(connection.isStarted());
    }

    /**
     * Test method for
     * {@link org.apache.camel.component.sjms.pool.ObjectPool#borrowObject()}.
     * 
     * @throws Exception
     */
    @Test
    public void testBorrowObject() throws Exception {
        ConnectionPool pool = new ConnectionPool(1, connectionFactory);
        assertNotNull(pool);
        ActiveMQConnection connection = (ActiveMQConnection) pool.borrowObject();
        assertNotNull(connection);
        assertTrue(connection.isStarted());

        ActiveMQConnection connection2 = (ActiveMQConnection) pool.borrowObject();
        assertNull(connection2);
        pool.destroyPool();
    }

    /**
     * Test method for
     * {@link org.apache.camel.component.sjms.pool.ObjectPool#returnObject(java.lang.Object)}
     * .
     * @throws Exception 
     */
    @Test
    public void testReturnObject() throws Exception {
        ConnectionPool pool = new ConnectionPool(1, connectionFactory);
        assertNotNull(pool);
        ActiveMQConnection connection = (ActiveMQConnection) pool.borrowObject();
        assertNotNull(connection);
        assertTrue(connection.isStarted());
        pool.returnObject(connection);
        ActiveMQConnection connection2 = (ActiveMQConnection) pool.borrowObject();
        assertNotNull(connection2);
        pool.destroyPool();
    }

}

/*
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
package org.apache.camel.component.sjms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.camel.component.sjms.utils.StringUtils;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO Add Class documentation for SjmsConnectionTestSupport
 * 
 */
public abstract class SjmsConnectionTestSupport {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    public static final String VM_BROKER_CONNECT_STRING = "vm://broker?broker.persistent=false";
    public static final String TCP_BROKER_CONNECT_STRING = "tcp://localhost:61616";
    private ActiveMQConnectionFactory vmTestConnectionFactory;
    private ActiveMQConnectionFactory testConnectionFactory;
    private BrokerService brokerService;
    private boolean persistenceEnabled = false;

    public abstract String getConnectionUri();

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setup() throws Exception {
        if (StringUtils.isEmpty(getConnectionUri())
                || getConnectionUri().startsWith("vm")) {
            vmTestConnectionFactory = new ActiveMQConnectionFactory(
                    VM_BROKER_CONNECT_STRING);
        } else {
            createBroker();
        }
    }

    @After
    public void teardown() throws Exception {

        if (vmTestConnectionFactory != null) {
            vmTestConnectionFactory = null;
        }
        if (testConnectionFactory != null) {
            testConnectionFactory = null;
        }
        if (brokerService != null) {
            destroyBroker();
        }
    }

    /**
     * Gets the ActiveMQConnectionFactory value of testConnectionFactory for
     * this instance of SjmsConnectionTestSupport.
     * 
     * @return the testConnectionFactory
     */
    public ActiveMQConnectionFactory createTestConnectionFactory(String uri) {
        ActiveMQConnectionFactory cf = null;
        if (StringUtils.isEmpty(uri)) {
            cf = new ActiveMQConnectionFactory(VM_BROKER_CONNECT_STRING);
        } else {
            cf = new ActiveMQConnectionFactory(uri);
        }
        return cf;
    }

    protected void createBroker() throws Exception {
        String connectString = getConnectionUri();
        if (StringUtils.isEmpty(connectString)) {
            connectString = TCP_BROKER_CONNECT_STRING;
        }
        brokerService = new BrokerService();
        brokerService.setPersistent(isPersistenceEnabled());
        brokerService.addConnector(connectString);
        brokerService.start();
        brokerService.waitUntilStarted();
    }

    protected void destroyBroker() throws Exception {
        if (brokerService != null) {
            brokerService.stop();
            brokerService.waitUntilStopped();
        }
    }

    /**
     * Sets the ActiveMQConnectionFactory value of testConnectionFactory for
     * this instance of SjmsConnectionTestSupport.
     * 
     * @param testConnectionFactory
     *            Sets ActiveMQConnectionFactory, default is TODO add default
     */
    public void setTestConnectionFactory(
            ActiveMQConnectionFactory testConnectionFactory) {
        this.testConnectionFactory = testConnectionFactory;
    }

    /**
     * Gets the ActiveMQConnectionFactory value of testConnectionFactory for
     * this instance of SjmsConnectionTestSupport.
     * 
     * @return the testConnectionFactory
     */
    public ActiveMQConnectionFactory getTestConnectionFactory() {
        return testConnectionFactory;
    }

    /**
     * Sets the boolean value of persistenceEnabled for this instance of
     * SjmsConnectionTestSupport.
     * 
     * @param persistenceEnabled
     *            Sets boolean, default is false
     */
    public void setPersistenceEnabled(boolean persistenceEnabled) {
        this.persistenceEnabled = persistenceEnabled;
    }

    /**
     * Gets the boolean value of persistenceEnabled for this instance of
     * SjmsConnectionTestSupport.
     * 
     * @return the persistenceEnabled
     */
    public boolean isPersistenceEnabled() {
        return persistenceEnabled;
    }
}

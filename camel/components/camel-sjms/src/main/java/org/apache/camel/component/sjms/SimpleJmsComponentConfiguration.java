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
package org.apache.camel.component.sjms;

import javax.jms.ConnectionFactory;

/**
 * TODO Add Class documentation for SimpleJmsComponentConfiguration
 * 
 */
public class SimpleJmsComponentConfiguration {
    private Integer maxConnections = 1;
    private Integer maxSessions = 1;
    private Integer maxConsumers = 1;
    private Integer maxProducers = 1;
    private ConnectionFactory connectionFactory;

    /**
     * Gets the Integer value of maxConnections for this instance of
     * SimpleJmsComponentConfiguration.
     * 
     * @return the maxConnections
     */
    public final Integer getMaxConnections() {
        return maxConnections;
    }

    /**
     * Sets the Integer value of maxConnections for this instance of
     * SimpleJmsComponentConfiguration.
     * 
     * @param maxConnections
     *            Sets Integer, default is 1
     */
    public final void setMaxConnections(Integer maxConnections) {
        this.maxConnections = maxConnections;
    }

    /**
     * Gets the Integer value of maxSessions for this instance of
     * SimpleJmsComponentConfiguration.
     * 
     * @return the maxSessions
     */
    public final Integer getMaxSessions() {
        return maxSessions;
    }

    /**
     * Sets the Integer value of maxSessions for this instance of
     * SimpleJmsComponentConfiguration.
     * 
     * @param maxSessions
     *            Sets Integer, default is 1
     */
    public final void setMaxSessions(Integer maxSessions) {
        this.maxSessions = maxSessions;
    }

    /**
     * Gets the Integer value of maxConsumers for this instance of
     * SimpleJmsComponentConfiguration.
     * 
     * @return the maxConsumers
     */
    public final Integer getMaxConsumers() {
        return maxConsumers;
    }

    /**
     * Sets the Integer value of maxConsumers for this instance of
     * SimpleJmsComponentConfiguration.
     * 
     * @param maxConsumers
     *            Sets Integer, default is 1
     */
    public final void setMaxConsumers(Integer maxConsumers) {
        this.maxConsumers = maxConsumers;
    }

    /**
     * Sets the ConnectionFactory value of connectionFactory for this instance of SimpleJmsComponentConfiguration.
     *
     * @param connectionFactory Sets ConnectionFactory, default is TODO add default
     */
    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    /**
     * Gets the ConnectionFactory value of connectionFactory for this instance of SimpleJmsComponentConfiguration.
     *
     * @return the connectionFactory
     */
    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    /**
     * Sets the Integer value of maxProducers for this instance of SimpleJmsComponentConfiguration.
     *
     * @param maxProducers Sets Integer, default is TODO add default
     */
    public void setMaxProducers(Integer maxProducers) {
        this.maxProducers = maxProducers;
    }

    /**
     * Gets the Integer value of maxProducers for this instance of SimpleJmsComponentConfiguration.
     *
     * @return the maxProducers
     */
    public Integer getMaxProducers() {
        return maxProducers;
    }

}

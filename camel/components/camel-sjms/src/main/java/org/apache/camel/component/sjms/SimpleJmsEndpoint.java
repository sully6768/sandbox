/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.sjms;

import org.apache.camel.MultipleConsumersSupport;
import org.apache.camel.component.sjms.jms.SessionAcknowledgementType;
import org.apache.camel.component.sjms.pool.ConnectionPool;
import org.apache.camel.component.sjms.pool.SessionPool;
import org.apache.camel.impl.DefaultEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a SimpleJms endpoint.
 */
public abstract class SimpleJmsEndpoint extends DefaultEndpoint implements MultipleConsumersSupport {
    protected final transient Logger logger = LoggerFactory.getLogger(getClass());
    
    private SimpleJmsComponentConfiguration configuration;

    private ConnectionPool connections;

    private SessionPool sessions;

    public SimpleJmsEndpoint() {
    }

    public SimpleJmsEndpoint(String uri, SimpleJmsComponent component) {
        super(uri, component);
        this.setConfiguration(component.getConfiguration());
        setConnections(new ConnectionPool(getConfiguration().getMaxConnections(), getConfiguration().getConnectionFactory()));
        SessionPool sessions = new SessionPool(getConfiguration().getMaxSessions(), getConnections());
        sessions.setAcknowledgeMode(SessionAcknowledgementType.valueOf(getConfiguration().getAcknowledgementMode()));
        sessions.setTransacted(getConfiguration().isTransacted());
        setSessions(sessions);
    }

    public boolean isSingleton() {
        return true;
    }

	/**
	 * @param endpoint
	 */
	public String getDestinationName() {
		return getEndpointUri().substring(getEndpointUri().lastIndexOf(":") + 1);
	}

    /**
     * Sets the SimpleJmsComponentConfiguration value of configuration for this
     * instance of SimpleJmsEndpoint.
     * 
     * @param configuration
     *            Sets SimpleJmsComponentConfiguration, default is TODO add
     *            default
     */
    public void setConfiguration(SimpleJmsComponentConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Gets the SimpleJmsComponentConfiguration value of configuration for this
     * instance of SimpleJmsEndpoint.
     * 
     * @return the configuration
     */
    public SimpleJmsComponentConfiguration getConfiguration() {
        return configuration;
    }

	/* (non-Javadoc)
	 * @see org.apache.camel.MultipleConsumersSupport#isMultipleConsumersSupported()
	 */
	@Override
	public boolean isMultipleConsumersSupported() {
		return true;
	}

    /**
     * 
     * @see org.apache.camel.impl.DefaultEndpoint#doStart()
     */
    @Override
    protected void doStart() throws Exception {
        super.doStart();
        getConnections().fillPool();
        getSessions().fillPool();
    }

    /**
     * 
     * @see org.apache.camel.impl.DefaultEndpoint#doStop()
     */
    @Override
    protected void doStop() throws Exception {
        super.doStop();
        getSessions().drainPool();
        getConnections().drainPool();
    }

    /**
     * Sets the value of connections for this instance of SimpleJmsQueueEndpoint.
     *
     * @param connections the connections to set
     */
    public void setConnections(ConnectionPool connections) {
        this.connections = connections;
    }

    /**
     * Returns the value of connections for this instance of SimpleJmsQueueEndpoint.
     *
     * @return the SimpleJmsQueueEndpoint or null
     */
    public ConnectionPool getConnections() {
        return connections;
    }

    /**
     * Sets the value of sessions for this instance of SimpleJmsQueueEndpoint.
     *
     * @param sessions the sessions to set
     */
    public void setSessions(SessionPool sessions) {
        this.sessions = sessions;
    }

    /**
     * Returns the value of sessions for this instance of SimpleJmsQueueEndpoint.
     *
     * @return the SimpleJmsQueueEndpoint or null
     */
    public SessionPool getSessions() {
        return sessions;
    }

    /**
     * @return
     */
    protected int getMaxProducers() {
        int maxProducers = ((SimpleJmsComponent)this.getComponent()).getConfiguration().getMaxProducers();
        return maxProducers;
    }
}

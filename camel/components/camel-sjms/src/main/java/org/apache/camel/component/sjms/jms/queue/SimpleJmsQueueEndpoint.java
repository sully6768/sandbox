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
package org.apache.camel.component.sjms.jms.queue;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.component.sjms.SimpleJmsComponent;
import org.apache.camel.component.sjms.SimpleJmsEndpoint;
import org.apache.camel.component.sjms.pool.ConnectionPool;
import org.apache.camel.component.sjms.pool.SessionPool;

/**
 * Represents a ActiveMQNoSpring endpoint.
 */
public class SimpleJmsQueueEndpoint extends SimpleJmsEndpoint {
    private ConnectionPool connections;
    private SessionPool sessions;
    
    public SimpleJmsQueueEndpoint() {
    }

    public SimpleJmsQueueEndpoint(String uri, SimpleJmsComponent component) {
        super(uri, component);
        connections = new ConnectionPool(component.getConfiguration().getMaxConnections(), component.getConfiguration().getConnectionFactory());
        sessions = new SessionPool(component.getConfiguration().getMaxSessions(), connections);
    }

    public Producer createProducer() throws Exception {
        int maxProducers = ((SimpleJmsComponent)this.getComponent()).getConfiguration().getMaxProducers();
        return new SimpleJmsQueueProducer(this, sessions, maxProducers);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        int maxConsumers = ((SimpleJmsComponent)this.getComponent()).getConfiguration().getMaxConsumers();
        return new SimpleJmsQueueConsumer(this, processor, sessions, maxConsumers);
    }

    public boolean isSingleton() {
        return true;
    }
    
    @Override
    protected void doStart() throws Exception {
        super.doStart();
    }
    
    @Override
    protected void doStop() throws Exception {
        super.doStop();
        sessions.destroyPool();
        connections.destroyPool();
    }
}

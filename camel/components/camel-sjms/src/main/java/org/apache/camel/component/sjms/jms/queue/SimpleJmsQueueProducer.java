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

import javax.jms.QueueSender;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.camel.Exchange;
import org.apache.camel.component.sjms.SimpleJmsProducer;
import org.apache.camel.component.sjms.pool.ProducerPool;
import org.apache.camel.component.sjms.pool.SessionPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ActiveMQNoSpring producer.
 */
public class SimpleJmsQueueProducer extends SimpleJmsProducer {
    private static final transient Logger LOG = LoggerFactory
            .getLogger(SimpleJmsQueueProducer.class);

    String destinationName = null;
    private SessionPool sessions;
    private ProducerPool producers;
    private int maxProducers = 1;

    public SimpleJmsQueueProducer(SimpleJmsQueueEndpoint endpoint, SessionPool sessionPool, int maxProducers) {
        super(endpoint);
        destinationName = endpoint.getEndpointUri().substring(endpoint.getEndpointUri().lastIndexOf(":"));
        this.sessions = sessionPool;
        this.maxProducers = maxProducers;
    }

    public void process(Exchange exchange) throws Exception {
        Session s = sessions.borrowObject();
        TextMessage textMessage = s.createTextMessage();
        sessions.returnObject(s);
        textMessage.setText((String) exchange.getIn().getBody());
        QueueSender sender = producers.borrowObject();
        sender.send(textMessage);
        producers.returnObject(sender);
        LOG.info((String) exchange.getIn().getBody());
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        producers = new ProducerPool(maxProducers, sessions, destinationName);
    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();
        producers.destroyPool();
        producers = null;
    }
}

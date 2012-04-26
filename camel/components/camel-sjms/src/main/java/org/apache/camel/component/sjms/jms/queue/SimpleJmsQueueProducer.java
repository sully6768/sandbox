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
import org.apache.camel.component.sjms.SimpleJmsEndpoint;
import org.apache.camel.component.sjms.SimpleJmsProducer;
import org.apache.camel.component.sjms.pool.QueueProducerPool;
import org.apache.camel.component.sjms.pool.SessionPool;

/**
 * The SimpleJmsQueue producer.
 */
public class SimpleJmsQueueProducer extends SimpleJmsProducer {

    private String destinationName = null;
    private SessionPool sessions;
    private QueueProducerPool producers;
    private int maxProducers = 1;

    public SimpleJmsQueueProducer(SimpleJmsEndpoint endpoint) {
        super(endpoint);
        this.destinationName = endpoint.getDestinationName();
        this.sessions = endpoint.getSessions();
        this.maxProducers = endpoint.getConfiguration().getMaxProducers();
    }

    public void process(Exchange exchange) throws Exception {
    	TextMessage textMessage = createTextMessage();
        textMessage.setText((String) exchange.getIn().getBody());
        QueueSender sender = producers.borrowObject();
        producers.returnObject(sender);
        sender.send(textMessage);
        logger.info((String) exchange.getIn().getBody());
    }
    
    private TextMessage createTextMessage() throws Exception {
    	Session s = sessions.borrowObject();
        TextMessage textMessage = s.createTextMessage();
        sessions.returnObject(s);
        return textMessage;
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        producers = new QueueProducerPool(maxProducers, sessions, destinationName);
        producers.fillPool();
    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();
        producers.drainPool();
        producers = null;
    }
}

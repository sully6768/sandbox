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

import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.sjms.SimpleJmsConsumer;
import org.apache.camel.component.sjms.pool.ConsumerPool;
import org.apache.camel.component.sjms.pool.SessionPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The SimpleJmsQueue consumer.
 */
public class SimpleJmsQueueConsumer extends SimpleJmsConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleJmsQueueConsumer.class);
    private SessionPool sessions;
    private ConsumerPool consumers;
    private int maxConsumers = 1;
    String destinationName = null;

    public SimpleJmsQueueConsumer(SimpleJmsQueueEndpoint endpoint,
            Processor processor, SessionPool sessionPool, int maxConsumers) {
        super(endpoint, processor);
        this.sessions = sessionPool;
        this.maxConsumers = maxConsumers;
        destinationName = endpoint.getEndpointUri().substring(endpoint.getEndpointUri().lastIndexOf(":"));
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        consumers = new ConsumerPool(maxConsumers, sessions, destinationName);
        for (int i = 0; i < maxConsumers; i++) {
            MessageConsumer consumer = consumers.borrowObject();
            consumer.setMessageListener(new SimpleJmsMessageListener());
        }
    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();
        consumers.destroyPool();
    }

    // @Override
    // protected int poll() throws Exception {
    // Exchange exchange = endpoint.createExchange();
    //
    // // create a message body
    // Date now = new Date();
    // exchange.getIn().setBody("Hello World! The time is " + now);
    //
    // try {
    // // send message to next processor in the route
    // getProcessor().process(exchange);
    // return 1; // number of messages polled
    // } finally {
    // // log exception if an exception occurred and was not handled
    // if (exchange.getException() != null) {
    // getExceptionHandler().handleException("Error processing exchange",
    // exchange, exchange.getException());
    // }
    // }
    // }

    public class SimpleJmsMessageListener implements MessageListener,
            ExceptionListener {

        /**
         * TODO Add Constructor Javadoc
         * 
         * @param endpoint
         */
        public SimpleJmsMessageListener() {
            super();
        }

        /**
         * TODO Add override javadoc
         * 
         * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
         * 
         * @param message
         */
        @Override
        public void onMessage(Message message) {
            TextMessage msg = (TextMessage) message;
            try {
                LOGGER.info("Message received = " + msg.getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
            Exchange exchange = getEndpoint().createExchange();

            try {
                // send message to next processor in the route
                try {
                    exchange.getIn().setBody(msg.getText() +  " How are you?");
                    getProcessor().process(exchange);
                } catch (Exception e) {
                    // e.printStackTrace();
                    exchange.setException(e);
                    // LOGGER.error("TODO Auto-generated catch block", e);
                }
            } finally {
                // log exception if an exception occurred and was not handled
                if (exchange.getException() != null) {
                    getExceptionHandler().handleException(
                            "Error processing exchange", exchange,
                            exchange.getException());
                }
            }
        }

        @Override
        public void onException(JMSException exception) {
            exception.printStackTrace();
        }

    }

}

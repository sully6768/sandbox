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
package org.apache.camel.component.sjms.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.spi.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author scenglan
 * 
 */
public class DefaultJmsMessageListener implements MessageListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultJmsMessageListener.class);
	private Endpoint endpoint;
	private ExceptionHandler exceptionHandler;
	private Processor processor;

	/**
	 * TODO Add Constructor Javadoc
	 * 
	 * @param endpoint
	 */
	public DefaultJmsMessageListener() {
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
				exchange.getIn().setBody(msg.getText() + " How are you?");
				getProcessor().process(exchange);
			} catch (Exception e) {
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

	public void setEndpoint(Endpoint endpoint) {
		this.endpoint = endpoint;
	}

	public Endpoint getEndpoint() {
		return endpoint;
	}

	public void setExceptionHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	public ExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

	public void setProcessor(Processor processor) {
		this.processor = processor;
	}

	public Processor getProcessor() {
		return processor;
	}

}
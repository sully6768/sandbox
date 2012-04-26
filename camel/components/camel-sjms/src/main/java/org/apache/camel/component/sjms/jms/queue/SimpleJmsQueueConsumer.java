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

import javax.jms.MessageConsumer;

import org.apache.camel.Processor;
import org.apache.camel.component.sjms.SimpleJmsConsumer;
import org.apache.camel.component.sjms.SimpleJmsEndpoint;
import org.apache.camel.component.sjms.jms.DefaultJmsMessageListener;
import org.apache.camel.component.sjms.pool.ConsumerPool;
import org.apache.camel.component.sjms.pool.SessionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The SimpleJmsQueue consumer.
 */
public class SimpleJmsQueueConsumer extends SimpleJmsConsumer {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SimpleJmsQueueConsumer.class);
	private SessionPool sessions;
	private ConsumerPool consumers;
	private int maxConsumers = 1;
	private String destinationName;
	private String messageSelector;

	public SimpleJmsQueueConsumer(SimpleJmsEndpoint endpoint,
			Processor processor, SessionPool sessionPool, int maxConsumers) {
		super(endpoint, processor);
		this.sessions = sessionPool;
		this.maxConsumers = maxConsumers;
		destinationName = endpoint.getEndpointUri().substring(
				endpoint.getEndpointUri().lastIndexOf(":") + 1);
	}

	@Override
	protected void doStart() throws Exception {
		super.doStart();
		consumers = new ConsumerPool(maxConsumers, sessions, destinationName);
		consumers.fillPool();
		for (int i = 0; i < maxConsumers; i++) {
			MessageConsumer consumer = consumers.borrowObject();
			DefaultJmsMessageListener listener = createMessageListener();
			if (getAsyncProcessor() != null)
				listener.setProcessor(getAsyncProcessor());
			else
				listener.setProcessor(getProcessor());

			consumer.setMessageListener(listener);
		}
	}

	/**
	 * @return
	 */
	private DefaultJmsMessageListener createMessageListener() {
		DefaultJmsMessageListener listener = new DefaultJmsMessageListener();
		listener.setEndpoint(getEndpoint());
		listener.setExceptionHandler(getExceptionHandler());
		return listener;
	}

	@Override
	protected void doStop() throws Exception {
		super.doStop();
		if (consumers != null)
			consumers.drainPool();
	}

}

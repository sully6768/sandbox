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
import org.apache.camel.component.sjms.pool.QueueConsumerPool;
import org.apache.camel.component.sjms.pool.SessionPool;

/**
 * The SimpleJmsQueue consumer.
 */
public class SimpleJmsQueueConsumer extends SimpleJmsConsumer {
	private SessionPool sessions;
	private QueueConsumerPool consumers;
	private int maxConsumers = 1;
	private String destinationName;
	private String messageSelector;

	public SimpleJmsQueueConsumer(SimpleJmsEndpoint endpoint,
			Processor processor) {
		super(endpoint, processor);
		this.sessions = endpoint.getSessions();
		this.maxConsumers = endpoint.getConfiguration().getMaxConsumers();
		destinationName = endpoint.getDestinationName();
	}

	@Override
	protected void doStart() throws Exception {
		super.doStart();
		consumers = new QueueConsumerPool(maxConsumers, sessions, destinationName, messageSelector);
		consumers.fillPool();
		for (int i = 0; i < maxConsumers; i++) {
			MessageConsumer consumer = consumers.borrowObject();
			DefaultJmsMessageListener listener = createMessageListener();
			consumer.setMessageListener(listener);
		}
	}

    @Override
    protected void doStop() throws Exception {
        super.doStop();
        if (consumers != null)
            consumers.drainPool();
    }

}

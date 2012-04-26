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
package org.apache.camel.component.sjms.pool;

import javax.jms.Queue;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;

/**
 * TODO Add Class documentation for QueueConsumerPool
 * 
 */
public class QueueConsumerPool extends ObjectPool<QueueReceiver> {

	private final SessionPool sessionPool;
	private final String destinationName;
	private final String messageSelector;

	public QueueConsumerPool(int poolSize, SessionPool sessionPool,
			String destinationName, String messageSelector) {
		super(poolSize);
		this.sessionPool = sessionPool;
		this.destinationName = destinationName;
		this.messageSelector = messageSelector;
	}

	@Override
	protected QueueReceiver createObject() throws Exception {
		QueueSession queueSession = (QueueSession) sessionPool.borrowObject();
		Queue myQueue = queueSession.createQueue(this.destinationName);
		QueueReceiver messageConsumer = null;
		if (messageSelector == null || messageSelector.equals(""))
			messageConsumer = queueSession.createReceiver(myQueue);
		else
			messageConsumer = queueSession.createReceiver(myQueue,
					this.messageSelector);
		sessionPool.returnObject(queueSession);
		return messageConsumer;
	}

	@Override
	protected void destroyObject(QueueReceiver consumer) throws Exception {
		if (consumer != null) {
			consumer.close();
		}
	}

	/**
	 * Gets the SessionPool value of sessionPool for this instance of
	 * QueueProducerPool.
	 * 
	 * @return the sessionPool
	 */
	public SessionPool getSessionPoolFactory() {
		return sessionPool;
	}
}

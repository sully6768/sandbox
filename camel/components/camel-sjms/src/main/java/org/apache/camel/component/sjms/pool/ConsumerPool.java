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

import java.util.List;

import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.QueueSession;

/**
 * TODO Add Class documentation for ConsumerPool
 *
 */
public class ConsumerPool extends ObjectPool<MessageConsumer>{

    private final SessionPool sessionPool;
    private final String destinationName;

    /**
     * TODO Add Constructor Javadoc
     *
     * @param sessionPool
     */
    public ConsumerPool(int poolSize, SessionPool sessionPool, String destinationName) {
        super(poolSize);
        this.sessionPool = sessionPool;
        this.destinationName = destinationName;
    }

    @Override
    protected MessageConsumer createObject() throws Exception {
        QueueSession queueSession = (QueueSession) sessionPool.borrowObject();
        Queue myQueue = queueSession.createQueue(this.destinationName);
        MessageConsumer messageConsumer = queueSession.createConsumer(myQueue);
        sessionPool.returnObject(queueSession);
        return messageConsumer;
    }

    @Override
    protected void destroyPoolObjects() throws Exception {
        List<MessageConsumer> list = drainObjectPool();
        for (MessageConsumer consumer : list) {
            consumer.close();
        }
    }

    /**
     * Gets the SessionPool value of sessionPool for this instance of ProducerPool.
     *
     * @return the sessionPool
     */
    public SessionPool getSessionPoolFactory() {
        return sessionPool;
    }
}

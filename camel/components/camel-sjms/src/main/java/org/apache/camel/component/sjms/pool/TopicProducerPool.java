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

import javax.jms.Topic;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

/**
 * TODO Add Class documentation for QueueProducerPool
 *
 */
public class TopicProducerPool extends ObjectPool<TopicPublisher>{
    private final SessionPool sessionPool;
    private final String destinationName;

    /**
     * TODO Add Constructor Javadoc
     *
     * @param sessionPool
     */
    public TopicProducerPool(int poolSize, SessionPool sessionPool, String destinationName, String messageSelector) {
        super(poolSize);
        this.sessionPool = sessionPool;
        this.destinationName = destinationName;
    }

    @Override
    protected TopicPublisher createObject() throws Exception {
        TopicSession topicSession = (TopicSession) sessionPool.borrowObject();
        Topic topic = topicSession.createTopic(this.destinationName);
        TopicPublisher queueSender = topicSession.createPublisher(topic);
        sessionPool.returnObject(topicSession);
        return queueSender;
    }
    
    @Override
    protected void destroyObject(TopicPublisher topicPublisher) throws Exception {
        if (topicPublisher != null) {
            topicPublisher.close();
        }
    }

    /**
     * Gets the SessionPool value of sessionPool for this instance of QueueProducerPool.
     *
     * @return the sessionPool
     */
    public SessionPool getSessionPoolFactory() {
        return sessionPool;
    }
    
}

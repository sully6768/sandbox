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
package org.apache.camel.component.sjms.jms.topic;

import javax.jms.MessageConsumer;
import javax.jms.Topic;
import javax.jms.TopicSession;

import org.apache.camel.Processor;
import org.apache.camel.component.sjms.SimpleJmsConsumer;
import org.apache.camel.component.sjms.SimpleJmsEndpoint;
import org.apache.camel.component.sjms.pool.SessionPool;

/**
 *
 */
public class SimpleJmsTopicSubscriber extends SimpleJmsConsumer {
    private MessageConsumer messageConsumer;
    private String subscriptionName;
    
    public SimpleJmsTopicSubscriber(SimpleJmsEndpoint endpoint,
            Processor processor) {
        super(endpoint, processor);
    }

    @Override
    protected void doStart() throws Exception {
        SessionPool pool = getSimpleJmsEndpoint().getSessions();
        TopicSession topicSession = (TopicSession) pool.borrowObject();
        Topic topic = topicSession.createTopic(getSimpleJmsEndpoint().getDestinationName());
        if(subscriptionName == null || subscriptionName.equals("")) {
            if (getMessageSelector() == null || getMessageSelector().equals(""))
                messageConsumer = topicSession.createSubscriber(topic);
            else
                messageConsumer = topicSession.createSubscriber(topic, this.getMessageSelector(), true);            
        } else {
            if (getMessageSelector() == null || getMessageSelector().equals(""))
                messageConsumer = topicSession.createDurableSubscriber(topic, subscriptionName);
            else
                messageConsumer = topicSession.createDurableSubscriber(topic, this.getMessageSelector(), subscriptionName, true);
        }

        pool.returnObject(topicSession);
        messageConsumer.setMessageListener(createMessageListener());
        super.doStart();
    }
    
    @Override
    protected void doStop() throws Exception {
        super.doStop();
        if(messageConsumer != null)
            messageConsumer.close();   
    }
}

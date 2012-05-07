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
package org.apache.camel.component.sjms;

import org.apache.camel.Processor;
import org.apache.camel.component.sjms.jms.DefaultJmsMessageListener;
import org.apache.camel.impl.DefaultConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The SimpleJmsConsumer consumer.
 */
public abstract class SimpleJmsConsumer extends DefaultConsumer {

    protected final transient Logger logger = LoggerFactory.getLogger(getClass());
    private String messageSelector;

    public SimpleJmsConsumer(SimpleJmsEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
    }

    /**
     * @return
     */
    protected DefaultJmsMessageListener createMessageListener() {
    	DefaultJmsMessageListener listener = new DefaultJmsMessageListener();
    	listener.setEndpoint(getEndpoint());
    	listener.setExceptionHandler(getExceptionHandler());
    	if (getAsyncProcessor() != null)
    		listener.setProcessor(getAsyncProcessor());
    	else
    		listener.setProcessor(getProcessor());
    	return listener;
    }

    protected SimpleJmsEndpoint getSimpleJmsEndpoint() {
        return (SimpleJmsEndpoint)this.getEndpoint();
    }

    /**
     * Sets the String value of messageSelector for this instance of SimpleJmsTopicSubscriber.
     *
     * @param messageSelector Sets String, default is TODO add default
     */
    public void setMessageSelector(String messageSelector) {
        this.messageSelector = messageSelector;
    }

    /**
     * Gets the String value of messageSelector for this instance of SimpleJmsTopicSubscriber.
     *
     * @return the messageSelector
     */
    public String getMessageSelector() {
        return messageSelector;
    }
}

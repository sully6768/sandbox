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

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.component.sjms.SimpleJmsComponent;
import org.apache.camel.component.sjms.SimpleJmsComponentConfiguration;
import org.apache.camel.impl.DefaultEndpoint;

/**
 * Represents a ActiveMQNoSpring endpoint.
 */
public class SimpleJmsTopicEndpoint extends DefaultEndpoint {
    private SimpleJmsComponentConfiguration configuration;
    
    public SimpleJmsTopicEndpoint() {
    }

    public SimpleJmsTopicEndpoint(String uri, SimpleJmsComponent component) {
        super(uri, component);
    }

    public Producer createProducer() throws Exception {
        return null;
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        return null;
    }

    public boolean isSingleton() {
        return true;
    }
    
    @Override
    protected void doStart() throws Exception {
        super.doStart();
    }
    
    @Override
    protected void doStop() throws Exception {
        super.doStop();
    }
}

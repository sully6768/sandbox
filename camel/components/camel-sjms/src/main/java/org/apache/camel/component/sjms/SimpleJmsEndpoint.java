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

import org.apache.camel.impl.DefaultEndpoint;

/**
 * Represents a ActiveMQNoSpring endpoint.
 */
public abstract class SimpleJmsEndpoint extends DefaultEndpoint {
    private SimpleJmsComponentConfiguration configuration;

    public SimpleJmsEndpoint() {
    }

    public SimpleJmsEndpoint(String uri, SimpleJmsComponent component) {
        super(uri, component);
    }

    public boolean isSingleton() {
        return true;
    }

    /**
     * Sets the SimpleJmsComponentConfiguration value of configuration for this
     * instance of SimpleJmsEndpoint.
     * 
     * @param configuration
     *            Sets SimpleJmsComponentConfiguration, default is TODO add
     *            default
     */
    public void setConfiguration(SimpleJmsComponentConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Gets the SimpleJmsComponentConfiguration value of configuration for this
     * instance of SimpleJmsEndpoint.
     * 
     * @return the configuration
     */
    public SimpleJmsComponentConfiguration getConfiguration() {
        return configuration;
    }
}

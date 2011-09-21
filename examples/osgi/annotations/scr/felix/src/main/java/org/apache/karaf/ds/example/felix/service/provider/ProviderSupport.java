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
package org.apache.karaf.ds.example.felix.service.provider;

import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.felix.scr.annotations.Component;
import org.apache.karaf.ds.example.felix.service.ExampleService;
import org.apache.karaf.ds.example.felix.service.component.ComponentSupport;
import org.osgi.service.log.LogService;

/**
 *
 */
@Component(componentAbstract=true)
public abstract class ProviderSupport extends ComponentSupport implements ExampleService {

    private String salutation;

    private String name;

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public void printGreetings() {
        try {
            lock.readLock().lock();
            logService.log(LogService.LOG_INFO, getMessage());
        } finally {
            lock.readLock().unlock();
        }
    }

    protected void setProperties(final Map<String, ?> properties) {
        try {
            lock.writeLock().lock();
            salutation = (String) properties.get("salutation");
            name = (String) properties.get("name");
        } finally {
            lock.writeLock().unlock();
        }
    }

    private String getMessage() {
        return salutation + " " + name;
    }

}

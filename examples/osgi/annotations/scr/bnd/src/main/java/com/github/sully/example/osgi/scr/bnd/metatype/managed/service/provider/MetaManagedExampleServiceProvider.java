/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.sully.example.osgi.scr.bnd.metatype.managed.service.provider;

import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.osgi.service.log.LogService;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Modified;
import aQute.bnd.annotation.component.Reference;
import aQute.bnd.annotation.metatype.Configurable;

import com.github.sully.example.osgi.scr.bnd.service.ExampleService;

/**
 *
 */
@Component(
        enabled = true, 
        immediate = true, 
        designateFactory = MetaManagedExampleServiceProviderConfiguration.class, 
        name = MetaManagedExampleServiceProvider.COMPONENT_NAME)
public class MetaManagedExampleServiceProvider implements ExampleService {

    public static final String COMPONENT_NAME = "com.github.sully.example.osgi.scr.bnd.metatype.managed.service.provider";

    public static final String COMPONENT_LABEL = "MetaType Managed Example Service Provider";

    private LogService logService;

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    private MetaManagedExampleServiceProviderConfiguration config;

    @Override
    public void printGreetings() {
        try {
            lock.readLock().lock();
            logService.log(LogService.LOG_INFO, getMessage());
        } finally {
            lock.readLock().unlock();
        }
    }

    private void setProperties(final Map<String, ?> properties) {
        try {
            lock.writeLock().lock();
            config = Configurable.createConfigurable(
                    MetaManagedExampleServiceProviderConfiguration.class,
                    properties);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private String getMessage() {
        return config.salutation() + " " + config.name();
    }

    @Activate
    public void activate(final Map<String, ?> properties) {
        logService
                .log(LogService.LOG_INFO, "Activating the " + COMPONENT_LABEL);
        setProperties(properties);
    }

    @Deactivate
    public void deactivate(final Map<String, ?> properties) throws Exception {
        logService.log(LogService.LOG_INFO, "Dectivating the "
                + COMPONENT_LABEL);
    }

    @Modified
    public void modified(final Map<String, ?> properties) throws Exception {
        logService
                .log(LogService.LOG_INFO, "Activating the " + COMPONENT_LABEL);
        setProperties(properties);
        printGreetings();
    }

    @Reference
    protected void setLogService(LogService logService) {
        this.logService = logService;
    }

    protected void unsetLogService(LogService logService) {
        this.logService = logService;
    }

}

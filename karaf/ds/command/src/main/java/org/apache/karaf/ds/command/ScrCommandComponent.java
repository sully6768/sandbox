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
package org.apache.karaf.ds.command;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.felix.gogo.commands.Action;
import org.apache.felix.scr.ScrService;
import org.apache.karaf.ds.command.action.ActivateComponent;
import org.apache.karaf.ds.command.action.DeactivateComponent;
import org.apache.karaf.ds.command.action.ListComponents;
import org.apache.karaf.ds.command.completer.ActivateComponentCompleter;
import org.apache.karaf.ds.command.completer.DeactivateComponentCompleter;
import org.apache.karaf.shell.console.Completer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;

/**
 *
 */
@Component(name = "ScrCommandComponent")
public class ScrCommandComponent {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ScrCommandComponent.class);

    private ScrService scrService;

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    private Map<String, ServiceRegistration> commandMap = new HashMap<String, ServiceRegistration>();

    /**
     * Called when all of the SCR Components required dependencies have been
     * satisfied
     * 
     * @param bundleContext
     * @param properties
     */
    // @SuppressWarnings({ "unchecked", "rawtypes" })
    @Activate
    public void activate(BundleContext bundleContext, Map<String, ?> properties) {
        LOGGER.info("Activating the ScrCommandComponent "
                + "(Enable Tracing to see the components activation properties)");
        traceProperties(properties);

        try {
            lock.writeLock().lock();
            createCommand(bundleContext, ListComponents.class, null);
            createCommand(bundleContext, ActivateComponent.class,
                    ActivateComponentCompleter.class);
            createCommand(bundleContext, DeactivateComponent.class,
                    DeactivateComponentCompleter.class);

        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Called when any of the SCR Components required dependencies become
     * unsatisfied
     * 
     * @param bundleContext
     * @param componentContext
     * @param properties
     */
    @Deactivate
    public void deactivate() {
        LOGGER.info("Deactivating the ScrCommandComponent");
        Set<String> keys = commandMap.keySet();
        for (String key : keys) {
            try {
                lock.writeLock().lock();
                ServiceRegistration sr = commandMap.get(key);
                sr.unregister();
            } finally {
                lock.writeLock().unlock();
            }
        }
    }

    @Reference
    public void setScrService(ScrService scrService) {
        this.scrService = scrService;
    }

    public void unsetScrService(ScrService scrService) {
        this.scrService = null;
    }

    private void createCommand(BundleContext bundleContext,
            Class<? extends Action> actionClass,
            Class<? extends Completer> completerClass) {
        ServiceRegistration scrList = ScrCommand.export(bundleContext,
                actionClass, completerClass, scrService);
        commandMap.put(ListComponents.class.getName(), scrList);
    }

    /**
     * Helper method that outputs the properties associated with this component
     * 
     * @param properties
     */
    private void traceProperties(Map<String, ?> properties) {
        if (LOGGER.isTraceEnabled()) {
            SortedSet<String> keys = new TreeSet<String>(properties.keySet());
            LOGGER.trace("  Component Properties :");
            for (String key : keys) {
                LOGGER.trace("    " + key + " = " + properties.get(key));
            }
        }
    }

}

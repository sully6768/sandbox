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
package org.apache.karaf.ds.management.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

import org.apache.felix.scr.Component;
import org.apache.felix.scr.ScrService;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.karaf.ds.management.ScrServiceMBean;
import org.apache.karaf.management.MBeanRegistrer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
@org.apache.felix.scr.annotations.Component(
        name = ScrServiceMBeanImpl.COMPONENT_NAME, 
        label = ScrServiceMBeanImpl.COMPONENT_LABEL, 
        enabled = true, 
        immediate = true)
public class ScrServiceMBeanImpl extends StandardMBean implements
        ScrServiceMBean {

    public static final String COMPONENT_NAME = "ScrServiceMBean";

    public static final String COMPONENT_LABEL = "Apache Karaf SCR Service MBean";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ScrServiceMBeanImpl.class);

    @Reference
    private MBeanServer mBeanServer;

    @Reference
    private ScrService scrService;

    private MBeanRegistrer mBeanRegistrer;

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Creates new Declarative Services mbean.
     * 
     * @throws NotCompliantMBeanException
     */
    public ScrServiceMBeanImpl() throws NotCompliantMBeanException {
        super(ScrServiceMBean.class);
    }

    @Activate
    public void activate() throws Exception {
        LOGGER.info("Activating the " + getComponentLabel());
        Map<Object, String> mbeans = new HashMap<Object, String>();
        String karafName = System.getProperty("karaf.name", "root");
        mbeans.put(this, "org.apache.karaf:type=scr,name=" + karafName);
        try {
            lock.writeLock().lock();
            mBeanRegistrer = new MBeanRegistrer();
            mBeanRegistrer.setMbeans(mbeans);
            mBeanRegistrer.registerMBeanServer(mBeanServer);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Deactivate
    public void deactivate() throws Exception {
        LOGGER.info("Deactivating the " + getComponentLabel());
        try {
            lock.writeLock().lock();
            mBeanRegistrer.unregisterMBeanServer(mBeanServer);
            mBeanRegistrer = null;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Overrides the super method noted below. See super documentation for
     * details.
     * 
     * @see org.apache.karaf.ds.management.ScrServiceMBean#listComponents()
     */
    public String[] listComponents() throws Exception {
        Component[] components = scrService.getComponents();
        String[] componentNames = new String[components.length];
        for (int i = 0; i < componentNames.length; i++) {
            componentNames[i] = components[i].getName();
        }
        return componentNames;
    }

    /**
     * Overrides the super method noted below. See super documentation for
     * details.
     * 
     * @see org.apache.karaf.ds.management.ScrServiceMBean#activateComponent(java.lang.String)
     */
    public void activateComponent(String componentName) throws Exception {
        if (scrService.getComponents(componentName) != null) {
            Component[] components = scrService.getComponents(componentName);
            for (Component component : components) {
                component.enable();
            }
        }
    }

    /**
     * Overrides the super method noted below. See super documentation for
     * details.
     * 
     * @see org.apache.karaf.ds.management.ScrServiceMBean#deactiveateComponent(java.lang.String)
     */
    public void deactiveateComponent(String componentName) throws Exception {
        if (scrService.getComponents(componentName) != null) {
            Component[] components = scrService.getComponents(componentName);
            for (Component component : components) {
                component.disable();
            }
        }
    }
    
    public String getComponentLabel() {
        return COMPONENT_LABEL;
    }

}

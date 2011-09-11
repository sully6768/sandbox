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
package org.apache.karaf.ds.example.felix.service.component;

import java.util.Map;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Modified;
import org.apache.felix.scr.annotations.Reference;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;

/**
 * 
 */
@Component(componentAbstract = true)
public abstract class ComponentSupport {

    @Reference
    protected LogService logService;

    /**
     * Called when any of the SCR Components required dependencies become
     * unsatisfied
     * 
     * @param properties
     */
    @Activate
    public void activate(final BundleContext bundleContext,
            final ComponentContext componentContext,
            final Map<String, ?> properties) throws Exception {
        logService.log(LogService.LOG_INFO, "Activating the " + getLabel());
        doActivate(properties);
    }

    /**
     * Called when all of the SCR Components required dependencies have been
     * satisfied
     * 
     * @param properties
     */
    @Deactivate
    public void deactivate(final Map<String, ?> properties) throws Exception {
        logService.log(LogService.LOG_INFO, "Dectivating the " + getLabel());
        doDeactivate(properties);
    }

    /**
     * Called when the configurations that backs this SCR Components has been
     * modified
     * 
     * @param properties
     */
    @Modified
    public void modified(final Map<String, ?> properties) throws Exception {
        logService.log(LogService.LOG_INFO, "Modifying the " + getLabel());
        doModified(properties);
    }

    /**
     * @param properties
     */
    protected void doActivate(final Map<String, ?> properties) throws Exception {

    }

    /**
     * @param properties
     */
    protected void doDeactivate(final Map<String, ?> properties) throws Exception {

    }

    /**
     * @param properties
     */
    protected void doModified(final Map<String, ?> properties) throws Exception {

    }

    /**
     * @return
     */
    public abstract String getLabel();

}

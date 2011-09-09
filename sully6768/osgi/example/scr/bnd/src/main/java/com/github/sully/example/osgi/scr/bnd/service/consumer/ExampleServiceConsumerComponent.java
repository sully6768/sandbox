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
package com.github.sully.example.osgi.scr.bnd.service.consumer;

import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;

import com.github.sully.example.osgi.scr.bnd.service.ExampleService;

/**
 * 
 */
@Component(
        enabled = true, 
        immediate = true, 
        name = ExampleServiceConsumerComponent.COMPONENT_NAME)
public class ExampleServiceConsumerComponent {

    public static final String COMPONENT_NAME = "com.github.sully.example.osgi.scr.bnd.service.consumer";

    public static final String COMPONENT_LABEL = "Example Service Consumer Component";

    private LogService logService;

    private ExampleService exampleService;

    /**
     * Called when all of the SCR Components required dependencies have been
     * satisfied
     * 
     * @param bundleContext
     * @param componentContext
     * @param properties
     */
    @Activate
    public void activate(final BundleContext bundleContext,
            final ComponentContext componentContext,
            final Map<String, ?> properties) {
        logService
                .log(LogService.LOG_INFO, "Activating the " + COMPONENT_LABEL);
        exampleService.printGreetings();
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
    public void deactivate(final BundleContext bundleContext,
            final ComponentContext componentContext,
            final Map<String, ?> properties) {
        logService.log(LogService.LOG_INFO, "Dectivating the "
                + COMPONENT_LABEL);
    }

    @Reference(target = "(component.name=com.github.sully.example.osgi.scr.bnd.service.provider.ExampleServiceProvider)")
    public void setExampleService(final ExampleService exampleService) {
        this.exampleService = exampleService;
    }

    public void unsetExampleService(final ExampleService exampleService) {
        this.exampleService = null;
    }

    @Reference
    protected void setLogService(LogService logService) {
        this.logService = logService;
    }

    protected void unsetLogService(LogService logService) {
        this.logService = logService;
    }
}

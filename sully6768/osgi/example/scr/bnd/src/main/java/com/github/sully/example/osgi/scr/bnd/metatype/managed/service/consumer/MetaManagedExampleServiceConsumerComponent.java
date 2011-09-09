package com.github.sully.example.osgi.scr.bnd.metatype.managed.service.consumer;

import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;

import com.github.sully.example.osgi.scr.bnd.service.ExampleService;


@Component(
        enabled = true, 
        immediate = true, 
        name = MetaManagedExampleServiceConsumerComponent.COMPONENT_NAME)
public class MetaManagedExampleServiceConsumerComponent {

    public static final String COMPONENT_NAME = "com.github.sully.example.osgi.scr.bnd.metatype.managed.service.consumer";

    public static final String COMPONENT_LABEL = "MetaType Managed Example Service Consumer Component";

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

    @Reference(target = "(component.name=com.github.sully.example.osgi.scr.bnd.metatype.managed.service.provider)")
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

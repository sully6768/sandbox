/**
 * 
 */
package org.apache.karaf.ds.example.component.service.consumer;

import java.util.Map;

import org.apache.karaf.ds.example.common.Tracing;
import org.apache.karaf.ds.example.service.ExampleService;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;

/**
 * @author sully6768
 * 
 */
@Component(enabled = true, immediate = true, name = "org.apache.karaf.ds.example.component.service.consumer")
public class ExampleServiceConsumerComponent {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ExampleServiceConsumerComponent.class);

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
        LOGGER.info("Activating the "
                + getClass().getSimpleName()
                + "(Enable Tracing to see the components activation properties)");
        Tracing.traceProperties(LOGGER, properties);
        try {
            exampleService.printGreetings();
        } catch (final Exception e) {
            LOGGER.error("Exception: " + e.getLocalizedMessage(), e);
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
    public void deactivate(final BundleContext bundleContext,
            final ComponentContext componentContext,
            final Map<String, ?> properties) {
        LOGGER.info("Deactivating the "
                + getClass().getSimpleName()
                + "(Enable Tracing to see the components deactivation properties)");
        Tracing.traceProperties(LOGGER, properties);
        try {
            exampleService.stopPrintingGreetings();
        } catch (final Exception e) {
            LOGGER.error("Exception: " + e.getLocalizedMessage(), e);
        }
    }

    @Reference(target = "(component.name=org.apache.karaf.ds.example.service.provider.ExampleServiceProvider)")
    public void setExampleService(final ExampleService exampleService) {
        this.exampleService = exampleService;
    }

    public void unsetExampleService(final ExampleService exampleService) {
        this.exampleService = null;
    }
}

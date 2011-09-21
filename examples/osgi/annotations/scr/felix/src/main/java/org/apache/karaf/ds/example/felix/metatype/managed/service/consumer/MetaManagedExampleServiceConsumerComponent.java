package org.apache.karaf.ds.example.felix.metatype.managed.service.consumer;

import java.util.Map;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.karaf.ds.example.felix.metatype.managed.service.provider.MetaManagedExampleServiceProvider;
import org.apache.karaf.ds.example.felix.service.ExampleService;
import org.apache.karaf.ds.example.felix.service.component.ComponentSupport;
import org.osgi.service.log.LogService;


@Component(
        enabled = true, 
        immediate = true, 
        name = MetaManagedExampleServiceConsumerComponent.COMPONENT_NAME)
public class MetaManagedExampleServiceConsumerComponent extends ComponentSupport {

    public static final String COMPONENT_NAME = "org.apache.karaf.ds.example.felix.metatype.managed.service.consumer";

    public static final String COMPONENT_LABEL = "MetaType Managed Example Service Consumer Component";

    @Reference(target = "(component.name="
            + MetaManagedExampleServiceProvider.COMPONENT_NAME + ")")
    private ExampleService exampleService;

    @Override
    protected void doActivate(Map<String, ?> properties) throws Exception {
        logService
                .log(LogService.LOG_INFO, "Activating the " + COMPONENT_LABEL);
        exampleService.printGreetings();
    }

    @Override
    protected void doDeactivate(Map<String, ?> properties) throws Exception {
        logService.log(LogService.LOG_INFO, "Dectivating the "
                + COMPONENT_LABEL);
    }

    @Override
    public String getLabel() {
        return COMPONENT_LABEL;
    }
}

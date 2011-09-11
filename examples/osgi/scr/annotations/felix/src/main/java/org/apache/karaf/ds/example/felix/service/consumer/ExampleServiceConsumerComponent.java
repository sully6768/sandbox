/**
 * 
 */
package org.apache.karaf.ds.example.felix.service.consumer;

import java.util.Map;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.karaf.ds.example.felix.service.ExampleService;
import org.apache.karaf.ds.example.felix.service.component.ComponentSupport;
import org.osgi.service.log.LogService;

/**
 * @author sully6768
 * 
 */
@Component(
        enabled = true, 
        immediate = true, 
        name = ExampleServiceConsumerComponent.COMPONENT_NAME)
public class ExampleServiceConsumerComponent extends ComponentSupport {
    
    public static final String COMPONENT_NAME = "org.apache.karaf.ds.example.felix.service.consumer";
    public static final String COMPONENT_LABEL = "Example Service Consumer Component";

    @Reference(target = "(component.name=org.apache.karaf.ds.example.felix.service.provider.ExampleServiceProvider)")
    private ExampleService exampleService;
    
    @Override
    protected void doActivate(Map<String, ?> properties) throws Exception {
        logService.log(LogService.LOG_INFO, "Activating the " + COMPONENT_LABEL);
        exampleService.printGreetings();
    }
    
    
    @Override
    public void doDeactivate(final Map<String, ?> properties) throws Exception {
        logService.log(LogService.LOG_INFO, "Dectivating the " + COMPONENT_LABEL);
    }

    @Override
    public String getLabel() {
        return COMPONENT_LABEL;
    }

}

/**
 * 
 */
package org.apache.karaf.ds.example.managed.provider.meta;

import java.util.Map;

import org.apache.karaf.ds.example.common.PrintActor;
import org.apache.karaf.ds.example.managed.provider.ManagedExampleServiceProvider;
import org.apache.karaf.ds.example.service.ExampleService;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Modified;
import aQute.bnd.annotation.metatype.Configurable;
import aQute.bnd.annotation.metatype.Meta;

/**
 *
 */
@Component(enabled = true, immediate = true, designateFactory = MetaManagedExampleServiceProviderConfiguration.class, name = MetaManagedExampleServiceProvider.COMPONENT_NAME)
@Meta.OCD(id = ManagedExampleServiceProvider.COMPONENT_NAME, name = MetaManagedExampleServiceProvider.CONFIG_NAME)
public class MetaManagedExampleServiceProvider implements ExampleService {

    public static final String COMPONENT_NAME = "org.apache.karaf.ds.example.managed.provider.meta";

    public static final String CONFIG_NAME = "MetaManagedExampleServiceProvider";

    private MetaManagedExampleServiceProviderConfiguration config;

    private Thread printer;

    private PrintActor pw;

    @Activate
    public void activate(final Map<String, ?> properties) {
        config = Configurable.createConfigurable(
                MetaManagedExampleServiceProviderConfiguration.class,
                properties);
    }

    @Deactivate
    public void deactivate(final Map<String, ?> properties) throws Exception {
        stopPrintingGreetings();
        config = null;
    }

    @Modified
    public void modified(final Map<String, ?> properties) throws Exception {

        stopPrintingGreetings();
        config = Configurable.createConfigurable(
                MetaManagedExampleServiceProviderConfiguration.class,
                properties);
        printGreetings();
    }

    @Override
    public void printGreetings() throws Exception {
        pw = new PrintActor();
        pw.setMessage(getMessage());
        pw.setMaxTimesToPrint(10);
        pw.setLoopDelay(1000);
        printer = new Thread(pw, "PrintActor");
        printer.start();
    }

    @Override
    public void stopPrintingGreetings() throws Exception {
        pw.setPrinting(false);
        printer = null;
    }

    public String getMessage() {
        return config.salutation() + " " + config.name();
    }

}

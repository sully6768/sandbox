/**
 * 
 */
package org.apache.karaf.ds.example.managed.provider;

import java.util.Map;

import org.apache.karaf.ds.example.common.PrintActor;
import org.apache.karaf.ds.example.service.ExampleService;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.ConfigurationPolicy;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Modified;
import aQute.bnd.annotation.metatype.Meta.OCD;

/**
 *
 */
@Component(configurationPolicy = ConfigurationPolicy.require, name = ManagedExampleServiceProvider.COMPONENT_NAME)
@OCD(name = "ManagedExampleServiceProvider")
public class ManagedExampleServiceProvider implements ExampleService {

    public static final String COMPONENT_NAME = "org.apache.karaf.ds.example.managed.provider";

    private String salutation;

    private String name;

    private Thread printer;

    private PrintActor pw;

    @Activate
    public void activate(final Map<String, ?> properties) {
        salutation = (String) properties.get("salutation");
        name = (String) properties.get("name");
    }

    @Deactivate
    public void deactivate(final Map<String, ?> properties) throws Exception {
        stopPrintingGreetings();
    }

    @Modified
    public void modified(final Map<String, ?> properties) throws Exception {
        stopPrintingGreetings();
        salutation = (String) properties.get("salutation");
        name = (String) properties.get("name");
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

    }

    public String getMessage() {
        return salutation + " " + name;
    }

}

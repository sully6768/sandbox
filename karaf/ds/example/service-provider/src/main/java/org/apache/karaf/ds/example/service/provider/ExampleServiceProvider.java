/**
 * 
 */
package org.apache.karaf.ds.example.service.provider;

import org.apache.karaf.ds.example.common.PrintActor;
import org.apache.karaf.ds.example.service.ExampleService;

import aQute.bnd.annotation.component.Component;

/**
 *
 */
@Component
public class ExampleServiceProvider implements ExampleService {

    private String salutation;

    private String name;

    private Thread printer;

    private PrintActor pw;

    @Override
    public void printGreetings() throws Exception {
        this.setName("Doug");
        this.setSalutation("Hello");
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

    public void setSalutation(final String salutation) {
        this.salutation = salutation;
    }

    public void setName(final String name) {
        this.name = name;
    }

}

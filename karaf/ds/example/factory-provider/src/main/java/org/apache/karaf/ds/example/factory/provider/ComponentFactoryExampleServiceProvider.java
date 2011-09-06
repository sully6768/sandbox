/**
 * 
 */
package org.apache.karaf.ds.example.factory.provider;

import org.apache.karaf.ds.example.service.ExampleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aQute.bnd.annotation.component.Component;


/**
 *
 */
@Component
public class ComponentFactoryExampleServiceProvider implements ExampleService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ComponentFactoryExampleServiceProvider.class);

	private String salutation;
	private String name;
	private Thread printer;
	private PrintWorker pw;

	@Override
	public void printGreetings() throws Exception {
		this.setName("Doug");
		this.setSalutation("Hello");
		pw = new PrintWorker();
		pw.setMessage(getMessage());
		pw.setMaxTimesToPrint(10);
		pw.setLoopDelay(1000);
		printer = new Thread(pw, "PrintWorker");
		printer.start();
	}

	@Override
	public void stopPrintingGreetings() throws Exception {
		pw.setPrinting(true);

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

	private class PrintWorker implements Runnable {
		private long maxTimesToPrint = 1;
		private long loopDelay = 5000;
		private boolean printing = true;
		private String message = "No Message Here";

		@Override
		public void run() {
			for (int i = 0; i < getMaxTimesToPrint() && isPrinting(); i++) {
				LOGGER.info(getMessage());
				try {
					Thread.sleep(getLoopDelay());
				} catch (final InterruptedException e) {
					// ignore
				}
			}
		}

		public boolean isPrinting() {
			return printing;
		}

		public void setPrinting(final boolean printing) {
			this.printing = printing;
		}

		public long getMaxTimesToPrint() {
			return maxTimesToPrint;
		}

		public void setMaxTimesToPrint(final long timesToPrint) {
			this.maxTimesToPrint = timesToPrint;
		}

		public long getLoopDelay() {
			return loopDelay;
		}

		public void setLoopDelay(final long loopDelay) {
			this.loopDelay = loopDelay;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(final String message) {
			this.message = message;
		}
	}

}

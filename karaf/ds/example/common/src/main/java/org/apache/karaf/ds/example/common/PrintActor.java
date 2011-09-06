package org.apache.karaf.ds.example.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintActor implements Runnable {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PrintActor.class);

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

package org.apache.karaf.ds.example.common;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;

public class Tracing {

	/**
	 * Helper method that outputs the properties associated with this component
	 * 
	 * @param properties
	 */
	public static void traceProperties(final Logger log,
			final Map<String, ?> properties) {
		if (log.isTraceEnabled()) {
			final SortedSet<String> keys = new TreeSet<String>(
					properties.keySet());
			log.trace("  Component Properties :");
			for (final String key : keys) {
				log.trace("    " + key + " = " + properties.get(key));
			}
		}
	}
}

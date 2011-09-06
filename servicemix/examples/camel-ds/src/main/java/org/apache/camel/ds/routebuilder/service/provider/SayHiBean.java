/**
 * 
 */
package org.apache.camel.ds.routebuilder.service.provider;

/**
 * @author sully6768
 *
 */
public class SayHiBean {
	public String sayHi(String inMessage) {
		return inMessage.concat(".  Saying Hi to Scott");
	}
}

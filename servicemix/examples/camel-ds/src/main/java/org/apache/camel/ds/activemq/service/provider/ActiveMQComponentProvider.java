/**
 * 
 */
package org.apache.camel.ds.activemq.service.provider;

import java.util.Map;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.Component;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Modified;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 */
@org.apache.felix.scr.annotations.Component(
        name = ActiveMQComponentProvider.COMPONENT_NAME, 
        label = ActiveMQComponentProvider.COMPONENT_LABEL, 
        enabled = true, 
        immediate = true, 
        metatype = true)
@Service(
        value = {Component.class})
@Properties({
        @Property(
                name = "camel.component.name", 
                value = { "activemq"}, 
                propertyPrivate = true)})
public class ActiveMQComponentProvider extends ActiveMQComponent {

    public static final String COMPONENT_NAME = "ActiveMQComponentProvider";
    public static final String COMPONENT_LABEL = "Apache ServiceMix DS Camel ActiveMQ Component";
    
	private static final transient Logger LOG = LoggerFactory
			.getLogger(ActiveMQComponentProvider.class);

	/**
	 * 
	 */
	public ActiveMQComponentProvider() {

	}

	@Activate
	public void activate(Map<?, ?> properties) {
		LOG.info("Activating the ActiveMQComponentProvider");
		ActiveMQConnectionFactory acf = new ActiveMQConnectionFactory();
		acf.setBrokerURL("tcp://localhost:61616");
		PooledConnectionFactory pcf = new PooledConnectionFactory();
		pcf.setMaxConnections(8);
		pcf.setMaximumActive(200);
		pcf.setConnectionFactory(acf);
		JmsConfiguration jc = new JmsConfiguration(pcf);
		this.setConfiguration(jc);

	}

	@Deactivate
	public void deactivate(Map<?, ?> properties) {
		LOG.info("Deactivating the ActiveMQComponentProvider");
	}

	@Modified
	public void modified(Map<?, ?> properties) {
		LOG.info("Modifying the ActiveMQComponentProvider");
	}

}

/**
 * 
 */
package org.apache.camel.ds.demo.component;

import java.util.Map;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.ds.producer.service.ProducerTemplateService;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Modified;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.References;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sully6768
 * 
 */
@Component
@References({
    @Reference(
            bind="setProducerTemplateService",
            unbind="unsetProducerTemplateService",
            referenceInterface=ProducerTemplateService.class)
})
public class DemoComponentProvider {

	private ProducerTemplate producerTemplate;

	private static final transient Logger LOG = LoggerFactory
			.getLogger(DemoComponentProvider.class);

	@Activate
	public void activate(Map<?, ?> properties) {
		LOG.info("Activating the DemoComponentProvider");
		for (int i = 0; i < 10; ++i) {
			producerTemplate.sendBody("activemq:route.builder.2.in",
					"Hello World Message " + (i + 1));
		}
	}

	@Deactivate
	public void deactivate(Map<?, ?> properties) {
		LOG.info("Deactivating the DemoComponentProvider");
	}

	@Modified
	public void modified(Map<?, ?> properties) {
		LOG.info("Modifying the DemoComponentProvider");
	}

	public void setProducerTemplateService(
			ProducerTemplateService producerTemplateService) {
		producerTemplate = producerTemplateService.getInstance();
	}

	public void unsetProducerTemplateService(
			ProducerTemplateService producerTemplateService) {
		producerTemplate = null;
	}

}

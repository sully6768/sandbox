package org.apache.camel.ds.producer.service.provider;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.ds.producer.service.ProducerTemplateService;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.References;
import org.apache.felix.scr.annotations.Service;

@Component
@Service(
        value = {ProducerTemplateService.class})
@References({
    @Reference(
            bind="setCamelContext",
            unbind="unsetCamelContext",
            cardinality=ReferenceCardinality.OPTIONAL_MULTIPLE,
            policy=ReferencePolicy.DYNAMIC,
            referenceInterface=CamelContext.class)
})
public class ProducerTemplateServiceProvider implements
		ProducerTemplateService {

	private CamelContext camelContext;

	public ProducerTemplate getInstance() {
		return camelContext.createProducerTemplate();
	}

	public void setCamelContext(CamelContext camelContext, Map<?, ?> properties) {
		this.camelContext = camelContext;
	}

	public void unsetCamelContext(CamelContext camelContext,
			Map<?, ?> properties) {
		this.camelContext = null;
	}

}

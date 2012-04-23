/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.ds.context.service.provider;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.core.osgi.OsgiDefaultCamelContext;
import org.apache.camel.ds.activemq.service.provider.ActiveMQComponentProvider;
import org.apache.camel.ds.producer.service.ProducerTemplateService;
import org.apache.camel.model.RouteDefinition;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.References;
import org.apache.felix.scr.annotations.Service;

import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sully6768
 * 
 */
@org.apache.felix.scr.annotations.Component(
        name = ActiveMQComponentProvider.COMPONENT_NAME, 
        label = ActiveMQComponentProvider.COMPONENT_LABEL, 
        enabled = true, 
        immediate = true, 
        metatype = true)
        
// TODO REMOVE THE CONTEXT
@Service(
        value = {CamelContext.class, ProducerTemplateService.class})
@Properties({
        @Property(
                name = "camel.component.name", 
                value = { "activemq"}, 
                propertyPrivate = true)})
@References({
    @Reference(
            bind="bindComponent",
            unbind="unbindComponent",
            cardinality=ReferenceCardinality.OPTIONAL_MULTIPLE,
            policy=ReferencePolicy.DYNAMIC,
            referenceInterface=Component.class),
    @Reference(
            bind="bindRoutesBuilder",
            unbind="unbindRoutesBuilder",
            cardinality=ReferenceCardinality.OPTIONAL_MULTIPLE,
            policy=ReferencePolicy.DYNAMIC,
            referenceInterface=RoutesBuilder.class)
})
public class CamelContextServcieProvider extends OsgiDefaultCamelContext
        implements ProducerTemplateService {

    private static final transient Logger LOG = LoggerFactory
            .getLogger(CamelContextServcieProvider.class);

    private static final String COMPONENT_ID_KEY = "component.id";
    private static final String COMPONENT_NAME_KEY = "component.name";

    private ReadWriteLock rwl = new ReentrantReadWriteLock(true);

    /**
     * 
     */
    public CamelContextServcieProvider() {
        // Remember, this is new if it breaks.
        super(FrameworkUtil.getBundle(CamelContextServcieProvider.class)
                .getBundleContext());
        setName("scr-camel-context");
    }

    @Activate
    public void activate(Map<?, ?> properties) {
        LOG.info("Activating the CamelContextServcieProvider");
        setName("ds.camel.context");
        try {
            rwl.readLock().lock();
            if (this.isStarted() || this.isStarting()) {
                LOG.info("CamelContext is started don't start!!!");
            } else {
                LOG.info("Starting CamelContext");
                this.start();
            }
        } catch (Exception e) {
            LOG.error("Exception Activating the CamelContextServcieProvider", e);
        } finally {
            rwl.readLock().unlock();
        }
    }

    @Deactivate
    public void deactivate(Map<?, ?> properties) {
        LOG.info("Deactivating the CamelContextServcieProvider");

        try {
            rwl.readLock().lock();
            this.stop();
        } catch (Exception e) {
            LOG.error("Exception Deactivating the CamelContextServcieProvider",
                    e);
        } finally {
            rwl.readLock().unlock();
        }
    }

    // @Modified
    // public void modified(Map<?, ?> properties) {
    // LOG.info("Modifying the CamelContextServcieProvider");
    // try {
    // rwl.writeLock().lock();
    // this.start();
    // this.stop();
    // } catch (Exception e) {
    // e.printStackTrace();
    // } finally {
    // rwl.writeLock().unlock();
    // }
    // }

    public void bindComponent(Component component, Map<String, ?> properties) {
        String id = (String) properties.get("camel.component.name");

        LOG.info("Binding Component ID(" + id + ") to the Camel Context");

        this.addComponent(id, component);
    }

    public void unbindComponent(Component component, Map<?, ?> properties) {
        String id = (String) properties.get("camel.component.name");
        LOG.info("Unbinding Component ID(" + id + ") to the Camel Context");
        this.removeComponent(id);
    }

    /**
     * @param routesBuilder
     * @param properties
     */
    public void bindRoutesBuilder(RoutesBuilder routesBuilder,
            Map<?, ?> properties) {

        try {
            rwl.writeLock().lock();

            LOG.info("Binding RoutesBuilder ID to the Camel Context");

            routesBuilder.addRoutesToCamelContext(this);

            List<RouteDefinition> routes = ((RouteBuilder) routesBuilder)
                    .getRouteCollection().getRoutes();

            if (LOG.isDebugEnabled()) {
                for (RouteDefinition definition : routes) {
                    LOG.debug("Adding Route: " + definition.getId());
                }
            }

        } catch (Exception e) {
            errorMessageLogger(
                    "Error unbinding RoutesBuilder routes from the CamelContextService",
                    properties, e);
        } finally {
            rwl.writeLock().unlock();
        }
    }

    public void unbindRoutesBuilder(RoutesBuilder routesBuilder,
            Map<?, ?> properties) {
        LOG.info("Unbinding RouteBuilder ID(" + routesBuilder
                + ") to the Camel Context");
        try {
            rwl.writeLock().lock();
            List<RouteDefinition> routes = ((RouteBuilder) routesBuilder)
                    .getRouteCollection().getRoutes();
            for (RouteDefinition definition : routes) {
                LOG.info("Removing Route: " + definition.getId());
                stopRoute(definition);
                removeRouteDefinition(definition);
            }
        } catch (Exception e) {
            errorMessageLogger(
                    "Error unbinding RoutesBuilder routes from the CamelContextService",
                    properties, e);
        } finally {
            rwl.writeLock().unlock();
        }
    }

    // @Reference(optional = true)
    // public void bindEndpoint(Endpoint endpoint, Map<?, ?> properties) {
    // String id = (String) properties.get("id");
    // LOG.info("Binding Endpoint ID(" + id + ") to the Camel Context");
    // try {
    // this.addEndpoint(id, endpoint);
    // } catch (Exception e) {
    // LOG.error("Error binding Endpoint ID(" + id + ")", e);
    // }
    // }
    //
    // public void unbindEndpoint(Endpoint endpoint, Map<?, ?> properties) {
    // String id = (String) properties.get("id");
    // LOG.info("Unbinding Endpoint ID(" + id + ") to the Camel Context");
    // try {
    // this.removeEndpoints("endpointName");
    // } catch (Exception e) {
    // LOG.error("Error unbinding Endpoint ID(" + id + ")", e);
    // }
    // }

    /**
     * @param message
     * @param properties
     * @param exception
     */
    private void errorMessageLogger(String message, Map<?, ?> properties,
            Exception exception) {
        LOG.error(message);
        LOG.error("  Component ID   : " + properties.get(COMPONENT_ID_KEY));
        LOG.error("  Component Name : " + properties.get(COMPONENT_NAME_KEY));
        LOG.error("  Error Message  : " + exception.getMessage(), exception);
    }

    public ProducerTemplate getInstance() {
        return createProducerTemplate();
    }

}

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
package org.apache.karaf.ds.example.component;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;

import org.apache.karaf.ds.example.component.service.consumer.ExampleServiceConsumerComponent;
import org.apache.karaf.ds.example.service.ExampleService;
import org.junit.Test;

/**
 * 
 */
public class PfmServiceConsumerComponentTest {

    /**
     * Test method for
     * {@link org.apache.karaf.ds.example.component.service.consumer.ExampleServiceConsumerComponent#activate(org.osgi.framework.BundleContext, org.osgi.service.component.ComponentContext, java.util.Map)}
     * and
     * {@link org.apache.karaf.ds.example.component.service.consumer.ExampleServiceConsumerComponent#deactivate(org.osgi.framework.BundleContext, org.osgi.service.component.ComponentContext, java.util.Map)}
     * .
     */
    // @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void testActivate() {
        // BundleContext bundleContext = mock(BundleContext.class);
        // ComponentContext componentContext = mock(ComponentContext.class);
        // Map properties = new HashMap();
        // properties.put("component.id", "21");
        // properties.put("component.name",
        // "org.apache.karaf.ds.example.component");
        // ExampleService pfmService = mock(ExampleService.class);
        // when(pfmService.createGreetings("Scott")).thenReturn("Hello Scott");
        // ExampleServiceConsumerComponent tcc = new ExampleServiceConsumerComponent();
        // tcc.setPfmService(pfmService);
        // tcc.activate(bundleContext, componentContext, properties);
        // verify(pfmService).createGreetings("Scott");
        //
        // tcc.deactivate(bundleContext, componentContext, properties);
        // verify(pfmService).createGreetings("Scott");
    }

    /**
     * Test method for
     * {@link org.apache.karaf.ds.example.component.service.consumer.ExampleServiceConsumerComponent#setPfmService(org.apache.karaf.ds.example.service.ExampleService)}
     * and
     * {@link org.apache.karaf.ds.example.component.service.consumer.ExampleServiceConsumerComponent#unsetPfmService(org.apache.karaf.ds.example.service.ExampleService)}
     * .
     * 
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    @Test
    public void testSetScrService() throws SecurityException,
            NoSuchFieldException, IllegalArgumentException,
            IllegalAccessException {
        ExampleServiceConsumerComponent tcc = new ExampleServiceConsumerComponent();
        ExampleService exampleService = mock(ExampleService.class);
        assertNotNull(exampleService);
        tcc.setExampleService(exampleService);
        Field pfmServiceField = tcc.getClass().getDeclaredField(
                "exampleService");
        assertNotNull(pfmServiceField);

        pfmServiceField.setAccessible(true);
        Object pfmServiceFieldValue = pfmServiceField.get(tcc);
        assertNotNull(pfmServiceFieldValue);

        tcc.unsetExampleService(exampleService);
        pfmServiceFieldValue = pfmServiceField.get(tcc);
        assertNull(pfmServiceFieldValue);

    }

}

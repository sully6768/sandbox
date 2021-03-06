/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.karaf.ds.example.felix.managed.service.provider;

import java.util.Map;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.karaf.ds.example.felix.service.ExampleService;
import org.apache.karaf.ds.example.felix.service.provider.ProviderSupport;
import org.osgi.service.log.LogService;

/**
 *
 */
@Component(
        policy = ConfigurationPolicy.REQUIRE, 
        name = ManagedExampleServiceProvider.COMPONENT_NAME)
public class ManagedExampleServiceProvider extends ProviderSupport implements ExampleService {

    public static final String COMPONENT_NAME = "org.apache.karaf.ds.example.felix.managed.service.provider";
    public static final String COMPONENT_LABEL = "Managed Example Service Provider";

    @Override
    protected void doActivate(Map<String, ?> properties) throws Exception {
        logService.log(LogService.LOG_INFO, "Activating the " + COMPONENT_LABEL);
        setProperties(properties);
    }

    @Override
    protected void doDeactivate(final Map<String, ?> properties) throws Exception {
        logService.log(LogService.LOG_INFO, "Dectivating the " + COMPONENT_LABEL);
    }

    @Override
    protected void doModified(Map<String, ?> properties) throws Exception {
        logService.log(LogService.LOG_INFO, "Activating the " + COMPONENT_LABEL);
        setProperties(properties);
        printGreetings();
    }

    @Override
    public String getLabel() {
        // TODO Auto-generated method stub
        return COMPONENT_LABEL;
    }

}

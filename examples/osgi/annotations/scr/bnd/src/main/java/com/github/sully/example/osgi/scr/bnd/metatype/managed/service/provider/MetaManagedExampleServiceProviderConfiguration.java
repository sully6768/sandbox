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
package com.github.sully.example.osgi.scr.bnd.metatype.managed.service.provider;

import com.github.sully.example.osgi.scr.bnd.managed.service.provider.ManagedExampleServiceProvider;

import aQute.bnd.annotation.metatype.Meta;

@Meta.OCD(
        id = ManagedExampleServiceProvider.COMPONENT_NAME, 
        name = MetaManagedExampleServiceProvider.COMPONENT_LABEL)
public interface MetaManagedExampleServiceProviderConfiguration {

    @Meta.AD(id = "salutation", deflt = "Hello", required = true)
    String salutation();

    @Meta.AD(id = "name", deflt = "to whom it may concern", required = true)
    String name();
}

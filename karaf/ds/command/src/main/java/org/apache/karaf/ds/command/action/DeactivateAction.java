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
package org.apache.karaf.ds.commands.action;

import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.scr.Component;
import org.apache.felix.scr.ScrService;

@Command(scope = "scr", name = "deactivate", description = "Shut down a component")
public class DeactivateAction extends ScrActionSupport {

    @Argument(index = 0, name = "name", description = "The name of the Component to deactivate ", required = true, multiValued = false)
    String name;

    @Override
    protected Object doScrAction(ScrService scrService) throws Exception {
        Component[] components = scrService.getComponents(name);
        if (components != null && components.length > 0) {
            for (int i = 0; i < components.length; i++) {
                components[i].disable();
            }
        }
        return null;
    }

}

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
package org.apache.karaf.ds.command;

import java.util.ArrayList;
import java.util.List;

import org.apache.felix.gogo.commands.Action;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.karaf.ds.command.action.DetailsAction;
import org.apache.karaf.ds.command.completer.DetailsCompleter;
import org.apache.karaf.shell.console.Completer;

/**
 * Karaf Shell Command that prints the current state details of a given 
 * Declarative Service Component.
 */
@Component(
        name = DetailsCommandComponent.COMPONENT_NAME, 
        label = "Apache Karaf SCR Details Command", 
        enabled = true, 
        immediate = true)
@Property(
        name = DsCommandConstants.OSGI_COMMAND_FUNCTION_KEY, 
        value = {DsCommandConstants.DETAILS_FUNCTION}, 
        propertyPrivate = true)
public class DetailsCommandComponent extends ScrCommandSupport {

    public static final String COMPONENT_NAME = "DetailsCommand";

    public static final String COMPONENT_LABEL = "Apache Karaf SCR Details Command";

    @Override
    public String getComponentLabel() {
        return COMPONENT_LABEL;
    }

    @Override
    public Class<? extends Action> getActionClass() {
        return DetailsAction.class;
    }

    @Override
    public List<Class<? extends Completer>> getCompleterClasses() {
        List<Class<? extends Completer>> completers = new ArrayList<Class<? extends Completer>>();
        completers.add(DetailsCompleter.class);
        return completers;
    }
}

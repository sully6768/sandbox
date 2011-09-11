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
package org.apache.karaf.ds.commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.felix.gogo.commands.Action;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.karaf.ds.commands.action.ActivateAction;
import org.apache.karaf.ds.commands.completer.ActivateCompleter;
import org.apache.karaf.shell.console.Completer;

/**
 * 
 */
@Component(name = ActivateCommandComponent.COMPONENT_NAME, label = "Apache Karaf SCR Activate Command", enabled = true, immediate = true, metatype = true)
@Properties({
        @Property(name = DsCommandConstants.OSGI_COMMAND_SCOPE_KEY, value = {DsCommandConstants.SCR_COMMAND}, propertyPrivate = true),
        @Property(name = DsCommandConstants.OSGI_COMMAND_FUNCTION_KEY, value = {DsCommandConstants.ACTIVATE_FUNCTION}, propertyPrivate = true)})
public class ActivateCommandComponent extends ScrCommandSupport {

    public static final String COMPONENT_NAME = "ActivateCommand";

    @Override
    public Class<? extends Action> getActionClass() {
        return ActivateAction.class;
    }

    @Override
    public List<Class<? extends Completer>> getCompleterClasses() {
        List<Class<? extends Completer>> completers = new ArrayList<Class<? extends Completer>>();
        completers.add(ActivateCompleter.class);
        return completers;
    }
}

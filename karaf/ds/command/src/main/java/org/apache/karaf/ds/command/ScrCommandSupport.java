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
import org.apache.felix.gogo.commands.basic.AbstractCommand;
import org.apache.felix.scr.ScrService;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.service.command.Function;
import org.apache.karaf.ds.command.action.ScrActionSupport;
import org.apache.karaf.ds.command.completer.ScrCompleterSupport;
import org.apache.karaf.shell.console.CompletableFunction;
import org.apache.karaf.shell.console.Completer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
@Component(
        componentAbstract = true,
        metatype = true)
@Service(
        value = {Function.class, CompletableFunction.class})
@Property(
        name = DsCommandConstants.OSGI_COMMAND_SCOPE_KEY, 
        value = { DsCommandConstants.SCR_COMMAND }, 
        propertyPrivate = true)
public abstract class ScrCommandSupport extends AbstractCommand implements
        CompletableFunction {
    
    protected final Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Reference
    private ScrService scrService;
    
    @Activate
    public void activate(){
        logger.info("Activating the " + getComponentLabel());
    }
    
    @Deactivate
    public void deactivate(){
        logger.info("Deactivating the " + getComponentLabel());
    }

    @Override
    public abstract Class<? extends Action> getActionClass();

    public abstract List<Class<? extends Completer>> getCompleterClasses();
    
    public abstract String getComponentLabel();

    @Override
    public Action createNewAction() {
        try {
            ScrActionSupport action = (ScrActionSupport) getActionClass()
                    .newInstance();
            action.setScrService(getScrService());
            return action;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.karaf.shell.console.CompletableFunction#getCompleters()
     */
    public List<Completer> getCompleters() {
        List<Completer> completers = null;

        if (getCompleterClasses() != null) {
            try {
                completers = new ArrayList<Completer>();
                for (Class<? extends Completer> completerClass : getCompleterClasses()) {
                    ScrCompleterSupport ccs = (ScrCompleterSupport) completerClass
                            .newInstance();
                    ccs.setScrService(scrService);
                    completers.add(ccs);
                }

            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return completers;
    }

    /**
     * Returns the value of scrService for this instance of ScrCommandSupport.
     *
     * @return the ScrCommandSupport or null
     */
    public ScrService getScrService() {
        return scrService;
    }
    
}

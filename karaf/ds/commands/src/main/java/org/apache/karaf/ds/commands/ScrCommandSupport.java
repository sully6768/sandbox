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
import org.apache.felix.gogo.commands.basic.AbstractCommand;
import org.apache.felix.scr.ScrService;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.service.command.Function;
import org.apache.karaf.ds.commands.action.ScrActionSupport;
import org.apache.karaf.ds.commands.completer.ScrCompleterSupport;
import org.apache.karaf.shell.console.CompletableFunction;
import org.apache.karaf.shell.console.Completer;

/**
 *
 */
@Component(componentAbstract = true)
@Service(value = {Function.class, CompletableFunction.class})
public abstract class ScrCommandSupport extends AbstractCommand implements
        CompletableFunction {

    @Reference
    private ScrService scrService;

    @Override
    public abstract Class<? extends Action> getActionClass();

    @Override
    public Action createNewAction() {
        try {
            ScrActionSupport action = (ScrActionSupport) getActionClass()
                    .newInstance();
            action.setScrService(scrService);
            return action;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract List<Class<? extends Completer>> getCompleterClasses();

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.karaf.shell.console.CompletableFunction#getCompleters()
     */
    @Override
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
     * @return the scrService
     */
    public ScrService getScrService() {
        return scrService;
    }
}

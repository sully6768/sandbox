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
package org.apache.karaf.ds.command;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.felix.gogo.commands.Action;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.basic.AbstractCommand;
import org.apache.felix.scr.ScrService;
import org.apache.felix.service.command.Function;
import org.apache.karaf.ds.command.action.ScrAction;
import org.apache.karaf.ds.command.completer.ComponentsCompleterSupport;
import org.apache.karaf.shell.console.CompletableFunction;
import org.apache.karaf.shell.console.Completer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class ScrCommand extends AbstractCommand implements CompletableFunction {

    private Class<? extends Action> actionClass;

    private ScrService scrService;

    List<Completer> completers;

    public ScrCommand() {
    }

    public ScrCommand(Class<? extends Action> actionClass,
            ScrService scrService, List<Completer> completers) {
        this.actionClass = actionClass;
        this.scrService = scrService;
        this.completers = completers;
    }

    @Override
    public Class<? extends Action> getActionClass() {
        return actionClass;
    }

    public void setActionClass(Class<? extends Action> actionClass) {
        this.actionClass = actionClass;
    }

    // @Override
    public List<Completer> getCompleters() {
        return completers;
    }

    @Override
    public Action createNewAction() {
        try {
            ScrAction action = (ScrAction) actionClass.newInstance();
            action.setScrService(scrService);
            return action;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({"rawtypes"})
    public static ServiceRegistration export(BundleContext context,
            Class<? extends Action> actionClass,
            Class<? extends Completer> completerClass, ScrService scrService) {

        final String[] interfaces = new String[] {Function.class.getName(),
                CompletableFunction.class.getName()};

        Hashtable props = createCommandProperties(actionClass);

        List<Completer> completerList = createCompleterList(completerClass,
                scrService);

        ScrCommand command = new ScrCommand(actionClass, scrService,
                completerList);

        return context.registerService(interfaces, command, props);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Hashtable createCommandProperties(
            Class<? extends Action> actionClass) {
        Command cmd = actionClass.getAnnotation(Command.class);
        if (cmd == null) {
            throw new IllegalArgumentException(
                    "Action class is not annotated with @Command: "
                            + actionClass.getSimpleName());
        }
        Hashtable props = new Hashtable();
        props.put("osgi.command.scope", cmd.scope());
        props.put("osgi.command.function", cmd.name());
        return props;
    }

    private static List<Completer> createCompleterList(
            Class<? extends Completer> completer, ScrService scrService) {

        List<Completer> completerList = null;
        if (completer != null) {
            completerList = new ArrayList<Completer>();
            try {
                ComponentsCompleterSupport completerInst = (ComponentsCompleterSupport) completer
                        .newInstance();
                completerInst.setScrService(scrService);
                completerList.add(completerInst);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return completerList;
    }
}

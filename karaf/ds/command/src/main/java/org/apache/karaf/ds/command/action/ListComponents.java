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
package org.apache.karaf.ds.command.action;

import org.apache.felix.gogo.commands.Command;
import org.apache.felix.scr.Component;
import org.apache.felix.scr.ScrService;
import org.fusesource.jansi.Ansi;

/**
 * Displays the last log entries
 */
@Command(scope = "scr", name = "list", description = "Displays a list of available components")
public class ListComponents extends ScrAction {

    @Override
    protected Object doScrAction(ScrService scrService) throws Exception {

        if (scrService == null) {
            String msg = "The ScrService is not available.  Please ensure the Service Component Runtime has installed correctly.";
            log.warn(msg);
            System.out.println(msg);
        } else {
            System.out.println("Executing command list");
            System.out.println(getPrettyBoldString(
                    "   ID   State             Component Name",
                    Ansi.Color.WHITE));
            Component[] components = scrService.getComponents();
            for (int i = 0; i < components.length; i++) {
                if (!components[i].getName().equals("ScrCommandComponent")) {
                    Component c = components[i];
                    String id = buildLeftPadBracketDisplay(c.getId() + "", 4);
                    String state = buildRightPadBracketDisplay(
                            getState(c.getState()), 16);
                    String name = c.getName();
                    System.out.println(getPrettyString("[", Ansi.Color.WHITE)
                            + getPrettyString(id, Ansi.Color.YELLOW)
                            + getPrettyString("] [", Ansi.Color.WHITE)
                            + getPrettyString(state, Ansi.Color.YELLOW)
                            + getPrettyString("] ", Ansi.Color.WHITE)
                            + getPrettyString(name, Ansi.Color.YELLOW)
                            + getPrettyString("", Ansi.Color.WHITE));
                }
            }
        }
        return null;
    }

    private String getPrettyString(String value, Ansi.Color color) {
        return Ansi.ansi().fg(color).a(value).toString();
    }

    private String getPrettyBoldString(String value, Ansi.Color color) {
        return Ansi.ansi().fg(color).a(Ansi.Attribute.INTENSITY_BOLD).a(value)
                .a(Ansi.Attribute.INTENSITY_BOLD_OFF).toString();
    }

    private String getState(int componentState) {
        String retVal = null;

        switch (componentState) {
        case Component.STATE_ACTIVE:
            retVal = "ACTIVE";
            break;
        case Component.STATE_ACTIVATING:
            retVal = "ACTIVATING";
            break;
        case Component.STATE_DEACTIVATING:
            retVal = "DEACTIVATING";
            break;
        case Component.STATE_DISABLED:
            retVal = "DISABLED";
            break;
        case Component.STATE_DISABLING:
            retVal = "DISABLING";
            break;
        case Component.STATE_DISPOSED:
            retVal = "DISPOSED";
            break;
        case Component.STATE_DISPOSING:
            retVal = "DISPOSING";
            break;
        case Component.STATE_ENABLING:
            retVal = "ENABLING";
            break;
        case Component.STATE_FACTORY:
            retVal = "FACTORY";
            break;
        case Component.STATE_REGISTERED:
            retVal = "REGISTERED";
            break;
        case Component.STATE_UNSATISFIED:
            retVal = "UNSATISFIED";
            break;

        default:
            break;
        }

        return retVal;
    }

    public String buildRightPadBracketDisplay(String s, int max) {
        return String.format("%1$-" + max + "s", s);
    }

    public String buildLeftPadBracketDisplay(String s, int max) {
        return String.format("%1$#" + max + "s", s);
    }
}

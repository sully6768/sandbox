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

import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.apache.felix.scr.Component;
import org.apache.felix.scr.ScrService;
import org.apache.karaf.ds.commands.ScrUtils;
import org.fusesource.jansi.Ansi;

/**
 * Displays the last log entries
 */
@Command(scope = "scr", name = "list", description = "Displays a list of available components")
public class ListAction extends ScrActionSupport {

    @Option(name = "-a", description = "Shows all components including the SCR Command", required = false, multiValued = false)
    boolean listAll;

    @Override
    protected Object doScrAction(ScrService scrService) throws Exception {

        if (scrService == null) {
            String msg = "The ScrService is not available.  Please ensure the Service Component Runtime has installed correctly.";
            log.warn(msg);
            System.out.println(msg);
        } else {
            System.out.println(getPrettyBoldString(
                    "   ID   State             Component Name",
                    Ansi.Color.WHITE));
            Component[] components = scrService.getComponents();
            for (int i = 0; i < components.length; i++) {
                Component c = components[i];
                String name = c.getName();
                if (isListable(name) || listAll) {
                    String id = buildLeftPadBracketDisplay(c.getId() + "", 4);
                    String state = buildRightPadBracketDisplay(
                            ScrUtils.getState(c.getState()), 16);
                    System.out.println(getPrettyString("[", Ansi.Color.WHITE)
                            + getPrettyString(id, Ansi.Color.YELLOW)
                            + getPrettyString("] [", Ansi.Color.WHITE)
                            + getPrettyString(state, Ansi.Color.YELLOW)
                            + getPrettyString("] ", Ansi.Color.WHITE)
                            + getPrettyString(name, Ansi.Color.YELLOW)
                            + Ansi.ansi().a(Ansi.Attribute.RESET).toString());
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

    public String buildRightPadBracketDisplay(String s, int max) {
        return String.format("%1$-" + max + "s", s);
    }

    public String buildLeftPadBracketDisplay(String s, int max) {
        return String.format("%1$#" + max + "s", s);
    }
}

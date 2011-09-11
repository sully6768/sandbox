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
import org.apache.felix.scr.Reference;
import org.apache.felix.scr.ScrService;
import org.apache.karaf.ds.commands.ScrUtils;
import org.fusesource.jansi.Ansi;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentConstants;

/**
 * Displays the last log entries
 */
@Command(scope = "scr", name = "list", description = "Displays a list of available components")
public class DetailsAction extends ScrActionSupport {

    @Argument(index = 0, name = "name", description = "The name of the Component to display the detials of", required = true, multiValued = false)
    String name;

    @Override
    protected Object doScrAction(ScrService scrService) throws Exception {

        if (scrService == null) {
            String msg = "The ScrService is not available.  Please ensure the Service Component Runtime has installed correctly.";
            log.warn(msg);
            System.out.println(msg);
        } else {
            System.out.println(getPrettyBoldString("Component Details",
                    Ansi.Color.WHITE));
            Component[] components = scrService.getComponents(name);
            for (int i = 0; i < components.length; i++) {
                Component c = components[i];

                printDetail("  Name                : ", c.getName());
                printDetail("  State               : ", ScrUtils.getState(c.getState()));
                Reference[] references = c.getReferences();
                System.out.println(getPrettyBoldString("References", Ansi.Color.WHITE)
                        + Ansi.ansi().a(Ansi.Attribute.RESET).toString());

                for (Reference reference : references) {
                    printDetail("  Reference           : ", reference.getName());
                    printDetail("    State             : ", (reference.isSatisfied())?"satisfied":"unsatisfied");
                    printDetail("    Multiple          : ", (reference.isMultiple() ? "multiple" : "single" ));
                    printDetail("    Optional          : ", (reference.isOptional() ? "optional" : "mandatory" ));
                    printDetail("    Policy            : ", (reference.isStatic() ? "static" : "dynamic" ));

                    // list bound services
                    ServiceReference[] boundRefs = reference.getServiceReferences();
                    if ( boundRefs != null && boundRefs.length > 0 )
                    {
                        for ( int j = 0; j < boundRefs.length; j++ )
                        {
                            final StringBuffer b = new StringBuffer();
                            b.append( "Bound Service ID " );
                            b.append( boundRefs[j].getProperty( Constants.SERVICE_ID ) );

                            String componentName = ( String ) boundRefs[j].getProperty( ComponentConstants.COMPONENT_NAME );
                            if ( componentName == null )
                            {
                                componentName = ( String ) boundRefs[j].getProperty( Constants.SERVICE_PID );
                                if ( componentName == null )
                                {
                                    componentName = ( String ) boundRefs[j].getProperty( Constants.SERVICE_DESCRIPTION );
                                }
                            }
                            if ( componentName != null )
                            {
                                b.append( " (" );
                                b.append( componentName );
                                b.append( ")" );
                            }
                            printDetail("    Service Reference : ", b.toString());
                        }
                    }
                    else
                    {
                        printDetail("  Service Reference : ", "No Services bound");
                    }
                }

            }
        }
        return null;
    }

    private void printDetail(String header, String value) {
        System.out.print(getPrettyBoldString(header, Ansi.Color.WHITE));
        System.out.println(getPrettyString(value, Ansi.Color.YELLOW)
                + Ansi.ansi().a(Ansi.Attribute.RESET).toString());
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

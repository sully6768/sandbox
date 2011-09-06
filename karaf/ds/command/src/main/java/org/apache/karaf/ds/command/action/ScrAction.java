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

import org.apache.felix.scr.ScrService;
import org.apache.karaf.shell.console.AbstractAction;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ScrAction extends AbstractAction {
    protected final Logger log = LoggerFactory.getLogger(ListComponents.class);

    private ScrService scrService;

    @Override
    protected Object doExecute() throws Exception {
        if (scrService == null) {
            System.out.println("ScrService service is unavailable.");
        } else {
            doScrAction(scrService);
        }
        return null;
    }

    protected abstract Object doScrAction(ScrService scrService)
            throws Exception;

    /**
     * Get the bundleContext Object associated with this instance of
     * ScrAction.
     * 
     * @return the bundleContext
     */
    public BundleContext getBundleContext() {
        return FrameworkUtil.getBundle(ListComponents.class).getBundleContext();
    }

    /**
     * Get the scrService Object associated with this instance of
     * ScrAction.
     * 
     * @return the scrService
     */
    public ScrService getScrService() {
        return scrService;
    }

    /**
     * Sets the scrService Object for this ScrAction instance.
     * 
     * @param scrService
     *            the scrService to set
     */
    public void setScrService(ScrService scrService) {
        this.scrService = scrService;
    }

}

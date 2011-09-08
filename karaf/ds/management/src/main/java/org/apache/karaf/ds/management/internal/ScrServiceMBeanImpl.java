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
package org.apache.karaf.ds.management.internal;

import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServer;
import javax.management.openmbean.TabularData;

import org.apache.felix.scr.ScrService;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.karaf.ds.management.ScrServiceMBean;
import org.apache.karaf.management.MBeanRegistrer;

/**
 *
 */
@Component(
    enabled=true,
    immediate=true)
public class ScrServiceMBeanImpl extends MBeanRegistrer implements
        ScrServiceMBean {
    
    @Reference
    private MBeanServer mBeanServer;
    
    @Reference
    private ScrService scrService;
    
    @Activate
    public void activate() throws Exception{
        Map<Object, String> mbeans = new HashMap<Object, String>();
        mbeans.put(this, "org.apache.karaf:type=scr,name=karaf.name");
        this.setMbeans(mbeans);
        this.registerMBeanServer(mBeanServer);
    }
    
    @Deactivate
    public void deactivate() throws Exception{
        this.unregisterMBeanServer(mBeanServer);
    }

    /**
     * Overrides the super method noted below.  
     * See super documentation for details.
     *
     * @see org.apache.karaf.ds.management.ScrServiceMBean#listComponents()
     */
    @Override
    public TabularData listComponents() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Overrides the super method noted below.  
     * See super documentation for details.
     *
     * @see org.apache.karaf.ds.management.ScrServiceMBean#activateComponent(java.lang.String)
     */
    @Override
    public void activateComponent(String componentName) throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * Overrides the super method noted below.  
     * See super documentation for details.
     *
     * @see org.apache.karaf.ds.management.ScrServiceMBean#deactiveateComponent(java.lang.String)
     */
    @Override
    public void deactiveateComponent(String componentName) throws Exception {

    }

}

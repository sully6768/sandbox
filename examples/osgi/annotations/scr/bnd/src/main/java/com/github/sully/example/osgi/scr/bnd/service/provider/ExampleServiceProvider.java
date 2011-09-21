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
package com.github.sully.example.osgi.scr.bnd.service.provider;

import org.osgi.service.log.LogService;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

import com.github.sully.example.osgi.scr.bnd.service.ExampleService;

/**
 *
 */
@Component
public class ExampleServiceProvider implements ExampleService {

    private LogService logService;

    private String name = "Scott";

    private String salutation = "Hello";

    @Override
    public void printGreetings() {
        logService.log(LogService.LOG_INFO, getMessage());
    }

    private String getMessage() {
        return salutation + " " + name;
    }

    @Reference
    protected void setLogService(LogService logService) {
        this.logService = logService;
    }

    protected void unsetLogService(LogService logService) {
        this.logService = logService;
    }

}

/*
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.camel.component.sjms.utils;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * TODO Add Class documentation for StringUtils
 *
 */
public class StringUtils {

    public static boolean isEmpty(String value) {
        return value != null && !value.equals("");
    }

    public static boolean isNotEmpty(String value) {
        return ! isEmpty(value);
    }
    
    public static boolean isValidUri(String uri) {
        boolean answer = true;
        try {
            new URI(uri);
        } catch (URISyntaxException e) {
            answer = false;
        }
        return answer;
    }
}
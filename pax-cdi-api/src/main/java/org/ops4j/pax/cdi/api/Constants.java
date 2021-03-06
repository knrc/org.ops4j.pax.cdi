/*
 * Copyright 2012 Harald Wellmann.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.pax.cdi.api;


public class Constants {

    /**
     * Opt-in manifest header, starting a CDI container
     */
    public static final String CDI_EXTENDER = "pax.cdi";
    
    public static final String EXTENDER_CAPABILITY = "osgi.extender";

    public static final String CDI_EXTENSION_CAPABILITY = "org.ops4j.pax.cdi.extension";
    
    /**
     * Hidden constructor of utility class.
     */
    private Constants() {
    }    
}

/*
 * Copyright 2014 Harald Wellmann.
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
 * 
 * Copied from Weld, where this class is not exported.
 */
package org.ops4j.pax.cdi.weld.impl.bda;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;

import org.jboss.weld.resources.spi.ResourceLoader;
import org.jboss.weld.resources.spi.ResourceLoadingException;
import org.ops4j.pax.cdi.weld.impl.EnumerationList;

/**
 * General {@link ResourceLoader} implementation that delegates resource loading to
 * {@link #classLoader()}.
 *
 * @author Jozef Hartinger
 *
 */
public abstract class AbstractClassLoaderResourceLoader implements ResourceLoader {

    private static final String ERROR_LOADING_CLASS = "Error loading class ";

    @Override
    public Class<?> classForName(String name) {
        try {
            return classLoader().loadClass(name);
        }
        catch (ClassNotFoundException e) {
            throw new ResourceLoadingException(ERROR_LOADING_CLASS + name, e);
        }
        catch (LinkageError e) {
            throw new ResourceLoadingException(ERROR_LOADING_CLASS + name, e);
        }
        catch (TypeNotPresentException e) {
            throw new ResourceLoadingException(ERROR_LOADING_CLASS + name, e);
        }
    }

    @Override
    public URL getResource(String name) {
        return classLoader().getResource(name);
    }

    @Override
    public Collection<URL> getResources(String name) {
        try {
            return new EnumerationList<URL>(classLoader().getResources(name));
        }
        catch (IOException e) {
            throw new ResourceLoadingException("Error loading resource " + name, e);
        }
    }

    protected abstract ClassLoader classLoader();
}

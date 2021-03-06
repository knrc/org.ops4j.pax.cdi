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
package org.ops4j.pax.cdi.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.ops4j.pax.cdi.test.support.TestConfiguration.cdiProviderBundles;
import static org.ops4j.pax.cdi.test.support.TestConfiguration.paxCdiProviderAdapter;
import static org.ops4j.pax.cdi.test.support.TestConfiguration.regressionDefaults;
import static org.ops4j.pax.cdi.test.support.TestConfiguration.workspaceBundle;
import static org.ops4j.pax.exam.CoreOptions.bundle;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.cdi.sample2.service.LibraryServiceClient;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.options.UrlProvisionOption;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.ops4j.pax.jpa.sample1.model.Author;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class TransactionalTest {

    @Inject
    private LibraryServiceClient libraryService;

    @Configuration
    public Option[] config() {
        return options(
            regressionDefaults(),
            paxCdiProviderAdapter(),
            cdiProviderBundles(),

            // OpenJPA and dependencies
            mavenBundle("org.apache.geronimo.specs", "geronimo-jpa_2.0_spec").versionAsInProject(),
            mavenBundle("commons-lang", "commons-lang").versionAsInProject(),
            mavenBundle("commons-collections", "commons-collections").versionAsInProject(),
            mavenBundle("commons-pool", "commons-pool").versionAsInProject(),
            mavenBundle("commons-dbcp", "commons-dbcp").versionAsInProject(),
            mavenBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.serp",
                "1.13.1_4"),
            mavenBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.asm",
                "3.3_2"),
            mavenBundle("org.apache.openjpa", "openjpa").versionAsInProject(),

            // Pax JPA, Pax JDBC and Derby driver
            mavenBundle("org.ops4j.pax.jpa", "pax-jpa").versionAsInProject().startLevel(2),
            mavenBundle("org.ops4j.pax.jdbc", "pax-jdbc").versionAsInProject(),
            mavenBundle("org.apache.derby", "derby").versionAsInProject(),
            mavenBundle("org.osgi", "org.osgi.enterprise").versionAsInProject(),

            // unchanged DeltaSpike bundles
            mavenBundle("org.apache.deltaspike.core", "deltaspike-core-api").versionAsInProject(),
            mavenBundle("org.apache.deltaspike.core", "deltaspike-core-impl").versionAsInProject(),
            mavenBundle("org.apache.deltaspike.modules", "deltaspike-jpa-module-api").versionAsInProject(),
            mavenBundle("org.apache.deltaspike.modules", "deltaspike-partial-bean-module-api").versionAsInProject(),
            mavenBundle("org.apache.deltaspike.modules", "deltaspike-data-module-api").versionAsInProject(),

            // DeltaSpike bundles with missing requirements and capabilities
            wrappedDeltaSpikeBundle("deltaspike-jpa-module-impl"),
            wrappedDeltaSpikeBundle("deltaspike-data-module-impl"),
            wrappedDeltaSpikeBundle("deltaspike-partial-bean-module-impl"),

//            mavenBundle("org.apache.deltaspike.modules", "deltaspike-jpa-module-impl").versionAsInProject(),
//            mavenBundle("org.apache.deltaspike.modules", "deltaspike-partial-bean-module-impl").versionAsInProject(),
//            mavenBundle("org.apache.deltaspike.modules", "deltaspike-data-module-impl").versionAsInProject(),

            mavenBundle("org.apache.geronimo.specs", "geronimo-servlet_3.0_spec", "1.0"),
            mavenBundle("org.apache.geronimo.specs", "geronimo-jta_1.1_spec").versionAsInProject(),

            // Sample bundles
            mavenBundle("org.ops4j.pax.jpa.samples", "pax-jpa-sample1").versionAsInProject(),
            workspaceBundle("org.ops4j.pax.cdi.samples", "pax-cdi-sample2-service"));
    }

    private static UrlProvisionOption wrappedDeltaSpikeBundle(String artifactId) {
        String url = mavenBundle("org.apache.deltaspike.modules", artifactId).versionAsInProject().getURL();
        return bundle(String.format("wrap:%s,file:src/test/resources/%s.bnd", url, artifactId));
    }

    @Test
    public void createAuthorInTransaction() {
        libraryService.createAuthorViaDao("Charles", "Dickens");
        Author author = libraryService.findAuthor("Charles", "Dickens");
        assertThat(author, is(notNullValue()));
        assertThat(author.getFirstName(), is("Charles"));
        assertThat(author.getLastName(), is("Dickens"));
    }
}

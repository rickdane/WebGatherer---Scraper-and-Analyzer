package org.WebGatherer.Controller;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.Webgatherer.DependencyInjection.DependencyBindingModule;
import org.junit.Before;

/**
 * @author Rick Dane
 */
public class IntegrationThreadTest {

    @Before
    public void setUp () {

        Injector injector = Guice.createInjector(new DependencyBindingModule());
    }

}

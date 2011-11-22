package org.Webgatherer.WorkflowExample.DependencyInjection;

import com.google.inject.AbstractModule;
import org.Webgatherer.ExperimentalLabs.HtmlProcessing.HtmlParser;
import org.Webgatherer.ExperimentalLabs.HtmlProcessing.HtmlParserImpl;

/**
 * @author Rick Dane
 */
public class DependencyBindingModule extends AbstractModule {

    @Override
    protected void configure() {

        bind (HtmlParser.class).to(HtmlParserImpl.class);

    }
}

package org.Webgatherer.WorkflowExample.DependencyInjection;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.Webgatherer.ExperimentalLabs.HtmlProcessing.HtmlParser;
import org.Webgatherer.ExperimentalLabs.HtmlProcessing.HtmlParserImpl;
import org.Webgatherer.WorkflowExample.Workflows.Base.DataInterpetor.TextExtraction;

/**
 * @author Rick Dane
 */
public class DependencyBindingModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(HtmlParser.class).to(HtmlParserImpl.class);
        bind(TextExtraction.class).in(Singleton.class);

    }
}

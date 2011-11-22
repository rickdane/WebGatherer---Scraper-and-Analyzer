package org.WorkflowExample.DependencyInjection;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.Webgatherer.Utility.HtmlParsing.HtmlParser;
import org.Webgatherer.Utility.HtmlParsing.HtmlParserImpl;

/**
 * @author Rick Dane
 */
public class DependencyBindingModule extends AbstractModule {

    @Override
    protected void configure() {

        bind (HtmlParser.class).to(HtmlParserImpl.class);

    }
}

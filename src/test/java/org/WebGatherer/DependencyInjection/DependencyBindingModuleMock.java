package org.WebGatherer.DependencyInjection;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.Webgatherer.Controller.ControllerFlow;
import org.Webgatherer.Controller.WorkflowControllerImpl_1;
import org.Webgatherer.Core.ThreadCommunication.FinalOutputContainer;
import org.Webgatherer.Core.ThreadCommunication.FinalOutputContainerImpl;
import org.Webgatherer.Core.ThreadCommunication.ThreadCommunication;
import org.Webgatherer.Core.ThreadCommunication.ThreadCommunicationImpl;
import org.Webgatherer.Core.Threadable.DataInterpreatation.DataInterpretor;
import org.Webgatherer.Core.Threadable.DataInterpreatation.DataInterpretorImpl;
import org.Webgatherer.Core.Threadable.WebGather.WebGather;
import org.Webgatherer.Core.Threadable.WebGather.WebGatherImpl;
import org.Webgatherer.Utility.HtmlParsing.HtmlParser;
import org.Webgatherer.Utility.HtmlParsing.HtmlParserImpl;
import org.Webgatherer.Workflow.WorfkflowWrapperImpl_Reflection;
import org.Webgatherer.Workflow.WorkflowWrapper;

/**
 * @author Rick Dane
 */
public class DependencyBindingModuleMock extends AbstractModule {
    @Override
    protected void configure() {

        bind(ThreadCommunication.class).to(ThreadCommunicationImpl.class);

        bind(ControllerFlow.class).to(WorkflowControllerImpl_1.class);

        bind(DataInterpretor.class).to(DataInterpretorImpl.class);

        bind(FinalOutputContainer.class).to(FinalOutputContainerImpl.class);

        bind (WorkflowWrapper.class).to(WorfkflowWrapperImpl_Reflection.class).in(Singleton.class);

        bind (WebGather.class).to(WebGatherImpl.class);

        bind (HtmlParser.class).to(HtmlParserImpl.class);

    }
}

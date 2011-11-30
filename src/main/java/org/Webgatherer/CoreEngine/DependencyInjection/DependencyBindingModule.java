package org.Webgatherer.CoreEngine.DependencyInjection;

import org.Webgatherer.Controller.ControllerFlow;
import org.Webgatherer.Controller.WorkflowControllerImpl_1;
import org.Webgatherer.CoreEngine.Core.Threadable.WebGather.WebGather;
import org.Webgatherer.CoreEngine.Core.Threadable.WebGather.WebGatherImpl;
import org.Webgatherer.ExperimentalLabs.HtmlProcessing.HtmlParser;
import org.Webgatherer.ExperimentalLabs.HtmlProcessing.HtmlParserImpl;
import org.Webgatherer.CoreEngine.Core.Threadable.DataInterpreatation.DataInterpretor;
import org.Webgatherer.CoreEngine.Core.Threadable.DataInterpreatation.DataInterpretorImpl;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.FinalOutputContainer;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.FinalOutputContainerImpl;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunicationImpl;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunication;
import org.Webgatherer.CoreEngine.Workflow.WorkflowWrapper;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.Webgatherer.CoreEngine.Workflow.WorkflowWrapperImpl_External;

/**
 * @author Rick Dane
 */
public class DependencyBindingModule extends AbstractModule {
    @Override
    protected void configure() {

        bind(ThreadCommunication.class).to(ThreadCommunicationImpl.class);

        bind(ControllerFlow.class).to(WorkflowControllerImpl_1.class);

        bind(DataInterpretor.class).to(DataInterpretorImpl.class);

        bind(FinalOutputContainer.class).to(FinalOutputContainerImpl.class);

        bind (WorkflowWrapper.class).to(WorkflowWrapperImpl_External.class).in(Singleton.class);

        bind (WebGather.class).to(WebGatherImpl.class);

        bind (HtmlParser.class).to(HtmlParserImpl.class);

    }
}

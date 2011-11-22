package org.Webgatherer.CoreEngine.Core.ThreadCommunication;

import java.util.List;

/**
 * @author Rick Dane
 */
public interface FinalOutputContainer {

    public void addToFinalOutputContainer (List addList);

    public List removeFromFinalOutputContainer ();

}

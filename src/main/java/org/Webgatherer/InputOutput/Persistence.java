package org.Webgatherer.InputOutput;

import java.util.Collection;

/**
 * @author Rick Dane
 */
public interface Persistence {

    /**
     * Persists key, value data
     */
    public void persistFlatData (Collection inputData, String persistenceTarget);

}

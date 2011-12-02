package org.Webgatherer.WorkflowExample.Workflows.Base.DataInterpetor;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.LinkedList;

/**
 * @author Rick Dane
 */
public class EmailExtractorTest extends TestCase {

    @Test
    public void testEmailExtract() {
        EmailExtractor emailExtractor = new EmailExtractor();

        String initStr = "fskjfska tesst@gh.com ' fskjfska skgaj aslkgkj gjg@gh.com asfasfsa";
        LinkedList<String> retList = emailExtractor.extractEmailAddressesList(initStr);
        assertTrue (retList.size() == 2);

        assertTrue (retList.get(0).equals("tesst@gh.com"));
        assertTrue (retList.get(1).equals("gjg@gh.com"));
    }
}

package org.Webgatherer.WorkflowExample.Workflows.Base.DataInterpetor;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Rick Dane
 */
public class EmailExtractor {

    private int lastIndexNeedsRemoval;
    private int calcLastIndex;
    private String curHtmlPage;
    private LinkedList<String> emailNamesUsed = new LinkedList<String>();

    public LinkedList<String> extractEmailAddressesList(String htmlPage) {
        curHtmlPage = htmlPage;
        String retEmailAddr = "";
        LinkedList<String> retList = new LinkedList<String>();
        boolean lastIteration = false;

        while (retEmailAddr != null) {
            try {
                retEmailAddr = extractEmailAddress();
                if (retEmailAddr != null) {
                    try {
                        int endIndex = curHtmlPage.length() - 1;
                        if (calcLastIndex - endIndex <= 0) {
                            lastIteration = true;
                        }
                        if (!lastIteration) {
                            curHtmlPage = curHtmlPage.substring(calcLastIndex, endIndex);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String[] splitEmail = retEmailAddr.split("@");
                    if (splitEmail.length >= 1) {
                        if (retEmailAddr != null && !retList.contains(retEmailAddr) && !emailNamesUsed.contains(splitEmail[0])) {
                            retList.add(retEmailAddr);
                            emailNamesUsed.add(splitEmail[0]);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (lastIteration) {
                break;
            }
        }
        return retList;
    }

    private String extractEmailAddress() {
        int index = curHtmlPage.indexOf("@"); //
        lastIndexNeedsRemoval = index;

        String retStr = runEmailFindLoop(curHtmlPage, index, false, false);
        String retStr2 = null;
        if (retStr == null) {
            return null;

        }
        retStr2 = runEmailFindLoop(curHtmlPage, index, true, false);
        if (retStr2 == null) {
            return null;
        }

        return retStr + "@" + retStr2;

    }

    private String doCalculation(String inputStr, int index, int iter, boolean isPlus) {
        if (isPlus) {
            int curIndex = index + iter;
            calcLastIndex = curIndex;
            return inputStr.substring(index + 1, curIndex);
        } else {
            int curIndex = index - iter;
            if (curIndex >= 0 && index >= 0 && inputStr.length() >= index) {
                return inputStr.substring(curIndex, index);
            }
            return null;
        }
    }

    private String runEmailFindLoop(String inputStr, int index, boolean isPlus, boolean isRecursive) {
        boolean isAlphaNumeric = true;
        String setStr = null;
        String retStr = null;

        int iter = 1;
        if (isPlus) {
            iter = 2;
        }
        while (isAlphaNumeric) {

            setStr = doCalculation(inputStr, index, iter, isPlus);

            if (setStr == null || !isAplhaNumeric(setStr)) {
                break;
            }
            retStr = setStr;
            iter++;
        }

        String emailDomain = "";
        if (!isRecursive && isPlus) {
            //check for the email address domain name
            String checkPeriod = inputStr.substring(index + iter - 1, index + iter);
            if (checkPeriod.equals(".")) {
                emailDomain = "." + runEmailFindLoop(inputStr, index + iter - 1, isPlus, true);
                if (emailDomain == null) {
                    return null;
                }
                lastIndexNeedsRemoval = calcLastIndex;
            } else {
                return null;
            }
        }
        return retStr + emailDomain;
    }

    /**
     * @param inputStr
     * @return
     */
    private boolean isAplhaNumeric(String inputStr) {
        String regexPattern = "^[a-zA-Z0-9-_]+$"; // "[^[a-zA-Z0-9][\\-][\\_]]*"; //"^[a-zA-Z0-9]+$";
        Pattern p = Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(inputStr);
        boolean isAlphaNumeric = m.find();

        return isAlphaNumeric;
    }
}

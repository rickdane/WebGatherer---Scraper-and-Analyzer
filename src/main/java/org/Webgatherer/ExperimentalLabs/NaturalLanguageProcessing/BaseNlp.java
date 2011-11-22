package org.Webgatherer.ExperimentalLabs.NaturalLanguageProcessing;

import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Experimental, this is not necessarily meant to be used yet
 * @author Rick Dane
 */
public class BaseNlp {
    private POSModel model;
    private PerformanceMonitor perfMon;
    private POSTaggerME tagger;

    public void prepare() {
        POSModel model = null;
        try {
            model = new POSModelLoader().load(new File("/home/user/Desktop/nlp/en-pos-maxent.bin"));
        } catch (Exception e) {
           e.printStackTrace();
        }
        perfMon = new PerformanceMonitor(System.err, "sent");
        tagger = new POSTaggerME(model);
    }

    public void test(String paragraph) {
        prepare();

        String keyword = "football";
        //begin strongly by declaring noun / action first as this should be easier for NLP to pick up
        String input = "Football is one of the most physical sports in the world.";

        //test article that is being searched
        String testArticle = "While there are many sports that are played, one of the least physical is soccer. On the other hand, American football is a very tough sport to play.";

        List<List<String>> searchPhraseResults = parsePhraseToList(input);
        List<List<String>> searcheeResults = parsePhraseToList(testArticle);

        List<Integer> searchPhraseIndexes = getKeywordSearchIndexs(searchPhraseResults.get(0), keyword);
        List<Integer> searcheeIndexes = getKeywordSearchIndexs(searcheeResults.get(0), keyword);

        List<List<String>> searchPhraseSub = getThresholdTags(searchPhraseIndexes, searchPhraseResults.get(0), searchPhraseResults.get(1));
        List<List<String>> searcheeSub = getThresholdTags(searcheeIndexes, searcheeResults.get(0), searcheeResults.get(1));

        determinePatternMatch(searchPhraseSub, searcheeSub);
    }

    private String determinePatternMatch(List<List<String>> searchPhraseSub, List<List<String>> searcheeSub) {
        List<String> searcheeTags = searcheeSub.get(1);
        List<String> searchPhraseTags = searchPhraseSub.get(1);
        System.out.println("Comparing: ");
        System.out.println(searcheeTags.get(0) + " " + searcheeTags.get(1));
        System.out.println("To: ");
        System.out.println(searchPhraseTags.get(0) + " " + searcheeTags.get(1));
        return null;
    }

    private List<List<String>> getThresholdTags(List<Integer> indexList, List<String> phraseResults, List<String> tagList) {
        List<List<String>> retList = new ArrayList<List<String>> ();
        int threshold = 4;
        for (int i : indexList) {
            int threshFloor = i - threshold;
            int threshCeiling = i + threshold;
            if (threshFloor < 0) {
                threshFloor = 0;
                threshCeiling = threshCeiling + threshold;
            }
            List<String> tagSubList = tagList.subList(threshFloor,threshCeiling);
            //TODO make this handle more than one match
            retList.add(tagSubList);
            List<String> wordList = phraseResults.subList(threshFloor, threshCeiling);
            retList.add(wordList);
            return retList;
        }
        return retList;
    }


    private List<Integer> getKeywordSearchIndexs(List<String> list, String keyword) {
        List<Integer> keywordIndexes = new LinkedList<Integer>();
        int i = 0;
        for (String curStr : list) {
            if (curStr.equalsIgnoreCase(keyword)) {
                keywordIndexes.add(i);
            }
            i++;
        }
        return keywordIndexes;
    }


    private List<List<String>> parsePhraseToList(String textToParse) {
        ObjectStream<String> lineStream =
                new PlainTextByLineStream(new StringReader(textToParse));
        //perfMon.start();
        String line;
        List<List<String>> outputList = new ArrayList<List<String>>();
        try {
            while ((line = lineStream.read()) != null) {

                String[] whitespaceTokenizerLine = WhitespaceTokenizer.INSTANCE.tokenize(line);
                String[] tags = tagger.tag(whitespaceTokenizerLine);

                outputList.add(convertStringArrayToList(whitespaceTokenizerLine));
                outputList.add(convertStringArrayToList(tags));

                //perfMon.incrementCounter();
            }
            //perfMon.stopAndPrintFinalResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputList;
    }

    private List<String> convertStringArrayToList(String[] stringArray) {
        List<String> retList = new ArrayList<String>();
        Collections.addAll(retList, stringArray);
        return retList;
    }
}


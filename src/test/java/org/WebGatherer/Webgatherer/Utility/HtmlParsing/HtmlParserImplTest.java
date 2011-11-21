package org.WebGatherer.Webgatherer.Utility.HtmlParsing;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.Webgatherer.DependencyInjection.DependencyBindingModule;
import org.Webgatherer.Utility.HtmlParsing.HtmlParser;

import java.io.*;
import java.util.List;

/**
 * @author Rick Dane
 */
public class HtmlParserImplTest {

    public static void main(String[] args) {

        Injector injector = Guice.createInjector(new DependencyBindingModule());
        HtmlParser htmlParser = injector.getInstance(HtmlParser.class);

        String pageText = readFile("/home/user/Desktop/test.html");
        List<String> retList = htmlParser.extractLinks(pageText);

        System.out.println(htmlParser.getText(pageText));
    }

    private static String readFile(String fileName) {

        File file = new File(fileName);

        char[] buffer = null;

        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new FileReader(file));

            buffer = new char[(int) file.length()];

            int i = 0;
            int c = bufferedReader.read();

            while (c != -1) {
                buffer[i++] = (char) c;
                c = bufferedReader.read();
            }
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
        return new String(buffer);
    }
}

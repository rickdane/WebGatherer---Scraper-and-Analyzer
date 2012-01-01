package org.Webgatherer.Utility;

import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * @author Rick Dane
 */
public class RandomSelector {

    private Random rand = new Random();

    public int randomListIndex(List list) {

        int adjSize = list.size() - 1;
        int randomNum = rand.nextInt(adjSize - 0 + 1) + 0;
        return randomNum;
    }

    public int generateRandomNumberInRange(int min, int max) {
        int randomNum = rand.nextInt(max - min + 1) + min;
        return randomNum;
    }

}

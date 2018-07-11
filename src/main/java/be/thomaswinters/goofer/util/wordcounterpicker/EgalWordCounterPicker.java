package be.thomaswinters.goofer.util.wordcounterpicker;


import be.thomaswinters.wordcounter.WordCounter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Picks elements uniformly spread over the word counter.
 *
 * @author Thomas Winters
 */
public class EgalWordCounterPicker implements IWordCounterPicker {

    @Override
    public Collection<String> pickWords(WordCounter wordCounter, int maxAmount) {
        Collection<String> elementSet = wordCounter.getElements();
        if (elementSet.size() <= maxAmount) {
            return wordCounter.getElements();
        }
        List<String> list = new ArrayList<>(elementSet);
        int jumpSize = (elementSet.size() / maxAmount) + 1;
        List<String> result = new ArrayList<>();
        for (int i = 0; i < elementSet.size(); i += jumpSize) {
            result.add(list.get(i));
        }
        return result;
    }

}

package be.thomaswinters.goofer.util.wordcounterpicker;

import be.thomaswinters.random.Picker;
import be.thomaswinters.wordcounter.WordCounter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class RandomWordsPicker implements IWordCounterPicker {

    @Override
    public Collection<String> pickWords(WordCounter wordCounter, int maxAmount) {
        List<String> elements = new ArrayList<>(wordCounter.getElements());
        if (elements.size() <= maxAmount) {
            return elements;
        }
        return Picker.pickRandomUniqueIndices(maxAmount, elements.size()).stream().map(e -> elements.get(e))
                .collect(Collectors.toList());
    }

}

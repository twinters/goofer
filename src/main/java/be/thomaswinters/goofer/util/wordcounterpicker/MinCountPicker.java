package be.thomaswinters.goofer.util.wordcounterpicker;


import be.thomaswinters.wordcounter.WordCounter;

import java.util.Collection;

public class MinCountPicker implements IWordCounterPicker {

    private final int minCount;
    private final IWordCounterPicker picker;

    public MinCountPicker(int minCount, IWordCounterPicker picker) {
        this.minCount = minCount;
        this.picker = picker;
    }

    @Override
    public Collection<String> pickWords(WordCounter wordCounter, int maxAmount) {
        return picker.pickWords(WordCounter.filterMininum(wordCounter, minCount), maxAmount);
    }

}

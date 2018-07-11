package be.thomaswinters.goofer.util.wordcounterpicker;

import be.thomaswinters.wordcounter.WordCounter;

import java.util.Collection;


public interface IWordCounterPicker {

    Collection<String> pickWords(WordCounter wordCounter, int maxAmount);

}

package be.thomaswinters.goofer.util.wordcounterpicker;

import java.util.Collection;

import be.thomaswinters.sentencemarkov.wordcounter.WordCounter;

public interface IWordCounterPicker {

	Collection<String> pickWords(WordCounter wordCounter, int maxAmount);

}

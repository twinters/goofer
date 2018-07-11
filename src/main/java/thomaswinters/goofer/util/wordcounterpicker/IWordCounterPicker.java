package thomaswinters.goofer.util.wordcounterpicker;

import java.util.Collection;

import thomaswinters.sentencemarkov.wordcounter.WordCounter;

public interface IWordCounterPicker {

	Collection<String> pickWords(WordCounter wordCounter, int maxAmount);

}

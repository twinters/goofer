package thomaswinters.goofer.util.wordcounterpicker;

import java.util.Collection;

import thomaswinters.sentencemarkov.wordcounter.WordCounter;
import thomaswinters.sentencemarkov.wordcounter.WordCounter.WordCounterBuilder;

public class MinCountPicker implements IWordCounterPicker {

	private final int minCount;
	private final IWordCounterPicker picker;

	public MinCountPicker(int minCount, IWordCounterPicker picker) {
		this.minCount = minCount;
		this.picker = picker;
	}

	@Override
	public Collection<String> pickWords(WordCounter wordCounter, int maxAmount) {
		return picker.pickWords(WordCounterBuilder.filterMininum(wordCounter, minCount), maxAmount);
	}

}

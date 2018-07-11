package thomaswinters.goofer.util.wordcounterpicker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import thomaswinters.random.RandomPicker;
import thomaswinters.sentencemarkov.wordcounter.WordCounter;

public class RandomWordsPicker implements IWordCounterPicker {

	@Override
	public Collection<String> pickWords(WordCounter wordCounter, int maxAmount) {
		List<String> elements = new ArrayList<>(wordCounter.getElements());
		if (elements.size() <= maxAmount) {
			return elements;
		}
		return RandomPicker.pickRandomUniqueIndices(maxAmount, elements.size()).stream().map(e -> elements.get(e))
				.collect(Collectors.toList());
	}

}

package be.thomaswinters.goofer.metrics.uni;

import java.util.Optional;

import be.thomaswinters.goofer.metrics.IUniArgumentMetric;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.POS;

/**
 * Class to that implements the single argument metric. It returns the amount of
 * meanings a word has according to the Wordnet library.
 * 
 * It will also need the Part of Speech tag when constructed, as the interface
 * of SingleArgumentMetric doesn't pass a POS-tag.
 * 
 * @author Thomas Winters
 */
public class WordSensesMetric implements IUniArgumentMetric {

	private final Dictionary dictionary;
	private final POS pos;

	/**
	 * Construct the metric to retrieve the amount of senses
	 * 
	 * @param dictionary
	 *            The WordNet database to use
	 * @param pos
	 *            The part of speech that this metric will always use
	 */
	public WordSensesMetric(Dictionary dictionary, POS pos) {
		this.dictionary = dictionary;
		this.pos = pos;
	}

	@Override
	public Optional<Object> rate(String word) {

		IIndexWord idxWord = dictionary.getIndexWord(word, pos);
		if (idxWord == null || idxWord.getWordIDs().isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(idxWord.getWordIDs().size());
	}

	@Override
	public String getName() {
		return "word_senses";
	}

}

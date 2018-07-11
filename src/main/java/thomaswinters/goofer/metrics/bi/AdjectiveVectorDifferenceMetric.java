package thomaswinters.goofer.metrics.bi;

import java.sql.SQLException;
import java.util.Optional;

import thomaswinters.goofer.generators.TwoGramWordCounterCreator;
import thomaswinters.goofer.metrics.IBiArgumentMetric;
import thomaswinters.sentencemarkov.wordcounter.WordCounter;

public class AdjectiveVectorDifferenceMetric implements IBiArgumentMetric {

	/**
	 * Max count of the used wordcounter: such that it can divide this
	 */
	private final TwoGramWordCounterCreator wordGenerator;

	public AdjectiveVectorDifferenceMetric(TwoGramWordCounterCreator wordGenerator) {
		this.wordGenerator = wordGenerator;
	}

	@Override
	public String getName() {
		return "adjective_vector_similarity";
	}

	@Override
	public Optional<? extends Object> rate(String noun1, String noun2) {
		WordCounter adjectives1, adjectives2;
		try {
			adjectives1 = wordGenerator.getAdjectives(noun1);
			adjectives2 = wordGenerator.getAdjectives(noun2);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return Optional.of(adjectives1.getRelativeSimilarWordsAs(adjectives2));
	}

}

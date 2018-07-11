package thomaswinters.goofer.metrics.uni;

import java.util.Optional;

import thomaswinters.goofer.metrics.IUniArgumentMetric;
import thomaswinters.sentencemarkov.wordcounter.WordCounter;

public class OneGramWCMetric implements IUniArgumentMetric {
	private final WordCounter wc;
	private final String name;

	public OneGramWCMetric(WordCounter wc, String name) {
		this.wc = wc;
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Optional<? extends Object> rate(String value) {
		return Optional.of(Math.log(wc.getCount(value)+1));
	}
}

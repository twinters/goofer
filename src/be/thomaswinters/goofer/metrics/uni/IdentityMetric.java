package be.thomaswinters.goofer.metrics.uni;

import java.util.List;
import java.util.Optional;

import be.thomaswinters.goofer.metrics.IUniArgumentMetric;
import weka.core.Attribute;

/**
 * Class used to map the string word as a feature.
 * 
 * @author Thomas Winters
 *
 */
public class IdentityMetric implements IUniArgumentMetric {

	@Override
	public Optional<Object> rate(String word) {
		return Optional.of(word);
	}

	@Override
	public String getName() {
		return "word";
	}

	@Override
	public Attribute getAttribute(String suffix) {
		return new Attribute(getName()+suffix, (List<String>) null);
	}

}

package thomaswinters.goofer.metrics;

import java.util.List;
import java.util.Optional;

import weka.core.Attribute;

public interface ISchemaMetric {
	String getName();

	Optional<? extends Object> rate(List<? extends String> values);

	default Attribute getAttribute(String suffix) {
		return new Attribute(getName() + suffix);
	}

}

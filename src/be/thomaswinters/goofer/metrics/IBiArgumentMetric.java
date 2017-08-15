package be.thomaswinters.goofer.metrics;

import java.util.List;
import java.util.Optional;

public interface IBiArgumentMetric extends ISchemaMetric {

	@Override
	default Optional<? extends Object> rate(List<? extends String> values) {
		if (values.size() != 2) {
			throw new IllegalArgumentException("Can not apply with " + values.size()
					+ " arguments. Only two arguments should be given");
		}
		return rate(values.get(0), values.get(1));
	}

	Optional<? extends Object> rate(String value1, String value2);

}

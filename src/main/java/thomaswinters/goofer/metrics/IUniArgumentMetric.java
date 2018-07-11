package thomaswinters.goofer.metrics;

import java.util.List;
import java.util.Optional;

public interface IUniArgumentMetric extends ISchemaMetric {

	@Override
	default Optional<? extends Object> rate(List<? extends String> values) {
		if (values.size() != 1) {
			throw new IllegalArgumentException("Can not apply with " + values.size()
					+ " arguments. Only one argument should be given");
		}
		return rate(values.get(0));

	}

	Optional<? extends Object> rate(String value);

}

package thomaswinters.goofer.metrics.uni;

import java.util.Arrays;
import java.util.Optional;

import thomaswinters.goofer.metrics.IUniArgumentMetric;
import thomaswinters.goofer.metrics.util.NGramsSQLMetric;
import thomaswinters.googlengrams.NgramMySQLConnector;

public class OneGramSQLMetric extends NGramsSQLMetric implements IUniArgumentMetric {

	/*-********************************************-*
	 *  Constructor
	*-********************************************-*/
	public OneGramSQLMetric(NgramMySQLConnector nGramService) {
		super(nGramService);
	}
	/*-********************************************-*/

	/*-********************************************-*
	 *  ISchemaMetric
	*-********************************************-*/
	@Override
	public String getName() {
		return "frequency";
	}

	@Override
	public Optional<Object> rate(String word) {
		double count = count(Arrays.asList(word));
		if (count > 0) {
			return Optional.of(Math.log(count));
		}
		return Optional.of(count);
	}
	/*-********************************************-*/

}

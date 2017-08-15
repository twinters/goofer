package be.thomaswinters.goofer.metrics.uni;

import java.util.Arrays;
import java.util.Optional;

import be.thomaswinters.goofer.metrics.IUniArgumentMetric;
import be.thomaswinters.goofer.metrics.util.NGramsSQLMetric;
import be.thomaswinters.googlengrams.NgramMySQLConnector;

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

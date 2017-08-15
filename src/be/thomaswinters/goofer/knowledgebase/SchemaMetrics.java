package be.thomaswinters.goofer.knowledgebase;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;

import be.thomaswinters.goofer.data.MetricValues;
import be.thomaswinters.goofer.data.MultiRating;
import be.thomaswinters.goofer.data.TemplateValues;
import be.thomaswinters.goofer.metrics.MetricApplicator;
import be.thomaswinters.goofer.ratingaggregation.IRatingAggregator;

/**
 * A store for all the metrics of the system. This is the prior "knowledgebase"
 * of the system.
 * 
 * @author Thomas Winters
 *
 */
public class SchemaMetrics {
	private final ImmutableList<MetricApplicator> allMetrics;
	private IRatingAggregator ratingAggregator;

	/*-********************************************-*
	 *  Constructor
	*-********************************************-*/

	/**
	 * Creates a new metric store with the given metrics
	 * 
	 * @param allMetrics
	 */
	public SchemaMetrics(List<? extends MetricApplicator> allMetrics, IRatingAggregator ratingAggregator) {
		this.allMetrics = ImmutableList.copyOf(allMetrics);
		this.ratingAggregator = ratingAggregator;
	}

	/*-********************************************-*/

	/*-********************************************-*
	 *  Getters
	*-********************************************-*/
	public ImmutableList<MetricApplicator> getAllMetrics() {
		return allMetrics;
	}

	public int getAmountOfMetrics() {
		return allMetrics.size();
	}

	public int getAmountOfRatings() {
		return 1;
	}

	public IRatingAggregator getRatingAggregator() {
		return ratingAggregator;
	}

	public int getTotalSize() {
		return allMetrics.size() + 1;// 1 for ratingAggregator
	}

	/*-********************************************-*/

	/*-********************************************-*
	 *  Private
	*-********************************************-*/
	private Stream<Optional<Object>> calculateMetricValuesStream(TemplateValues values) {
		return allMetrics.stream().map(e -> e.rate(values).map(f -> (Object) f));
	}
	/*-********************************************-*/

	/*-********************************************-*
	 *  Public calculators
	*-********************************************-*/
	public MetricValues calculateMetricValues(TemplateValues values) {
		return new MetricValues(calculateMetricValuesStream(values));
	}

	public MetricValues calculateMetricValues(MultiRating<TemplateValues> values) {
		return new MetricValues(Stream.concat(calculateMetricValuesStream(values.getElement()),
				Stream.of(Optional.of((Object) getRatingAggregator().calculateRating(values.getRatings())))));
	}

	/*-********************************************-*/

}

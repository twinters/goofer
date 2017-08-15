package be.thomaswinters.goofer.metrics;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.ArrayUtils;

import com.google.common.collect.ImmutableList;

import be.thomaswinters.goofer.data.TemplateValues;
import weka.core.Attribute;

public class MetricApplicator {
	private final ImmutableList<Integer> templateValueIndices;
	private final ISchemaMetric metric;

	/*-********************************************-*
	 *  Constructor
	*-********************************************-*/
	public MetricApplicator(ISchemaMetric metric, List<? extends Integer> templateValueIndices) {
		this.metric = metric;
		this.templateValueIndices = ImmutableList.copyOf(templateValueIndices);
	}

	public MetricApplicator(ISchemaMetric metric, int... values) {
		this(metric, Arrays.asList(ArrayUtils.toObject(values)));
	}

	/*-********************************************-*/

	public Optional<? extends Object> rate(TemplateValues templateValues) {
		return metric.rate(templateValueIndices.stream().map(e -> templateValues.get(e)).collect(Collectors.toList()));
	}

	private String getSuffix() {
		return "_"+templateValueIndices.stream().map(e -> e.toString()).collect(Collectors.joining("_"));
	}

	public Attribute getAttribute() {
		return metric.getAttribute(getSuffix());
	}
}

package be.thomaswinters.goofer.data;

public class RatedFeatures {
	private final MultiRating<TemplateValues> ratedTemplateValues;
	private final MetricValues featureValues;

	public RatedFeatures(MultiRating<TemplateValues> ratedTemplateValues, MetricValues featureValues) {
		this.ratedTemplateValues = ratedTemplateValues;
		this.featureValues = featureValues;
	}

	public MultiRating<TemplateValues> getRatedTemplateValues() {
		return ratedTemplateValues;
	}

	public MetricValues getMetricValues() {
		return featureValues;
	}

}

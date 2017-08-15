package be.thomaswinters.goofer.ratingaggregation;

import java.util.Collection;

import weka.core.Attribute;

public class AverageRatingAggregator implements IRatingAggregator {

	public static double calculateAverage(Collection<Integer> ratings) {
		return ratings.stream().mapToDouble(e -> (double) e).sum() / (double) ratings.size();
	}
	
	@Override
	public Object calculateRating(Collection<Integer> jokeRatings) {
		return calculateAverage(jokeRatings);
	}

	/**
	 * Returns the casted double.
	 */
	@Override
	public double toRating(double classificationResult) {
		return classificationResult;
	}

	@Override
	public Attribute getAttribute() {
		return new Attribute("average rating");
	}

}

package thomaswinters.goofer.ratingaggregation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import thomaswinters.goofer.data.MultiRating;
import weka.core.Attribute;

public interface IRatingAggregator {
	default Object calculateRating(MultiRating<String> joke) {
		return calculateRating(joke.getRatings());
	}

	default List<Integer> getPossibleValues() {
		return new ArrayList<Integer>();
	}

	Object calculateRating(Collection<Integer> ratings);

	Attribute getAttribute();

	double toRating(double classificationResult);
}

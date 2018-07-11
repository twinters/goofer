package be.thomaswinters.goofer.ratingaggregation;

import be.thomaswinters.goofer.data.MultiRating;
import weka.core.Attribute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

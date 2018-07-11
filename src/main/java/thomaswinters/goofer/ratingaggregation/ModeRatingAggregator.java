package thomaswinters.goofer.ratingaggregation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;

import weka.core.Attribute;

public class ModeRatingAggregator implements IRatingAggregator {

	private final int minRating;
	private final int maxRating;
	private final Attribute attribute;

	public ModeRatingAggregator(int minRating, int maxRating) {
		this.minRating = minRating;
		this.maxRating = maxRating;

		attribute = new Attribute("mode rating",
				getPossibleValues().stream().map(e -> prefix + e.toString()).collect(Collectors.toList()));
	}

	@Override
	public List<Integer> getPossibleValues() {
		return IntStream.rangeClosed(minRating, maxRating).mapToObj(e -> (Integer) e).collect(Collectors.toList());
	}

	private final String prefix = "rating";

	@Override
	public Object calculateRating(Collection<Integer> jokeRatings) {
		Multiset<Integer> ratings = HashMultiset.create();

		for (Integer rating : jokeRatings) {
			ratings.add(rating);
		}

		Multiset<Integer> sortedRatings = Multisets.copyHighestCountFirst(ratings);
		List<Integer> modes = new ArrayList<Integer>();
		int highestAmount = sortedRatings.entrySet().iterator().next().getCount();
		for (Integer rating : sortedRatings) {
			if (sortedRatings.count(rating) != highestAmount) {
				break;
			}
			modes.add(rating);
		}

		return prefix + (int) modes.get(0);
	}

	@Override
	public Attribute getAttribute() {
		return attribute;
	}

	@Override
	public double toRating(double classificationResult) {
		String prediction = getAttribute().value((int) classificationResult);
		// return (double)classificationResult;
		String number = prediction.substring(prefix.length());
		return Integer.parseInt(number);
	}

}

package be.thomaswinters.goofer.util.argumentconverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

import be.thomaswinters.goofer.ratingaggregation.AverageRatingAggregator;
import be.thomaswinters.goofer.ratingaggregation.IRatingAggregator;
import be.thomaswinters.goofer.ratingaggregation.ModeRatingAggregator;

public class RatingAggregatorConverter implements IStringConverter<IRatingAggregator> {

	@Override
	public IRatingAggregator convert(String value) {
		String[] splitted = value.split(":");
		String type = splitted[0];
		List<String> arguments;
		if (splitted.length > 1) {
			arguments = Arrays.asList(splitted).subList(1, splitted.length);
		} else {
			arguments = new ArrayList<>();
		}

		switch (type.toLowerCase()) {
		case "mode":
			int min = 1;
			int max = 5;
			if (arguments.size() >= 2) {
				min = Integer.parseInt(arguments.get(0));
				max = Integer.parseInt(arguments.get(1));
//				System.out.println(arguments);
//				throw new ParameterException(
//						"Please specify the min and maximum for the mode rating aggregator, seperated by colons(:) between the aggregator, min and max");
			}
			return new ModeRatingAggregator(min,max);
		case "average":
			return new AverageRatingAggregator();
		default:
			throw new ParameterException("Unknown rating aggregator type '" + type + "'.");
		}
	}

}

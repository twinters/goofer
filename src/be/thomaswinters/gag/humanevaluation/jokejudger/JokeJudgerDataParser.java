package be.thomaswinters.gag.humanevaluation.jokejudger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import be.thomaswinters.goofer.data.MultiRating;
import be.thomaswinters.goofer.data.TemplateValues;

/**
 * This parser is used to parse the data gathered from the JokeJudgerScraper. It
 * results in a set of rated template values.
 * 
 * @author Thomas Winters
 *
 */
public class JokeJudgerDataParser {
	public List<MultiRating<TemplateValues>> parse(File file) throws IOException {
		List<String> lines = Files.readAllLines(file.toPath());

		// Throw away header
		lines = lines.subList(1, lines.size());

		return lines.stream()
				// Lines as list of arguments
				.map(e -> Arrays.asList(e.split("\t")))
				// As rated joke
				.map(e -> new MultiRating<TemplateValues>(
						new TemplateValues(Arrays.asList(e.get(0), e.get(1), e.get(2))), scoresToList(e.get(3))))
				.collect(Collectors.toList());

	}

	private List<Integer> scoresToList(String scoreString) {
		// Parse off the brackets
		scoreString = scoreString.substring(1, scoreString.length() - 1);
		if (scoreString.equals("[null]")) {
			return new ArrayList<>();
		}

		return Arrays.asList(scoreString.split(",")).stream().map(e -> Integer.parseInt(e))
				.collect(Collectors.toList());
	}

}

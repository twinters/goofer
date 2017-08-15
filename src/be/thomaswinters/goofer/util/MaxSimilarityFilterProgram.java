package be.thomaswinters.goofer.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import be.thomaswinters.goofer.data.TemplateValues;
import be.thomaswinters.goofer.outputfilter.SimilarValuesFilter;

public class MaxSimilarityFilterProgram {
	private final SimilarValuesFilter filter;

	public MaxSimilarityFilterProgram(int maxSimilarity) {
		this.filter = new SimilarValuesFilter(maxSimilarity);
	}

	public void filter(File file) throws IOException {
		List<String> lines = Files.readAllLines(file.toPath());
		List<TemplateValues> values = lines.stream().map(e -> new TemplateValues(e.split("\t"))).filter(e -> {
			boolean result = filter.isDifferentEnough(e);
			if (!result) {
				System.out.println("Not enough: " + e);
			}
			return result;
		}).collect(Collectors.toList());

		System.out.println("\n\nAccepted");

		System.out.println(values.stream().map(e -> e.getValues().stream().collect(Collectors.joining("\t"))).collect(Collectors.joining("\n")));
	}

	public static void main(String[] args) throws NumberFormatException, IOException {
		String file = args[0];
		String maxSimilarity = args[1];
		new MaxSimilarityFilterProgram(Integer.parseInt(maxSimilarity)).filter(new File(file));
	}

}

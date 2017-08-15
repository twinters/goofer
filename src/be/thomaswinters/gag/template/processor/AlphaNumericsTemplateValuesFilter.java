package be.thomaswinters.gag.template.processor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import be.thomaswinters.goofer.data.TemplateValues;

public class AlphaNumericsTemplateValuesFilter implements ITemplateValuesProcessor {

	@Override
	public List<TemplateValues> process(TemplateValues templateValues) {
		return Arrays.asList(new TemplateValues(templateValues.getValues().stream().map(this::filter).collect(Collectors.toList())));
	}
	
	public String filter(String input) {
		return input.replaceAll("[^\\p{L}\\p{N} -]+", "");
	}

}

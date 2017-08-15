package be.thomaswinters.gag.template.processor;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import be.thomaswinters.goofer.data.MultiRating;
import be.thomaswinters.goofer.data.TemplateValues;

public interface ITemplateValuesProcessor {
	default List<MultiRating<TemplateValues>> process(Collection<MultiRating<TemplateValues>> ratedTemplateValues) {
		return ratedTemplateValues.stream().flatMap(e -> process(e).stream()).collect(Collectors.toList());
	}

	default List<MultiRating<TemplateValues>> process(MultiRating<TemplateValues> ratedTemplateValues) {
		return process(ratedTemplateValues.getElement()).stream()
				.map(e -> new MultiRating<TemplateValues>(e, ratedTemplateValues.getRatings()))
				.collect(Collectors.toList());
	}

	List<TemplateValues> process(TemplateValues templateValues);
}

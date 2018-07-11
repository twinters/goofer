package thomaswinters.goofer.templateextraction;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import thomaswinters.goofer.data.MultiRating;
import thomaswinters.goofer.data.Template;
import thomaswinters.goofer.data.TemplateValues;
import thomaswinters.goofer.data.exceptions.UnextractableStringException;

public interface ITemplateStripper {
	Template getTemplate();

	default List<MultiRating<TemplateValues>> extract(Collection<MultiRating<String>> jokes) {
		return jokes.stream().map(this::extract).collect(Collectors.toList());
	}

	default MultiRating<TemplateValues> extract(MultiRating<String> joke) throws UnextractableStringException {
		return extract(joke.getElement(), joke.getRatings());
	}

	default MultiRating<TemplateValues> extract(String joke, Collection<Integer> ratings)
			throws UnextractableStringException {
		return new MultiRating<TemplateValues>(extract(joke), ratings);
	}

	TemplateValues extract(String joke);
}

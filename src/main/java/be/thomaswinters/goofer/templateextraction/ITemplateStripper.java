package be.thomaswinters.goofer.templateextraction;

import be.thomaswinters.goofer.data.MultiRating;
import be.thomaswinters.goofer.data.Template;
import be.thomaswinters.goofer.data.TemplateValues;
import be.thomaswinters.goofer.data.exceptions.UnextractableStringException;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

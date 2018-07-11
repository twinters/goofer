package be.thomaswinters.goofer.generators;

import be.thomaswinters.goofer.data.TemplateValues;

import java.util.ArrayList;
import java.util.stream.Stream;

public class EmptyTemplateValuesGenerator implements ITemplateValuesGenerator {

    private final int templateSize;

    public EmptyTemplateValuesGenerator(int templateSize) {
        this.templateSize = templateSize;
    }

    @Override
    public Stream<TemplateValues> generateStream(PartialTemplateValues seed) {
        return new ArrayList<TemplateValues>().stream();
    }

    @Override
    public PartialTemplateValues emptySeed() {
        return new PartialTemplateValues(templateSize);
    }

}

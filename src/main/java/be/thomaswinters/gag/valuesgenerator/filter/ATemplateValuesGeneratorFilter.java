package be.thomaswinters.gag.valuesgenerator.filter;

import be.thomaswinters.goofer.data.TemplateValues;
import be.thomaswinters.goofer.generators.ITemplateValuesGenerator;
import be.thomaswinters.goofer.generators.PartialTemplateValues;

import java.util.stream.Stream;

public abstract class ATemplateValuesGeneratorFilter implements ITemplateValuesGenerator {

    private final ITemplateValuesGenerator generator;

    public ATemplateValuesGeneratorFilter(ITemplateValuesGenerator generator) {
        this.generator = generator;
    }

    @Override
    public Stream<TemplateValues> generateStream(PartialTemplateValues seed) {
        return generator.generateStream(seed).filter(this::isAllowed);
    }

    protected abstract boolean isAllowed(TemplateValues values);

    @Override
    public PartialTemplateValues emptySeed() {
        return generator.emptySeed();
    }
}

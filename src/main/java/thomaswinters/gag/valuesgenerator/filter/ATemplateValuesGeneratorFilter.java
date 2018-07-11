package thomaswinters.gag.valuesgenerator.filter;

import java.util.stream.Stream;

import thomaswinters.goofer.data.TemplateValues;
import thomaswinters.goofer.generators.ITemplateValuesGenerator;
import thomaswinters.goofer.generators.PartialTemplateValues;

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

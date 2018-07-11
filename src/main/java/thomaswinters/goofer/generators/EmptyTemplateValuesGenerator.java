package thomaswinters.goofer.generators;

import java.util.ArrayList;
import java.util.stream.Stream;

import thomaswinters.goofer.data.TemplateValues;

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

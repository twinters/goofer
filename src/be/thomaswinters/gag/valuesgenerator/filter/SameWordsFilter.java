package be.thomaswinters.gag.valuesgenerator.filter;

import be.thomaswinters.goofer.data.TemplateValues;
import be.thomaswinters.goofer.generators.ITemplateValuesGenerator;

public class SameWordsFilter extends ATemplateValuesGeneratorFilter {

	public SameWordsFilter(ITemplateValuesGenerator generator) {
		super(generator);
	}

	/**
	 * Checks if all elements are distinct
	 */
	@Override
	protected boolean isAllowed(TemplateValues values) {
		return values.getValues().stream().distinct().count() == values.getArity();
	}

}

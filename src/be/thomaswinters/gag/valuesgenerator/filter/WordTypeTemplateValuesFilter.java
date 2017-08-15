package be.thomaswinters.gag.valuesgenerator.filter;

import java.util.List;

import com.google.common.collect.ImmutableList;

import be.thomaswinters.goofer.data.TemplateValues;
import be.thomaswinters.goofer.generators.ITemplateValuesGenerator;
import be.thomaswinters.pos.WordTypeCalculator;
import edu.mit.jwi.item.POS;

public class WordTypeTemplateValuesFilter extends ATemplateValuesGeneratorFilter {
	private final WordTypeCalculator wordTypeCalculator;
	private final List<POS> allowedPOS;

	public WordTypeTemplateValuesFilter(ITemplateValuesGenerator innerGenerator, WordTypeCalculator wordTypeCalculator,
			List<POS> allowedPOS) {
		super(innerGenerator);
		this.wordTypeCalculator = wordTypeCalculator;
		this.allowedPOS = ImmutableList.copyOf(allowedPOS);
	}

	/**
	 * Final check to see if they are really adjective-adjective-noun
	 * 
	 * @param templateValues
	 * @return
	 */
	private boolean isAccordingToTemplate(TemplateValues templateValues) {
		for (int i = 0; i < templateValues.getArity(); i++) {
			if (!wordTypeCalculator.getWordTypes(templateValues.get(i)).contains(allowedPOS.get(i))) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected boolean isAllowed(TemplateValues values) {
		return isAccordingToTemplate(values);
	}
}

package be.thomaswinters.gag.template.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableList;

import be.thomaswinters.goofer.data.TemplateValues;

/**
 * Composite class processing template values using other templatevalues
 * processors
 * 
 * @author Thomas Winters
 *
 */
public class MultiTemplateValuesProcessor implements ITemplateValuesProcessor {

	private final ImmutableList<ITemplateValuesProcessor> processors;

	/*-********************************************-*
	 *  Constructors
	*-********************************************-*/
	public MultiTemplateValuesProcessor(List<? extends ITemplateValuesProcessor> processors) {
		this.processors = ImmutableList.copyOf(processors);
	}

	public MultiTemplateValuesProcessor(ITemplateValuesProcessor... processors) {
		this(Arrays.asList(processors));
	}
	/*-********************************************-*/

	@Override
	public List<TemplateValues> process(TemplateValues templateValues) {
		List<TemplateValues> values = Arrays.asList(templateValues);
		for (ITemplateValuesProcessor processor : processors) {
			List<TemplateValues> newValues = new ArrayList<>();
			for (TemplateValues value : values) {
				newValues.addAll(processor.process(value));
			}
			values = newValues;
		}
//		System.out.println("FROM: " + templateValues);
//		System.out.println("TO:   " + values.stream().map(e->e.toString()).collect(Collectors.joining("\n      "))+"\n\n");
		return values;
	}

}

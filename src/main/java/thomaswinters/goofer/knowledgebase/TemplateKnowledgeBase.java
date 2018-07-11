package thomaswinters.goofer.knowledgebase;

import thomaswinters.gag.template.processor.ITemplateValuesProcessor;
import thomaswinters.goofer.generators.ITemplateValuesGenerator;

/**
 * Simple datastructur to represent the knowledgebase about certain kind of
 * template fillers. Mainly used to return from functions creating this for
 * specific kind of jokes.
 * 
 * It has SchemaMetric to rate templates and TemplatedValueGenerators for
 * generating possible values
 * 
 * @author Thomas Winters
 *
 */
public class TemplateKnowledgeBase {
	private final ITemplateValuesProcessor templatevaluesProcessor;
	private final SchemaMetrics schemaMetrics;
	private final ITemplateValuesGenerator valueGenerator;

	/*-********************************************-*
	 *  Constructor
	*-********************************************-*/

	public TemplateKnowledgeBase(ITemplateValuesProcessor templatevaluesProcessor, SchemaMetrics schemaMetrics,
			ITemplateValuesGenerator valueGenerator) {
		super();
		this.templatevaluesProcessor = templatevaluesProcessor;
		this.schemaMetrics = schemaMetrics;
		this.valueGenerator = valueGenerator;
	}

	/*-********************************************-*/

	/*-********************************************-*
	 *  Getters
	*-********************************************-*/

	public SchemaMetrics getSchemaMetrics() {
		return schemaMetrics;
	}

	public ITemplateValuesGenerator getValueGenerator() {
		return valueGenerator;
	}

	public ITemplateValuesProcessor getTemplatevaluesProcessor() {
		return templatevaluesProcessor;
	}

	/*-********************************************-*/
}
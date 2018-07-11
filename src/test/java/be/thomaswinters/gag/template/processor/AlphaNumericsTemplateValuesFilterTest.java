package thomaswinters.gag.template.processor;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import thomaswinters.gag.template.processor.AlphaNumericsTemplateValuesFilter;
import thomaswinters.gag.template.processor.ITemplateValuesProcessor;
import thomaswinters.goofer.data.TemplateValues;

public class AlphaNumericsTemplateValuesFilterTest {

	@Test
	public void test() {
		ITemplateValuesProcessor processor = new AlphaNumericsTemplateValuesFilter();
		TemplateValues expected = new TemplateValues("Hello", "Its", "me the guy yóú know as Thómàçs-Winters");
		assertEquals(expected, processor
				.process(new TemplateValues("Hello!", "It's", "me, the guy yóú know as Thómàçs-Winters")).get(0));
	}

}

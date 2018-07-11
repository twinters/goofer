package be.thomaswinters.gag.template.processor;

import org.junit.Test;
import be.thomaswinters.gag.template.processor.AlphaNumericsTemplateValuesFilter;
import be.thomaswinters.gag.template.processor.ITemplateValuesProcessor;
import be.thomaswinters.goofer.data.TemplateValues;

import static org.junit.Assert.assertEquals;

public class AlphaNumericsTemplateValuesFilterTest {

    @Test
    public void test() {
        ITemplateValuesProcessor processor = new AlphaNumericsTemplateValuesFilter();
        TemplateValues expected = new TemplateValues("Hello", "Its", "me the guy yóú know as Thómàçs-Winters");
        assertEquals(expected, processor
                .process(new TemplateValues("Hello!", "It's", "me, the guy yóú know as Thómàçs-Winters")).get(0));
    }

}

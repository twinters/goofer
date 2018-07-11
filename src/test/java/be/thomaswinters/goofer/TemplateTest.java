package be.thomaswinters.goofer;

import org.junit.Test;
import be.thomaswinters.goofer.data.Template;
import be.thomaswinters.goofer.data.TemplateValues;

import static org.junit.Assert.assertEquals;

public class TemplateTest {

    @Test
    public void apply_test() {
        Template template = new Template("Hello ", ", I'm ", ".");
        assertEquals("Hello John, I'm Thomas.", template.apply(new TemplateValues("John", "Thomas")));
    }

}

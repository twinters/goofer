package thomaswinters.goofer;

import static org.junit.Assert.*;

import org.junit.Test;

import thomaswinters.goofer.data.Template;
import thomaswinters.goofer.data.TemplateValues;

public class TemplateTest {

	@Test
	public void apply_test() {
		Template template = new Template("Hello ", ", I'm ", ".");
		assertEquals("Hello John, I'm Thomas.", template.apply(new TemplateValues("John","Thomas")));
	}

}

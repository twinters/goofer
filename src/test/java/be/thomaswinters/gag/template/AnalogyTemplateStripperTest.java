package be.thomaswinters.gag.template;

import org.junit.Before;
import org.junit.Test;
import be.thomaswinters.gag.template.AnalogyTemplateStripper;

import static org.junit.Assert.assertTrue;

public class AnalogyTemplateStripperTest {

    private AnalogyTemplateStripper parser;

    @Before
    public void setup() {
        parser = new AnalogyTemplateStripper();
    }

    @Test
    public void sentencetypes_test() {
        assertTrue(parser.extract("I like my women like I like my coffee, black").get(2).equals("black"));
        assertTrue(parser.extract("I like my women like I like my coffee: black").get(2).equals("black"));
        assertTrue(parser.extract("I like my women like I like my coffee... black").get(2).equals("black"));
        assertTrue(parser.extract("I like my women like I like my coffee. black").get(2).equals("black"));
        assertTrue(parser.extract("I like my women like I like my coffee; black").get(2).equals("black"));
    }

}

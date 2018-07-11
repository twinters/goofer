package be.thomaswinters.goofer.util;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.item.POS;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import org.junit.BeforeClass;
import org.junit.Test;
import be.thomaswinters.pos.WordTypeCalculator;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class WordTypeCalculatorTest {

    private static Dictionary dict;
    private static WordTypeCalculator calculator;
    private static MaxentTagger tagger;

    @BeforeClass
    public static void setup() throws IOException {
        dict = new Dictionary(new File("./data/wordnet/"));
        dict.open();
        tagger = new MaxentTagger("data/stanford-pos/english-bidirectional-distsim.tagger");
        calculator = new WordTypeCalculator(dict, tagger);
    }

    @Test
    public void big_test() {
        List<String> words = Arrays.asList("accomplish", "player", "play", "test", "tester", "beautiful", "cute");
        List<List<POS>> expected = Arrays.asList(
                // Accomplish
                Arrays.asList(POS.VERB),
                // Player
                Arrays.asList(POS.NOUN),
                // Play
                Arrays.asList(POS.NOUN, POS.VERB),
                // Test
                Arrays.asList(POS.NOUN, POS.VERB),
                // Tester
                Arrays.asList(POS.NOUN),
                // Beautiful
                Arrays.asList(POS.ADJECTIVE),
                // Cute
                Arrays.asList(POS.ADJECTIVE));

        for (int i = 0; i < words.size(); i++) {
            assertEquals(new HashSet<>(expected.get(i)), calculator.getWordTypes(words.get(i)));
        }
    }

    @Test
    public void new_cases() {
        assertEquals(convert(POS.NOUN), calculator.getWordTypes("woman"));
        assertEquals(convert(POS.NOUN), calculator.getWordTypes("women"));
    }

    public Collection<POS> convert(POS... pos) {
        return new HashSet<POS>(Arrays.asList(pos));
    }


}

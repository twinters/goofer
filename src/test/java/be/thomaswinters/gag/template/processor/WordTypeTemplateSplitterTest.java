package be.thomaswinters.gag.template.processor;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.item.POS;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import org.junit.BeforeClass;
import org.junit.Test;
import be.thomaswinters.goofer.data.TemplateValues;
import be.thomaswinters.pos.WordTypeCalculator;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class WordTypeTemplateSplitterTest {
    private static WordTypeTemplateSplitter splitter;
    private static ITemplateValuesProcessor alphaNumericAndWordTypeSplitter;

    @BeforeClass
    public static void beforeClass() throws IOException {
        Dictionary dict = new Dictionary(new File("./data/wordnet/"));
        MaxentTagger tagger = new MaxentTagger("models/english-bidirectional-distsim.tagger");
        dict.open();
        splitter = new WordTypeTemplateSplitter(new WordTypeCalculator(dict, tagger), POS.NOUN, POS.NOUN,
                POS.ADJECTIVE);
        alphaNumericAndWordTypeSplitter = new MultiTemplateValuesProcessor(new AlphaNumericsTemplateValuesFilter(),
                splitter);
    }

    @Test
    public void identity_test() {
        TemplateValues input = new TemplateValues("coffee", "woman", "black");
        List<TemplateValues> expected = Arrays.asList(input);
        assertEquals(expected, splitter.process(input));
    }

    @Test
    public void identity_plural_test() {
        TemplateValues input = new TemplateValues("coffee", "women", "black");
        List<TemplateValues> expected = Arrays.asList(input);
        assertEquals(expected, splitter.process(input));
    }

    @Test
    public void three_adjectives_test() {
        TemplateValues input = new TemplateValues("coffee", "women", "sweet, black and milky");
        Collection<TemplateValues> expected = convert(new TemplateValues("coffee", "women", "sweet"),
                new TemplateValues("coffee", "women", "black"), new TemplateValues("coffee", "women", "milky"));
        assertEquals(expected, convert(alphaNumericAndWordTypeSplitter.process(input)));
    }

    @Test
    public void three_adjectives_two_nouns_test() {
        TemplateValues input = new TemplateValues("coffee and tea", "women", "sweet, black and milky");
        Collection<TemplateValues> expected = convert(new TemplateValues("coffee", "women", "sweet"),
                new TemplateValues("coffee", "women", "black"), new TemplateValues("coffee", "women", "milky"),
                new TemplateValues("tea", "women", "sweet"),
                new TemplateValues("tea", "women", "black"), new TemplateValues("tea", "women", "milky"));
        assertEquals(expected, convert(alphaNumericAndWordTypeSplitter.process(input)));
    }


    public Collection<TemplateValues> convert(Collection<TemplateValues> tvs) {
        return new HashSet<>(tvs);
    }

    public Collection<TemplateValues> convert(TemplateValues... tvs) {
        return convert(Arrays.asList(tvs));
    }

}

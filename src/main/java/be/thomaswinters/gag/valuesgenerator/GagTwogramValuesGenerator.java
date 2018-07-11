package be.thomaswinters.gag.valuesgenerator;

import be.thomaswinters.goofer.data.TemplateValues;
import be.thomaswinters.goofer.generators.ITemplateValuesGenerator;
import be.thomaswinters.goofer.generators.PartialTemplateValues;
import be.thomaswinters.goofer.generators.TwoGramWordCounterCreator;
import be.thomaswinters.goofer.util.wordcounterpicker.IWordCounterPicker;
import be.thomaswinters.goofer.util.wordcounterpicker.MinCountPicker;
import be.thomaswinters.goofer.util.wordcounterpicker.RandomWordsPicker;
import be.thomaswinters.wordcounter.WordCounter;

import java.sql.SQLException;
import java.util.Collection;
import java.util.stream.Stream;

public class GagTwogramValuesGenerator implements ITemplateValuesGenerator {

    private final TwoGramWordCounterCreator creator;
    private final int maxDifferentAdjectives;
    private final int maxDifferentNouns;
    private final IWordCounterPicker randomPicker;

    public GagTwogramValuesGenerator(TwoGramWordCounterCreator creator, int maxAdj, int maxNoun, int minTwogramCount) {
        this.creator = creator;
        this.maxDifferentAdjectives = maxAdj;
        this.maxDifferentNouns = maxNoun;
        this.randomPicker = new MinCountPicker(minTwogramCount, new RandomWordsPicker());
    }

    @Override
    public Stream<TemplateValues> generateStream(PartialTemplateValues partialTv) {
        if (!partialTv.isPresent(0)) {
            throw new IllegalArgumentException("First word must be filled in into the seed!");
        }
        String seed = partialTv.get(0).get();

        return getAdjectives(seed).stream().flatMap(
                adjective -> getNouns(adjective).stream().map(noun -> new TemplateValues(seed, noun, adjective)));

    }


    private Collection<String> getAdjectives(String noun) {
        try {
            return getRandom(creator.getAdjectives(noun), maxDifferentAdjectives);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Collection<String> getNouns(String adjective) {
        try {
            return getRandom(creator.getNouns(adjective), maxDifferentNouns);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Collection<String> getRandom(WordCounter wc, int maxAmount) {
        if (wc.getElements().size() < maxAmount) {
            return wc.getElements();
        }
        return randomPicker.pickWords(wc, maxAmount);
    }

    @Override
    public PartialTemplateValues emptySeed() {
        return new PartialTemplateValues(3);
    }

}

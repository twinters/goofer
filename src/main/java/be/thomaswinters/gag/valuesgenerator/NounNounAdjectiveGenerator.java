package be.thomaswinters.gag.valuesgenerator;

import com.google.common.collect.Sets;
import be.thomaswinters.goofer.data.TemplateValues;
import be.thomaswinters.goofer.generators.ITemplateValuesGenerator;
import be.thomaswinters.goofer.generators.PartialTemplateValues;
import be.thomaswinters.goofer.generators.string.IRelatedStringGenerator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NounNounAdjectiveGenerator implements ITemplateValuesGenerator {
    private final IRelatedStringGenerator adjectivesGen;
    private final IRelatedStringGenerator nounGen;
    private final boolean useAdjectiveIntersection = false;

    /*-********************************************-*
     *  Private generators
     *-********************************************-*/

    public NounNounAdjectiveGenerator(IRelatedStringGenerator adjectivesGen, IRelatedStringGenerator nounGen) {
        this.adjectivesGen = adjectivesGen;
        this.nounGen = nounGen;
    }

    private Collection<String> generateAdjectiveForNoun(String noun) {
        try {
            return adjectivesGen.generate(noun);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Collection<String> generateNounForNoun(String noun) {
        try {
            Collection<String> result = nounGen.generate(noun);

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Collection<PartialTemplateValues> fillXforY(PartialTemplateValues partialWithY) {
        assert partialWithY.get(1).isPresent();
        return generateNounForNoun(partialWithY.get(1).get()).stream().map(e -> partialWithY.fillIn(0, e))
                .collect(Collectors.toSet());
    }

    private Collection<PartialTemplateValues> fillYforX(PartialTemplateValues partialWithX) {
        assert partialWithX.get(0).isPresent();
        return generateNounForNoun(partialWithX.get(0).get()).stream().map(e -> partialWithX.fillIn(1, e))
                .collect(Collectors.toSet());
    }

    private Stream<PartialTemplateValues> fillZ(PartialTemplateValues partialWithYandX) {
        assert partialWithYandX.get(0).isPresent() && partialWithYandX.get(1).isPresent();
        Set<String> possibleAdjectives = new HashSet<>();

        // Extract x and y
        String x = partialWithYandX.get(0).get();
        String y = partialWithYandX.get(1).get();

        // Add all possible
        Set<String> xAdjectives = new HashSet<>(generateAdjectiveForNoun(x));
        Set<String> yAdjectives = new HashSet<>(generateAdjectiveForNoun(y));
        System.out.println("Calculated for x and y");

        // Calculate possible
        if (useAdjectiveIntersection) {
            // Intersection
            possibleAdjectives = Sets.intersection(xAdjectives, yAdjectives);
            // possibleAdjectives.retainAll(yAdjectives);
        } else {
            // Union
            possibleAdjectives = Sets.union(xAdjectives, yAdjectives);
            // possibleAdjectives.addAll(yAdjectives);
        }

        System.out.println("Possible adjectives for " + partialWithYandX + ": " + possibleAdjectives);

        return possibleAdjectives.stream().map(e -> partialWithYandX.fillIn(2, e));

    }

    /*-********************************************-*/

    /*-********************************************-*
     *  ITemplateValueGenerator
     *-********************************************-*/

    @Override
    public Stream<TemplateValues> generateStream(PartialTemplateValues seed) {

        Collection<PartialTemplateValues> possibleTemplateValues = new HashSet<PartialTemplateValues>();
        if (!seed.get(0).isPresent() && !seed.get(1).isPresent()) {
            // maybe later allow this if necessary
            throw new IllegalStateException("Needs a noun to start generating.\nGiven: " + seed);
        }

        // Complete the nouns
        if (seed.get(0).isPresent() && !seed.get(1).isPresent()) {
            possibleTemplateValues = fillYforX(seed);
        } else if (!seed.get(0).isPresent() && seed.get(1).isPresent()) {
            possibleTemplateValues = fillXforY(seed);
        } else {
            possibleTemplateValues.add(seed);
        }

        System.out.println("Completed for nouns: " + possibleTemplateValues);

        // Complete the adjectives
        return possibleTemplateValues.stream().flatMap(e -> fillZ(e))
                // Convert to complete template values
                .map(e -> e.toTemplateValues());
    }

    @Override
    public PartialTemplateValues emptySeed() {
        return new PartialTemplateValues(3);
    }

    /*-********************************************-*/
}

package be.thomaswinters.goofer.generators;

import be.thomaswinters.goofer.data.TemplateValues;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface ITemplateValuesGenerator {
    Stream<TemplateValues> generateStream(PartialTemplateValues seed);

    default Collection<TemplateValues> generateAll(PartialTemplateValues seed) {
        return generateStream(seed).collect(Collectors.toSet());
    }

    default Collection<TemplateValues> generateAll(List<String> seedWords) {
        PartialTemplateValues seed = emptySeed();
        seed.fillIn(0, seedWords);
        return generateAll(seed);
    }

    default Collection<TemplateValues> generateAll(String seed) {
        return generateAll(Arrays.asList(seed));
    }

    // PartialTemplateValues seedToTemplateValues(String seed);
    //
    // PartialTemplateValues randomSeed();
    //
    PartialTemplateValues emptySeed();
}

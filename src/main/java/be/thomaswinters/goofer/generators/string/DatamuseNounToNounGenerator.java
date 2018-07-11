package be.thomaswinters.goofer.generators.string;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DatamuseNounToNounGenerator implements IRelatedStringGenerator {

    protected final IRelatedStringGenerator nounGen;
    protected final IRelatedStringGenerator adjectiveGen;

    public DatamuseNounToNounGenerator(IRelatedStringGenerator nounGen, IRelatedStringGenerator adjectiveGen) {
        this.nounGen = nounGen;
        this.adjectiveGen = adjectiveGen;
    }

    protected Set<String> getRelatedStrings(String x) throws Exception {
        Collection<String> adjectiveVectorX = adjectiveGen.generate(x);

        Set<String> candidates = new HashSet<>();
        for (String a : adjectiveVectorX) {
            Collection<String> substantivesUsedWithA = nounGen.generate(a);
            candidates.addAll(substantivesUsedWithA);

        }
        return candidates;

    }

    @Override
    public Collection<String> generate(String relatedWord) throws Exception {
        return getRelatedStrings(relatedWord);
    }

}
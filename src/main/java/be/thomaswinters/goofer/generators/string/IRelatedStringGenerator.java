package be.thomaswinters.goofer.generators.string;

import java.util.Collection;

public interface IRelatedStringGenerator {
    Collection<String> generate(String relatedWord) throws Exception;
}

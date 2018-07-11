package thomaswinters.goofer.generators.string;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RelatedStringCache implements IRelatedStringGenerator {
	private final IRelatedStringGenerator generator;
	private final Map<String, Collection<String>> cache = new HashMap<>();

	public RelatedStringCache(IRelatedStringGenerator generator) {
		this.generator = generator;
	}

	@Override
	public Collection<String> generate(String word) throws Exception {
		if (cache.containsKey(word)) {
			return cache.get(word);
		}
		Collection<String> result = generator.generate(word);
		cache.put(word, result);
		return result;
	}

}

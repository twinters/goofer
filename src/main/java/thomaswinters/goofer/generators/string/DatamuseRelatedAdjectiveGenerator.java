package thomaswinters.goofer.generators.string;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import thomaswinters.datamuse.connection.DatamuseCaller;
import thomaswinters.datamuse.data.DatamuseWord;
import thomaswinters.datamuse.query.DatamuseQuery;

public class DatamuseRelatedAdjectiveGenerator implements IRelatedStringGenerator {

	private final DatamuseQuery basicQuery = new DatamuseQuery().maximumAnswers(1000);
	protected final DatamuseCaller caller;

	public DatamuseRelatedAdjectiveGenerator(DatamuseCaller caller) {
		this.caller = caller;
	}

	protected List<DatamuseWord> getRelatedDatamuseWords(String noun) throws IOException {
		DatamuseQuery query = basicQuery.relatedAdjectiveFor(noun);
		List<DatamuseWord> result = caller.call(query);
		return result;
	}

	@Override
	public List<String> generate(String relatedWord) throws Exception {
		return getRelatedDatamuseWords(relatedWord).stream().map(e -> e.getWord()).collect(Collectors.toList());
	}

}
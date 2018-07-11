package be.thomaswinters.goofer.generators.string;

import be.thomaswinters.datamuse.connection.DatamuseCaller;
import be.thomaswinters.datamuse.data.DatamuseWord;
import be.thomaswinters.datamuse.query.DatamuseQuery;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class DatamuseRelatedAdjectiveGenerator implements IRelatedStringGenerator {

    protected final DatamuseCaller caller;
    private final DatamuseQuery basicQuery = new DatamuseQuery().maximumAnswers(1000);

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
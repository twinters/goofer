package be.thomaswinters.goofer.generators.string;

import be.thomaswinters.datamuse.connection.DatamuseCaller;
import be.thomaswinters.datamuse.data.DatamuseWord;
import be.thomaswinters.datamuse.query.DatamuseQuery;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DatamuseAdjectiveToNounGenerator implements IRelatedStringGenerator {

    protected final DatamuseCaller caller;
    private final DatamuseQuery basicQuery = new DatamuseQuery().maximumAnswers(1000);

    public DatamuseAdjectiveToNounGenerator(DatamuseCaller caller) {
        this.caller = caller;
    }

    public List<DatamuseWord> getRelatedDatamuseNouns(String adjective) throws IOException {
        DatamuseQuery query = basicQuery.relatedNounFor(adjective);
        List<DatamuseWord> result = caller.call(query);
        return result;

    }

    @Override
    public Collection<String> generate(String relatedWord) throws Exception {
        return getRelatedDatamuseNouns(relatedWord).stream().map(e -> e.getWord()).collect(Collectors.toList());
    }

}
package be.thomaswinters.goofer.data;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MetricValues {
    private final ImmutableList<Optional<Object>> values;

    /*-********************************************-*
     *  Constructors
     *-********************************************-*/
    public MetricValues(List<Optional<Object>> values) {
        this.values = ImmutableList.copyOf(values);
    }

    public MetricValues(Stream<Optional<Object>> values) {
        this.values = ImmutableList.copyOf(values.iterator());
    }

    /*-********************************************-*/

    /*-********************************************-*
     *  List getters
     *-********************************************-*/

    public List<Optional<Object>> getValues() {
        return values;
    }

    public Optional<Object> getValue(int index) {
        return values.get(index);
    }

    public int size() {
        return values.size();
    }

    /*-********************************************-*/

    @Override
    public String toString() {
        return "<" + values.stream().map(e -> e.orElse("?") + "").collect(Collectors.joining(",")) + ">";
    }

}

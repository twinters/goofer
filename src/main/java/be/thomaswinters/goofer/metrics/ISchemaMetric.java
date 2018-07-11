package be.thomaswinters.goofer.metrics;

import weka.core.Attribute;

import java.util.List;
import java.util.Optional;

public interface ISchemaMetric {
    String getName();

    Optional<? extends Object> rate(List<? extends String> values);

    default Attribute getAttribute(String suffix) {
        return new Attribute(getName() + suffix);
    }

}

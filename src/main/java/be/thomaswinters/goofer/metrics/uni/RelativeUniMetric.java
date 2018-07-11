package be.thomaswinters.goofer.metrics.uni;

import be.thomaswinters.goofer.metrics.IUniArgumentMetric;

import java.util.Optional;

public class RelativeUniMetric implements IUniArgumentMetric {
    private final String name;
    private final IUniArgumentMetric numerator;
    private final IUniArgumentMetric denominator;

    public RelativeUniMetric(String name, IUniArgumentMetric numerator, IUniArgumentMetric denominator) {
        this.name = name;
        this.numerator = numerator;
        this.denominator = denominator;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Optional<? extends Object> rate(String value) {
        Optional<? extends Object> first = numerator.rate(value);
        Optional<? extends Object> second = denominator.rate(value);

        if (!first.isPresent() || !second.isPresent()) {
            return Optional.empty();
        }
        double result = ((Double) first.get() + 1) / ((Double) second.get() + 1);
        return Optional.of(result);
    }

}

package be.thomaswinters.goofer.metrics.bi;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.math3.util.Pair;
import be.thomaswinters.goofer.metrics.IBiArgumentMetric;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class CachedBiMetric implements IBiArgumentMetric {

    private static final int DEFAULT_CACHE_SIZE = 10;

    private final IBiArgumentMetric metric;
    private final Cache<Pair<String, String>, Optional<? extends Object>> cache;

    public CachedBiMetric(IBiArgumentMetric metric, int cacheSize) {
        this.metric = metric;
        this.cache = CacheBuilder.newBuilder().maximumSize(cacheSize).build();
    }

    public CachedBiMetric(IBiArgumentMetric metric) {
        this(metric, DEFAULT_CACHE_SIZE);
    }

    @Override
    public String getName() {
        return metric.getName();
    }

    @Override
    public Optional<? extends Object> rate(String value1, String value2) {
        try {
            return cache.get(new Pair<>(value1, value2), () -> metric.rate(value1, value2));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

}

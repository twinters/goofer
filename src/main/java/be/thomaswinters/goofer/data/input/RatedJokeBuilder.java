package be.thomaswinters.goofer.data.input;

import be.thomaswinters.goofer.data.MultiRating;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RatedJokeBuilder {
    private Map<String, List<Integer>> ratings = new HashMap<>();

    public void add(String joke, int rating) {
        if (!ratings.containsKey(joke)) {
            ratings.put(joke, new ArrayList<>());
        }
        ratings.get(joke).add(rating);
    }

    public List<MultiRating<String>> build() {
        return ratings.entrySet().stream().map(e -> new MultiRating<String>(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }
}

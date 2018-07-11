package be.thomaswinters.goofer.util;

import be.thomaswinters.gag.humanevaluation.jokejudger.JokeJudgerDataParser;
import be.thomaswinters.goofer.data.MultiRating;
import be.thomaswinters.goofer.data.TemplateValues;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RatingAgreementCalculator {
    public static double calculate(Collection<? extends MultiRating<? extends Object>> ratings) {
        List<Double> agreements = new ArrayList<>();
        for (MultiRating<? extends Object> rating : ratings) {
            List<Integer> modes = ModeCalculator.mode(rating.getRatings());
            List<Double> agreementPerMode = modes.stream()
                    .map(mode -> getAgreementFor(rating.getRatings(), mode))
                    .collect(Collectors.toList());
            agreements.add(average(agreementPerMode));
        }

        return agreements.stream().mapToDouble(e -> (double) e).sum() / ((double) agreements.size());
    }

    private static double getAgreementFor(Collection<Integer> ratings, int number) {
        double result = ((double) ratings.stream().filter(e -> e.equals(number)).count()) / (double) ratings.size();
        System.out.println(number + ": " + result + " \t\t" + ratings);
        return result;
    }

    private static double average(Collection<Double> collection) {
        return collection.stream().mapToDouble(e -> e.doubleValue()).sum() / (double) collection.size();
    }

    public static void main(String[] args) throws IOException {
        JokeJudgerDataParser parser = new JokeJudgerDataParser();
        List<MultiRating<TemplateValues>> ratings = parser.parse(new File("data/jokejudger/ratings.csv"));
        System.out.println(RatingAgreementCalculator.calculate(ratings));
    }
}

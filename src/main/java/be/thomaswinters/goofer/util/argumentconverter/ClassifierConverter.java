package be.thomaswinters.goofer.util.argumentconverter;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;
import weka.classifiers.Classifier;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.lazy.KStar;
import weka.classifiers.meta.RandomCommittee;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;

public class ClassifierConverter implements IStringConverter<Classifier> {

    @Override
    public Classifier convert(String value) {
        String[] splitted = value.split(":");
        String type = splitted[0];
//		List<String> arguments = Arrays.asList(splitted).subList(1, splitted.length - 1);

        switch (type.toLowerCase()) {
            case "j48":
                return new J48();
            case "randomforest":
                return new RandomForest();
            case "mlp":
                return new MultilayerPerceptron();
            case "decisiontable":
                return new DecisionTable();
            case "kstar":
                return new KStar();
            case "randomcommittee":
                return new RandomCommittee();
            default:
                throw new ParameterException("Unknown classifier type '" + type + "'.");
        }
    }

}

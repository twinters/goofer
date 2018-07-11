package be.thomaswinters.goofer.classifier;

import be.thomaswinters.goofer.data.MetricValues;
import be.thomaswinters.goofer.data.MultiRating;
import be.thomaswinters.goofer.data.Rating;
import be.thomaswinters.goofer.data.Template;
import be.thomaswinters.goofer.data.TemplateValues;
import be.thomaswinters.goofer.knowledgebase.SchemaMetrics;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.RemoveType;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class JokeClassifier {
    private final Classifier classifier;
    private final SchemaMetrics metrics;

    /*-********************************************-*
     *  Constructor
     *-********************************************-*/
    /*-********************************************-*
     *  Creating the classifier
     *-********************************************-*/
    private final boolean evaluateClassifier = true;

    // public JokeClassifier(ITemplateStripper templateStripper,
    // Collection<MultiRating<String>> ratedJokes,
    // SchemaMetrics metrics, ITemplateValuesGenerator templateValuesGenerator,
    // Optional<File> modelOutputFile)
    // throws Exception {
    // this(templateStripper.getTemplate(), templateStripper.extract(ratedJokes),
    // metrics, templateValuesGenerator,
    // modelOutputFile);
    // }
    /*-********************************************-*/
    private MetricValuesToArffConverter converter = new MetricValuesToArffConverter();
    private Instances testInstances;

    /*-********************************************-*/

    /*-********************************************-*
     *  Generator
     *-********************************************-*/
//	public Stream<Rating<TemplateValues>> generate(String seed) {
//
//		return generate(templateValueGenerator.seedToTemplateValues(seed));
//
//	}

//	public Stream<Rating<TemplateValues>> generate(PartialTemplateValues partialTemplate) {
//		Stream<TemplateValues> generations = templateValueGenerator.generateStream(partialTemplate);
//
////		System.out.println("Partial template: " + partialTemplate);
////		System.out.println("Amount of candidates: " + generations.size());
//
//
//		return generations.map(this::classify);
//		// Output the generation
//		// System.out.println("Output:" + classifications.stream()
//		// .map(e -> e.getRating() + ": " +
//		// templateStripper.getTemplate().apply(e.getSolution()))
//		// .collect(Collectors.joining("\n")));
//
//	}

    public JokeClassifier(Classifier classifier, Template template, Collection<MultiRating<TemplateValues>> ratedJokes,
                          SchemaMetrics metrics, File modelOutputFile)
            throws Exception {
        this.metrics = metrics;
        this.classifier = createClassifier(classifier, template, ratedJokes, metrics, modelOutputFile);
    }

    private Classifier createClassifier(Classifier classifier, Template template,
                                        Collection<MultiRating<TemplateValues>> ratedJokes, SchemaMetrics metrics, File modelOutputFile)
            throws Exception {

        // Rate all the jokes
        List<MetricValues> metricValues = ratedJokes.stream().map(metrics::calculateMetricValues)
                .collect(Collectors.toList());

        // Convert to instances, so Weka can read it
        MetricValuesToArffConverter converter = new MetricValuesToArffConverter(modelOutputFile);
        Instances instances = converter.convert(metrics, converter.createEmptyInstances(metrics, "TrainingData"),
                metricValues);

        // Specify the algorithm
        // J48 classifier = new J48();
        // LinearRegression classifier = new LinearRegression();
        // MultilayerPerceptron classifier = new MultilayerPerceptron();
        // SMOreg classifier = new SMOreg();
        // IBk classifier = new IBk();

        // Remove string attributes
        RemoveType removeType = new RemoveType();
        removeType.setInputFormat(instances);
        Instances filteredInstances = Filter.useFilter(instances, removeType);

        // Evaluate if wanted
        if (evaluateClassifier) {
            try {
                Evaluation eval = new Evaluation(filteredInstances);
                eval.crossValidateModel(classifier, filteredInstances, 10, new Random(1));
                System.out.println(eval.toSummaryString());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }


        // Train the classifier on the instances
        classifier.buildClassifier(filteredInstances);

        // Set testinstances header
        testInstances = converter.createEmptyInstances(metrics, "TestInstances");
        instances.setClassIndex(instances.numAttributes() - 1);

        // Return the build classifier
        return classifier;
    }

    public Rating<TemplateValues> classify(TemplateValues value) {
        Instance instance = converter.convert(metrics, testInstances, metrics.calculateMetricValues(value));

        instance.setDataset(testInstances);
        try {
            double classificationResult = classifier.classifyInstance(instance);
            double parsedClassification = metrics.getRatingAggregator().toRating(classificationResult);
            return new Rating<TemplateValues>(value, parsedClassification);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Could not classify " + value);
    }

    /*-********************************************-*/
}

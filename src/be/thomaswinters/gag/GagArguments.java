package be.thomaswinters.gag;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

import be.thomaswinters.goofer.ratingaggregation.IRatingAggregator;
import be.thomaswinters.goofer.ratingaggregation.ModeRatingAggregator;
import be.thomaswinters.goofer.util.argumentconverter.ClassifierConverter;
import be.thomaswinters.goofer.util.argumentconverter.RatingAggregatorConverter;
import be.thomaswinters.goofer.util.argumentconverter.SemiColonSplitter;
import edu.mit.jwi.Dictionary;
import weka.classifiers.Classifier;
import weka.classifiers.trees.RandomForest;

/**
 * Arguments for the GAG system.
 * @author Thomas Winters
 *
 */
public class GagArguments {

	@Parameter(names = "-outputModel", description = "Path where the program should output the training model file", converter = FileConverter.class)
	private File modelOutputFile = new File("out/model.arff");

	@Parameter(names = "-output", description = "Path where the program should output the training model file", converter = FileConverter.class)
	private File generationsOutputFile = new File("out/generations.csv");

	@Parameter(names = { "-maxSimilarity",
			"-maxSim" }, description = "If given, GAG will only output generations if it differs enough (no more words similar than this value) from previous generations")
	private int maxSimilarity = -1;

	@Parameter(names = "-outputWords", description = "Allow the template values in the training model file: classifiers have diffulty dealing with strings though!", converter = FileConverter.class)
	private boolean outputWords = false;

	@Parameter(names = "-inputJokes", description = "Path to the input jokes file", converter = FileConverter.class, splitter = SemiColonSplitter.class)
	private List<File> dataInput = Arrays.asList(new File("data/jokejudger/training-data.csv"));

	@Parameter(names = { "-sortRating",
			"-sort" }, description = "Wether or not the output should be sorted by their rating")
	private boolean sortOnRating = false;

	@Parameter(names = "-minScore", description = "Minimal score threshold to be considered a good joke")
	private double ratingThreshold = 4;

	@Parameter(names = "-sqlHost", description = "Host of the SQL database of the n-grams database")
	private String sqlHost = "localhost";
	@Parameter(names = "-sqlPost", description = "Port of the SQL database of the n-grams database")
	private int sqlPort = 3306;
	@Parameter(names = "-sqlUser", description = "Username of the SQL database of the n-grams database")
	private String sqlUsername = "ngram";
	@Parameter(names = "-sqlPassword", description = "Password of the SQL database of the n-grams database")
	private String sqlPassword = "ngram";
	@Parameter(names = "-sqlDB", description = "Database name of the SQL database of the n-grams database")
	private String sqlDatabaseName = "ngram";

	@Parameter(names = "-dictionary", description = "Path to the WordNet dictionary", converter = FileConverter.class)
	private File dictionaryFile = new File("./data/wordnet/");

	@Parameter(names = "-posFile", description = "Path to the Stanford POS tagger")
	private String posTaggerFile = "data/stanford-pos/english-bidirectional-distsim.tagger";

	@Parameter(names = "-classifier", description = "The classifier to use to learn from the input jokes", converter = ClassifierConverter.class)
	private Classifier classifier = new RandomForest();

	@Parameter(names = "-aggregator", description = "The rating aggregator to combine the ratings with", converter = RatingAggregatorConverter.class)
	private IRatingAggregator aggregator = new ModeRatingAggregator(1, 5);

	@Parameter(names = "-x", description = "First template value of an analogy joke")
	private String x = "coffee";

	@Parameter(names = "-y", description = "Second template value of an analogy joke")
	private String y;

	@Parameter(names = "-z", description = "Third template value of an analogy joke")
	private String z;

	@Parameter(names = { "-generator",
			"-g" }, description = "Type of template values generator: sql, datamuse or twogram")
	private String generatorType = "datamuse";

	/*-********************************************-*
	 *  Derived
	*-********************************************-*/
	private Dictionary dictionary;

	/*-********************************************-*/

	/*-********************************************-*
	 *  Getters
	*-********************************************-*/
	public Optional<File> getModelOutputFile() {
		return modelOutputFile == null ? Optional.empty() : Optional.of(modelOutputFile);
	}

	public List<File> getDataInput() {
		return dataInput;
	}

	public boolean isSortOnRating() {
		return sortOnRating;
	}

	public double getRatingThreshold() {
		return ratingThreshold;
	}

	public String getSqlHost() {
		return sqlHost;
	}

	public int getSqlPort() {
		return sqlPort;
	}

	public String getSqlUsername() {
		return sqlUsername;
	}

	public String getSqlPassword() {
		return sqlPassword;
	}

	public String getSqlDatabaseName() {
		return sqlDatabaseName;
	}

	public File getDictionaryFile() {
		return dictionaryFile;
	}

	public Dictionary getDictionary() throws IOException {
		if (dictionary == null) {
			dictionary = new Dictionary(getDictionaryFile());
			dictionary.open();
		}
		return dictionary;
	}

	public String getPosTaggerFile() {
		return posTaggerFile;
	}
	/*-********************************************-*/

	public Classifier getClassifier() {
		return classifier;
	}

	public IRatingAggregator getAggregator() {
		return aggregator;
	}

	public boolean outputWords() {
		return outputWords;
	}

	public boolean isOutputWords() {
		return outputWords;
	}

	public Optional<String> getX() {
		return Optional.ofNullable(x);
	}

	public Optional<String> getY() {
		return Optional.ofNullable(y);
	}

	public Optional<String> getZ() {
		return Optional.ofNullable(z);
	}
	
	public String getGeneratorType() {
		return generatorType;
	}

	public File getGenerationsOutputFile() {
		return generationsOutputFile;
	}

	public Optional<Integer> getMaxSimilarity() {
		return maxSimilarity <= 0 ? Optional.empty() : Optional.of(maxSimilarity);
	}

}

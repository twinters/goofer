package thomaswinters.gag;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.beust.jcommander.JCommander;

import thomaswinters.gag.humanevaluation.jokejudger.JokeJudgerDataParser;
import thomaswinters.gag.template.AnalogyTemplateStripper;
import thomaswinters.goofer.classifier.JokeClassifier;
import thomaswinters.goofer.data.MultiRating;
import thomaswinters.goofer.data.Rating;
import thomaswinters.goofer.data.TemplateValues;
import thomaswinters.goofer.generators.PartialTemplateValues;
import thomaswinters.goofer.knowledgebase.TemplateKnowledgeBase;
import thomaswinters.goofer.outputfilter.SimilarValuesFilter;
import thomaswinters.goofer.util.RatingComparators;

/**
 * The Generalised Analogy Generator (GAG). Based on the Goofer framework.
 * 
 * @author Thomas Winters
 *
 */
public class GeneralisedAnalogyGenerator {
	/*-********************************************-*
	 *  Instance variables
	*-********************************************-*/
	// ARGUMENTS
	private final GagArguments arguments;

	// TEMPLATE VALUES SPLITTER
	// private final ITemplateValuesProcessor templateValuesProcessor;

	// TOTAL KNOWLEDGEBASE
	private final TemplateKnowledgeBase knowledgebase;

	/*-********************************************-*/

	/*-********************************************-*
	 *  Constructor
	*-********************************************-*/
	public GeneralisedAnalogyGenerator(GagArguments arguments)
			throws IOException, ClassNotFoundException, URISyntaxException, SQLException {
		System.out.println("Initialising GAG");
		// SET ARGUMENTS
		this.arguments = arguments;

		System.out.println("Creating Knowledgebase");
		// Create knowledgebase
		knowledgebase = new GagKnowledgeBaseBuilder(arguments).createKnowledgeBase();
	}

	/*-********************************************-*
	 *  Generator
	*-********************************************-*/

	public void generateAnalogies(PartialTemplateValues partialTemplate) throws Exception {

		// Get the input jokes
		System.out.println("Reading input jokes");
		JokeJudgerDataParser dataParser = new JokeJudgerDataParser();
		List<MultiRating<TemplateValues>> ratedTemplateValues = new ArrayList<>();
		for (File file : arguments.getDataInput()) {
			ratedTemplateValues.addAll(dataParser.parse(file));
		}

		// Process the values
		System.out.println("Processing input jokes");
		ratedTemplateValues = knowledgebase.getTemplatevaluesProcessor().process(ratedTemplateValues);

		// Create the classifier based on the knowledgebase
		System.out.println("Training classifier");
		JokeClassifier jc = new JokeClassifier(arguments.getClassifier(), AnalogyTemplateStripper.TEMPLATE,
				ratedTemplateValues, knowledgebase.getSchemaMetrics(), arguments.getModelOutputFile());

		// Generate rated template values
		System.out.println("Generating jokes for template values " + partialTemplate);

		Stream<Rating<TemplateValues>> generations = knowledgebase.getValueGenerator().generateStream(partialTemplate)
				// Classify
				.map(jc::classify)
				// Filter: above threshold!
				.filter(e -> e.getRating() >= arguments.getRatingThreshold());

		// Sort on ratings if required
		if (arguments.isSortOnRating()) {
			generations = generations.sorted(RatingComparators.DESCENDING);
		}

		// Filter on max similarity
		if (arguments.getMaxSimilarity().isPresent()) {
			SimilarValuesFilter filter = new SimilarValuesFilter(arguments.getMaxSimilarity().get());
			generations = generations.filter(e -> filter.isDifferentEnough(e.getElement()));
		}

		// Write to file
		FileOutputStream fos = new FileOutputStream(arguments.getGenerationsOutputFile());
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

		generations.map(e -> df.format(e.getRating()) + "\t"
				+ AnalogyTemplateStripper.TEMPLATE.apply(e.getElement())).forEach(e -> {
					System.out.println(e);
					try {
						bw.write(e + "\n");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				});
		bw.close();

		// Files.write(result, outputFile, Charsets.UTF_8);
		System.out.println("Wrote ratings to " + arguments.getGenerationsOutputFile());
	}

	private final DecimalFormat df = new DecimalFormat("#.#######");

	/*-********************************************-*/

	public static void main(String[] args) throws Exception {
		// Parse arguments
		GagArguments gagArguments = new GagArguments();
		JCommander.newBuilder().addObject(gagArguments).build().parse(args);

		// Create generator
		GeneralisedAnalogyGenerator gag = new GeneralisedAnalogyGenerator(gagArguments);

		// Generate
		try {
			gag.generateAnalogies(new PartialTemplateValues(
					Arrays.asList(gagArguments.getX(), gagArguments.getY(), gagArguments.getZ())));
		} catch (OutOfMemoryError e) {
			System.out.println("Out of memory.");
		}
	}

}

package be.thomaswinters.gag;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.beust.jcommander.ParameterException;

import be.thomaswinters.datamuse.connection.DatamuseCaller;
import be.thomaswinters.gag.template.processor.AlphaNumericsTemplateValuesFilter;
import be.thomaswinters.gag.template.processor.ITemplateValuesProcessor;
import be.thomaswinters.gag.template.processor.MultiTemplateValuesProcessor;
import be.thomaswinters.gag.template.processor.WordTypeTemplateSplitter;
import be.thomaswinters.gag.valuesgenerator.GagTwogramValuesGenerator;
import be.thomaswinters.gag.valuesgenerator.NounNounAdjectiveGenerator;
import be.thomaswinters.gag.valuesgenerator.filter.SameWordsFilter;
import be.thomaswinters.gag.valuesgenerator.filter.WordTypeTemplateValuesFilter;
import be.thomaswinters.goofer.generators.EmptyTemplateValuesGenerator;
import be.thomaswinters.goofer.generators.ITemplateValuesGenerator;
import be.thomaswinters.goofer.generators.TwoGramWordCounterCreator;
import be.thomaswinters.goofer.generators.string.DatamuseAdjectiveToNounGenerator;
import be.thomaswinters.goofer.generators.string.DatamuseNounToNounGenerator;
import be.thomaswinters.goofer.generators.string.DatamuseRelatedAdjectiveGenerator;
import be.thomaswinters.goofer.generators.string.IRelatedStringGenerator;
import be.thomaswinters.goofer.generators.string.TwoGramSQLAdjectiveGenerator;
import be.thomaswinters.goofer.generators.string.TwoGramSQLNounGenerator;
import be.thomaswinters.goofer.knowledgebase.SchemaMetrics;
import be.thomaswinters.goofer.knowledgebase.TemplateKnowledgeBase;
import be.thomaswinters.goofer.metrics.IBiArgumentMetric;
import be.thomaswinters.goofer.metrics.IUniArgumentMetric;
import be.thomaswinters.goofer.metrics.MetricApplicator;
import be.thomaswinters.goofer.metrics.bi.AdjectiveVectorDifferenceMetric;
import be.thomaswinters.goofer.metrics.bi.CachedBiMetric;
import be.thomaswinters.goofer.metrics.bi.TwoGramSQLMetric;
import be.thomaswinters.goofer.metrics.uni.CachedUniMetric;
import be.thomaswinters.goofer.metrics.uni.IdentityMetric;
import be.thomaswinters.goofer.metrics.uni.OneGramSQLMetric;
import be.thomaswinters.goofer.metrics.uni.OneGramWCMetric;
import be.thomaswinters.goofer.metrics.uni.RelativeUniMetric;
import be.thomaswinters.goofer.metrics.uni.WordSensesMetric;
import be.thomaswinters.googlengrams.NgramMySQLConnector;
import be.thomaswinters.pos.WordTypeCalculator;
import be.thomaswinters.sentencemarkov.wordcounter.WordCounter;
import be.thomaswinters.sentencemarkov.wordcounter.WordCounterIO;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.item.POS;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * Knowledge base of the GAG system, with the relevant template strippers,
 * values generators and metrics.
 * 
 * @author Thomas Winters
 *
 */
public class GagKnowledgeBaseBuilder {

	private final static List<POS> ALLOWED_POS = Arrays.asList(POS.NOUN, POS.NOUN, POS.ADJECTIVE);

	private final GagArguments arguments;

	public GagKnowledgeBaseBuilder(GagArguments arguments) {
		this.arguments = arguments;
	}

	public TemplateKnowledgeBase createKnowledgeBase()
			throws ClassNotFoundException, URISyntaxException, SQLException, IOException {

		/*-********************************************-*
		 *  Dictionaries
		*-********************************************-*/
		MaxentTagger tagger = new MaxentTagger(arguments.getPosTaggerFile());
		Dictionary dictionary = arguments.getDictionary();
		/*-********************************************-*/

		/*-********************************************-*
		 *  Template splitter
		*-********************************************-*/
		WordTypeCalculator wordTypeCalculator = new WordTypeCalculator(arguments.getDictionary(), tagger);
		ITemplateValuesProcessor wordTypeSplitter = new WordTypeTemplateSplitter(wordTypeCalculator, ALLOWED_POS);
		ITemplateValuesProcessor templateValuesProcessor = new MultiTemplateValuesProcessor(
				new AlphaNumericsTemplateValuesFilter(), wordTypeSplitter);
		/*-********************************************-*/

		/*-********************************************-*
		 *  N-grams using MySQL
		*-********************************************-*/
		// Create MySQL connection
		Connection connection = NgramMySQLConnector.createConnection(arguments.getSqlHost(), arguments.getSqlPort(),
				arguments.getSqlUsername(), arguments.getSqlPassword(), arguments.getSqlDatabaseName());

		// Create connectors
		NgramMySQLConnector onegramConnector = new NgramMySQLConnector(1, connection);
		NgramMySQLConnector twogramConnector = new NgramMySQLConnector(2, connection);
		TwoGramWordCounterCreator wordCounterCreator = new TwoGramWordCounterCreator(connection);
		/*-********************************************-*/

		/*-********************************************-*
		 *  Erotic word counter
		*-********************************************-*/
		WordCounter eroWc = WordCounterIO.read(new File("data/textfiles/ero-1gram.txt"));
		/*-********************************************-*/

		/*-********************************************-*
		 *  Others
		*-********************************************-*/
		// N-gram metrics
		IdentityMetric identityMetric = new IdentityMetric();
		IUniArgumentMetric oneGram = new CachedUniMetric(new OneGramSQLMetric(onegramConnector));
		IBiArgumentMetric twoGram = new CachedBiMetric(new TwoGramSQLMetric(twogramConnector));
		IBiArgumentMetric adjectiveDifference = new AdjectiveVectorDifferenceMetric(wordCounterCreator);

		// Erotic
		IUniArgumentMetric eroOneGram = new CachedUniMetric(new OneGramWCMetric(eroWc, "sexual freq"));
		IUniArgumentMetric relativeErotic = new CachedUniMetric(
				new RelativeUniMetric("relative_sexual_freq", oneGram, eroOneGram));
		IUniArgumentMetric nounSenses = new CachedUniMetric(new WordSensesMetric(dictionary, POS.NOUN));
		IUniArgumentMetric adjectiveSenses = new CachedUniMetric(new WordSensesMetric(dictionary, POS.ADJECTIVE));
		/*-********************************************-*/

		/*-********************************************-*
		 *  Schema Metrics object
		*-********************************************-*/

		List<MetricApplicator> metrics = new ArrayList<>();
		if (arguments.outputWords()) {
			System.out.println("adding identity metrics");
			metrics.addAll(Arrays.asList(new MetricApplicator(identityMetric, 0),
					//
					new MetricApplicator(identityMetric, 1),
					//
					new MetricApplicator(identityMetric, 2)));
		}
		metrics.addAll(Arrays.asList(

				// Frequency metrics
				new MetricApplicator(oneGram, 0),
				//
				new MetricApplicator(oneGram, 1),
				//
				new MetricApplicator(oneGram, 2),

				// Erotic Frequency metrics
				new MetricApplicator(eroOneGram, 0),
				//
				new MetricApplicator(eroOneGram, 1),
				//
				new MetricApplicator(eroOneGram, 2),

				// Relative erotic Frequency metrics
				new MetricApplicator(relativeErotic, 0),
				//
				new MetricApplicator(relativeErotic, 1),
				//
				new MetricApplicator(relativeErotic, 2),

				// 2-gram metrics
				new MetricApplicator(adjectiveDifference, 0, 1),
				//
				new MetricApplicator(twoGram, 2, 0),
				//
				new MetricApplicator(twoGram, 2, 1),

				// Word senses
				new MetricApplicator(nounSenses, 0),
				//
				new MetricApplicator(nounSenses, 1),
				//
				new MetricApplicator(adjectiveSenses, 2)

		));

		SchemaMetrics schemaMetrics = new SchemaMetrics(metrics, arguments.getAggregator());

		/*-********************************************-*/

		/*-********************************************-*
		 *  TemplateValueGenerator
		*-********************************************-*/

		// MySQL 2 gram generators
		// ITemplateValuesGenerator valueGenerator =
		// createSqlValuesGenerator(twogramConnector);

		// DatamuseWord generator
		ITemplateValuesGenerator valueGenerator = new SameWordsFilter(new WordTypeTemplateValuesFilter(
				getTemplateGenerator(arguments.getGeneratorType(), twogramConnector, wordCounterCreator),
				wordTypeCalculator, ALLOWED_POS));

		/*-********************************************-*/

		return new TemplateKnowledgeBase(templateValuesProcessor, schemaMetrics, valueGenerator);

	}

	protected ITemplateValuesGenerator getTemplateGenerator(String type, NgramMySQLConnector twogramConnector,
			TwoGramWordCounterCreator creator) {
		String[] splitted = type.split(":");
		String generatorType = splitted[0];
		List<String> arguments = new ArrayList<>();
		if (splitted.length > 1) {
			arguments = Arrays.asList(splitted).subList(1, splitted.length);
		}

		switch (generatorType) {
		case "sql":
			return createSqlValuesGenerator(twogramConnector);
		case "datamuse":
			return createDatamuseValuesGenerator();
		case "twogram":
			int maxAdj = arguments.size() > 0 ? Integer.parseInt(arguments.get(0)) : Integer.MAX_VALUE;
			int maxNoun = arguments.size() > 1 ? Integer.parseInt(arguments.get(1)) : Integer.MAX_VALUE;
			int minTwogramCount = arguments.size() > 2 ? Integer.parseInt(arguments.get(2)) : Integer.MAX_VALUE;
			return createTwogramValuesGenerator(creator, maxAdj, maxNoun, minTwogramCount);
		case "none":
			return new EmptyTemplateValuesGenerator(3);
		default:
			throw new ParameterException("Not a valid type of generator: " + type);
		}
	}

	protected ITemplateValuesGenerator createSqlValuesGenerator(NgramMySQLConnector twogramConnector) {
		IRelatedStringGenerator adjectiveGen = new TwoGramSQLAdjectiveGenerator(twogramConnector);
		IRelatedStringGenerator nounGen = new TwoGramSQLNounGenerator(twogramConnector);
		return new NounNounAdjectiveGenerator(adjectiveGen, nounGen);
	}

	protected ITemplateValuesGenerator createTwogramValuesGenerator(TwoGramWordCounterCreator creator, int maxAdj,
			int maxNoun, int minTwogramCount) {
		return new GagTwogramValuesGenerator(creator, maxAdj, maxNoun, minTwogramCount);
	}

	protected ITemplateValuesGenerator createDatamuseValuesGenerator() {
		DatamuseCaller caller = new DatamuseCaller();
		IRelatedStringGenerator adjectiveGen = new DatamuseRelatedAdjectiveGenerator(caller);
		IRelatedStringGenerator adjectiveToNounGen = new DatamuseAdjectiveToNounGenerator(caller);
		IRelatedStringGenerator nounGen = new DatamuseNounToNounGenerator(adjectiveGen, adjectiveToNounGen);
		return new NounNounAdjectiveGenerator(adjectiveGen, nounGen);
	}

}

package thomaswinters.goofer.generators.string;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import thomaswinters.googlengrams.NgramMySQLConnector;

public class TwoGramSQLNounGenerator implements IRelatedStringGenerator {

	private static final String QUERY = "SELECT DISTINCT y.word2 "
			+ "FROM 2grams as x JOIN 2grams as y ON x.word1=y.word1 " + "WHERE x.word2=?";

	private final NgramMySQLConnector nGramService;

	public TwoGramSQLNounGenerator(NgramMySQLConnector nGramService) {
		this.nGramService = nGramService;
	}

	@Override
	public List<String> generate(String noun) throws Exception {
		ResultSet rs = nGramService.getCustomQuery(QUERY, noun);
		List<String> result = new ArrayList<>();
		if (rs.first()) {
			do {
				result.add(rs.getString(2));
			} while (rs.next());
		}
		System.out.println("Nouns for noun '" + noun + "': " + result);
		return result;

	}

}

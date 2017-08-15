package be.thomaswinters.goofer.generators.string;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import be.thomaswinters.googlengrams.NgramMySQLConnector;

public class TwoGramSQLAdjectiveGenerator implements IRelatedStringGenerator {

	private final NgramMySQLConnector nGramService;

	public TwoGramSQLAdjectiveGenerator(NgramMySQLConnector nGramService) {
		this.nGramService = nGramService;
	}

	@Override
	public List<String> generate(String noun) throws Exception {
		ResultSet rs = nGramService.getRows(Arrays.asList("%", noun));
		List<String> result = new ArrayList<>();
		if (rs.first()) {
			do {
				result.add(rs.getString(2));
			} while (rs.next());
		}
		return result;

	}

}

package be.thomaswinters.goofer.metrics.util;

import be.thomaswinters.googlengrams.NgramMySQLConnector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class NGramsSQLMetric {

    private final NgramMySQLConnector nGramService;
    private final int n;

    public NGramsSQLMetric(NgramMySQLConnector nGramService) {
        this.nGramService = nGramService;
        this.n = nGramService.getN();
    }

    public int count(List<String> words) {
        try {
            ResultSet rs = nGramService.getRows(words);
            int amount = 0;
            if (rs.first()) {
                do {
                    amount += rs.getLong(n + 2);
                } while (rs.next());
            }
            return amount;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}

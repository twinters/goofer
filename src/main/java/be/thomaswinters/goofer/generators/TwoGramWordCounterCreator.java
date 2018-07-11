package be.thomaswinters.goofer.generators;

import be.thomaswinters.googlengrams.NgramMySQLConnector;
import be.thomaswinters.wordcounter.WordCounter;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Generates related adjectives to nouns and nouns to adjectives Assumes that an
 * ngram service is used with only adjective-noun pairs
 *
 * @author Thomas Winters *
 */
public class TwoGramWordCounterCreator {

    private final static int CACHE_SIZE = 5000;

    private final PreparedStatement getNounsPS;
    private final PreparedStatement getAdjectivesPS;
    public Cache<String, WordCounter> savedNounVectors = CacheBuilder.newBuilder().maximumSize(CACHE_SIZE).build();
    public Cache<String, WordCounter> savedAdjectiveVectors = CacheBuilder.newBuilder().maximumSize(CACHE_SIZE).build();

    public TwoGramWordCounterCreator(Connection nGramConnection) throws SQLException {
        this.getAdjectivesPS = nGramConnection
                .prepareStatement("SELECT n.word1, n.count FROM 2grams as n where word2=?;");
        this.getNounsPS = nGramConnection.prepareStatement("SELECT n.word2, n.count FROM 2grams as n where word1=?;");
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException, URISyntaxException {
        TwoGramWordCounterCreator connect = new TwoGramWordCounterCreator(
                NgramMySQLConnector.createConnection("localhost", 3306, "ngram", "ngram", "ngram"));

        System.out.println(connect.getNouns("fresh").getDistinctSize());
    }

    public WordCounter getNouns(String adjective) throws SQLException {
        WordCounter cached = savedNounVectors.getIfPresent(adjective);
        if (cached != null) {
            return cached;
        }

        // Prepared statement: fill in word
        getNounsPS.setString(1, adjective);

        // Retrieve resultset
        ResultSet executeQuery = getNounsPS.executeQuery();

        // Convert to WordCounter
        WordCounter result = convertToWordCounter(executeQuery, "word2");

        savedNounVectors.put(adjective, result);

        return result;
    }

    public WordCounter getAdjectives(String noun) throws SQLException {
        WordCounter cached = savedAdjectiveVectors.getIfPresent(noun);
        if (cached != null) {
            return cached;
        }

        // Prepared statement: fill in word
        getAdjectivesPS.setString(1, noun);

        // Retrieve resultset
        ResultSet executeQuery = getAdjectivesPS.executeQuery();

        // Convert to WordCounter
        WordCounter result = convertToWordCounter(executeQuery, "word1");

        savedAdjectiveVectors.put(noun, result);

        return result;
    }

    private WordCounter convertToWordCounter(ResultSet resultSet, String string) {
        WordCounter.Builder b = new WordCounter.Builder();
        try {
            if (resultSet.first()) {
                do {
                    String word = resultSet.getString(1);
                    long count = resultSet.getLong(2);
                    int intCount = count >= Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) count;
                    b.addAndConvertWord(word, intCount);
                } while (resultSet.next());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return b.build();
    }
}

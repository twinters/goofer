package thomaswinters.gag.humanevaluation.jokejudger;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import thomaswinters.goofer.ratingaggregation.AverageRatingAggregator;
import thomaswinters.goofer.util.ModeCalculator;

/**
 * This class can be used to scrape the ratings of a deployed platform using the
 * JokeJudger specifications. Give the url of the MySQL database as an argument
 * in order to gather the ratings.
 * 
 * @author Thomas Winters
 *
 */
public class JokeJudgerScraper {

	private interface RatingCollector {
		public String getHeader();

		public String collect(String x, String y, String z, String score, int amountIncomprehensives,
				int amountOffensives);
	}

	private static class TextRatingCollector implements RatingCollector {
		private static final String delim = "\t";

		@Override
		public String collect(String x, String y, String z, String score, int amountIncomprehensives,
				int amountOffensives) {
			return x + delim + y + delim + z + delim + "[" + score + "]" + delim + amountIncomprehensives + delim
					+ amountOffensives;
		}

		@Override
		public String getHeader() {
			return "x\ty\tz\tscore\tincomprehensibleMarkings\toffensiveMarkings";
		}
	}

	private static class TextWithAveragesRatingCollector implements RatingCollector {
		private static final String delim = "\t";

		public List<Integer> parseScore(String score) {
			return Stream.of(score.split(",")).map(e -> Integer.parseInt(e)).collect(Collectors.toList());
		}

		@Override
		public String collect(String x, String y, String z, String score, int amountIncomprehensives,
				int amountOffensives) {
			List<Integer> ratings = parseScore(score);
			String average = (Math.round(AverageRatingAggregator.calculateAverage(ratings) * 100d) / 100d + "")
					.replace('.', ',');
			List<Integer> mode = ModeCalculator.mode(ratings);
			return x + delim + y + delim + z + delim + average + delim + mode + delim + "[" + score + "]" + delim
					+ amountIncomprehensives + delim + amountOffensives;
		}

		@Override
		public String getHeader() {
			return "x\ty\tz\taverage\tmode(s)\tscore\tincomprehensibleMarkings\toffensiveMarkings";
		}
	}

	private static Connection getConnection(String connectionUrl)
			throws ClassNotFoundException, URISyntaxException, SQLException {
		// Create driver
		Class.forName("com.mysql.jdbc.Driver");
		URI jdbUri = new URI(connectionUrl);

		String username = jdbUri.getUserInfo().split(":")[0];
		String password = jdbUri.getUserInfo().split(":")[1];
		String port = String.valueOf(jdbUri.getPort());
		String jdbUrl = "jdbc:mysql://" + jdbUri.getHost() + ":" + port + jdbUri.getPath();

		return DriverManager.getConnection(jdbUrl, username, password);
	}

	private static int person = -1;

	private static List<String> getJokeRatings(String connectionUrl, RatingCollector ratingCollector)
			throws ClassNotFoundException, URISyntaxException, SQLException {
		List<String> result = new ArrayList<>();
		try {
			Connection conn = getConnection(connectionUrl);

			String query = "SELECT id,x,y,z, "
					//
					+ "GROUP_CONCAT(r.score ORDER BY r.score) AS score, "
					//
					+ "SUM(m.incomprehensive) as amountIncomprehensives, "
					//
					+ "SUM(m.offensive)  as amountOffensives "
					//
					+ "FROM jokes AS j "
					//
					+ "LEFT JOIN ratings AS r ON j.id=r.joke_id "
					//
					+ "LEFT JOIN markings AS m ON j.id=m.joke_id "
					//
					+ "WHERE EXISTS (" + "SELECT * "
					//
					+ "FROM jokecreations AS c "
					//
					+ "WHERE c.joke_id = j.id) "
					//
					+ (person>=0?"AND r.user_id = " + person + " ": "")
					//
					+ "GROUP BY j.id;";

			Statement st = conn.createStatement();

			ResultSet rs = st.executeQuery(query);

			result.add(ratingCollector.getHeader());

			while (rs.next()) {
				// long id = rs.getLong("id");
				String x = sanitize(rs.getString("x"));
				String y = sanitize(rs.getString("y"));
				String z = sanitize(rs.getString("z"));
				String score = rs.getString("score");
				int amountOfRatings = getAmountOfRatings(score);

				String incomprehensivesString = rs.getString("amountIncomprehensives");
				int amountIncomprehensives = incomprehensivesString == null ? 0
						: Integer.parseInt(incomprehensivesString) / amountOfRatings;

				String offencesString = rs.getString("amountOffensives");
				int amountOffensives = offencesString == null ? 0 : Integer.parseInt(offencesString) / amountOfRatings;

				result.add(ratingCollector.collect(x, y, z, score, amountIncomprehensives, amountOffensives));
			}
			st.close();
		} catch (Exception e) {
			System.err.println("Exception:" + e);
			System.err.println(e.getMessage());
		}

		return result;
	}

	private static String sanitize(String input) {
		return input.replaceAll("\t", " ").replaceAll("\n", " ");
	}

	private static int getAmountOfRatings(String score) {
		return StringUtils.countMatches(score, ",") + 1;
	}

	public static void generateToFile(File outFile, String connectionUrl, RatingCollector collector)
			throws ClassNotFoundException, IOException, URISyntaxException, SQLException {
		Files.write(getJokeRatings(connectionUrl, collector).stream().collect(Collectors.joining("\n")), outFile,
				Charsets.UTF_8);
	}

	public static void main(String[] args)
			throws ClassNotFoundException, URISyntaxException, SQLException, IOException {

		if (args.length < 1) {
			throw new RuntimeException("Please provide a link to the connection URL as the first parameter");
		}
		RatingCollector collector = new TextRatingCollector();
		if (args.length >= 2 && args[1].equals("average")) {
			collector = new TextWithAveragesRatingCollector();
		}

		String connectionUrl = args[0];
		File folder = new File("data/jokejudger");
		if (!folder.exists()) {
			folder.mkdirs();
		}
		generateToFile(new File(folder, "ratings.csv"), connectionUrl, collector);
	}

}

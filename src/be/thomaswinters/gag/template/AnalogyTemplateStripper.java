package be.thomaswinters.gag.template;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import be.thomaswinters.goofer.data.Template;
import be.thomaswinters.goofer.data.TemplateValues;
import be.thomaswinters.goofer.templateextraction.ITemplateStripper;

/**
 * Strips off the template, resulting in just the variable parts of the jokes in
 * the format "I like my X like I like my Y, Z"
 * 
 * @author Thomas Winters
 *
 */
public class AnalogyTemplateStripper implements ITemplateStripper {

	/*-********************************************-*
	 *  Template
	*-********************************************-*/
	public static final Template TEMPLATE = new Template("I like my ", " like I like my ", ": ", ".");
	/*-********************************************-*/

	/*-********************************************-*
	 *  Parsing variables
	*-********************************************-*/
	// Shorter regex: There is a lot of trouble recognising the longer once if
	// the shorter one is blended in
	private final static String REGEX_STRING = "I (like|love) (my )?([\\w\\s-]+) (the way |how |like )(I|he) (like |love )?(my )?([\\w\\s\\-]+)[,.;:\\-]+ ?(.+).?!?";
	private final static String SHORTER_REGEX_STRING = "I (like|love) (my )?([\\w\\s-]+) like (my )?([\\w\\s\\-]+)[,.;:\\-]+ ?(.+).?!?";
	private final static Pattern REGEX = Pattern.compile(REGEX_STRING, Pattern.CASE_INSENSITIVE);
	private final static Pattern SHORTER_REGEX = Pattern.compile(SHORTER_REGEX_STRING, Pattern.CASE_INSENSITIVE);
	/*-********************************************-*/

	/*-********************************************-*
	 *  Extraction
	*-********************************************-*/
	/**
	 * Extracts the variables out of jokes in the form of "I like my X like I
	 * like my Y, Z"
	 */
	@Override
	public TemplateValues extract(String sentence) {
		// System.out.println(sentence);
		Matcher m = REGEX.matcher(sentence);
		if (m.find()) {
			String x = m.group(3);
			String y = m.group(8);
			String z = m.group(9);
			return new TemplateValues(x, y, z);
		} else {
			Matcher m2 = SHORTER_REGEX.matcher(sentence);
			if (m2.find()) {
				String x = m2.group(3);
				String y = m2.group(5);
				String z = m2.group(6);
				return new TemplateValues(x, y, z);
			}
		}
		throw new IllegalArgumentException("Non-parseable joke: " + sentence);
	}

	/*-********************************************-*/

	/*-********************************************-*
	 *  Template
	*-********************************************-*/
	@Override
	public Template getTemplate() {
		return TEMPLATE;
	}
	/*-********************************************-*/

}

package thomaswinters.goofer.data;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * Class representing a "sentence with gaps". Each sentence part is an element
 * of the sentence parts
 * 
 * @author Thomas Winters
 *
 */
public class Template {
	private final ImmutableList<String> sentenceParts;

	public Template(List<String> sentenceParts) {
		this.sentenceParts = ImmutableList.copyOf(sentenceParts);
	}

	public Template(String... sentenceParts) {
		this(Arrays.asList(sentenceParts));
	}

	public String apply(TemplateValues values) {
		if (!canApply(values)) {
			throw new IllegalArgumentException("Invalid template values size (" + values.getArity()
					+ ") for template with " + sentenceParts.size() + " sentence parts.");
		}

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < Math.max(values.getArity(), sentenceParts.size()); i++) {
			if (i < sentenceParts.size()) {
				builder.append(sentenceParts.get(i));
			}
			if (i < values.getArity()) {
				builder.append(values.get(i));
			}
		}

		return builder.toString();
	}

	public boolean canApply(TemplateValues values) {
		return values.getArity() == sentenceParts.size() - 1;
	}

}

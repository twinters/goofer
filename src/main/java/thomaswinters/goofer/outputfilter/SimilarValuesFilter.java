package thomaswinters.goofer.outputfilter;

import java.util.HashSet;
import java.util.Set;

import thomaswinters.goofer.data.TemplateValues;

/**
 * Filters similar template values
 * 
 * @author Thomas Winters
 *
 */
public class SimilarValuesFilter {

	private final int maxSimilarTemplateValues;
	private final Set<TemplateValues> accepted = new HashSet<>();

	public SimilarValuesFilter(int maxSimilarTemplateValues) {
		this.maxSimilarTemplateValues = maxSimilarTemplateValues;
	}
	public boolean isDifferentEnough(TemplateValues given) {

		if (!accepted.stream().anyMatch(e -> isTooSimilar(e, given))) {
			accepted.add(given);
			return true;
		}
		return false;

	}

	private boolean isTooSimilar(TemplateValues tv1, TemplateValues tv2) {
		return getAmountSimilarValues(tv1, tv2) > maxSimilarTemplateValues;
	}

	private int getAmountSimilarValues(TemplateValues tv1, TemplateValues tv2) {
		int amount = 0;
		for (int i = 0; i < tv1.getArity(); i++) {
			if (tv1.get(i).equals(tv2.get(i))) {
				amount += 1;
			}
		}
		return amount;
	}

}

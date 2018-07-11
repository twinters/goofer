package thomaswinters.goofer.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * From https://stackoverflow.com/questions/4191687/how-to-calculate-mean-median-mode-and-range-from-a-set-of-numbers
 *
 */
public class ModeCalculator {
	public static List<Integer> mode(Collection<Integer> numbers) {
	    final List<Integer> modes = new ArrayList<Integer>();
	    final Map<Integer, Integer> countMap = new HashMap<Integer, Integer>();

	    int max = -1;

	    for (final int n : numbers) {
	        int count = 0;

	        if (countMap.containsKey(n)) {
	            count = countMap.get(n) + 1;
	        } else {
	            count = 1;
	        }

	        countMap.put(n, count);

	        if (count > max) {
	            max = count;
	        }
	    }

	    for (final Map.Entry<Integer, Integer> tuple : countMap.entrySet()) {
	        if (tuple.getValue() == max) {
	            modes.add(tuple.getKey());
	        }
	    }

	    return modes;
	}
}

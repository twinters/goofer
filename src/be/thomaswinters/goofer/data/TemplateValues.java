package be.thomaswinters.goofer.data;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

/**
 * Class representing the values to be filled in into a template. These are the
 * variables of a joke
 * 
 * @author Thomas Winters
 *
 */
public class TemplateValues {
	private final ImmutableList<String> values;

	/*-********************************************-*
	 *  Constructor
	*-********************************************-*/
	public TemplateValues(List<String> values) {
		this.values = ImmutableList.copyOf(values);
	}

	public TemplateValues(Iterator<String> values) {
		this.values = ImmutableList.copyOf(values);
	}

	public TemplateValues(String... values) {
		this(Arrays.asList(values));
	}
	/*-********************************************-*/

	/*-********************************************-*
	 *  Getters
	*-********************************************-*/
	public int getArity() {
		return values.size();
	}

	public List<String> getValues() {
		return values;
	}

	public String get(int index) {
		return values.get(index);
	}

	/*-********************************************-*/

	/*-********************************************-*
	 *  Value class
	*-********************************************-*/

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((values == null) ? 0 : values.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TemplateValues other = (TemplateValues) obj;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!values.equals(other.values))
			return false;
		return true;
	}
	
	
	@Override
	public String toString() {
		return "<" + values.stream().collect(Collectors.joining(",")) + ">";
	}
	/*-********************************************-*/

}

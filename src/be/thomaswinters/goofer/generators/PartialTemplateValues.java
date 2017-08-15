package be.thomaswinters.goofer.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import be.thomaswinters.goofer.data.TemplateValues;

public class PartialTemplateValues {

	private final List<Optional<String>> values;

	/*-********************************************-*
	 *  Constructor
	*-********************************************-*/
	public PartialTemplateValues(List<Optional<String>> values) {
		this.values = values;
	}

	public PartialTemplateValues(int size) {
		this(createEmptyList(size));
	}

	private static List<Optional<String>> createEmptyList(int size) {
		Builder<Optional<String>> b = ImmutableList.builder();
		for (int i = 0; i < size; i++) {
			b.add(Optional.empty());
		}
		return b.build();
	}

	/*-********************************************-*/

	/*-********************************************-*
	 *  Setters
	*-********************************************-*/

	public PartialTemplateValues fillIn(int index, String value) {
		List<Optional<String>> newList = new ArrayList<Optional<String>>(values);
		newList.set(index, Optional.of(value));
		return new PartialTemplateValues(newList);
	}

	/*-********************************************-*/

	/*-********************************************-*
	 *  Getters
	*-********************************************-*/

	public int getArity() {
		return values.size();
	}

	public List<Optional<String>> getValues() {
		return values;
	}

	public Optional<String> get(int index) {
		return values.get(index);
	}

	public boolean isPresent(int index) {
		return values.get(index).isPresent();
	}

	public boolean isComplete() {
		return values.stream().allMatch(e -> e.isPresent());
	}

	public boolean isEmpty() {
		return values.stream().allMatch(e -> !e.isPresent());
	}

	/*-********************************************-*/

	/*-********************************************-*
	 *  Builder
	*-********************************************-*/
	public TemplateValues toTemplateValues() {
		if (!isComplete()) {
			throw new IllegalStateException("The partial template values are not complete");
		}
		return new TemplateValues(values.stream().map(e -> e.get()).iterator());
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
		PartialTemplateValues other = (PartialTemplateValues) obj;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!values.equals(other.values))
			return false;
		return true;
	}
	/*-********************************************-*/

	@Override
	public String toString() {
		return "<" + values.stream().map(e -> e.orElse("?")).collect(Collectors.joining(",")) + ">";
	}

	public void fillIn(int index, List<String> seedWords) {
		for (int i = 0; i<seedWords.size();i++) {
			fillIn(index+i, seedWords.get(i));
		}
		
	}
}

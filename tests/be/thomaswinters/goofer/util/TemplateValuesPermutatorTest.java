package be.thomaswinters.goofer.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import be.thomaswinters.goofer.data.TemplateValues;

public class TemplateValuesPermutatorTest {

	@Test
	public void six_test() {
		List<TemplateValues> expected = Arrays.asList(
				//
				new TemplateValues(Arrays.asList("a", "c", "d")),
				//
				new TemplateValues(Arrays.asList("b", "c", "d")),
				//
				new TemplateValues(Arrays.asList("a", "c", "e")),
				//
				new TemplateValues(Arrays.asList("b", "c", "e")),
				//
				new TemplateValues(Arrays.asList("a", "c", "f")),
				//
				new TemplateValues(Arrays.asList("b", "c", "f")));
		assertEquals(new HashSet<>(expected),
				new HashSet<>(TemplateValuesPermutator.createAllTemplateValuesPermutations(
						Arrays.asList(Arrays.asList("a", "b"), Arrays.asList("c"), Arrays.asList("d", "e", "f")))));
	}

	@Test
	public void empty_test() {
		List<TemplateValues> expected = new ArrayList<>();
		assertEquals(new HashSet<>(expected),
				new HashSet<>(TemplateValuesPermutator.createAllTemplateValuesPermutations(
						Arrays.asList(new ArrayList<>(), Arrays.asList("c"), Arrays.asList("d", "e", "f")))));
	}

}

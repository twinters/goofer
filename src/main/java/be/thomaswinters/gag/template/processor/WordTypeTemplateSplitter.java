package be.thomaswinters.gag.template.processor;

import com.google.common.collect.ImmutableList;
import edu.mit.jwi.item.POS;
import be.thomaswinters.goofer.data.TemplateValues;
import be.thomaswinters.goofer.util.TemplateValuesPermutator;
import be.thomaswinters.pos.WordTypeCalculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Slices template values into template values with only one single word for
 * every value. Every word has the typing given in the allowedWordTypePerValue.
 *
 * @author Thomas Winters
 */
public class WordTypeTemplateSplitter implements ITemplateValuesProcessor {

    private final WordTypeCalculator wordTypeCalculator;
    private final List<POS> allowedWordTypePerValue;

    /*-********************************************-*
     *  Constructors
     *-********************************************-*/
    public WordTypeTemplateSplitter(WordTypeCalculator wordTypeCalculator, List<POS> allowedWordTypePerValue) {
        this.wordTypeCalculator = wordTypeCalculator;
        this.allowedWordTypePerValue = ImmutableList.copyOf(allowedWordTypePerValue);
    }

    public WordTypeTemplateSplitter(WordTypeCalculator wordTypeCalculator, POS... allowedWordTypePerValue) {
        this(wordTypeCalculator, Arrays.asList(allowedWordTypePerValue));
    }

    /*-********************************************-*/

    @Override
    public List<TemplateValues> process(TemplateValues templateValues) {
        // Check if correct size
        if (templateValues.getArity() != allowedWordTypePerValue.size()) {
            throw new IllegalArgumentException(
                    "Invalid amount of template values. Expected " + allowedWordTypePerValue.size() + ", but was "
                            + templateValues.getArity() + ". \n" + templateValues);
        }

        // Get word types of every word in every space of the template
        List<Collection<String>> acceptedWords = new ArrayList<>();
        for (int i = 0; i < templateValues.getArity(); i++) {
            String value = templateValues.get(i);
            POS allowedPOS = allowedWordTypePerValue.get(i);
            Collection<String> allowedWords = wordTypeCalculator.getWordsOfTypes(Arrays.asList(allowedPOS), value);
//			System.out.println("allowed" + allowedWords);
            acceptedWords.add(allowedWords);
        }

        // Create all permutations
        List<TemplateValues> values = TemplateValuesPermutator.createAllTemplateValuesPermutations(acceptedWords);
        if (values.isEmpty()) {
//			System.out.println("None for: " + templateValues);
//			System.out.println("> " + acceptedWords + "\n\n");
        }

        return values;
    }

}

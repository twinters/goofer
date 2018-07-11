package be.thomaswinters.gag.template.processor;

import be.thomaswinters.goofer.data.TemplateValues;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AnalogyConclusionSplitter implements ITemplateValuesProcessor {

    @Override
    public List<TemplateValues> process(TemplateValues templateValues) {
        List<TemplateValues> result = sliceConclusion(templateValues.get(2)).stream().map(conclusion -> createNewTemplateValues(templateValues, conclusion))
                .collect(Collectors.toList());

//		System.out.println("FROM: " + templateValues + "\nTO:   " + result.stream().map(e->e.toString()).collect(Collectors.joining("\n      ")) + "\n\n");

        return result;
    }

    private TemplateValues createNewTemplateValues(TemplateValues previous, String z) {
        return new TemplateValues(previous.get(0), previous.get(1), z);
    }

    /*-********************************************-*
     *  Slicer
     *-********************************************-*/

    public List<String> sliceConclusion(String conclusion) {
        String newZ = conclusion.replaceAll("&", " and ").replaceAll(", and ", " and ").replaceAll("  ", " ")
                .replaceAll("[.?!]+\\s*?$", "");

        List<String> zs = Arrays.asList(newZ.split(" and "));
        zs = zs.stream().flatMap(e -> Arrays.asList(e.split("[.,]")).stream()).map(e -> e.trim())
                .collect(Collectors.toList());
        return zs;
    }

    /**
     * Slices the Z of a solution so it becomes multiple templated solutions
     *
     * @param templateValues
     * @return
     */
    public Collection<TemplateValues> slice(TemplateValues templateValues) {
        List<String> zs = sliceConclusion(templateValues.get(2));

        return zs.stream().map(e -> new TemplateValues(templateValues.get(0), templateValues.get(1), e))
                .collect(Collectors.toList());
    }

    /*-********************************************-*/

}

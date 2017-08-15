package be.thomaswinters.goofer.util.argumentconverter;

import java.util.Arrays;
import java.util.List;

import com.beust.jcommander.converters.IParameterSplitter;

public class SemiColonSplitter implements IParameterSplitter {
    public List<String> split(String value) {
      return Arrays.asList(value.split(";"));
    }
}

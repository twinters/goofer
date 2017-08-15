package be.thomaswinters.goofer.classifier;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.NotImplementedException;

import be.thomaswinters.goofer.data.MetricValues;
import be.thomaswinters.goofer.knowledgebase.SchemaMetrics;
import be.thomaswinters.goofer.metrics.MetricApplicator;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

/**
 * This class is used to convert our MetricValues class to an arff file, usable
 * by WEKA.
 * 
 * @author Thomas Winters
 *
 */
public class MetricValuesToArffConverter {

	private final Optional<File> outputFile;

	/*-********************************************-*
	 *  Constructor
	*-********************************************-*/
	public MetricValuesToArffConverter(Optional<File> outputFile) {
		this.outputFile = outputFile;
	}

	public MetricValuesToArffConverter(File outputFile) {
		this(Optional.of(outputFile));
	}

	public MetricValuesToArffConverter() {
		this(Optional.empty());
	}
	/*-********************************************-*/

	/*-********************************************-*
	 *  Actual converter
	*-********************************************-*/

	public ArrayList<Attribute> createAttributes(SchemaMetrics header) {

		// Declare two numeric attributes
		ArrayList<Attribute> attributes = new ArrayList<Attribute>(
				header.getAmountOfMetrics() + header.getAmountOfRatings());
		for (MetricApplicator metric : header.getAllMetrics()) {
			attributes.add(metric.getAttribute());
		}
		Attribute ratingAttr = header.getRatingAggregator().getAttribute();
		attributes.add(ratingAttr);

		return attributes;
	}
	
	public Attribute getRatingAttribute(List<Attribute> attributes) {
		return attributes.get(attributes.size() - 1);
	}

	public Instances createEmptyInstances(SchemaMetrics header, String name) {
		// Attributes
		ArrayList<Attribute> attributes = createAttributes(header);
		Attribute ratingAttr = getRatingAttribute(attributes);

		// Set class index
		Instances instances = new Instances(name, attributes, 1);
		instances.setClass(ratingAttr);
		
		return instances;
	}
	
	public Instances convert(SchemaMetrics header, Instances instances, Collection<MetricValues> metricValues) {
		Attribute ratingAttr = instances.classAttribute();

		// Create an empty training set
		for (MetricValues values : metricValues) {
			Instance instance = convert(header, instances, values);

			// Add rating
			Optional<Object> rating = values.getValue(header.getAmountOfMetrics()+header.getAmountOfRatings()-1);
			if (rating.isPresent()) {
				Object ratingObj = rating.get();
				if (ratingObj instanceof Double) {
					instance.setValue(ratingAttr, (Double) ratingObj);
				} else {
					instance.setValue(ratingAttr, "" + ratingObj);
				}
			}

			// Add to the instances dataset
			instances.add(instance);

		}

		outputInstances(instances);

		return instances;
	}

	public Instance convert(SchemaMetrics header, Instances instances, MetricValues values) {
		// Create the instance
		DenseInstance instance = new DenseInstance(header.getAmountOfMetrics() + header.getAmountOfRatings());
		instance.setDataset(instances);

		// Add all values in the correct place
		for (int i = 0; i < values.size() - 1; i++) {
			Optional<Object> value = values.getValue(i);
			if (value.isPresent()) {
				Object number = value.get();
				if (number instanceof Double) {
					instance.setValue(i, (double) number);
				} else if (number instanceof Integer) {
					instance.setValue(i, (int) number);
				}  else if (number instanceof String) {
					instance.setValue(i, (String) number);
				} else {
					throw new NotImplementedException(number + "(" + number.getClass() + ")");
					// instance.setDataset(instances);
					// instance.setValue(i, number.toString());
				}
			} else {
				instance.setMissing(i);
			}
		}
		return instance;
	}

	/*-********************************************-*/

	/*-********************************************-*
	 *  Outputter
	*-********************************************-*/
	public void outputInstances(Instances instances) {
		if (!outputFile.isPresent()) {
			return;
		}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile.get()));
			writer.write(instances.toString());
			writer.newLine();
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*-********************************************-*/
}

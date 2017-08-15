package be.thomaswinters.goofer.data.exceptions;

import be.thomaswinters.goofer.data.Template;

public class UnextractableStringException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8465421598659417081L;

	public UnextractableStringException(String argument, Template template) {
		super("Given string \"" + argument + "\" can be converted to template values of template " + template);
	}
	public UnextractableStringException(String message) {
		super(message);
	}
	public UnextractableStringException(String message, Exception otherException) {
		super(message, otherException);
	}

}

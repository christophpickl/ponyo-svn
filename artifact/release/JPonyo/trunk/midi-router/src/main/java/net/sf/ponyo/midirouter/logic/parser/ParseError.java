package net.sf.ponyo.midirouter.logic.parser;

public class ParseError {
	
	private final String description;
	private final int lineNumber;
	
	public ParseError(String description, int lineNumber) {
		this.description = description;
		this.lineNumber = lineNumber;
	}

	public String getDescription() {
		return this.description;
	}

	public int getLineNumber() {
		return this.lineNumber;
	}
	
}

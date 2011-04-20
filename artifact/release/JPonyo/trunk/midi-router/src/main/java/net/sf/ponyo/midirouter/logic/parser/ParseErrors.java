package net.sf.ponyo.midirouter.logic.parser;

import java.util.Collection;
import java.util.LinkedList;


public class ParseErrors {
	
	private final Collection<ParseError> errors = new LinkedList<ParseError>();
	
	void addError(ParseError error) {
		this.errors.add(error);
	}
	
	public boolean hasErrors() {
		return this.errors.isEmpty() == false;
	}

	public String prettyPrint() {
		if(this.hasErrors() == false) {
			return "No errors.";
		}
		
		StringBuilder sb = new StringBuilder();
		for (ParseError error : this.errors) {
			sb.append("\n").append(error.getDescription());
		}
		
		return sb.substring(1);
	}
}

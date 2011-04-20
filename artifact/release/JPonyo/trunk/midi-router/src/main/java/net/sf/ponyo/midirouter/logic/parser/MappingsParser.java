package net.sf.ponyo.midirouter.logic.parser;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.ponyo.jponyo.common.geom.Range;
import net.sf.ponyo.jponyo.common.math.FloatPair;
import net.sf.ponyo.jponyo.common.math.IntPair;
import net.sf.ponyo.jponyo.entity.Direction;
import net.sf.ponyo.jponyo.entity.Joint;
import net.sf.ponyo.midirouter.logic.midi.MidiMapping;
import net.sf.ponyo.midirouter.logic.midi.MidiMappings;

public class MappingsParser {
	
	private static final Log LOG = LogFactory.getLog(MappingsParser.class);
	
	public MidiMappings parseMappings(String raw, ParseErrors errors) {
		List<MidiMapping> mappings = new LinkedList<MidiMapping>();
		
		if(raw.trim().isEmpty()) {
			errors.addError(new ParseError("Configuration is completely empty!", -1));
			return null;
		}
		
		String[] lines = raw.split("\n");
		int lineNumber = 0;
		for (String line : lines) {
			lineNumber++;
			line = line.trim();
			if(line.startsWith("#") || line.isEmpty()) {
				continue;
			}
			
			String[] tokens = line.split(",");
			
			if(tokens.length != 5) {
				errors.addError(new ParseError("Expected 5 arguments, but given: " + tokens.length, lineNumber));
				continue;
			}
			
			final String jointPart = tokens[0].trim();
			final String rawJointOsceletonId;
			final Joint relativeToJoint;
			if(jointPart.contains("#") == true) {
				final String[] jointPartParts = jointPart.split("#");
				rawJointOsceletonId = jointPartParts[0];
				final String rawRelativeToJoint = jointPartParts[1];
				relativeToJoint = Joint.byStringId(rawRelativeToJoint);
			} else {
				rawJointOsceletonId = jointPart;
				relativeToJoint = null;
			}
			Joint joint = Joint.byStringId(rawJointOsceletonId);
			Direction direction = Direction.valueOf(tokens[1].trim());
			
			Range range = parseRange(tokens[2].trim(), lineNumber, errors);
			if(range == null) {
				continue;
			}
			
			Integer midiChannel = parseInt(tokens[3]);
			// FIXME if(midiChannel == null) { display user error message "entered invalid midi channel" }
			Integer controllerNumber = parseInt(tokens[4]);
			
			MidiMapping mapping = new MidiMapping(joint, direction, range, midiChannel.intValue(), controllerNumber.intValue(), relativeToJoint);
			LOG.debug("successfully parsed mapping: " + mapping);
			mappings.add(mapping);
		}
		
		if(errors.hasErrors() == false && mappings.isEmpty() == true) {
			errors.addError(new ParseError("Configuration is empty!", -1));
		}
		
		if(errors.hasErrors() == true) {
			return null;
		}
		
		return new MidiMappings(mappings);
	}
	
	private Range parseRange(String in, int lineNumber, ParseErrors errors) {
		if(in.charAt(0) != '[') {
			errors.addError(new ParseError("Invalid: " + in, lineNumber));
			return null;
		}
		
		if(in.charAt(in.length() - 1) != ']') {
			errors.addError(new ParseError("Invalid: " + in, lineNumber));
			return null;
		}
		
		final String inBracketsCleanded = in.substring(1, in.length() - 1).trim();
		if(inBracketsCleanded.indexOf("=") == -1) {
			errors.addError(new ParseError("Invalid: " + in, lineNumber));
			return null;
		}
		
		final String[] inFromToPair = inBracketsCleanded.split("=>");
		if(inFromToPair.length != 2) {
			errors.addError(new ParseError("Invalid: " + in, lineNumber));
			return null;
		}
		final String rawFrom = inFromToPair[0].trim();
		final String rawTo = inFromToPair[1].trim();
		
		final String[] rawFromParts = rawFrom.split("\\.\\.");
		final float fromStart = Float.parseFloat(rawFromParts[0].trim());
		final float fromEnd = Float.parseFloat(rawFromParts[1].trim());
		
		final String[] rawToParts = rawTo.split("\\.\\.");
		final int toStart = Integer.parseInt(rawToParts[0].trim());
		final int toEnd = Integer.parseInt(rawToParts[1].trim());
		
		return new Range(new FloatPair(fromStart, fromEnd), new IntPair(toStart, toEnd));
	}
	
	private static Integer parseInt(String s) {
		try {
			return Integer.valueOf(Integer.parseInt(s.trim()));
		} catch(NumberFormatException e) {
			return null;
		}
	}
}

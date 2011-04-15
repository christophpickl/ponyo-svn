package net.sf.ponyo.midirouter.refactor;

import java.util.LinkedList;
import java.util.List;

import net.sf.josceleton.Josceleton;
import net.sf.josceleton.core.api.entity.joint.Joint;
import net.sf.josceleton.core.api.entity.location.Direction;
import net.sf.josceleton.core.api.entity.location.Range;
import net.sf.josceleton.prototype.midi.util.SomeUtil;

public class MappingsParser {
	public static int fooAdd(int x, int y) {
		return x + y;
	}
	public static MidiMapping[] parseMappings(String raw) throws InvalidInputException {
		List<MidiMapping> mappings = new LinkedList<MidiMapping>();
		
		String[] lines = raw.split("\n");
		for (String line : lines) {
			line = line.trim();
			if(line.startsWith("#") || line.isEmpty()) {
				continue;
			}
			
			String[] tokens = line.split(",");
			
			if(tokens.length != 5) {
				throw new RuntimeException("Expected 5 arguments, but given: " + tokens.length);
			}
			
			final String jointPart = tokens[0].trim();
			final String rawJointOsceletonId;
			final Joint relativeToJoint;
			if(jointPart.contains("#") == true) {
				final String[] jointPartParts = jointPart.split("#");
				rawJointOsceletonId = jointPartParts[0];
				final String rawRelativeToJoint = jointPartParts[1];
				relativeToJoint = SomeUtil.jointByOsceletonId(rawRelativeToJoint);
			} else {
				rawJointOsceletonId = jointPart;
				relativeToJoint = null;
			}
			Joint joint = SomeUtil.jointByOsceletonId(rawJointOsceletonId);
			Direction direction = Direction.valueOf(tokens[1].trim());
			Range range = parseRange(tokens[2].trim());
			int midiChannel = parseInt(tokens[3]);
			int controllerNumber = parseInt(tokens[4]);
			mappings.add(new MidiMapping(joint, direction, range, midiChannel, controllerNumber, relativeToJoint));
		}
		
		return mappings.toArray(new MidiMapping[mappings.size()]);
	}
	
	private static Range parseRange(String in) {
		if(in.charAt(0) != '[') {
			throw new RuntimeException("Invalid: " + in);
		}
		if(in.charAt(in.length() - 1) != ']') {
			throw new RuntimeException("Invalid: " + in);
		}
		final String inBracketsCleanded = in.substring(1, in.length() - 1).trim();
		if(inBracketsCleanded.indexOf("=") == -1) {
			throw new RuntimeException("Invalid: " + in);
		}
		
		final String[] inFromToPair = inBracketsCleanded.split("=>");
		if(inFromToPair.length != 2) {
			throw new RuntimeException("Invalid: " + in);
		}
		final String rawFrom = inFromToPair[0].trim();
		final String rawTo = inFromToPair[1].trim();
		
		final String[] rawFromParts = rawFrom.split("\\.\\.");
		final float fromStart = Float.parseFloat(rawFromParts[0].trim());
		final float fromEnd = Float.parseFloat(rawFromParts[1].trim());
		
		final String[] rawToParts = rawTo.split("\\.\\.");
		final int toStart = Integer.parseInt(rawToParts[0].trim());
		final int toEnd = Integer.parseInt(rawToParts[1].trim());
		
		return Josceleton.newRange(fromStart, fromEnd, toStart, toEnd);
	}
	
	private static int parseInt(String s) throws InvalidInputException {
		try {
			return Integer.parseInt(s.trim());
		} catch(NumberFormatException e) {
			throw InvalidInputException.newInvalidNumber(s);
		}
	}
}

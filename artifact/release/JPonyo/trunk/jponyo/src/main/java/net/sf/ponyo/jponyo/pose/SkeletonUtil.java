package net.sf.ponyo.jponyo.pose;

import net.sf.ponyo.jponyo.entity.Direction;
import net.sf.ponyo.jponyo.entity.Joint;
import net.sf.ponyo.jponyo.entity.Skeleton;

public class SkeletonUtil {

	public static boolean areInOneLine(Skeleton skeleton, Direction direction, float diffTolerance, Joint... joints) {
		float minValue = Float.MAX_VALUE;
		float maxValue = Float.MIN_VALUE;
		
		for (Joint joint : joints) {
			final float jointCoord = direction.extractValue(skeleton.getCoordinates(joint));
			if(jointCoord > maxValue) maxValue = jointCoord;
			if(jointCoord < minValue) minValue = jointCoord;
		}
		return Math.abs(minValue - maxValue) < diffTolerance;
	}
}

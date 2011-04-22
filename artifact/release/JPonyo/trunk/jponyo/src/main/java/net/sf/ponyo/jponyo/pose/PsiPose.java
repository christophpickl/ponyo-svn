package net.sf.ponyo.jponyo.pose;

import java.util.Arrays;

import net.sf.ponyo.jponyo.entity.Direction;
import net.sf.ponyo.jponyo.entity.Joint;
import net.sf.ponyo.jponyo.entity.Skeleton;

public class PsiPose extends AbstractPose {

	static float SIMILARITY_ELBOWS_WITH_SHOULDERS = 100.0F;
	static float SIMILARITY_HANDS_WITH_HEAD = 50.0F;
	
	public PsiPose() {
		super("Psi Pose", Arrays.asList(

			new PoseRule("hands are Y-similar with head") {
				@Override protected boolean validate(Skeleton skeleton) {
					return SkeletonUtil.areInOneLine(
						skeleton, Direction.Y, SIMILARITY_HANDS_WITH_HEAD,
						Joint.HEAD, Joint.LEFT_HAND, Joint.RIGHT_HAND);
				}
			},
			
			new PoseRule("elbows/shoulders are Y-similar") {
				@Override protected boolean validate(Skeleton skeleton) {
					return SkeletonUtil.areInOneLine(
						skeleton, Direction.Y, SIMILARITY_ELBOWS_WITH_SHOULDERS,
						Joint.LEFT_ELBOW, Joint.LEFT_SHOULDER,
						Joint.RIGHT_ELBOW, Joint.RIGHT_SHOULDER);
				}
			}
			
		));
	}

}

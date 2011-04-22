package net.sf.ponyo.jponyo.pose;

import java.util.Arrays;

import net.sf.ponyo.jponyo.entity.Skeleton;

public class PsiPose extends AbstractPose {

	public PsiPose() {
		super("Psi Pose", Arrays.asList(
			new PoseRule("Elbows raised") {
				@Override boolean validate(Skeleton skeleton) {
					return false;
				}
			},
			new PoseRule("Elbows raised") {
				@Override boolean validate(Skeleton skeleton) {
					return false;
				}
			}
		));
	}

}

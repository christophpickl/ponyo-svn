package net.pulseproject.mkinector.debugapp.view;

import java.awt.Component;

import net.pulseproject.mkinector.josceleton.api.entity.BodyPart;
import net.pulseproject.mkinector.josceleton.api.entity.Coordinate;

public interface UserPanel {

	Component asComponent();

	boolean isSkeletonAvailable();

	void setSkeletonAvailableTrue();
	
	void updateCoordinate(final BodyPart bodyPart, final Coordinate coordinate);
	

}

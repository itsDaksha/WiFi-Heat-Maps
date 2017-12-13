package org.aricent.hack100.heatmaps.model;

import java.util.ArrayList;

public class FloorPlan {
	String floorPixels;
	Float scalingFactor;
	
	ArrayList<AccessPoints> accessPoints = new ArrayList<AccessPoints>();

	public String getFloorPixels() {
		return floorPixels;
	}
	public void setFloorPixels(String floorSize) {
		this.floorPixels = floorSize;
	}
	public Float getScalingFactor() {
		return scalingFactor;
	}
	public void setScalingFactor(Float scalingFactor) {
		this.scalingFactor = scalingFactor;
	}
	public ArrayList<AccessPoints> getAccessPoints() {
		return accessPoints;
	}
	public void setAccessPoints(ArrayList<AccessPoints> accessPoints) {
		this.accessPoints = accessPoints;
	}
}

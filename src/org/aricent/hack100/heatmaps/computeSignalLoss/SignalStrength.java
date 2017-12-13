package org.aricent.hack100.heatmaps.computeSignalLoss;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.aricent.hack100.heatmaps.model.AccessPoints;
import org.aricent.hack100.heatmaps.model.FloorPlan;
import org.aricent.hack100.heatmaps.model.ObstructionPoints;

public class SignalStrength {
	
	public List<Byte[]> calculateSignalStrength(FloorPlan plan, Boolean [][] floorArray){
		
		Byte a[] , offset=100;
		List<Byte[]> cumulativeAPSignalling = new ArrayList<Byte[]>();
		int  i, j, permeabilityLoss, count=0, pathLossCofficient;
		long pathLoss;
		double distance;
		
		Float scale = plan.getScalingFactor();
		List<AccessPoints> accessPoints = plan.getAccessPoints();
		ObstructionPoints obtPoint;
		HashMap<String, Boolean> obstructionVerificationMap = new HashMap<String, Boolean>();
		for(i=0;i<480;i++)
		{
			for(j=0;j<640;j++)
			{
				if(floorArray[i][j])
				{
					obtPoint = new ObstructionPoints();
					obtPoint.setrValue(i);
					obtPoint.setcValue(j);
					//System.out.println("I: "+i+", j: "+j);
					obstructionVerificationMap.put(i+"+"+j, true);
				}
			}
		}

		if(accessPoints!=null)
		{
			for(AccessPoints ap : accessPoints)
			{
				a = new Byte[307200];
				count=0;
				pathLossCofficient = ap.getPowerLossCoff();
				for(i=0;i<480;i++)
				{
					for(j=0;j<640;j++)
					{
						distance = calculateDistance(ap.getxCoordinate(), ap.getyCoordinate(), i, j, scale);
						if(distance == 0.0)
							pathLoss = 63;
						else
							pathLoss = Math.round(32.4+(pathLossCofficient*Math.log10(ap.getFrequency()))+(pathLossCofficient*Math.log10(distance)));
						permeabilityLoss = checkForObstructionInBetween(ap.getxCoordinate(), ap.getyCoordinate(), i, j, obstructionVerificationMap);
						pathLoss = offset - (pathLoss + permeabilityLoss);
						if(pathLoss < 0)
							a[count++]=0;
						else
							a[count++]=(byte)pathLoss;
						/*if(a[count-1]>max)
						{
							max = a[count-1];
						}
						if(a[count-1]<min)
						{
							min = a[count-1];
						}*/
					}
				}
				cumulativeAPSignalling.add(a);
			}
		}
		return cumulativeAPSignalling;
	}
	
	public double calculateDistance(int rValueAP, int cValueAP, int i, int j, Float scale)
	{
		return (Math.sqrt(Math.pow((rValueAP-i),2) + Math.pow((cValueAP-j),2)) * scale );
	}
	
	public int checkForObstructionInBetween(int rValueAP, int cValueAP, int i, int j, HashMap<String, Boolean> obstructionVerificationMap)
	{
		//i=y and j=x
		int permeabilityLoss=0;
		if(j == cValueAP)
		{
			permeabilityLoss = checkForObstructionInStraightVerticalLine(rValueAP, cValueAP, i, j, obstructionVerificationMap);
		}
		else if(i == rValueAP)
		{
			permeabilityLoss = checkForObstructionInStraightHorizontalLine(rValueAP, cValueAP, i, j, obstructionVerificationMap);
		}
		else
		{
			float slope = (1.0f*(i - rValueAP))/(j - cValueAP);
			int startVertical, endVertical;
			// y - rValueAP = slope (x - cValueAP)
			int quadrant = checkForQuadrant(rValueAP, cValueAP, i, j);
			if(quadrant == 1)
			{
				startVertical=i;
				endVertical=rValueAP;
			}
			else if(quadrant == 2)
			{
				startVertical=i;
				endVertical=rValueAP;
			}
			else if(quadrant == 3)
			{
				startVertical=rValueAP;
				endVertical=i;
			}
			else
			{
				startVertical=rValueAP;
				endVertical=i;
			}

			for(int r=startVertical;r<=endVertical;r++)
			{
				// y - rValueAP = slope (x - cValueAP)
				
				/*int c1 = (int)Math.round(Math.ceil(((1.0f*(r-rValueAP)/slope)+cValueAP)));
				int c2 = (int)Math.round(Math.floor(((1.0f*(r-rValueAP)/slope)+cValueAP)));
				obj = new ObstructionPoints();
				obj2 = new ObstructionPoints();
				obj.setcValue(c1);
				obj.setrValue(r);
				obj2.setcValue(c2);
				obj2.setrValue(r);
				if(obstructionVerificationMap.get(r+"+"+c1) != null)
				{
					permeabilityLoss=+15;
				}
				else if(obstructionVerificationMap.get(r+"+"+c2) != null)
				{
					permeabilityLoss=+15;
				}*/
				
				int c = Math.round(((1.0f*(r-rValueAP)/slope)+cValueAP));
				if(obstructionVerificationMap.get(r+"+"+c) != null)
				{
					permeabilityLoss=+15;
				}
			}
		}
		/*if(permeabilityLoss>30)
			System.out.println("I: "+i+" J: "+j);*/
		return permeabilityLoss;
	}
	
	private int checkForObstructionInStraightHorizontalLine(int rValueAP, int cValueAP, int i, int j,
			HashMap<String, Boolean> obstructionVerificationMap) {
		int startHorizontal, endHorizontal, permeabilityLoss=0;
		if(j>=cValueAP)
		{
			startHorizontal = cValueAP;
			endHorizontal = j;
		}
		else
		{
			startHorizontal = j;
			endHorizontal = cValueAP;
		}
		for(int k = startHorizontal; k<=endHorizontal;k++)
		{
			if(obstructionVerificationMap.get(rValueAP+"+"+k) != null)
			{
				permeabilityLoss=+15;
			}
		}
		return permeabilityLoss;
	}

	public int checkForQuadrant(int rValueAP, int cValueAP, int i, int j)
	{
		if(j>=cValueAP && i<=rValueAP)
			return 1;
		else if(j>=cValueAP && i>=rValueAP)
			return 4;
		else if(j<=cValueAP && i>=rValueAP)
			return 3;
		else
			return 2;
		
	}
	
	public int checkForObstructionInStraightVerticalLine(int rValueAP, int cValueAP, int i, int j, HashMap<String, Boolean> obstructionVerificationMap)
	{
		int startVertical, endVertical, permeabilityLoss=0;
		if(i>=rValueAP)
		{
			startVertical = rValueAP;
			endVertical = i;
		}
		else
		{
			startVertical = i;
			endVertical = rValueAP;
		}
		for(int k = startVertical; k<=endVertical;k++)
		{
			if(obstructionVerificationMap.get(k+"+"+cValueAP) != null)
			{
				permeabilityLoss=+15;
			}
		}
		return permeabilityLoss;
	}
}
package org.aricent.hack100.heatmaps.jsonHandler;

import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aricent.hack100.heatmaps.computeSignalLoss.SignalStrength;
import org.aricent.hack100.heatmaps.model.AccessPoints;
import org.aricent.hack100.heatmaps.model.FloorPlan;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonHandler {

	public String processHandleJsonRequest(URI uri, HttpServletRequest request, HttpServletResponse response) {
		String jsonResponse = null;
		Boolean [][] floorArray = new Boolean[480][640];
        String method = uri.getPath();        
        FloorPlan plan = new FloorPlan();
        int count=0;
        /*for (NameValuePair param : params) {
        
       System.out.println( "Parameter Name : " 
        +param.getName() +" Param Value: "+param.getValue());
			if (!param.getName().contains("AP")) {
				switch (param.getName()) {
				case "floorSize":
					plan.setFloorPixels(param.getValue());
					break;
				case "apNum":
					plan.setApNum(Integer.parseInt(param.getValue()));
					break;
				case "factor":
					plan.setScalingFactor(Float.parseFloat(param.getValue()));
					break;
				}
			}else{
				parseAndFillAcessPoints(plan, param.getValue());
				break;
			}
            
        }
        */
        Map<String, String[]> requestMap = request.getParameterMap();
        Iterator<?> iterator = requestMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry pair = (Map.Entry) iterator.next();
			//System.out.println("Key: " + pair.getKey() + ", Value: " + pair.getValue());
			if (pair.getKey().equals("floorSize")) {
				plan.setFloorPixels(request.getParameter("floorSize"));
			} else if (pair.getKey().equals("factor")) {
				plan.setScalingFactor(Float.parseFloat(request.getParameter("factor")));
			}else {
				count++;
				parseAndFillAcessPoints(plan, request.getParameter("ap" + count));
			}
		}
       
       
       
        createFloorPlan(plan,floorArray);
        switch(method){
        case "generateHeatMap":
        	SignalStrength signal = new SignalStrength();
        	try {
        		jsonResponse = createJsonResponse(signal.calculateSignalStrength(plan,floorArray));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	break;
        }

		return jsonResponse;
	}

	private String createJsonResponse(List<Byte[]> calculateSignalStrength) throws JSONException {
		JSONObject jsonObj = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		for(Byte[] signalLoss:calculateSignalStrength){
			//System.out.println("Signal Streangth:- "+signalLoss);
			jsonArray.put(calculateSignalStrength);
			
		}
		jsonObj.put("SignalStrength", jsonArray);
		return jsonObj.toString();
		
	}

	private void createFloorPlan(FloorPlan plan, Boolean[][] floorArray) {
		
		int element = 0;
		int row=0;
		while(element<=614399){			
			String substring = plan.getFloorPixels().substring(element, element+1280-1);
			String [] stringArray = substring.split(",");
			for(int column = 0;column<stringArray.length;column++){
				if(stringArray[column].equals("1")){
					floorArray[row][column] = true;
				}else{
					floorArray[row][column] = false;
				}
				/*System.out.println("Value"+Boolean.parseBoolean(stringArray[column]));
				floorArray[row][column] = Boolean.parseBoolean(stringArray[column]);*/
			}
			row=row+1;
			element = element+1280;
			
		}
	}

	private void parseAndFillAcessPoints(FloorPlan plan, String string) {
		String[] acessPointValues = string.split(",");
		AccessPoints accessPoints = new AccessPoints();
		accessPoints.setxCoordinate(Integer.parseInt(acessPointValues[0]));
		accessPoints.setyCoordinate(Integer.parseInt(acessPointValues[1]));
		accessPoints.setFrequency(Float.parseFloat(acessPointValues[2]));
		accessPoints.setPowerLossCoff(Integer.parseInt(acessPointValues[3]));
		plan.getAccessPoints().add(accessPoints);
		
	}
}

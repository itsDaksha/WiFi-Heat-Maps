package org.aricent.hack100.heatmaps.requestHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aricent.hack100.heatmaps.jsonHandler.JsonHandler;
import org.json.JSONException;

public class HTTPRequestHandler extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
             ex.printStackTrace();
        }
    }
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException {
		String responseJson=null;
		String requestURL =	request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/") + 1) + "?" + request.getQueryString();
        URI uri = null;
        try {
            if (requestURL != null) {
                uri = new URI(requestURL);
            }
        } catch (URISyntaxException ex) {
        	
        }
        JsonHandler jsonRequestHandler = new JsonHandler();
        if (requestURL != null) {
            responseJson = jsonRequestHandler.processHandleJsonRequest(uri, request, response);
        }

		//JSONObject json = JsonReader.readJsonFromUrl(requestURL);
		
		/*responseJson = jsonRequestHandler.processHandleJsonRequest(json);*/
        response.setContentType("application/json");

        try (PrintWriter out = response.getWriter()) {
            if (responseJson != null) {
                out.println(responseJson);
            }
        }

	}

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            //System.out.println("IN POST");
            processRequest(request, response);
        } catch (Exception e){
        	e.printStackTrace();
        }
        	
        }



}


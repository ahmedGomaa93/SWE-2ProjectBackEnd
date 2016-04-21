package com.services;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.mvc.Viewable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.models.DBConnection;
import com.models.PlaceModel;
import com.models.TipModel;

@Path("/Places")
public class PlaceServices {

	@POST
	@Path("/addNewPlace")
	@Produces(MediaType.TEXT_PLAIN)
	public String addNewPlace(@FormParam("latitude") Double latitude, @FormParam("longitude") Double longitude,
			@FormParam("name") String name, @FormParam("description") String description) {
		PlaceModel newPlace = PlaceModel.addNewPlace(latitude, longitude, name, description);
		
		JSONObject json = new JSONObject();
		
		json.put("id", newPlace.getPlaceID());
		json.put("name", newPlace.getName());
		json.put("description", newPlace.getDescription());
		json.put("lat", newPlace.getLatitude());
		json.put("long", newPlace.getLongitude());
		
		return json.toJSONString();
	}
	
	@POST
	@Path("/getPlacebyName")
	@Produces(MediaType.TEXT_PLAIN)
	public String getPlacebyCoordinates(@FormParam("name") String name) {
		ArrayList<PlaceModel> places = new ArrayList<>();
		JSONArray jsonArray = new JSONArray();
		JSONObject json = new JSONObject();
		
		places = PlaceModel.getPlacebyName(name);
		
		for (PlaceModel place : places) {
			json.put("id", place.getPlaceID());
			json.put("name", place.getName());
			json.put("description", place.getDescription());
			json.put("lat", place.getLatitude());
			json.put("long", place.getLongitude());
			
			jsonArray.add(json);
		}
		
		return jsonArray.toJSONString();
	}

	/*@GET
	@Path("/getPlace")
	@Produces(MediaType.TEXT_PLAIN)
	public String getPlacebyCoordinates(Double latitude, Double longitude) {
		return null;
	}
	
	@GET
	@Path("/getPlaces")
	@Produces(MediaType.TEXT_PLAIN)
	public String getPlaces(){
		return null;
	}

	@POST
	@Path("/addTip")
	@Produces(MediaType.TEXT_PLAIN)
	public String addTip(String userID, String placeID, String tipTitle, String tipBody) {
		return null;
	}
	
	@GET
	@Path("/ratePlace")
	@Produces(MediaType.TEXT_PLAIN)
	public String ratePlace(String placeID, int rate) {
		return null;
	}

	@GET
	@Path("/likeTip")
	@Produces(MediaType.TEXT_PLAIN)
	public String likeTip(String userID, String tipID) {
		return null;
	}*/
}
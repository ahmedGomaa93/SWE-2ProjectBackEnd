package com.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

public class PlaceModel {
	
	private int placeID;
	private Double latitude;
	private Double longitude;
	private String name;
	private String description;
	private int rate;
	private ArrayList<String> tests;
	
	public int getPlaceID() {
		return placeID;
	}

	public void setPlaceID(int placeID) {
		this.placeID = placeID;
	}
	
	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public ArrayList<String> getTests() {
		return tests;
	}

	public void setTests(ArrayList<String> tests) {
		this.tests = tests;
	}

	public static PlaceModel addNewPlace(Double latitude, Double longitude, String name, String description) {
		try {
			Connection conn = DBConnection.getActiveConnection();
			PreparedStatement stmt;
			ResultSet result;
			String sql;
			
			sql = "INSERT INTO places(`name`,`description`, `latitude`, `longitude`) VALUES(?,?,?,?)";
			
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			System.out.println(stmt);
			stmt.setString(1, name);
			stmt.setString(2, description);
			stmt.setDouble(3, latitude);
			stmt.setDouble(4, longitude);
			
			stmt.executeUpdate();
			
			result = stmt.getGeneratedKeys();
			
			if (result.next()) {
				PlaceModel newPlace = new PlaceModel();
				
				newPlace.placeID = result.getInt(1);
				newPlace.name = name;
				newPlace.description = description;
				newPlace.latitude = latitude;
				newPlace.longitude = longitude;
				
				return newPlace;
			}
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public PlaceModel getPlacebyCoordinates(Double latitude, Double longitude) {
		return null;
	}

	//public abstract ArrayList<Places> sort();

	public int ratePlace(String placeID) {
		return 0;
	}
}
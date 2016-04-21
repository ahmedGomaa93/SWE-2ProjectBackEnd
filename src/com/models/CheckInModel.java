package com.models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mysql.jdbc.Statement;
import com.sun.jmx.snmp.Timestamp;

public class CheckInModel {

	private int checkInID;
	private int userID;
	private int placeID;
	private String checkInBody;
	private java.sql.Timestamp date;
	private ArrayList<String> likes;
	private Map<String, String> comments;
	  
	public CheckInModel() {
		// TODO Auto-generated constructor stub
		setComments(new HashMap<String, String>());
	}


	public int getCheckInID() {
		return checkInID;
	}


	public void setCheckInID(int checkInID) {
		this.checkInID = checkInID;
	}


	public int getUserID() {
		return userID;
	}


	public void setUserID(int userID) {
		this.userID = userID;
	}


	public int getPlaceID() {
		return placeID;
	}


	public void setPlaceID(int placeID) {
		this.placeID = placeID;
	}


	public String getCheckInBody() {
		return checkInBody;
	}


	public void setCheckInBody(String checkInBody) {
		this.checkInBody = checkInBody;
	}


	public java.sql.Timestamp getDate() {
		return date;
	}


	public void setDate(java.sql.Timestamp date) {
		this.date = date;
	}


	public ArrayList<String> getLikes() {
		return likes;
	}


	public void setLikes(ArrayList<String> likes) {
		this.likes = likes;
	}


	public Map<String, String> getComments() {
		return comments;
	}


	public void setComments(Map<String, String> comments) {
		this.comments = comments;
	}

	public static CheckInModel checkIn(int userID, int placeID, String body) {
		try {
			Connection conn = DBConnection.getActiveConnection();
			CheckInModel checkInpost = new CheckInModel();
			PreparedStatement stmt;
			ResultSet result;
			String sql;
			int checkInID;
			
			sql = "INSERT INTO check_ins(`body`,`date`) VALUES(?,NOW())";
			
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, body);
			stmt.executeUpdate();
			result = stmt.getGeneratedKeys();
			
			if(result.next()){
				checkInID = result.getInt(1);
				
				sql = "INSERT INTO users_has_check_ins(`users_userId`,`check_ins_id`) VALUES(?,?)";
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, userID);
				stmt.setInt(2, checkInID);
				stmt.executeUpdate();
				
				sql = "INSERT INTO places_has_check_ins(`places_placeId`,`check_ins_id`) VALUES(?,?)";
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, placeID);
				stmt.setInt(2, checkInID);
				stmt.executeUpdate();
				
				//To get the date
				sql = "SELECT * FROM check_ins WHERE `id` = ?";
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, checkInID);
				result = stmt.executeQuery();
				
				if(result.next()){
					checkInpost.checkInID = checkInID;
					checkInpost.userID = userID;
					checkInpost.placeID = placeID;
					checkInpost.checkInBody = body;
					checkInpost.date = result.getTimestamp("date");
					checkInpost.likes = null;
					checkInpost.comments = null;
				}
				
				return checkInpost;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public void likeCheckIn(String checkInID, String userID) {
	}

	public void commentCheckIn(String checkInID, Integer userID, String comment) {
	}

	public CheckInModel getCheckIns(Integer userID) {
		return null;
	}
}
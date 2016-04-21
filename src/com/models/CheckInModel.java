package com.models;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CheckInModel {

	public String checkInID;
	
	public String userID;
	
	public String placeID;
	
	public String checkInBody;
	
	public Date date;
	
	public ArrayList<String> likes;
	
	public Map<String, String> comments;
	  
	public CheckInModel() {
		// TODO Auto-generated constructor stub
		comments = new HashMap<String, String>();
	}


	public void checkIn(Integer userID, String placeID, String body) {
	}

	public void likeCheckIn(String checkInID, String userID) {
	}

	public void commentCheckIn(String checkInID, Integer userID, String comment) {
	}

	public CheckInModel getCheckIns(Integer userID) {
		return null;
	}
}
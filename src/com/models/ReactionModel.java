package com.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.mysql.jdbc.Statement;

public class ReactionModel {
	private int reactionID;
	private String reactionType;
	private int reactorID;
	private String reactorName;
	private Timestamp reactionDate;
	private String reactionBody;
	
	public ReactionModel() {
		// TODO Auto-generated constructor stub
		reactionID = 0;
		reactionType = "";
		reactorID = 0;
		reactorName = "";
		reactionDate = null;
		reactionBody = "";
	}
	
	public int getReactionID() {
		return reactionID;
	}
	
	public void setReactionID(int reactionID) {
		this.reactionID = reactionID;
	}
	
	public String getReactionType() {
		return reactionType;
	}
	
	public void setReactionType(String reactionType) {
		this.reactionType = reactionType;
	}
	
	public int getReactorID() {
		return reactorID;
	}

	public void setReactorID(int reactorID) {
		this.reactorID = reactorID;
	}

	public String getReactorName() {
		return reactorName;
	}

	public void setReactorName(String reactorName) {
		this.reactorName = reactorName;
	}

	public Timestamp getReactionDate() {
		return reactionDate;
	}
	
	public void setReactionDate(Timestamp reactionDate) {
		this.reactionDate = reactionDate;
	}
	
	public String getReactionBody() {
		return reactionBody;
	}
	
	public void setReactionBody(String reactionBody) {
		this.reactionBody = reactionBody;
	}
	
	public static int saveReaction(String reactionType, int checkinID, int tipID, int reactorID, 
			String reactionBody){
		try {
			Connection conn = DBConnection.getActiveConnection();
			PreparedStatement stmt;
			ResultSet result;
			String sql;
			int reactionID = 0, ownerID;
			
			sql = "INSERT INTO reactions(`reactionType`,`check_ins_id`, `tips_tipId`, `reactorId`, `reactionBody`,"
					+ " `reactionDate`) VALUES(?,?,?,?,?,NOW())";
			
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, reactionType);
			if(tipID == 0){
				stmt.setInt(2, checkinID);
				stmt.setString(3, null);
			}
			else{
				stmt.setString(2, null);
				stmt.setInt(3, tipID);
			}
			stmt.setInt(4, reactorID);
			stmt.setString(5, reactionBody);
			
			stmt.executeUpdate();
			result = stmt.getGeneratedKeys();
			
			if(result.next()){
				reactionID = result.getInt(1);
				ownerID = CheckInModel.getCheckIn(checkinID).getOwnerID();
				
				NotificationModel.saveNotification(checkinID, tipID, ownerID, reactionID);
			}
			
			return reactionID;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
}

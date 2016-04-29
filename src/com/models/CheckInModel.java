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

import org.json.simple.JSONObject;

import com.mysql.jdbc.Statement;
import com.sun.jmx.snmp.Timestamp;

public class CheckInModel {

	private int checkInID;
	private int ownerID;
	private String ownerName;
	private int placeID;
	private String placeName;
	private String checkInBody;
	private java.sql.Timestamp date;
	private ArrayList<ReactionModel> reactions;

	public CheckInModel() {
		// TODO Auto-generated constructor stub
		checkInID = 0;
		ownerID = 0;
		ownerName = "";
		placeID = 0;
		placeName = "";
		checkInBody = "";
		date = null;
		reactions = new ArrayList<>();
	}
	
	public int getCheckInID() {
		return checkInID;
	}

	public void setCheckInID(int checkInID) {
		this.checkInID = checkInID;
	}

	public int getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(int ownerID) {
		this.ownerID = ownerID;
	}
	
	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public int getPlaceID() {
		return placeID;
	}


	public void setPlaceID(int placeID) {
		this.placeID = placeID;
	}


	public String getPlaceName() {
		return placeName;
	}


	public void setPlaceName(String placeName) {
		this.placeName = placeName;
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

	public ArrayList<ReactionModel> getReactions() {
		return reactions;
	}

	public void setReactions(ArrayList<ReactionModel> reactions) {
		this.reactions = reactions;
	}

	public static CheckInModel checkIn(int userID, int placeID, String body) {
		try {
			Connection conn = DBConnection.getActiveConnection();
			CheckInModel checkInpost = new CheckInModel();
			PreparedStatement stmt;
			ResultSet result;
			String sql;
			int checkInID;
			
			sql = "INSERT INTO check_ins(`body`,`date`, `placeId`, `userId`) VALUES(?,NOW(),?,?)";
			
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, body);
			stmt.setInt(2, placeID);
			stmt.setInt(3, userID);
			stmt.executeUpdate();
			result = stmt.getGeneratedKeys();
			
			if(result.next()){
				checkInID = result.getInt(1);
				
				//To get the date
				sql = "SELECT * FROM check_ins WHERE `id` = ?";
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, checkInID);
				result = stmt.executeQuery();
				
				if(result.next()){
					checkInpost.checkInID = checkInID;
					checkInpost.ownerID = userID;
					checkInpost.placeID = placeID;
					checkInpost.checkInBody = body;
					checkInpost.date = result.getTimestamp("date");
				}
				
				//Register post subject
				PostSubject postSubject = new PostSubject();
				LikeObserver likeObserver = new LikeObserver();
				CommentObserver commentObserver = new CommentObserver();
				
				likeObserver.setObserverID(userID);
				commentObserver.setObserverID(userID);
				
				postSubject.register(likeObserver);
				postSubject.register(commentObserver);
				
				likeObserver.setPost(postSubject);
				commentObserver.setPost(postSubject);
				
				return checkInpost;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public static void likeCheckIn(int checkinID, int reactorID) {
		ReactionModel.saveReaction("like", checkinID, 0, reactorID, null);
	}
	
	public static void commentCheckIn(int checkinID, int reactorID, String comment) {
		ReactionModel.saveReaction("comment", checkinID, 0, reactorID, comment);
	}

	public static CheckInModel getCheckIn(int checkinID){
		try {
			Connection conn = DBConnection.getActiveConnection();
			CheckInModel checkinPost = new CheckInModel();
			ReactionModel reaction = new ReactionModel();
			PreparedStatement stmt;
			ResultSet result;
			String sql;
			
			sql = "Select owners.name as ownerName, check_ins.body as checkinBody, check_ins.date as checkinDate,"
					+ " places.name as placeName, reactions.reactionType, reactors.name as reactorName, "
					+ "reactions.reactionDate, reactions.reactionBody From check_ins LEFT JOIN users as owners "
					+ "ON owners.userId = check_ins.userId LEFT JOIN places ON check_ins.placeId = places.placeId "
					+ "LEFT Join reactions ON check_ins.id = reactions.check_ins_id LEFT Join users as reactors "
					+ "ON reactions.reactorId = reactors.userId WHERE check_ins.id = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, checkinID);
			result = stmt.executeQuery();
				
			if(result.next()){
				checkinPost.checkInID = checkinID;
				checkinPost.ownerName = result.getString("ownerName");
				checkinPost.checkInBody = result.getString("checkinBody");
				checkinPost.date = result.getTimestamp("checkinDate");
				checkinPost.placeName = result.getString("placeName");
				
				reaction.setReactionType(result.getString("reactionType"));
				reaction.setReactorName(result.getString("reactorName"));
				reaction.setReactionDate(result.getTimestamp("reactionDate"));
				reaction.setReactionBody(result.getString("reactionBody"));
				
				checkinPost.reactions.add(reaction);
			}
			
			if(!result.isLast()){
				while(result.next()){
					reaction.setReactionType(result.getString("reactionType"));
					reaction.setReactorName(result.getString("reactorName"));
					reaction.setReactionDate(result.getTimestamp("reactionDate"));
					reaction.setReactionBody(result.getString("reactionBody"));
					
					checkinPost.reactions.add(reaction);
				}
			}
			
			return checkinPost;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void saveNotification(int ownerID, int reactionID, int postID) {
		try {
			Connection conn = DBConnection.getActiveConnection();
			PreparedStatement stmt;
			String sql;
			
			sql = "INSERT INTO notifications(`ownerId`,`postId`, `reactionId`) VALUES(?,?,?)";
			
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, ownerID);
			stmt.setInt(2, postID);
			stmt.setInt(3, reactionID);
			
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ArrayList<CheckInModel> getUserCheckIns(Integer userID) {
		try {
			Connection conn = DBConnection.getActiveConnection();
			ArrayList<CheckInModel> userPosts = new ArrayList<>();
			CheckInModel checkinPost = new CheckInModel();
			ReactionModel reaction = new ReactionModel();
			PreparedStatement stmt;
			ResultSet result;
			String sql;
			int checkinId, counter;
			
			sql = "Select check_ins.Id as checkinId, owners.name as ownerName, check_ins.body as checkinBody, "
					+ "check_ins.date as checkinDate, check_ins.placeId as checkinPlaceId, places.name as placeName, reactions.reactionType, "
					+ "reactors.name as reactorName, reactions.reactionDate, reactions.reactionBody "
					+ "From check_ins LEFT JOIN users as owners ON owners.userId = check_ins.userId "
					+ "LEFT JOIN places ON check_ins.placeId = places.placeId LEFT Join reactions ON"
					+ " check_ins.id = reactions.check_ins_id LEFT Join users as reactors "
					+ "ON reactions.reactorId = reactors.userId WHERE check_ins.userId = ?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userID);
			result = stmt.executeQuery();
			
			while(result.next()){
				checkinId = result.getInt("checkinId");
				counter = 0;
				
				for (CheckInModel post : userPosts) {
						if(post.checkInID == checkinId){
							reaction.setReactionType(result.getString("reactionType"));
							reaction.setReactorName(result.getString("reactorName"));
							reaction.setReactionDate(result.getTimestamp("reactionDate"));
							reaction.setReactionBody(result.getString("reactionBody"));
							
							checkinPost.reactions.add(reaction);
							break;
						}
						
						counter++;
					}
				
				if(counter == userPosts.size()){
					checkinPost = new CheckInModel();
					
					checkinPost.checkInID = checkinId;
					checkinPost.ownerID = userID;
					checkinPost.checkInBody = result.getString("checkinBody");
					checkinPost.date = result.getTimestamp("checkinDate");
					checkinPost.placeID = result.getInt("checkinPlaceId");
					checkinPost.placeName = result.getString("placeName");
					checkinPost.ownerName = result.getString("ownerName");
					
					if(result.getString("reactionType") != null){
						reaction.setReactionType(result.getString("reactionType"));
						reaction.setReactorName(result.getString("reactorName"));
						reaction.setReactionDate(result.getTimestamp("reactionDate"));
						reaction.setReactionBody(result.getString("reactionBody"));
						checkinPost.reactions.add(reaction);
					}
					else{
						checkinPost.reactions = null;
					}
					
					userPosts.add(checkinPost);
				}
			}
			
			return userPosts;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}

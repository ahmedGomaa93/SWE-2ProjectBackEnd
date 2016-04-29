package com.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.mysql.jdbc.Statement;

public abstract class NotificationModel {
	private int notificationID;
	private int postID;
	private int ownerID;
	private int reactionID;
	private String reactorName;
	private String notificationDescription;
	private Boolean isRead;
	
	public int getNotificationID() {
		return notificationID;
	}
	public void setNotificationID(int notificationID) {
		this.notificationID = notificationID;
	}
	public int getPostID() {
		return postID;
	}
	public void setPostID(int postID) {
		this.postID = postID;
	}
	public int getOwnerID() {
		return ownerID;
	}
	public void setOwnerID(int ownerID) {
		this.ownerID = ownerID;
	}
	public int getReactionID() {
		return reactionID;
	}
	public void setReactionID(int reactionID) {
		this.reactionID = reactionID;
	}
	public Boolean getIsRead() {
		return isRead;
	}
	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}
	
	public String getReactorName() {
		return reactorName;
	}
	public void setReactorName(String reactorName) {
		this.reactorName = reactorName;
	}
	public String getNotificationDescription() {
		return notificationDescription;
	}
	public void setNotificationDescription(String notificationDescription) {
		this.notificationDescription = notificationDescription;
	}
	public static void saveNotification(int checkinID, int tipID, int ownerID, int reactionID){
		try {
			Connection conn = DBConnection.getActiveConnection();
			PreparedStatement stmt;
			String sql;
			
			sql = "INSERT INTO notifications(`ownerId`,`checkinId`, `tipId`, `reactionId`) VALUES(?,?,?,?)";
			
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, ownerID);
			if(tipID == 0){
				stmt.setInt(2, checkinID);
				stmt.setString(3, null);
			}
			else{
				stmt.setString(2, null);
				stmt.setInt(3, tipID);
			}
			stmt.setInt(4, reactionID);
			
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ArrayList<NotificationModel> getUnreadNotifications(int userID){
		try {
			Connection conn = DBConnection.getActiveConnection();
			ArrayList<NotificationModel> notifications = new ArrayList<>();
			PreparedStatement stmt;
			ResultSet result;
			String sql, reactionType, notificationsIDs;
			StringBuilder updatedNotifactions = new StringBuilder();
			
			sql = "SELECT notifications.*, users.name as reactorName, reactions.reactionType, reactions.reactorId "
					+ "FROM notifications LEFT JOIN reactions ON notifications.reactionId = reactions.reactionId "
					+ "LEFT JOIN users ON users.userId = reactions.reactorId WHERE `ownerId` = ? AND isRead = 0";
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userID);
			
			result = stmt.executeQuery();
			
			while(result.next()){
				reactionType = result.getString("reactionType");
				if(reactionType.equalsIgnoreCase("like")){
					LikeNotification likeNotification = new LikeNotification();
					
					likeNotification.setNotificationID(result.getInt("notificationId"));
					likeNotification.setNotificationDescription(result.getString("reactorName") +
							" like your check in");
					
					notifications.add(likeNotification);
				}
				else if(reactionType.equalsIgnoreCase("comment")){
					CommentNotification commentNotification = new CommentNotification();
					
					commentNotification.setNotificationID(result.getInt("notificationId"));
					commentNotification.setNotificationDescription(result.getString("reactorName") +
							" comment on your check in");
					
					notifications.add(commentNotification);
				}
				
				//Mark notifications as read
				updatedNotifactions.append(result.getString("notificationId"));
				updatedNotifactions.append(", ");
			}
			
			updatedNotifactions.delete(updatedNotifactions.length() - 2, 1);
            notificationsIDs = updatedNotifactions.toString();
            
            sql = "UPDATE notifications SET isRead = 1 WHERE notificationId IN (?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, notificationsIDs);
            stmt.executeUpdate();
            
			return notifications;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static ArrayList<NotificationModel> getAllNotifications(int userID){
		try {
			Connection conn = DBConnection.getActiveConnection();
			ArrayList<NotificationModel> notifications = new ArrayList<>();
			PreparedStatement stmt;
			ResultSet result;
			String sql, reactionType;
			
			sql = "SELECT notifications.*, users.name as reactorName, reactions.reactionType, reactions.reactorId "
					+ "FROM notifications LEFT JOIN reactions ON notifications.reactionId = reactions.reactionId "
					+ "LEFT JOIN users ON users.userId = reactions.reactorId WHERE `ownerId` = ?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userID);
			
			result = stmt.executeQuery();
			
			while(result.next()){
				reactionType = result.getString("reactionType");
				if(reactionType.equalsIgnoreCase("like")){
					LikeNotification likeNotification = new LikeNotification();
					
					likeNotification.setNotificationID(result.getInt("notificationId"));
					likeNotification.setNotificationDescription(result.getString("reactorName") +
							" like your check in");
					
					notifications.add(likeNotification);
				}
				else if(reactionType.equalsIgnoreCase("comment")){
					CommentNotification commentNotification = new CommentNotification();
					
					commentNotification.setNotificationID(result.getInt("notificationId"));
					commentNotification.setNotificationDescription(result.getString("reactorName") +
							" comment on your check in");
					/*commentNotification.setNotificationID(result.getInt("notificationId"));
					commentNotification.setOwnerID(userID);
					commentNotification.setPostID(result.getInt("checkinId"));
					commentNotification.setReactionID(result.getInt("reactionId"));
					commentNotification.setReactorName(result.getString("reactorName"));*/
					
					notifications.add(commentNotification);
				}
			}
			
			return notifications;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static NotificationModel getNotificationByID(int notificationID){
		try {
			Connection conn = DBConnection.getActiveConnection();
			PreparedStatement stmt;
			ResultSet result;
			String sql, reactionType;
			
			sql = "SELECT notifications.*, users.name as reactorName, reactions.reactionType, reactions.reactorId "
					+ "FROM notifications LEFT JOIN reactions ON notifications.reactionId = reactions.reactionId "
					+ "LEFT JOIN users ON users.userId = reactions.reactorId WHERE `notificationId` = ?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, notificationID);
			
			result = stmt.executeQuery();
			
			if(result.next()){
				reactionType = result.getString("reactionType");
				if(reactionType.equalsIgnoreCase("like")){
					LikeNotification likeNotification = new LikeNotification();
					
					likeNotification.setNotificationID(notificationID);
					likeNotification.setOwnerID(result.getInt("userID"));
					likeNotification.setPostID(result.getInt("checkinId"));
					likeNotification.setReactionID(result.getInt("reactionId"));
					likeNotification.setReactorName(result.getString("reactorName"));
					
					return likeNotification;
				}
				else if(reactionType.equalsIgnoreCase("comment")){
					CommentNotification commentNotification = new CommentNotification();
					
					commentNotification.setNotificationID(notificationID);
					commentNotification.setOwnerID(result.getInt("userID"));
					commentNotification.setPostID(result.getInt("checkinId"));
					commentNotification.setReactionID(result.getInt("reactionId"));
					commentNotification.setReactorName(result.getString("reactorName"));
					
					return commentNotification;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public abstract CheckInModel respond();
}

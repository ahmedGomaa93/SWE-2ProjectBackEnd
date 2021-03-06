package com.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mysql.jdbc.Statement;

public class UserModel {
	
	private int id;
	private String name;
	private String email;
	private String pass;
	private Double lat;
	private Double lon;
	private ArrayList<Integer> placesList;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getPass(){
		return pass;
	}
	
	public void setPass(String pass){
		this.pass = pass;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public ArrayList<Integer> getPlacesList() {
		return placesList;
	}

	public void setPlacesList(ArrayList<Integer> placesList) {
		this.placesList = placesList;
	}

	public static UserModel addNewUser(String name, String email, String pass) {
		try {
			Connection conn = DBConnection.getActiveConnection();
			String sql = "Insert into users (`name`,`email`,`password`) VALUES  (?,?,?)";
			// System.out.println(sql);

			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, name);
			stmt.setString(2, email);
			stmt.setString(3, pass);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				UserModel user = new UserModel();
				user.id = rs.getInt(1);
				user.email = email;
				user.pass = pass;
				user.name = name;
				user.lat = 0.0;
				user.lon = 0.0;
				return user;
			}
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static UserModel login(String email, String pass) {
		try {
			Connection conn = DBConnection.getActiveConnection();
			String sql = "Select * from users where `email` = ? and `password` = ?";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			stmt.setString(2, pass);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				UserModel user = new UserModel();
				user.id = rs.getInt(1);
				user.email = rs.getString("email");
				user.pass = rs.getString("password");
				user.name = rs.getString("name");
				user.lat = rs.getDouble("lat");
				user.lon = rs.getDouble("long");
				return user;
			}
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	// method to get the user name by the user id
	// it will return null if the there is no user with the supplied id
	public static String getUserNameById(Integer id)
	{
		try{
			Connection conn = DBConnection.getActiveConnection();
			String sql = "select * from users WHERE `id`= ?";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if(rs.next())
			{
				return rs.getString("name");
			}	
		}catch(SQLException e){
			e.printStackTrace();
		}
			
		return null;
	}

	public static boolean updateUserPosition(Integer id, Double lat, Double lon) {
		try{
			Connection conn = DBConnection.getActiveConnection();
			String sql = "Update users set `lat` = ? , `long` = ? where `id` = ?";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setDouble(1, lat);
			stmt.setDouble(2, lon);
			stmt.setInt(3, id);
			stmt.executeUpdate();
			return true;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public static java.util.List<Double> getUserLastPosition(int userID){
		java.util.List<Double> place = new java.util.ArrayList<>();
		try{
			Connection con = DBConnection.getActiveConnection();
			String sql = "SELECT lat, users.long FROM `users` WHERE users.id = ?";
			PreparedStatement stmt;
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, userID);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				place.add(rs.getDouble("lat"));
				place.add(rs.getDouble("long"));
				return place;
			}
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean follow(Integer followedId, Integer followerId)
	{
		try{
			Connection conn = DBConnection.getActiveConnection();
			String sql = "INSERT INTO followers (`followedId`, `followerId`) VALUES (?, ?)";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, followedId);
			stmt.setDouble(2, followerId);
			
			if (stmt.executeUpdate() > 0) 
			{
				return true;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return false;	
	}
	
	public static boolean unfollow(Integer followedId, Integer followerId)
	{
		try{
			Connection conn = DBConnection.getActiveConnection();
			String sql = "DELETE FROM followers WHERE `followedId`= ? AND `followerId`= ?";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, followedId);
			stmt.setDouble(2, followerId);
			
			if (stmt.executeUpdate() > 0) 
			{
				return true;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return false;	
	}
	
	// method to get list of followers of the user with the followed id
	// it will return a json array containing list of followers each follower with id and name 
	public static JSONArray getFollowers(Integer followedId)
	{
		JSONArray followers = new JSONArray();
		try{
			Connection conn = DBConnection.getActiveConnection();
			String sql = "select * from followers WHERE `followerID`= ?";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, followedId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				JSONObject follower = new JSONObject();
				follower.put("follower id", rs.getInt("followerId"));
				follower.put("follower name", getUserNameById(rs.getInt("followerId")));
				
				followers.add(follower);
			}
			return followers;
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;	
	}
	
	public static ArrayList<Integer> savePlace(Integer userID, Integer placeID){
		try {
			Connection conn = DBConnection.getActiveConnection();
			PreparedStatement stmt;
			ResultSet result;
			String sql;
			int placeId;
			ArrayList<Integer> savedPlaces = new ArrayList<>();
			
			sql = "INSERT INTO users_has_places(`users_userId`,`places_placeId`) VALUES(?,?)";
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userID);
			stmt.setInt(2, placeID);
			int rows = stmt.executeUpdate();
			
			if(rows > 0){
				sql = "SELECT places_placeId FROM users_has_places WHERE `users_userId` = ?";
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, userID);
				
				result = stmt.executeQuery();
				
				while(result.next()){
					placeId = result.getInt(1);
					
					savedPlaces.add(placeId);
				}
				
				return savedPlaces;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}

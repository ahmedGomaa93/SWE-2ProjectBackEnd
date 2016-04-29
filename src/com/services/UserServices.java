package com.services;

import java.util.ArrayList;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.mvc.Viewable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.models.CheckInModel;
import com.models.NotificationModel;
import com.models.ReactionModel;
import com.models.UserModel;

@Path("/User")
public class UserServices {

	/*
	 * @POST
	 * 
	 * @Path("/signup")
	 * 
	 * @Produces(MediaType.TEXT_HTML) public Response signUp(){ return
	 * Response.ok(new Viewable("/Signup.jsp")).build(); }
	 */

	@POST
	@Path("/signup")
	@Produces(MediaType.TEXT_PLAIN)
	public String signUp(@FormParam("name") String name, @FormParam("email") String email,
			@FormParam("pass") String pass) {
		UserModel user = UserModel.addNewUser(name, email, pass);
		JSONObject json = new JSONObject();
		json.put("id", user.getId());
		json.put("name", user.getName());
		json.put("email", user.getEmail());
		json.put("pass", user.getPass());
		json.put("lat", user.getLat());
		json.put("long", user.getLon());
		return json.toJSONString();
	}

	@POST
	@Path("/login")
	@Produces(MediaType.TEXT_PLAIN)
	public String login(@FormParam("email") String email,
			@FormParam("pass") String pass) {
		UserModel user = UserModel.login(email, pass);
		JSONObject json = new JSONObject();
		json.put("id", user.getId());
		json.put("name", user.getName());
		json.put("email", user.getEmail());
		json.put("pass", user.getPass());
		json.put("lat", user.getLat());
		json.put("long", user.getLon());
		return json.toJSONString();
	}
	
	@POST
	@Path("/updatePosition")
	@Produces(MediaType.TEXT_PLAIN)
	public String updatePosition(@FormParam("id") String id,
			@FormParam("lat") String lat, @FormParam("long") String lon) {
		Boolean status = UserModel.updateUserPosition(Integer.parseInt(id), Double.parseDouble(lat), Double.parseDouble(lon));
		JSONObject json = new JSONObject();
		json.put("status", status ? 1 : 0);
		return json.toJSONString();
	}
	
	@POST
	@Path("/UserLastPosition")
	@Produces(MediaType.TEXT_PLAIN)
	public String getLastPosition(@FormParam("id") String userID){
		java.util.List<Double> place = new java.util.ArrayList<>();
		place = UserModel.getUserLastPosition(Integer.parseInt(userID));
		
		if(place.size() != 0){
			JSONObject json = new JSONObject();
			json.put("lat", place.get(0));
			json.put("long", place.get(1));
			return json.toJSONString();
		}
		else{
			return "This user doesn't update his current place yet";
		}
	}
	
	@POST
	@Path("/follow")
	@Produces(MediaType.TEXT_PLAIN)
	public String follow(@FormParam("user id") String userId,
			@FormParam("user to follow id") String userTofollowId) {
		
		JSONObject json = new JSONObject();
		json.put("status", UserModel.follow(Integer.parseInt(userTofollowId), Integer.parseInt(userId)) ? "followed" : "Error");
		return json.toJSONString();
	}
	
	@POST
	@Path("/unfollow")
	@Produces(MediaType.TEXT_PLAIN)
	public String unfollow(@FormParam("user id") String userId,
			@FormParam("user to unfollow id") String userToUnfollowId) {
		
		JSONObject json = new JSONObject();
		json.put("status", UserModel.unfollow(Integer.parseInt(userToUnfollowId), Integer.parseInt(userId)) ? "unfollowed" : "Error");
		
		return json.toJSONString();
	}
	
	@POST
	@Path("/followers")
	@Produces(MediaType.TEXT_PLAIN)
	public String followers(@FormParam("user id") String userId)
	{
		JSONObject json = new JSONObject();
		
		json.put("followers", UserModel.getFollowers(Integer.parseInt(userId)));
		
		return json.toJSONString();
	}
	
	@POST
	@Path("/checkIn")
	@Produces(MediaType.TEXT_PLAIN)
	public String checkIn(@FormParam("userID") int userID, @FormParam("placeID") int placeID, 
			@FormParam("checkInBody") String body){
		CheckInModel checkInPost = new CheckInModel();
		JSONObject json = new JSONObject();
		
		checkInPost = CheckInModel.checkIn(userID, placeID, body);
		
		if(checkInPost != null){
			json.put("checkInID", checkInPost.getCheckInID());
			json.put("ownerID", checkInPost.getOwnerID());
			json.put("placeID", checkInPost.getPlaceID());
			json.put("checkInBody", checkInPost.getCheckInBody());
			json.put("checkInDate", checkInPost.getDate());
			json.put("reactions", null);
			
			return json.toJSONString();
		}
		
		return null;
	}
	
	@POST
	@Path("/savePlace")
	@Produces(MediaType.TEXT_PLAIN)
	public String savePlace(@FormParam("userID") int userID, @FormParam("placeID") int placeID){
		ArrayList<Integer> userPlaces = new ArrayList<>();
		JSONArray jsonArray = new JSONArray();
		JSONObject json = new JSONObject();
		
		userPlaces = UserModel.savePlace(userID, placeID);
		
		if(userPlaces != null && userPlaces.size() > 0){
			for (Integer placeId : userPlaces) {
				json.put("userPlaceID", placeId);
				
				jsonArray.add(json);
			}
			
			return jsonArray.toJSONString();
		}
		
		return null;
	}
	
	@POST
	@Path("/likeCheckIn")
	@Produces(MediaType.TEXT_PLAIN)
	public String likeCheckIn(@FormParam("checkInID") int checkInID, @FormParam("userID") int userID){
		CheckInModel.likeCheckIn(checkInID, userID);

		return null;
	}
	
	@POST
	@Path("/commentCheckIn")
	@Produces(MediaType.TEXT_PLAIN)
	public String commentCheckIn(@FormParam("checkInID") int checkInID, @FormParam("userID") int userID, @FormParam("comment") String comment){
		CheckInModel.commentCheckIn(checkInID, userID, comment);

		return null;
	}
	

	@POST
	@Path("/getNotifications")
	@Produces(MediaType.TEXT_PLAIN)
	public String getNotifications(@FormParam("userID") int userID){
		ArrayList<NotificationModel> notifications = NotificationModel.getUnreadNotifications(userID);
		JSONArray jsonArray = new JSONArray();
		JSONObject json = new JSONObject();
		
		if(notifications != null && notifications.size() > 0){
			for (NotificationModel notification : notifications) {
				json.put("notificationId", notification.getNotificationID());
				json.put("notificationDescription", notification.getNotificationDescription());
				
				jsonArray.add(json);
			}
			
			return jsonArray.toJSONString();
		}
		return null;
	}
	
	@POST
	@Path("/getAllNotifications")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllNotifications(@FormParam("userID") int userID){
		ArrayList<NotificationModel> notifications = NotificationModel.getAllNotifications(userID);
		JSONArray jsonArray = new JSONArray();
		JSONObject json = new JSONObject();
		
		if(notifications != null && notifications.size() > 0){
			for (NotificationModel notification : notifications) {
				json.put("notificationId", notification.getNotificationID());
				json.put("notificationDescription", notification.getNotificationDescription());
				
				jsonArray.add(json);
			}
			
			return jsonArray.toJSONString();
		}
		return null;
	}
	
	@POST
	@Path("/respondToNotification")
	@Produces(MediaType.TEXT_PLAIN)
	public String respondToNotification(@FormParam("notificationId") int notificationID){
		JSONObject postJson = new JSONObject(), reactionJson = new JSONObject();
		JSONArray reactionsJson = new JSONArray();
		NotificationModel notification;
		CheckInModel checkinPost;
		
		notification = NotificationModel.getNotificationByID(notificationID);
		checkinPost = notification.respond();
		
		postJson.put("checkinId", checkinPost.getCheckInID());
		postJson.put("checkinOwnerId", checkinPost.getOwnerID());
		postJson.put("checkinOwnerName", checkinPost.getOwnerName());
		postJson.put("checkinPlaceId", checkinPost.getPlaceID());
		postJson.put("checkinPlaceName", checkinPost.getPlaceName());
		postJson.put("checkinBody", checkinPost.getCheckInBody());
		postJson.put("checkinDate", checkinPost.getDate());
		
		for (ReactionModel reaction : checkinPost.getReactions()) {
			reactionJson.put("reactionType", reaction.getReactionType());
			reactionJson.put("reactorName", reaction.getReactorName());
			reactionJson.put("reactionDate", reaction.getReactionDate());
			reactionJson.put("reactionBody", reaction.getReactionBody());
			
			reactionsJson.add(reactionJson);
		}
		postJson.put("reactions", reactionsJson);
		
		return postJson.toJSONString();
	}
	
	@POST
	@Path("/showHomePage")
	@Produces(MediaType.TEXT_PLAIN)
	public String showHomePage(@FormParam("userID") int userID){
		ArrayList<CheckInModel> userPosts = new ArrayList<>();
		ArrayList<CheckInModel> followersPosts = new ArrayList<>();
		ArrayList<CheckInModel> homePosts = new ArrayList<>();
		JSONArray userFollowers = new JSONArray(), reactionsJson = new JSONArray(), posts = new JSONArray();
		JSONObject postJson = new JSONObject(), reactionJson = new JSONObject();
		
		userPosts = CheckInModel.getUserCheckIns(userID);
		
		userFollowers = UserModel.getFollowers(userID);
		
		homePosts.addAll(userPosts);
		homePosts.addAll(followersPosts);
		
		for (CheckInModel checkinPost : homePosts) {
			postJson.put("checkinId", checkinPost.getCheckInID());
			postJson.put("checkinOwnerId", checkinPost.getOwnerID());
			postJson.put("checkinOwnerName", checkinPost.getOwnerName());
			postJson.put("checkinPlaceId", checkinPost.getPlaceID());
			postJson.put("checkinPlaceName", checkinPost.getPlaceName());
			postJson.put("checkinBody", checkinPost.getCheckInBody());
			postJson.put("checkinDate", checkinPost.getDate());
			
			if(checkinPost.getReactions() != null){
				for (ReactionModel reaction : checkinPost.getReactions()) {
					reactionJson.put("reactionType", reaction.getReactionType());
					reactionJson.put("reactorName", reaction.getReactorName());
					reactionJson.put("reactionDate", reaction.getReactionDate());
					reactionJson.put("reactionBody", reaction.getReactionBody());
					
					reactionsJson.add(reactionJson);
				}
				postJson.put("reactions", reactionsJson);
			}
			else{
				postJson.put("reactions", null);
			}
			
			posts.add(postJson);
		}
		
		return posts.toJSONString();
	}
}

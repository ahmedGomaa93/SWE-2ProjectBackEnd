package com.models;

public class CommentObserver implements Observer{

	private int observerID;
	private PostSubject post;

	public int getObserverID() {
		return observerID;
	}

	public void setObserverID(int observerID) {
		this.observerID = observerID;
	}

	public PostSubject getPost(){
		return post;
	}
	
	public void setPost(PostSubject post){
		this.post = post;
	}
	
	@Override
	public void update(int reactionID) {
		// TODO Auto-generated method stub
		int postID = this.post.getPostID();
		saveLikeNotification(this.observerID, reactionID, postID);
	}

	@Override
	public CheckInModel respond() {
		// TODO Auto-generated method stub
		
		return CheckInModel.getCheckIn(post.getPostID());
	}

	public void saveLikeNotification(int ownerID, int reactionID, int postID) {
		//Save new notification in DB
		NotificationModel.saveNotification(postID, ownerID, reactionID);
	}
}

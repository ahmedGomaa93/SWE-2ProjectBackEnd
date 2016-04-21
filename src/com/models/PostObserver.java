package com.models;

public class PostObserver implements Observer {

	private int observerID;
	private PostSubject post;

	public int getObserverID() {
		return observerID;
	}

	public void setObserverID(int observerID) {
		this.observerID = observerID;
	}

	@Override
	public void update(int reactionID) {
		// TODO Auto-generated method stub
		int postID = this.post.getPostID();
		saveNotification(this.observerID, reactionID, postID);
	}

	public void saveNotification(int ownerID, int reactionID, int postID) {
		//Save new notification in DB
	}

}
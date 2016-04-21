package com.models;

public class CommentObserver implements Observer {

	public String observerID;

	public CheckInSubject checkIn;

	public String userName;

  	public String comment;

  	@Override
	public void update(String userName, String body) {
		// TODO Auto-generated method stub
	
	}

  	public void sendCommentNotification() {
  	}
}
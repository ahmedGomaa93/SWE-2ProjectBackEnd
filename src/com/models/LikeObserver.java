package com.models;

public class LikeObserver implements Observer {

	public String observerID;

	public CheckInSubject checkIn;

	public String userName;
  
	@Override
	public void update(String userName, String body) {
		// TODO Auto-generated method stub
	}


	public void sendLikeNotification() {
		
	}

}
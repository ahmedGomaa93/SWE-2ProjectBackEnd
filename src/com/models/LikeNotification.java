package com.models;

public class LikeNotification extends NotificationModel{

	@Override
	public CheckInModel respond() {
		// TODO Auto-generated method stub
		CheckInModel checkinPost = CheckInModel.getCheckIn(this.getPostID());
		return checkinPost;
	}

}

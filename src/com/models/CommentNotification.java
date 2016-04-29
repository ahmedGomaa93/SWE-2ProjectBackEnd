package com.models;

public class CommentNotification extends NotificationModel{

	@Override
	public CheckInModel respond() {
		// TODO Auto-generated method stub
		CheckInModel checkinPost = CheckInModel.getCheckIn(this.getPostID());
		return checkinPost;
	}

}

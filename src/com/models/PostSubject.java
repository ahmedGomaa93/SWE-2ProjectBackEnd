package com.models;

import java.util.ArrayList;

public class PostSubject implements Subject {

	private ArrayList<Observer> observers;
	private int postID;

	public PostSubject() {
		// TODO Auto-generated constructor stub
		observers = new ArrayList<>();
	}
	public ArrayList<Observer> getObservers() {
		return observers;
	}

	public void setObservers(ArrayList<Observer> observers) {
		this.observers = observers;
	}

	public int getPostID() {
		return postID;
	}
	
	public void setPostID(int postID) {
		this.postID = postID;
	}

	@Override
	public void register(Observer newObserver) {
		observers.add(newObserver);
	}
	
	@Override
	public void unregister(Observer observer) {
		int index = observers.indexOf(observer);
		observers.remove(index);
	}
	
	@Override
	public void notifyObservers(String reactionType, int reactionID) {
		if(reactionType.equalsIgnoreCase("like")){
			for (Observer observer : observers) {
				if(observer.getClass().equals(LikeObserver.class)){
					observer.update(reactionID);
				}
			}
		}else if(reactionType.equalsIgnoreCase("like")){
			for (Observer observer : observers) {
				if(observer.getClass().equals(CommentObserver.class)){
					observer.update(reactionID);
				}
			}
		}
	}

	public void react(String reactionType, int checkinID, int tipID, int reactorID, String reactionBody){
		int reactionID = ReactionModel.saveReaction(reactionType, checkinID, tipID, reactorID, reactionBody);
		notifyObservers(reactionType, reactionID);
	}
}
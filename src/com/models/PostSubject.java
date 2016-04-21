package com.models;

import java.util.ArrayList;

public class PostSubject implements Subject {

	private ArrayList<Observer> observers;
	private int postID;
	private int reactionID;

	public PostSubject() {
		// TODO Auto-generated constructor stub
		observers = new ArrayList<>();
		reactionID = 0;
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
	
	public int getreactionID() {
		return reactionID;
	}

	public void setreactionID(int reactionID) {
		this.reactionID = reactionID;
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
	public void notifyObservers() {
		for (Observer observer : observers) {
			observer.update(reactionID);
		}
	}

	public void react(){
		notifyObservers();
	}
}
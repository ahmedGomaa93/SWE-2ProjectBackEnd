package com.models;

import java.util.ArrayList;

public class CheckInSubject implements Subject {

	public ArrayList<Observer> Observers;

	public String checkInID;

	public String userName;

	@Override
	public void register(Observer newObserver) {
	}
	
	@Override
	public void unregister(Observer observer) {
	}
	
	@Override
	public void notifyObservers() {
	}

	public void likeCheckIn() {
	}

	public void commentCheckIn(String comment) {
	}
}
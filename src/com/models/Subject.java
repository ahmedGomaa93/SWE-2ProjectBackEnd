package com.models;


public interface Subject {

  public void register(Observer newObserver);

  public void unregister(Observer observer);

  public void notifyObservers(String reactionType, int reactionID);

}
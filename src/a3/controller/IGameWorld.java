package a3.controller;

import a3.objects.GameObject;

import java.awt.event.MouseEvent;

/**
 * Created by Victor Ignatenkov on 3/22/15.
 */
public interface IGameWorld {
    //specifications here for all GameWorld methods


    public int getCurrentClockTime();
    public int getLivesRemaining();
    public int getLastPylonReached();
    public float getCurrentFuelLevel();
    public float getDamageLevel();
    public boolean isSound();
    public int getFramesPerSecond();
    public void addToTheDeleteObjectsCollection(GameObject obj);
    public void eraseFromTheDeleteObjectsCollections(GameObject obj);
    public boolean isItInPause();
    public void setLastMouseEvent(MouseEvent e);
    public int getTime();
    public void resetTime();

}

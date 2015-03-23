package a3.controller;

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
}

package a4.model;


import a4.controller.IGameWorld;
import a4.objects.GameObject;

import java.awt.event.MouseEvent;

/**
 * Created by Victor Ignatenkov on 3/22/15.
 */
public class GameWorldProxy implements  IGameWorld {
    // code here to accept and hold a GameWorld, provide implementations
    //  of all the public methods in a GameWorld, forward allowed
    //  calls to the actual GameWorld, and reject calls to methods
    //  which the outside should not be able to access in the GameWorld.

    private GameWorld realGameWorld;

    public GameWorldProxy (GameWorld gw){
        realGameWorld = gw;
    }

    public Iterator getIterator(){
        return realGameWorld.getIterator();
    }

    @Override
    public int getCurrentClockTime() {
        return realGameWorld.getCurrentClockTime();
    }

    @Override
    public int getLivesRemaining() {
        return realGameWorld.getLivesRemaining();
    }

    @Override
    public int getLastPylonReached() {
        return realGameWorld.getLastPylonReached();
    }

    @Override
    public float getCurrentFuelLevel() {
        return realGameWorld.getCurrentFuelLevel();
    }

    @Override
    public float getDamageLevel() {
        return realGameWorld.getDamageLevel();
    }

    @Override
    public boolean isSound() {
        return realGameWorld.isSound();
    }

    @Override
    public int getFramesPerSecond() {
        return realGameWorld.getFramesPerSecond();
    }

    @Override
    public void addToTheDeleteObjectsCollection(GameObject obj) {
        realGameWorld.addToTheDeleteObjectsCollection(obj);
    }

    @Override
    public void eraseFromTheDeleteObjectsCollections(GameObject obj) {
        realGameWorld.eraseFromTheDeleteObjectsCollections(obj);
    }

    @Override
    public boolean isItInPause() {
        return realGameWorld.isItInPause();
    }
    @Override
    public void setLastMouseEvent(MouseEvent e) {
        realGameWorld.setLastMouseEvent(e);
    }

    @Override
    public int getTimeInTicks() {
        return realGameWorld.getTimeInTicks();
    }

    @Override
    public void resetTimeCounterInTermsOfTicks(){
        realGameWorld.resetTimeCounterInTermsOfTicks();
    }

    @Override
    public int getTimeInSeconds(){
        return realGameWorld.getTimeInSeconds();
    }

    @Override
    public void rotateMuzzleToLeft() {

    }

    @Override
    public void rotateMuzzleToRight() {

    }


}

/*Created by Victor Ignatenkov on 3/22/15. */

package a4.controller;
import a4.objects.GameObject;
import java.awt.event.MouseEvent;


/**
 * IGameWorld - interface for specifying a set of
 * methods, which are to be accessed by the views.
 */

public interface IGameWorld {
    //specifications here for all GameWorld methods
    int getCurrentClockTime();
    int getLivesRemaining();
    int getLastPylonReached();
    float getCurrentFuelLevel();
    float getDamageLevel();
    boolean isSound();
    int getFramesPerSecond();
    void addToTheDeleteObjectsCollection(GameObject obj);
    void eraseFromTheDeleteObjectsCollections(GameObject obj);
    boolean isItInPause();
    void setLastMouseEvent(MouseEvent e);
    int getTimeInTicks();
    void resetTimeCounterInTermsOfTicks();
    int getTimeInSeconds();
    void rotateMuzzleToLeft();
    void rotateMuzzleToRight();
}

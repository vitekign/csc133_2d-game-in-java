package a4.app.strategies;

import a4.model.GameWorld;
import a4.objects.NPCCar;

/**
 * Created by Victor Ignatenkov on 3/10/15.
 */

/**
 * IStrategy Interface for all Strategy classes.
 */
public interface IStrategy {

    public void performStrategy(NPCCar car, GameWorld gw);
    public String toString();
}

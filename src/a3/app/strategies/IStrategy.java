package a3.app.strategies;

import a3.model.GameWorld;
import a3.objects.NPCCar;

/**
 * Created by Victor Ignatenkov on 3/10/15.
 */
public interface IStrategy {

    public void performStrategy(NPCCar car, GameWorld gw);
    public String toString();
}

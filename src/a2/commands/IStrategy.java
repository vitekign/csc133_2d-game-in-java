package a2.commands;

import a2.model.GameWorld;
import a2.objects.NPCCar;

/**
 * Created by Victor Ignatenkov on 3/10/15.
 */
public interface IStrategy {

    public void performStrategy(NPCCar car, GameWorld gw);

}

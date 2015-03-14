package a1.commands;

import a1.model.GameWorld;
import a1.objects.NPCCar;

/**
 * Created by Victor Ignatenkov on 3/10/15.
 */
public interface IStrategy {

    public void performStrategy(NPCCar car, GameWorld gw);

}

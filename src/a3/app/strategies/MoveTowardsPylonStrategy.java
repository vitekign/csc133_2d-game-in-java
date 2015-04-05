/**
 * Created by Victor Ignatenkov on 3/13/15.
 */

package a3.app.strategies;
import a3.model.GameWorld;
import a3.objects.Location;
import a3.objects.NPCCar;
import a3.objects.Pylon;
import a3.app.utilities.Services;

import static java.lang.Math.atan2;

/**
 * MoveTowardsPylonStrategy implements the IStrategy so
 * it can be used for the NPCCars as a strategy.
 */
public class MoveTowardsPylonStrategy implements IStrategy {


    /**
     * The functionality which is responsible for
     * moving a NPCCar, functioning as its strategy.
     * @param car
     * Car
     * @param gw
     * GameWorld
     */
    @Override
    public void performStrategy(NPCCar car, GameWorld gw) {


            if(Services.findTheNumberOfPylons() != 0) {
                int lastHighestPylonReached = car.getLastHighestPylonReached();

                if (lastHighestPylonReached >= (Pylon.getCount() + 1)) {
                    car.setLastHighestPylonReached(1);
                }



                /*  1. Retrieve all pylons in the game world
                    2. Sort them
                */
                Pylon tmpPylon = null;
                while (Services.findPylonWithIndexNumber(car.getLastHighestPylonReached()) == null) {
                    car.setLastHighestPylonReached(car.getLastHighestPylonReached() + 1);
                    if (car.getLastHighestPylonReached() >= (Pylon.getCount() +1)) {
                        car.setLastHighestPylonReached(1);
                    }
                }
                tmpPylon = Services.findPylonWithIndexNumber(car.getLastHighestPylonReached());


                /**
                 System.out.println("The last highest pylon:  " + car.getLastHighestPylonReached());
                 System.out.println("The last highest pylon:  " + tmpPylon.getIndexNumber());
                 **/

                Location pylonLocation = tmpPylon.getLocation();
                Location npcLocation = car.getLocation();
                float adj = pylonLocation.getX() - npcLocation.getX();
                float op = pylonLocation.getY() - npcLocation.getY();

                /**
                 System.out.println("Adj is :  " + adj);
                 System.out.println("Op is : " + op);
                 **/

                float angleToAdd;

                angleToAdd = (float) Math.toDegrees(atan2(op, adj));

                car.setHeading(angleToAdd);

                /**
                 System.out.println("angleToAdd is: " + angleToAdd);
                 System.out.println("the heading is: " + car.getHeading());
                 **/

                float deltaY = (float) (Math.sin(Math.toRadians(car.getHeading())) * car.getSpeed() * 10);
                float deltaX = (float) (Math.cos(Math.toRadians(car.getHeading())) * car.getSpeed() * 10);
                Location temp = new Location(car.getLocation().getX() + deltaX, car.getLocation().getY() + deltaY);

                car.setX(temp.getX());
                car.setY(temp.getY());

                car.setHeading(90 - angleToAdd);

                if ((car.getX() < (tmpPylon.getX() + 10) && car.getX() > (tmpPylon.getX() - 10)) &&
                        (car.getY() < (tmpPylon.getY() + 10) && car.getY() > (tmpPylon.getY() - 10))) {
                    car.setLastHighestPylonReached(car.getLastHighestPylonReached() + 1);
                }
            }
    }

    /**
     * The name of the Strategy.
     * @return
     */
    public String toString(){
        return "Move to Pylon";
    }
}
/**
 * Created by Victor Ignatenkov on 3/13/15.
 */

package a4.app.strategies;
import a4.model.GameWorld;
import a4.objects.Location;
import a4.objects.NPCCar;
import a4.objects.Pylon;
import a4.app.utilities.Utilities;

import java.util.Vector;

import static java.lang.Math.atan2;

/**
 * MoveTowardsPylonStrategy implements the IStrategy so
 * it can be used for the NPCCars as a strategy.
 */
public class MoveTowardsPylonStrategy implements IStrategy {


    /**
     * The functionality which is responsible for
     * moving a NPCCar, functioning as its strategy.
     *
     *   1. Retrieve all pylons of the world
     *   2. Sort'em
     *   3. Go from the left most one to the the right,
     *   until the most right one is hit, in which
     *   case return the the left most one.
     */

    //TODO  Move sort out of here. It needs to be sorted only after deleting/creating new pylons.
    @Override
    public void performStrategy(NPCCar car, GameWorld gw) {


            if(Utilities.findTheNumberOfPylons() != 0) {

             /* **********************************************
              * **********************************************
              *         Index Number Agnostic               **
              * **********************************************
              * **********************************************/

                /* 1. Retrieve all pylons of the world */
                Vector<Pylon> allPylons = Utilities.getAllPylons();
                /* 2. Sort'em
                *
                * Returns negative one, zero, or one if the first argument is smaller,
                * equal, or larger than the second.
                */
                allPylons.sort( (Pylon p1, Pylon p2) -> {
                    if(p1.getIndexNumber() < p2.getIndexNumber()){
                        return -1;
                    } else {
                        return 1;
                    }
                });

                Pylon nextPylonToFollow = null;

                java.util.Iterator<Pylon> iterInALlPylons;
                for(iterInALlPylons = allPylons.iterator(); iterInALlPylons.hasNext(); ){
                    Pylon temp = iterInALlPylons.next();
                    if(temp.getIndexNumber() > car.getLastHighestPylonReached()){
                        nextPylonToFollow = temp;
                        break;
                    }
                }
                    if(Utilities.findTheNumberOfPylons() != 0 && nextPylonToFollow == null){
                        nextPylonToFollow = allPylons.firstElement();
                    }

                    if(nextPylonToFollow.getIndexNumber() != car.getLastHighestPylonReached()){
                    Location pylonLocation = nextPylonToFollow.getLocation();
                    Location npcLocation = car.getLocation();
                    float adj = pylonLocation.getX() - npcLocation.getX();
                    float op = pylonLocation.getY() - npcLocation.getY();


                    float angleToAdd;

                    angleToAdd = (float) Math.toDegrees(atan2(op, adj));

                    car.setHeading(angleToAdd);

                    float deltaY = (float) (Math.sin(Math.toRadians(car.getHeading())) * car.getSpeed() * 10);
                    float deltaX = (float) (Math.cos(Math.toRadians(car.getHeading())) * car.getSpeed() * 10);

                    car.setX(deltaX);
                    car.setY(deltaY);

                    car.setHeading(90 - angleToAdd);

                    if ((car.getX() < (nextPylonToFollow.getX() + 10) && car.getX() > (nextPylonToFollow.getX() - 10)) &&
                            (car.getY() < (nextPylonToFollow.getY() + 10) && car.getY() > (nextPylonToFollow.getY() - 10))) {
                        car.setLastHighestPylonReached(nextPylonToFollow.getIndexNumber());
                    }
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
/**
 * Created by Victor Ignatenkov on 3/13/15.
 */

package a3.app.strategies;
import a3.controller.Game;
import a3.model.GameWorld;
import a3.model.Iterator;
import a3.objects.GameObject;
import a3.objects.Location;
import a3.objects.NPCCar;
import a3.objects.Pylon;
import a3.app.utilities.Services;

import java.util.Comparator;
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
     * @param car
     * Car
     * @param gw
     * GameWorld
     */
    @Override
    public void performStrategy(NPCCar car, GameWorld gw) {


            if(Services.findTheNumberOfPylons() != 0) {
//                int lastHighestPylonReached = car.getLastHighestPylonReached();
//
//                if (lastHighestPylonReached >= (Pylon.getCount() + 1)) {
//                    car.setLastHighestPylonReached(1);
//                }



             /* **********************************************
              * **********************************************
              *     New Algorithm, Index Number Agnostic    **
              * **********************************************
              * **********************************************/

                /* 1. Retrieve all pylons of the world */
                Vector<Pylon> allPylons = Services.getAllPylons();
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


                int lastIndex = 1;

                Pylon nextPylonToFollow = null;

                java.util.Iterator<Pylon> iterInALlPylons;
                for(iterInALlPylons = allPylons.iterator(); iterInALlPylons.hasNext(); ){
                    Pylon temp = iterInALlPylons.next();
                    if(temp.getIndexNumber() > car.getLastHighestPylonReached()){
                        nextPylonToFollow = temp;
                        break;
                    }
                }


                    /**
                     System.out.println("The last highest pylon:  " + car.getLastHighestPylonReached());
                     System.out.println("The last highest pylon:  " + tmpPylon.getIndexNumber());
                     **/

                    if(Services.findTheNumberOfPylons() != 0 && nextPylonToFollow == null){
                        nextPylonToFollow = allPylons.firstElement();
                    }

                    if(nextPylonToFollow.getIndexNumber() != car.getLastHighestPylonReached()){
                    Location pylonLocation = nextPylonToFollow.getLocation();
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

                    if ((car.getX() < (nextPylonToFollow.getX() + 10) && car.getX() > (nextPylonToFollow.getX() - 10)) &&
                            (car.getY() < (nextPylonToFollow.getY() + 10) && car.getY() > (nextPylonToFollow.getY() - 10))) {
                        car.setLastHighestPylonReached(nextPylonToFollow.getIndexNumber());
                    }
                }
            }




//                lastHighestPylonReached = nextPylonToFollow.getIndexNumber();
//
//
//                if (lastHighestPylonReached >= (Pylon.getCount() + 1)) {
//                    car.setLastHighestPylonReached(1);
//                }
//


                /* **********************************************
                 * **********************************************
                 *                   END                       **
                 * **********************************************
                 * **********************************************/







    }

    /**
     * The name of the Strategy.
     * @return
     */
    public String toString(){
        return "Move to Pylon";
    }
}
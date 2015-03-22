/**
 * Created by Victor Ignatenkov on 3/13/15.
 */

package a3.app.strategies;
import a3.model.GameWorld;
import a3.objects.Location;
import a3.objects.NPCCar;
import a3.objects.Pylon;
import a3.objects.Services;

import java.util.Random;

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

        /**
         * In the beginning of the game, when NPC cars have not
         * yet reached any of the pylons, the strategy patter is to
         * used this part of the businnes logic.
         */
        if (car.getLastHighestPylonReached() == 0) {


            int numOfPylons = Services.findTheNumberOfPylons();
            if (numOfPylons == 0) {
                //TODO replace by throwing exception
                //No pylons in the game.
                System.out.println("No pylons, cannot do MoveTowardsPylonStrategy");
            } else {
                if(car.getFirstPylonToMove() == 0) {
                    //TODO switch back to random
                    int numPylon = new Random().nextInt(numOfPylons);
                    numPylon++;

                   // System.out.println("The random pylon is : " + numPylon);
                    car.setFirstPylonToMove(numPylon);
                }

                try {
                    Pylon tmpPylon = Services.findPylonWithIndexNumber(car.getFirstPylonToMove());

                   // System.out.println("The number of pylon is: " + car.getFirstPylonToMove());

                    Location unit = new Location();
                    Location pylonLocation = tmpPylon.getLocation();
                    Location npcLocation = car.getLocation();
                    float adj = pylonLocation.getX() - npcLocation.getX();
                    float op = pylonLocation.getY() - npcLocation.getY();

                    /**
                    System.out.println("Adj is :  " + adj);
                    System.out.println("Op is : " + op);
                    **/

                    float angleToAdd;

                    /**
                     *    Find out the angle between the NPCCar and another
                     *    point in the game world.
                     *
                     *    It works because atan2 return an angle which is
                     *    up to 180 degrees or up to -180 degrees. So, it basically
                     *    takes into consideration the information about the quadrants,
                     *    and return an ange in terms of 360 degrees.
                     */

                    angleToAdd = (float) Math.toDegrees(atan2(op, adj));

                    car.setHeading(angleToAdd);

                    float angle = (float) (car.getHeading());
                    float deltaY = (float) (Math.sin(Math.toRadians(angle)) * car.getSpeed());
                    float deltaX = (float) (Math.cos(Math.toRadians(angle)) * car.getSpeed());
                    Location temp = new Location(car.getLocation().getX() + deltaX, car.getLocation().getY() + deltaY);

                    car.setHeading(90 - angleToAdd);

                    car.setX(temp.getX());
                    car.setY(temp.getY());

                    if((car.getX() < (tmpPylon.getX() + 10) && car.getX() > (tmpPylon.getX() - 10)) &&
                    (car.getY() < (tmpPylon.getY() + 10) && car.getY() > (tmpPylon.getY() - 10))){
                        car.setLastHighestPylonReached(car.getFirstPylonToMove());
                    }

                } catch (Exception e) {
                    System.out.println("The pylon wasn't found " + e.getMessage());
                }
            }
        } else {

            /**
             * This business logic is to be used when an NPCCar has already
             * reached any pylon in the world.
             */
            int lastHighestPylonReached = car.getLastHighestPylonReached();
            if(lastHighestPylonReached == Services.findTheNumberOfPylons()){
               // System.out.println("Too many movements, set a lower pylon");
                car.setLastHighestPylonReached(1);
                lastHighestPylonReached = car.getLastHighestPylonReached();
            }

            Pylon tmpPylon = null;
            try {
                tmpPylon = Services.findPylonWithIndexNumber(++lastHighestPylonReached);
            } catch (Exception e) {
                System.out.println("No such a pylon " + e.toString() + e.getStackTrace());
            }

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

            angleToAdd = (float) Math.toDegrees(atan2(op,adj));

            car.setHeading(angleToAdd);

            /**
            System.out.println("angleToAdd is: " + angleToAdd);
            System.out.println("the heading is: " + car.getHeading());
            **/

            float deltaY = (float) (Math.sin(Math.toRadians(car.getHeading())) * car.getSpeed());
            float deltaX = (float) (Math.cos(Math.toRadians(car.getHeading())) * car.getSpeed());
            Location temp = new Location(car.getLocation().getX() + deltaX, car.getLocation().getY() + deltaY);

            car.setX(temp.getX());
            car.setY(temp.getY());

            car.setHeading(90 - angleToAdd);

            if((car.getX() < (tmpPylon.getX() + 10) && car.getX() > (tmpPylon.getX() - 10)) &&
                    (car.getY() < (tmpPylon.getY() + 10) && car.getY() > (tmpPylon.getY() - 10))){
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
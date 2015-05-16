/**
 * Created by Victor Ignatenkov on 3/13/15.
 */

package a4.app.strategies;
import a4.model.GameWorld;
import a4.objects.Location;
import a4.objects.NPCCar;
import static java.lang.Math.atan2;


/**
 * FollowThePlayerCarStrategy implements the IStrategy so
 * it can be used for the NPCCars as a strategy.
 */
public class FollowThePlayerCarStrategy implements IStrategy {


    @Override
    public void performStrategy(NPCCar car, GameWorld gw) {


        /**
         * This strategy is based the following steps:
         * 1. Acquire the Location of the Car
         * 2. Acquire the Location of the NPCCar
         * 3. Find out the ange between them in terms of 360 degrees
         * 4. Change the location of the NPCCar with respect
         * to the character car.
         */

        if(car.getX() != gw.getCharacterCar().getX() |
                car.getY() != gw.getCharacterCar().getY()) {

            Location unit = new Location();
            Location characterLocation = gw.getCharacterCar().getLocation();
            Location npcLocation = car.getLocation();
            float adj = characterLocation.getX() - npcLocation.getX();
            float op = characterLocation.getY() - npcLocation.getY();


            float angleToAdd;
            angleToAdd = (float) Math.toDegrees(atan2(op, adj));
            car.setHeading(angleToAdd);


            car.setHeading(car.getHeading() + car.getSteeringDirection());
            float deltaY = (float) (Math.sin(Math.toRadians(angleToAdd)) * car.getSpeed());
            float deltaX = (float) (Math.cos(Math.toRadians(angleToAdd)) * car.getSpeed());
            Location temp = new Location(car.getLocation().getX() + deltaX,
                    car.getLocation().getY() + deltaY);

            car.setHeading(90 - angleToAdd);

            car.setX(deltaX);
            car.setY(deltaY);


        }

    }
    /**
     * The name of the strategy.
     * @return
     */
    public String toString(){
        return "Follow the Player Car";
    }
}

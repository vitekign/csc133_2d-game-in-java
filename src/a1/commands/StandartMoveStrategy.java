package a1.commands;

import a1.model.GameWorld;
import a1.objects.Car;
import a1.objects.Location;

/**
 * Created by Victor Ignatenkov on 3/10/15.
 */
public class StandartMoveStrategy implements IStrategy {

    @Override
    public void performStrategy(Car car, GameWorld gw) {
        if(car.isInOilSlick()){
            return ;
        }

        car.setHeading(car.getHeading() + car.getSteeringDirection());
        float angle = (float) (90 - car.getHeading());
        float deltaY = (float) (Math.sin(Math.toRadians(angle))*car.getSpeed());
        float deltaX = (float) (Math.cos(Math.toRadians(angle))*car.getSpeed());
        Location temp = new Location(car.getLocation().getX() + deltaX,
                car.getLocation().getY() + deltaY);

        car.setX(temp.getX());
        car.setY(temp.getY());


        /**
         * Decrease the amount of fuel
         */
        car.changeFuelLevel(-gw.DAMAGE_FOR_COLLIDING_WITH_CARS);

        /**
         *  Reset steering direction after applying it to the direction
         *  of the car.
         */
        car.setSteeringDirection(0);
    }
}

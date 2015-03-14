package a1.commands;

import a1.model.GameWorld;
import a1.objects.Car;
import a1.objects.Location;
import a1.objects.NPCCar;

/**
 * Created by Victor Ignatenkov on 3/13/15.
 */
public class FollowThePlayerCarStrategy implements IStrategy{


    private float SPEED_RATIO = 1;

    @Override
    public void performStrategy(NPCCar car, GameWorld gw) {

        /**
         * 1. Find the distance between npc and character in terms
         * of x and y
         * 2. Find the distance between npc and character in terms of vector.
         * 3. Find a unit vector.
         */


        /*
        Location unit = new Location();
        Location characterLocation = gw.getCharacterCar().getLocation();
        Location npcLocation = car.getLocation();
        float x = characterLocation.getX() - npcLocation.getX();
        float y = characterLocation.getY() - npcLocation.getY();
        float mag = (float)Math.sqrt(x*x + y*y);
        unit.setX(x/(mag * SPEED_RATIO));
        unit.setY(y/(mag * SPEED_RATIO));

        car.setX(car.getX() + unit.getX());
        car.setY(car.getY() + unit.getY());
        */

        if(car.getX() != gw.getCharacterCar().getX() |
                car.getY() != gw.getCharacterCar().getY()) {

            Location unit = new Location();
            Location characterLocation = gw.getCharacterCar().getLocation();
            Location npcLocation = car.getLocation();
            float adj = characterLocation.getX() - npcLocation.getX();
            float op = characterLocation.getY() - npcLocation.getY();

            System.out.println("Adj is :  " + adj);
            System.out.println("Op is : " + op);


            float angleToAdd;

            angleToAdd = (float) Math.toDegrees(Math.atan(op / adj));
            angleToAdd -= 90;


            System.out.println("angleToAdd is: " + angleToAdd);
            car.heading = angleToAdd;


            car.setHeading(car.getHeading() + car.getSteeringDirection());
            float angle = (float) (90 + car.getHeading());
            float deltaY = (float) (Math.sin(Math.toRadians(angle)) * car.getSpeed());
            float deltaX = (float) (Math.cos(Math.toRadians(angle)) * car.getSpeed());
            Location temp = new Location(car.getLocation().getX() + deltaX,
                    car.getLocation().getY() + deltaY);

//
//            float overlapX = Math.abs(car.getX() - gw.getCharacterCar().getX());
//            float overlapY = Math.abs(car.getY() - gw.getCharacterCar().getY());

//            if(deltaX < overlapX | deltaY < overlapY){
//                car.setX(temp.getX());
//                car.setY(temp.getY());
//            } else {
//                car.setX(overlapX);
//                car.setY(overlapY);
//            }

            car.setX(temp.getX());
            car.setY(temp.getY());


        }

    }

    public void setSpeedRatio(float speedRatio) {
        this.SPEED_RATIO = speedRatio;
    }
}

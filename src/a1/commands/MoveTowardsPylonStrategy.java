package a1.commands;

/**
 * Created by Victor Ignatenkov on 3/14/15.
 */

import a1.model.GameWorld;
import a1.objects.Location;
import a1.objects.NPCCar;
import a1.objects.Pylon;
import a1.objects.Services;


/**
 * Created by Victor Ignatenkov on 3/13/15.
 */
public class MoveTowardsPylonStrategy implements IStrategy {


    private float SPEED_RATIO = 1;

    @Override
    public void performStrategy(NPCCar car, GameWorld gw) {


        if (car.getLastHighestPylonReached() == 0) {


            int numOfPylons = Services.findTheNumberOfPylons();
            if (numOfPylons == 0) {
                //TODO replace by throwing exception
                System.out.println("No pylons, cannot do MoveTowardsPylonStrategy");
            } else {
                if(car.getFirstPylonToMove() == 0) {
                    //TODO switch back to random
//                    int numPylon = new Random().nextInt(numOfPylons);
//                    numPylon++;

                    int numPylon = 1;

                    System.out.println("The random pylon is : " + numPylon);
                    car.setFirstPylonToMove(numPylon);
                }

                try {
                    Pylon tmpPylon = Services.findPylonWithIndexNumber(car.getFirstPylonToMove());

                    System.out.println("The number of pylon is: " + car.getFirstPylonToMove());

                    Location unit = new Location();
                    Location pylonLocation = tmpPylon.getLocation();
                    Location npcLocation = car.getLocation();
                    float adj = pylonLocation.getX() - npcLocation.getX();
                    float op = pylonLocation.getY() - npcLocation.getY();

                    System.out.println("Adj is :  " + adj);
                    System.out.println("Op is : " + op);


                    float angleToAdd;

                    angleToAdd = (float) Math.toDegrees(Math.atan(op / adj));
                  //  angleToAdd -= 90;


                    System.out.println("angleToAdd is: " + angleToAdd);
                    car.heading = angleToAdd;


                    car.setHeading(car.getHeading() + car.getSteeringDirection());
                    float angle = (float) (car.getHeading());
                    float deltaY = (float) (Math.sin(Math.toRadians(angle)) * car.getSpeed());
                    float deltaX = (float) (Math.cos(Math.toRadians(angle)) * car.getSpeed());
                    Location temp = new Location(car.getLocation().getX() + deltaX, car.getLocation().getY() + deltaY);

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

            int lastHighestPylonReached = car.getLastHighestPylonReached();
            if(lastHighestPylonReached == Services.findTheNumberOfPylons()){
                System.out.println("Too many movements, set a lower pylon");
                car.setLastHighestPylonReached(1);
                lastHighestPylonReached = car.getLastHighestPylonReached();
            }

            Pylon tmpPylon = null;
            try {
                tmpPylon = Services.findPylonWithIndexNumber(++lastHighestPylonReached);
            } catch (Exception e) {
                System.out.println("No such a pylon " + e.toString() + e.getStackTrace());
            }

            System.out.println("The last highest pylon:  " + car.getLastHighestPylonReached());
            System.out.println("The last highest pylon:  " + tmpPylon.getIndexNumber());


            Location pylonLocation = tmpPylon.getLocation();
            Location npcLocation = car.getLocation();
            float adj = pylonLocation.getX() - npcLocation.getX();
            float op = pylonLocation.getY() - npcLocation.getY();

            System.out.println("Adj is :  " + adj);
            System.out.println("Op is : " + op);


            float angleToAdd;



            angleToAdd = (float) Math.toDegrees(Math.atan(op / adj));
            //angleToAdd -= 90;

            if(adj > 0 && op > 0){//1
                angleToAdd = Math.abs(angleToAdd);
            } else if(adj < 0 && op > 0 ) {// 2
                angleToAdd = Math.abs(angleToAdd) + 90;
            } else if(adj < 0 && op < 0) {//3
                angleToAdd = Math.abs(angleToAdd) + 180;
            } else if(adj > 0 && op < 0){//4
                angleToAdd = Math.abs(angleToAdd) + 270;
            }
            car.heading = angleToAdd;

            System.out.println("angleToAdd is: " + angleToAdd);
            System.out.println("the heading is: " + car.heading);


            float deltaY = (float) (Math.sin(Math.toRadians(car.heading)) * car.getSpeed());
            float deltaX = (float) (Math.cos(Math.toRadians(car.heading)) * car.getSpeed());
            Location temp = new Location(car.getLocation().getX() + deltaX, car.getLocation().getY() + deltaY);

            car.setX(temp.getX());
            car.setY(temp.getY());

            if((car.getX() < (tmpPylon.getX() + 10) && car.getX() > (tmpPylon.getX() - 10)) &&
                    (car.getY() < (tmpPylon.getY() + 10) && car.getY() > (tmpPylon.getY() - 10))){
                car.setLastHighestPylonReached(car.getLastHighestPylonReached() + 1);
            }


        }
    }
}
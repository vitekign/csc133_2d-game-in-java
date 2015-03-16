package a1.objects;

/**
 * Created by Victor Ignatenkov on 3/13/15.
 */

import a1.commands.IStrategy;
import a1.model.GameWorld;

import java.awt.*;



import a1.commands.IStrategy;
import a1.model.GameWorld;

import java.awt.*;

/**
 * Created by Victor Ignatenkov on 2/9/15.
 */
public class NPCCar extends Moveable implements ISteerable {


    IStrategy strategy;


    private float width;
    private float length;

    private boolean inOilSlick;
    private int lastHighestPylonReached;
    private float steeringDirection;
    private float maximumSpeed;
    private float maximumDamageLevel;
    private float fuelLevel;
    private float speed;
    private float damageLevel;

    private int firstPylonToMove;

    public void setLastHighestPylonReached(int lastHighestPylonReached) {
        this.lastHighestPylonReached = lastHighestPylonReached;
    }

    public int getFirstPylonToMove() {

        return firstPylonToMove;
    }

    public void setFirstPylonToMove(int firstPylonToMove) {
        this.firstPylonToMove = firstPylonToMove;
    }

    public float getWidth() {
        return width;
    }

    public float getLength() {
        return length;
    }

    public boolean isInOilSlick() {
        return inOilSlick;
    }

    public float getSteeringDirection() {
        return steeringDirection;
    }

    public float getMaximumSpeed() {
        return maximumSpeed;
    }

    public float getMaximumDamageLevel() {
        return maximumDamageLevel;
    }

    public float getSpeed() {
        return speed;
    }

    public GameWorld getGw() {
        return gw;
    }

    public void setSteeringDirection(float steeringDirection) {
        this.steeringDirection = steeringDirection;
    }

    public void setUpStrategy(IStrategy str){
        strategy = str;
    }

    /**
     * The car object needs to access the GameWorld
     * because it needs to know certain methods in the
     * GameWorld in order to update data.
     */
    private GameWorld gw;

    public NPCCar(Location location, GameWorld gw, Color color, float width, float length,
                  float steeringDirection, float maximumSpeed, float fuelLevel,
                  float speed, float damageLevel, float maximumDamageLevel, int lastHighestPylonReached){
        super(color);

        /**
         * If further versions of the game require
         * creating multiple versions of cars, then
         * most of the following values will be
         * assigned by a corresponding constructor.
         */
        this.width = width;
        this.length = length;
        this.steeringDirection = steeringDirection;
        this.maximumSpeed = maximumSpeed;
        this.fuelLevel = fuelLevel;
        this.speed = speed;
        this.damageLevel = damageLevel;
        this.maximumDamageLevel = maximumDamageLevel;
        this.lastHighestPylonReached = lastHighestPylonReached;
//        width               = 5;
//        length              = 5;
//        steeringDirection   = 0;
//        maximumSpeed        = 100;
//        fuelLevel           = 100;
//        speed               = 0;
//        damageLevel         = 0;
//        maximumDamageLevel  = 100;
//        lastHighestPylonReached = 1;

        inOilSlick = false;


        this.X = location.getX();
        this.Y = location.getY();

        this.gw = gw;

       // gw.setNewFuelLevel(this.fuelLevel);
       // gw.updateDamageLevel(this.damageLevel);
    }



    /**
     *  If not in oil slick, then:
     *  1. the car’s
     *  heading should be incremented or decremented by the car’s
     *  steeringDirection.
     *  2. The car’s fuel level is reduced by a small amount.
     */


    @Override
    public void move() {

        if(strategy != null){
            strategy.performStrategy(this, gw);
        }
        else {
            System.out.println("The strategy is not set up");
        }

//        if(isCarInOilSlick()){
//            return ;
//        }
//
//        heading += steeringDirection;
//        float angle = (float) (90 - heading);
//        float deltaY = (float) (Math.sin(Math.toRadians(angle))*speed);
//        float deltaX = (float) (Math.cos(Math.toRadians(angle))*speed);
//        Location temp = new Location(this.getLocation().getX() + deltaX,
//                                     this.getLocation().getY() + deltaY);
//
//        this.X = temp.getX();
//        this.Y = temp.getY();
//
//
//        /**
//         * Decrease the amount of fuel
//         */
//        changeFuelLevel(-gw.DAMAGE_FOR_COLLIDING_WITH_CARS);
//
//        /**
//         *  Reset steering direction after applying it to the direction
//         *  of the car.
//         */
//        steeringDirection = 0;

    }

    /**
     * Change fuel level and update the data in the GameWorld
     * @param volume
     * the amount of fuel by which the amount should be
     * decrease/increased
     */
    public void changeFuelLevel(float volume){
        fuelLevel += volume;
        if(fuelLevel <= 0) {
            fuelLevel = 0;
            gw.deleteOneLife();
        } else {
            gw.setNewFuelLevel(this.fuelLevel);
        }
    }

    /**
     * Accelerate the car
     * @param additionalSpeed
     * amount of speed to accelerate car
     */
    public void accelerate(float additionalSpeed){
        if (isCarInOilSlick()) return;

        if(speed != maximumSpeed && fuelLevel != 0){
            if(speed == 0){
                speed += additionalSpeed;
            } else {
                if (damageLevel != 100)
                    if (damageLevel == 0) {
                        speed += additionalSpeed;
                    } else {
                        speed += (1 - (damageLevel / 100)) * additionalSpeed;
                    }
                if (speed > maximumSpeed) {
                    speed = maximumSpeed;
                }
            }
        }
    }

    /**
     * Slow down the car.
     * @param brakeSpeed
     * amount of speed to slow down the car.
     */
    public void brake(float brakeSpeed) {
        if (isCarInOilSlick()) return;

        if(speed != 0){
            speed -= brakeSpeed;
            if(speed < 0){
                speed = 0;
            }
        }

    }


    /**
     * Find out if the car in the oilSlick
     * @return
     * TRUE - car is in oil slick,
     * FALSE - car is not in oil slick
     */
    private boolean isCarInOilSlick() {
        return inOilSlick;

    }

    /**
     * Increase or decrease the amount of steeringDirection
     * @param deflection
     */
    private void changeCurrentHeading(double deflection){
        if((Math.abs(steeringDirection + deflection)) > Math.abs(MAX_HEADING_DEFLECTION)){
            return;
        }
        steeringDirection += deflection;
    }


    @Override
    public void changeCurrentHeadingToTheLeft() {
        changeCurrentHeading(-ROTATION_UNIT);
    }

    @Override
    public void changeCurrentHeadingToTheRight() {
        changeCurrentHeading(ROTATION_UNIT);
    }


    @Override
    public String toString() {
        return "Car: "       + super.toString() +
                " width="    + (int)this.width +
                " length="   + (int)this.length +
                " heading="  + (int)heading +
                " speed="    + speed +
                " \n\t  maxSpeed=" + (int)this.maximumSpeed +
                " steeringDirection=" + (int)this.steeringDirection +
                " fuelLevel="         + (int)this.fuelLevel +
                " damage="            + (int)this.damageLevel;
    }


    /**
     * Propagate the responsibility for dealing with
     * the car's critical damage level to the GameWorld
     *
     * @param damage
     * amount of damage to add to the Car
     */
    public void increaseDamageLevel(float damage) {
        damageLevel += damage;
        if(damageLevel > maximumDamageLevel){
            damageLevel = maximumDamageLevel;
        }
        gw.updateDamageLevel(damageLevel);
    }


    /**
     *
     * @return
     * current fuel level.
     */
    public float getFuelLevel() {
        return fuelLevel;
    }

    /**
     * @return
     * current damage level.
     */
    public float getDamageLevel() {
        return damageLevel;
    }


    /**
     * Return last pylon reached.
     * @return
     */
    public int getLastHighestPylonReached() {
        return lastHighestPylonReached;
    }

    /**
     * Responsible for dealing with running
     * into pylons. Update the GameWorld class's
     * state regarding the last pylon reached.
     * @param numberOfPylon
     * number of the pylon
     */
    public void collideWithPylon(int numberOfPylon) {
        int temp = lastHighestPylonReached;
        if(++temp == numberOfPylon && lastHighestPylonReached != Pylon.getCount()) {
            ++lastHighestPylonReached;
            gw.updateLastPylonReached(lastHighestPylonReached);
        }
    }

    /**
     * Responsible for entering oil slicks.
     */
    public void enterAnOilSlick(){
        inOilSlick = true;
    }

    /**
     * Responsible for leaving oil slicks.
     */
    public void exitAnOilSlick(){
        inOilSlick = false;
    }

    /**
     * Responsible for picking up FuelCans.
     * @param fuelCan
     * fuel can to be picked up.
     */
    public void pickUpFuelCan(FuelCan fuelCan) {
        changeFuelLevel(fuelCan.getSize());
    }

    /**
     * In the current version - damage of colliding
     * with birds equals to 1/2 damage of colliding
     * with other cars.
     * @return
     * damage for colliding with birds
     */
    public static float damageForCollidingWithBirds(){
        return GameWorld.DAMAGE_FOR_COLLIDING_WITH_CARS /2;
    }
}

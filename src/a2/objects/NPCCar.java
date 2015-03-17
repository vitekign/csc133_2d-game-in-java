package a2.objects;

/**
 * Created by Victor Ignatenkov on 3/13/15.
 */

//TODO Create Maximum damage level
//TODO override all methods which update the data in the model
import a2.commands.IStrategy;
import a2.model.GameWorld;

import java.awt.*;

/**
 * Created by Victor Ignatenkov on 2/9/15.
 */
public class NPCCar extends Car {


    IStrategy strategy;
    private int firstPylonToMove;


    public int getFirstPylonToMove() {

        return firstPylonToMove;
    }

    public IStrategy returnCurrentStrategy(){
        return strategy;
    }

    public void setUpStrategy(IStrategy str){
        strategy = str;
    }


    public NPCCar(Location location, GameWorld gw, Color color, float width, float length,
                  float steeringDirection, float maximumSpeed, float fuelLevel,
                  float speed, float damageLevel, float maximumDamageLevel, int lastHighestPylonReached){
        super(location, gw, color);

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
        inOilSlick = false;


        this.X = location.getX();
        this.Y = location.getY();

        this.gw = gw;
    }


    @Override
    public void move() {

        if(strategy != null){
            strategy.performStrategy(this, gw);
        }
        else {
            System.out.println("The strategy is not set up");
        }
    }

    @Override
    public String toString() {
        return "Car: "       + super.toString() +
                " strategy="  + this.strategy.toString() +
                " \n\t  lastPylonReached=" + this.getLastHighestPylonReached();
    }

    public void setFirstPylonToMove(int firstPylonToMove) {
        this.firstPylonToMove = firstPylonToMove;
    }

    public void setLastHighestPylonReached(int lastHighestPylonReached) {
        this.lastHighestPylonReached = lastHighestPylonReached;
    }


    @Override
    public void increaseDamageLevel(float damage) {
        damageLevel += damage;
        if(damageLevel > maximumDamageLevel){
            damageLevel = maximumDamageLevel;
        }
    }
}

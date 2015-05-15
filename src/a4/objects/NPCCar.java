/**
 * Created by Victor Ignatenkov on 3/13/15.
 */

package a4.objects;

//TODO override all methods which update the data in the model
import a4.app.strategies.IStrategy;
import a4.model.GameWorld;
import java.awt.*;
import java.util.Vector;

/**
 * NPCCar class inherits from the Car
 * and is responsible for non player character car.
 */
public class NPCCar extends Car {


    public static int zIndex;


    @Override
    public void draw(Graphics2D g) {


        g.setColor(color);
        g.drawRect((int)getX() - (int)(width/2), (int)getY()-(int)(length/2), (int)width, (int)length);
        g.setColor(Color.white);
        g.drawOval((int)getX(), (int)getY(), 1,1);
        g.setColor(Color.black);
    }




    IStrategy strategy;
    private int firstPylonToMove;

    /**
     *
     * @return
     */
    public int getFirstPylonToMove() {

        return firstPylonToMove;
    }

    public IStrategy returnCurrentStrategy(){
        return strategy;
    }

    public void setUpStrategy(IStrategy str){
        strategy = str;
    }



    public int getZIndex(){
        return NPCCar.zIndex;
    }



    /**
     * Create a NPC car.
     * The class needs to override the information
     * inherited from the Car.
     * @param location
     * @param gw
     * @param color
     * @param width
     * @param length
     * @param steeringDirection
     * @param maximumSpeed
     * @param fuelLevel
     * @param speed
     * @param damageLevel
     * @param maximumDamageLevel
     * @param lastHighestPylonReached
     */
    public NPCCar(Location location, GameWorld gw, Color color, float width, float length,
                  float steeringDirection, float maximumSpeed, float fuelLevel,
                  float speed, float damageLevel, float maximumDamageLevel, int lastHighestPylonReached){
        super(location, gw, color);


        objectsCollidedWith = new Vector<>();

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


    /**
     * The move command is implemented by using a
     * strategy pattern. Basically, it redirects move's
     * responsibility from NPCCar to the performStrategy
     * method of the Strategy class.
     * @param framesPerSecond
     */
    @Override
    public void move(int framesPerSecond) {

        if(strategy != null){
            strategy.performStrategy(this, gw);
        }
        else {
            System.out.println("The strategy is not set up");
        }
    }

    /**
     * Information about the current state
     * of the object.
     * @return
     * thorough information
     */
    @Override
    public String toString() {
        return "NPCCar: "       + super.toString() +
                " strategy="  + this.strategy.toString() +
                " \n\t  lastPylonReached=" + this.getLastHighestPylonReached();
    }

    /**
     * Set the first pylon, so the the NPCCar object
     * knows where to move after creation.
     * @param firstPylonToMove
     */
    public void setFirstPylonToMove(int firstPylonToMove) {
        this.firstPylonToMove = firstPylonToMove;
    }

    /**
     * Set the last pylon the the NPCcar object has reached,
     * so the the NPCCar object knows which pylon to aim at.
     * @param lastHighestPylonReached
     */
    public void setLastHighestPylonReached(int lastHighestPylonReached) {
        this.lastHighestPylonReached = lastHighestPylonReached;
    }


    /**
     * All methods which update the model in the Car object,
     * have to be overridden, so they don't update the model's
     * date which must listen only to changes which occur in the
     * primary character car.
     */

    /**
     * Increase damage of the NPCCar object.
     * @param damage
     */
    //TODO Delete NPCCars with fatal level of damage
    @Override
    public void increaseDamageLevel(float damage) {
        damageLevel += damage;
        if(damageLevel > maximumDamageLevel){
            damageLevel = maximumDamageLevel;
        }
    }


    /*
     * ICollider implementation
     */
    @Override
    public float getDistanceOfReference() {
        return (width + length) / 2;
    }

    /**
     * The logic which detects the collision
     * with other object in the Game World
     * @param obj the other object
     * @return true if collision has happened
     */
    @Override
    public boolean collidesWith(ICollider obj) {
        double distX = this.getX() - ((GameObject)obj).getX();
        double distY = this.getY() - ((GameObject)obj).getY();
        float distanceBtwnCenters = (float) Math.sqrt(distX * distX + distY * distY);

        if((this.getDistanceOfReference() + obj.getDistanceOfReference() >
                distanceBtwnCenters)){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Handle collision of the NPCCar with other Game Objects.
     * @param otherObject
     */
    @Override
    public void handleCollision(ICollider otherObject) {
        if(otherObject instanceof Car && !(otherObject instanceof NPCCar)){
            if(!objectsCollidedWith.contains((GameObject)otherObject)){
                objectsCollidedWith.add((GameObject)otherObject);

            }
        }
    }
}

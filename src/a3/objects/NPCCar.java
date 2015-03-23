/**
 * Created by Victor Ignatenkov on 3/13/15.
 */

package a3.objects;

//TODO override all methods which update the data in the model
import a3.app.strategies.IStrategy;
import a3.model.GameWorld;
import java.awt.*;

/**
 * NPCCar class inherits from the Car
 * and is responsible for non player character car.
 */
public class NPCCar extends Car {


    @Override
    public void draw(Graphics g) {
        g.drawString("NPC", (int)getX(), (int)getY());
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
     */
    @Override
    public void move() {

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
    @Override
    public void increaseDamageLevel(float damage) {
        damageLevel += damage;
        if(damageLevel > maximumDamageLevel){
            damageLevel = maximumDamageLevel;
        }
    }
}

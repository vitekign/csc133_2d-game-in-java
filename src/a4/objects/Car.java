package a4.objects;

import a4.app.utilities.Services;
import a4.app.utilities.Sound;
import a4.model.GameWorld;
import a4.objects.character_car.Body;
import a4.objects.character_car.FrontAxle;
import a4.objects.character_car.RearAxle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;

/**
 * Created by Victor Ignatenkov on 2/9/15.
 */
public class Car extends Moveable implements ISteerable , IDrawable, ICollider{


    protected float width;
    protected float length;

    protected boolean inOilSlick;
    protected int lastHighestPylonReached;
    protected float steeringDirection;
    protected float maximumSpeed;
    protected float maximumDamageLevel;
    protected float fuelLevel;
    protected float speed;
    protected float damageLevel;

    public static int zIndex;

    Image imageRes;





    public int getZIndex(){
        return Car.zIndex;
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



    /**
     * The car object needs to access the GameWorld
     * because it needs to know certain methods in the
     * GameWorld in order to update data.
     */
    protected GameWorld gw;
    public Sound collideWithNPCSound;
    public Sound collideWithFuelCanSound;

    Body myBody;
    FrontAxle myFrontAxle;
    RearAxle myRearAxle;


    public Car(Location location, GameWorld gw, Color color){
        super(color);

        objectsCollidedWith = new Vector<>();



        /**
         * If further versions of the game require
         * creating multiple versions of cars, then
         * most of the following values will be
         * assigned by a corresponding constructor.
         */
        width               = 25;
        length              = 25;
        steeringDirection   = 0;
        maximumSpeed        = 100;
        fuelLevel           = 100;
        speed               = 0;
        damageLevel         = 0;
        maximumDamageLevel  = 100;



        myBody = new Body(30, 60);

        myFrontAxle = new FrontAxle(40, 9);
        myFrontAxle.translate(0,20);
        myRearAxle = new RearAxle(40,9);
        myRearAxle.translate(0,-20);


        inOilSlick = false;
        lastHighestPylonReached = 1;

        this.X = location.getX();
        this.Y = location.getY();

        this.gw = gw;

        gw.setNewFuelLevel(this.fuelLevel);
        gw.updateDamageLevel(this.damageLevel);

        collideWithNPCSound = new Sound("hittingWall.wav");
        collideWithFuelCanSound = new Sound("slurp.wav");



        heading = 180;

        try {

            String pathToResources = Services.getPathToImgResources();
            String imgName = "car.png";
            imageRes= ImageIO.read(new File(pathToResources + imgName));
        }catch (IOException ex){
            System.out.println("An error happened: " + ex.getMessage());
        }


        myRotationMatrix = new AffineTransform();
        myTranslationMatrix = new AffineTransform();
        myScaleMatrix = new AffineTransform();

    }



    public void playSound(){
        collideWithNPCSound.play();
    }

    public void playSoundForFuelEating(){
        collideWithFuelCanSound.play();
    }

    /**
     *  If not in oil slick, then:
     *  1. the car’s
     *  heading should be incremented or decremented by the car’s
     *  steeringDirection.
     *  2. The car’s fuel level is reduced by a small amount.
     * @param framesPerSecond
     */


    @Override
    public void move(int framesPerSecond) {
        //TODO find out what to do with framesPerSecond


            if (isCarInOilSlick()) {
                float angle = (float) (90 - heading);
                float deltaY = (float) (Math.sin(Math.toRadians(angle + 180)) * speed * framesPerSecond / 5);
                float deltaX = (float) (Math.cos(Math.toRadians(angle + 180)) * speed * framesPerSecond / 5);
                Location temp = new Location(this.getLocation().getX() + deltaX,
                        this.getLocation().getY() + deltaY);

                this.X = temp.getX();
                this.Y = temp.getY();
                /**
                 * Decrease the amount of fuel
                 */
                //TODO Find out by which amount to decrease the amount of fuel
                changeFuelLevel((float) -0.02);
            } else {

                if(gw.getTime()%5 == 0 && gw.getTime() != 0)
                  heading += steeringDirection;
                float angle = (float) (90 - heading);
                float deltaY = (float) (Math.sin(Math.toRadians(angle + 180)) * speed * framesPerSecond / 5);
                float deltaX = (float) (Math.cos(Math.toRadians(angle + 180)) * speed * framesPerSecond / 5);
                Location temp = new Location(this.getLocation().getX() + deltaX,
                        this.getLocation().getY() + deltaY);

                this.X = temp.getX();
                this.Y = temp.getY();


                /**
                 * Decrease the amount of fuel
                 */
                //TODO Find out by which amount to decrease the amount of fuel
                changeFuelLevel((float) -0.02);

                /**
                 *  Reset steering direction after applying it to the direction
                 *  of the car.
                 */

                //steeringDirection = 0;
            }


    }

    /**
     * Change fuel level and update the data in the GameWorld
     * @param volume
     * the amount of fuel by which the amount should be
     * decrease/increased
     */

    //TODO 2. Fuel level not increased by indicated level
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
        //if (isCarInOilSlick()) return;

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

        myFrontAxle.updateSteeringDirection((float) deflection);
    }


    @Override
    public void changeCurrentHeadingToTheLeft() {
        changeCurrentHeading(-ROTATION_UNIT);
    }

    @Override
    public void changeCurrentHeadingToTheRight() {
        changeCurrentHeading(ROTATION_UNIT);
    }


    /**
     * Create a thorough information.
     * @return
     * string with information about main features.
     */
    @Override
    public String toString() {
        return "Car: "       + super.toString() +
                " width="    + (int)this.width +
                " length="   + (int)this.length +
                " heading="  + (int)heading +
                " speed="    + (int)speed +
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

        Vector<Pylon> allPylons = Services.getAllPylons();

        allPylons.sort((Pylon o1, Pylon o2)->{
                if(o1.getIndexNumber() < o2.getIndexNumber()){
                    return -1;
                } else {
                    return 1;
                }
            });



        Pylon currentPylon = null;
        for(Pylon pylon : allPylons){
            if(pylon.getIndexNumber() > this.getLastHighestPylonReached()){
                currentPylon = pylon;
                break;
            }
        }

        if(currentPylon != null) {
            if (numberOfPylon == currentPylon.getIndexNumber()) {
                gw.updateLastPylonReached(numberOfPylon);
                this.lastHighestPylonReached = numberOfPylon;
            }
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

    /**
     * Draw the Car
     * @param g2d supply the Graphics
     */
    @Override
    public void draw(Graphics2D g2d) {

        AffineTransform saveAt = g2d.getTransform();
        myTranslationMatrix.translate((int) getX() - (int) (width / 2), (int) getY() - (int) (length / 2));
        myRotationMatrix.rotate(Math.toRadians(90));
        myRotationMatrix.rotate(Math.toRadians(90 - heading));

        g2d.transform(myTranslationMatrix);
        g2d.transform(myRotationMatrix);


        /**
         * New stuff
         */
        /**
         translate(100, 100);
         rotate(-45);
         scale(1, 1);
         /* *
         *
         */
        /**
        g2d.transform(myTranslationMatrix);
        g2d.transform(myRotationMatrix);
        g2d.transform(myScaleMatrix);
        */

        myBody.translate(0, 0);
        myBody.draw(g2d);
        myFrontAxle.draw(g2d);
        myRearAxle.draw(g2d);
        /*****************************/




        g2d.drawOval(0, 0, 1, 1);
       // g2d.drawImage( imageRes, 0,0, 45, 30, null);



        setToIdentity();
        g2d.setTransform(saveAt);

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
     * Handle collision of the Car with other Game Objects.
     * @param otherObject
     */
    @Override
    public void handleCollision(ICollider otherObject) {
        /********* BIRD ***********/
       if(otherObject instanceof Bird){
           if(!objectsCollidedWith.contains((GameObject)otherObject)){
               objectsCollidedWith.add((GameObject)otherObject);
           }
           gw.birdFlyOver();
           gw.createNewShockWave(this.getLocation());
       }
       /********* FUEL CAN ***********/
        else if(otherObject instanceof FuelCan){
           gw.gameObjectsToDelete.add((GameObject) otherObject);
           if(!objectsCollidedWith.contains((GameObject)otherObject)){
               objectsCollidedWith.add((GameObject)otherObject);

           }
       }
       /********* NPC CAR ***********/
        else if(otherObject instanceof NPCCar){
           if(!objectsCollidedWith.contains((GameObject)otherObject)){
               objectsCollidedWith.add((GameObject)otherObject);
           }
           int chanceNewOilSlick = (int) ((new Random().nextFloat())*100);
           if(chanceNewOilSlick > 80){
               //create a new oil slick
               gw.addOilSlickWithLocation(this.getLocation());
           }
           if(gw.isSound()) {
               playSound();
           }
           gw.carCollideWithCar((NPCCar)otherObject);
           gw.createNewShockWave(this.getLocation());
           gw.switchStrategies();
       }
       /********* OIL SLICK ***********/
       else if(otherObject instanceof OilSlick){
           if(!objectsCollidedWith.contains((GameObject)otherObject)){
               objectsCollidedWith.add((GameObject) otherObject);
           }

           gw.enterOilSlick();
       }

       /********* PYLON ***********/
       else if(otherObject instanceof Pylon){
           if(!objectsCollidedWith.contains((GameObject)otherObject)){
               objectsCollidedWith.add((GameObject)otherObject);
           }
           gw.carCollideWithPylon( ((Pylon) otherObject).getIndexNumber());
       }
    }

    public void setLastHighestPylonReachedToZero(){
        lastHighestPylonReached = 0;
    }

    @Override
    public float getDistanceOfReference() {
        //TODO supply an actual number
        return 15;
    }
}

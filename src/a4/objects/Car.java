/* Created by Victor Ignatenkov on 2/9/15. */

package a4.objects;
import a4.app.utilities.Services;
import a4.app.utilities.Sound;
import a4.model.GameWorld;
import a4.objects.character_car.Body;
import a4.objects.character_car.FrontAxle;
import a4.objects.character_car.RearAxle;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Random;
import java.util.Vector;

public class Car extends Moveable implements ISteerable , IDrawable, ICollider {
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
    protected GameWorld gw;

    public static int zIndex;

    public Image imageRes;
    public Sound collideWithNPCSound;
    public Sound collideWithFuelCanSound;

    private Body myBody;
    private FrontAxle myFrontAxle;
    private RearAxle myRearAxle;

    public Car(Location location, GameWorld gw, Color color) {
        super(color);

        objectsCollidedWith = new Vector<>();

        /**
         * If further versions of the game require
         * creating multiple versions of cars, then
         * most of the following values will be
         * assigned by a corresponding constructor.
         */
        width = 25;
        length = 25;
        steeringDirection = 0;
        maximumSpeed = 100;
        fuelLevel = 100;
        speed = 0;
        damageLevel = 0;
        maximumDamageLevel = 100;


        myBody = new Body(30, 60);

        myFrontAxle = new FrontAxle(40, 9);
        myFrontAxle.translate(0, 20);
        myRearAxle = new RearAxle(40, 9);
        myRearAxle.translate(0, -20);


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

        imageRes = Services.getImage("car.png");

        myRotationMatrix = new AffineTransform();
        myTranslationMatrix = new AffineTransform();
        myScaleMatrix = new AffineTransform();

    }

    /*  If not in oil slick, then:
     *  1. the car’s
     *  heading should be incremented or decremented by the car’s
     *  steeringDirection.
     *  2. The car’s fuel level is reduced by a small amount. */
    @Override
    public void move(int framesPerSecond) {
        if (isCarInOilSlick()) {
            float angle = (90 - heading);
            float deltaY = (float) (Math.sin(Math.toRadians(angle + 180)) * speed * framesPerSecond / 5);
            float deltaX = (float) (Math.cos(Math.toRadians(angle + 180)) * speed * framesPerSecond / 5);
            Location temp = new Location(this.getLocation().getX() + deltaX,
                    this.getLocation().getY() + deltaY);

            this.X = temp.getX();
            this.Y = temp.getY();

            changeFuelLevel((float) -0.02);
        } else {
            if (gw.getTime() % 3 == 0 && gw.getTime() != 0)
                heading += steeringDirection;
            float angle = (90 - heading);
            float deltaY = (float) (Math.sin(Math.toRadians(angle + 180)) * speed * framesPerSecond / 5);
            float deltaX = (float) (Math.cos(Math.toRadians(angle + 180)) * speed * framesPerSecond / 5);
            Location temp = new Location(this.getLocation().getX() + deltaX,
                    this.getLocation().getY() + deltaY);

            this.X = temp.getX();
            this.Y = temp.getY();

            changeFuelLevel((float) -0.02);
        }
    }

    public void accelerateTheCar(float additionalSpeed) {
        speed += additionalSpeed;
        if (speed != maximumSpeed && fuelLevel != 0) {
            if (speed == 0) {
            } else {
                if (damageLevel != 100) {
                    if (damageLevel == 0) {
                        speed += additionalSpeed;
                    } else {
                        speed += (1 - (damageLevel / 100)) * additionalSpeed;
                    }
                }
                if (speed > maximumSpeed) {
                    speed = maximumSpeed;
                }
            }
        }
    }

    /* Change fuel level and update the data in the GameWorld */
    public void changeFuelLevel(float volume) {
        fuelLevel += volume;
        if (fuelLevel <= 0) {
            fuelLevel = 0;
            gw.deleteOneLifeAndRestartLevel();
        } else {
            gw.setNewFuelLevel(this.fuelLevel);
        }
    }

    public void applyBreaks(float brakeSpeed) {
        if (isCarInOilSlick()) return;
        if (speed != 0) {
            speed -= brakeSpeed;
            if (speed < 0) {
                speed = 0;
            }
        }
    }


    private boolean isCarInOilSlick() {
        return inOilSlick;

    }

    private void changeCurrentHeading(double deflection) {
        if ((Math.abs(steeringDirection + deflection)) > Math.abs(MAX_HEADING_DEFLECTION)) {
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


    @Override
    public String toString() {
        return "Car: " + super.toString() +
                " width=" + (int) this.width +
                " length=" + (int) this.length +
                " heading=" + (int) heading +
                " speed=" + (int) speed +
                " \n\t  maxSpeed=" + (int) this.maximumSpeed +
                " steeringDirection=" + (int) this.steeringDirection +
                " fuelLevel=" + (int) this.fuelLevel +
                " damage=" + (int) this.damageLevel;

    }



    public int getZIndex(){
        return Car.zIndex;
    }

    public float getSteeringDirection() {
        return steeringDirection;
    }

    public float getSpeed() {
        return speed;
    }

    public GameWorld getGw() {
        return gw;
    }

    public void playSound(){
        collideWithNPCSound.play();
    }

    public void playSoundForFuelEating(){
        collideWithFuelCanSound.play();
    }

    public void increaseDamageLevelAndUpdateGameWorld(float damage) {
    damageLevel += damage;
    if(damageLevel > maximumDamageLevel){
            damageLevel = maximumDamageLevel;
        }
        gw.updateDamageLevel(damageLevel);
    }

    public int getLastHighestPylonReached() {
        return lastHighestPylonReached;
    }

    public void collideWithPylonAndUpdateGameWorld(int numberOfPylon) {

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

    public void enterOilSlick(){
        inOilSlick = true;
    }

    public void exitOilSlick(){
        inOilSlick = false;
    }

    public void pickUpFuelCan(FuelCan fuelCan) {
        changeFuelLevel(fuelCan.getSize());
    }

    public static float getAmountOfDamageForCollidingWithBirds(){
        return GameWorld.DAMAGE_FOR_COLLIDING_WITH_CARS /2;
    }

    @Override
    public void draw(Graphics2D g2d) {

        AffineTransform saveAt = g2d.getTransform();
        myTranslationMatrix.translate((int) getX() - (int) (width / 2), (int) getY() - (int) (length / 2));
        myRotationMatrix.rotate(Math.toRadians(90));
        myRotationMatrix.rotate(Math.toRadians(90 - heading));

        g2d.transform(myTranslationMatrix);
        g2d.transform(myRotationMatrix);

        myBody.translate(0, 0);
        myFrontAxle.draw(g2d);
        myRearAxle.draw(g2d);
        myBody.draw(g2d);

        g2d.drawOval(0, 0, 1, 1);

        setToIdentity();
        g2d.setTransform(saveAt);
    }

    @Override
    public boolean didCollideWithAnotherObject(ICollider obj) {
        double distX = this.getX() - ((GameObject)obj).getX();
        double distY = this.getY() - ((GameObject)obj).getY();
        float distanceBetweenCenters = (float) Math.sqrt(distX * distX + distY * distY);

        return (this.getDistanceOfReference() + obj.getDistanceOfReference() > distanceBetweenCenters);
    }

    @Override
    public void handleCollision(ICollider otherObject) {
        /********* BIRD ***********/
       if(otherObject instanceof Bird){
           if(!objectsCollidedWith.contains(otherObject)){
               objectsCollidedWith.add((GameObject)otherObject);
           }
           gw.birdFlyOver();
           gw.createNewShockWave(this.getLocation());
       }
       /********* FUEL CAN ***********/
        else if(otherObject instanceof FuelCan){
           gw.gameObjectsToBeRemoved.add((GameObject) otherObject);
           if(!objectsCollidedWith.contains(otherObject)){
               objectsCollidedWith.add((GameObject)otherObject);

           }
       }
       /********* NPC CAR ***********/
        else if(otherObject instanceof NPCCar){
           if(!objectsCollidedWith.contains(otherObject)){
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
           if(!objectsCollidedWith.contains(otherObject)){
               objectsCollidedWith.add((GameObject) otherObject);
           }

           gw.enterOilSlick();
       }

       /********* PYLON ***********/
       else if(otherObject instanceof Pylon){
           if(!objectsCollidedWith.contains(otherObject)){
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

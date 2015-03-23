/**
 * Created by Victor Ignatenkov on 2/9/15.
 */

package a3.model;
import a3.app.strategies.FollowThePlayerCarStrategy;
import a3.app.strategies.MoveTowardsPylonStrategy;
import a3.controller.IGameWorld;
import a3.objects.*;
import java.awt.*;
import java.util.Random;
import java.util.Vector;




public class GameWorld implements Container , IObservable, IGameWorld {


    /*
     *  Code here to hold and manipulate world objects, handle
     *  observer registration, invoke observer callbacks, etc.
     */




    /**
     * Some basic constants
     */

    public static final int GLOBAL_WIDTH = 1000;
    public static final int GLOBAL_HEIGHT = 800;
    public static final int THE_FIRST_PYLON = 1;


    public final static float DAMAGE_FOR_COLLIDING_WITH_CARS = 5;
    public static final int NUMBER_OF_LIVES = 3;
    Random rand = new Random();


    private int currentClockTime;
    private int livesRemaining = NUMBER_OF_LIVES;
    private int lastPylonReached = THE_FIRST_PYLON;
    private float currentFuelLevel;
    private float damageLevel;
    private boolean sound;

    Car car;

    Vector<GameObject> theWorldVector;
    Vector<IObserver> observers = new Vector<>();


    FollowThePlayerCarStrategy followStr;
    MoveTowardsPylonStrategy moveToPylon;

    public void initLayout() {

        /**
         * Collection with all game objects
         */
        theWorldVector = new Vector<>();

        turnOnSound(false);

        /**
         * Initialize oll needed objects on the fly
         * and add them to the collection.
         * If one life is lost - make a hard reset and
         * initialize all objects again with the same data.
         */
        theWorldVector.add(new Pylon(new Location(200, 50), 50, new Color(64, 64, 64)));
        theWorldVector.add(new Pylon(new Location(70, 100), 50, new Color(64, 64, 64)));
        theWorldVector.add(new Pylon(new Location(200, 50), 50, new Color(64, 64, 64)));
        theWorldVector.add(new Pylon(new Location(800, 800), 50, new Color(64, 64, 64)));


        Services.supplyServicesWithGameWorld(this);

        /**
         * Find the pylon with the sequence number of THE_FIRST_PYLON
         * and create a car based on the location information
         */
        Pylon firstPylon = null;
        Location locationToPlaceCar = null;
        try {
            firstPylon = Services.findPylonWithIndexNumber(THE_FIRST_PYLON);
            locationToPlaceCar = firstPylon.getLocation();
            car = new Car(locationToPlaceCar, this, new Color(240, 0, 0));
            theWorldVector.add(car);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }


        /**
         * Add another required objects with random data
         */
        theWorldVector.add(new Bird(new Location(new Random().nextFloat() * GLOBAL_WIDTH,
                new Random().nextFloat() * GLOBAL_HEIGHT), new Random().nextFloat() * 360,
                new Random().nextFloat() * 3, new Color(255, 0, 255)));
        theWorldVector.add(new Bird(new Location(new Random().nextFloat() * GLOBAL_WIDTH,
                new Random().nextFloat() * GLOBAL_HEIGHT), new Random().nextFloat() * 360,
                new Random().nextFloat() * 3, new Color(255, 100, 0)));


        theWorldVector.add(new OilSlick(new Location(rand.nextFloat() * GLOBAL_WIDTH,
                rand.nextFloat() * GLOBAL_HEIGHT),
                rand.nextFloat() * 100,
                rand.nextFloat() * 100,
                new Color(0, 0, 0)));
        theWorldVector.add(new OilSlick(new Location(rand.nextFloat() * GLOBAL_WIDTH,
                rand.nextFloat() * GLOBAL_HEIGHT),
                rand.nextFloat() * 100,
                rand.nextFloat() * 100,
                new Color(0, 0, 0)));

        theWorldVector.add(new FuelCan(new Location(50, 50), rand.nextFloat() * 25, new Color(255, 25, 5)));
        theWorldVector.add(new FuelCan(new Location(20, 555), rand.nextFloat() * 25, new Color(5, 25, 255)));


        NPCCar npcCar1 = new NPCCar(new Location(new Random().nextInt(100),new Random().nextInt(600)), this, Services.generateRandomColor(),
                5,5,0,100,100,10,0,200,0);
        NPCCar npcCar2 = new NPCCar(new Location(new Random().nextInt(100),new Random().nextInt(600)), this, Services.generateRandomColor(),
                5,5,0,100,100,10,0,200,0);
        NPCCar npcCar3 = new NPCCar(new Location(new Random().nextInt(100),new Random().nextInt(600)), this, Services.generateRandomColor(),
                5,5,0,100,100,10,0,200,0);

         followStr = new FollowThePlayerCarStrategy();
         moveToPylon = new MoveTowardsPylonStrategy();

        npcCar1.setUpStrategy(moveToPylon);
        npcCar2.setUpStrategy(followStr);
        npcCar3.setUpStrategy(moveToPylon);


        theWorldVector.add(npcCar1);
        theWorldVector.add(npcCar2);
        theWorldVector.add(npcCar3);

    }


    /**
     * Additional methods to manipulate world
     * objects and related game date
     */

    /**
     * Accelerate the car
     */
    public void accelerate() {
        car.accelerate(2.5f);
        notifyObserver();
    }

    /**
     * Slow down the car
     */
    public void brake() {
        car.brake(2.5f);
        notifyObserver();
    }

    /**
     * Apply left steering to the car
     */
    public void changeSteeringToLeft() {
        car.changeCurrentHeadingToTheLeft();
        notifyObserver();

    }

    /**
     * Apply right steering to the car
     */
    public void changeSteeringToRight() {
        car.changeCurrentHeadingToTheRight();
        notifyObserver();
    }

    /**
     * Add new oilSlick to the collection
     */
    public void addOilSlick() {
        OilSlick oilSlick = new OilSlick(new Location(rand.nextFloat() * GLOBAL_WIDTH,
                rand.nextFloat() * GLOBAL_HEIGHT), rand.nextFloat() * 50,
                rand.nextFloat() * 50, Services.generateRandomColor());
        theWorldVector.addElement(oilSlick);

        notifyObserver();
    }

    /**
     * Pretend that the car has collided with
     * another car
     */

    public void carCollideWithCar() {
        car.increaseDamageLevel(DAMAGE_FOR_COLLIDING_WITH_CARS);

        Iterator iter = this.getIterator();

        int counter = 0;
        while(iter.hasNext()) {
            GameObject mObj = (GameObject) iter.getNext();
            if(mObj instanceof NPCCar){
              counter++;
            }
        }
        //3
        int rand = new Random().nextInt(counter); // [0-2]
        rand++;
        //[1-3]
        iter = this.getIterator();

        int checkCounter = 0;
        while(iter.hasNext()) {
            GameObject mObj = (GameObject) iter.getNext();
            if(mObj instanceof NPCCar){
                checkCounter++;
                if(checkCounter == rand) {
                    ((NPCCar) mObj).increaseDamageLevel(10);
                }
            }
        }
        notifyObserver();
    }

    /**
     * Pretend that the car has collided with a
     * pylon
     *
     * @param num sequential number of the pylon to place the car in
     */
    public void carCollideWithPylon(int num) {
        car.collideWithPylon(num);

        notifyObserver();
    }


    /**
     * Pretend that the car has picked up a fuel can.
     * Grab hold of the first fuelCan in the collection
     * by using general-purpose Services class.
     */
    public void pickUpFuelCan() {
        FuelCan temp = null;
        try {
            temp = Services.findTheFirstFuelCan();
            car.pickUpFuelCan(temp);
            theWorldVector.remove(temp);
            temp = new FuelCan(new Location(new Random().nextInt(800), new Random().nextInt(800)), (rand.nextFloat() * 20) + 1, Services.generateRandomColor());
            theWorldVector.add(temp);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        notifyObserver();
    }

    /**
     * Pretend that a bird has flown over the car,
     * thus making the car gain some damage
     */
    public void birdFlyOver() {
        car.increaseDamageLevel(car.damageForCollidingWithBirds());

        notifyObserver();
    }


    /**
     * Pretend that the car has entered
     * an oilSlick
     */


    public void enterOilSlick() {
        car.enterAnOilSlick();

        notifyObserver();
    }

    /**
     * Pretend that the car has left the
     * oilSlick
     */
    public void leaveOilSlick() {
        car.exitAnOilSlick();

        notifyObserver();
    }

    /**
     * Generate and apply random colors to
     * the objects which have the ability to
     * have their color changed.
     */
    public void generateNewColors() {
        Iterator iter = this.getIterator();

        while(iter.hasNext()) {
            GameObject mObj = (GameObject) iter.getNext();
            mObj.changeColor(Services.generateRandomColor());

        }


        notifyObserver();
    }

    /**
     * Pretend that one tick has happened.
     * Make all classes run move method.
     */
    public void makeTick() {


        Iterator iter = this.getIterator();

        while(iter.hasNext()) {
            GameObject mObj = (GameObject) iter.getNext();
            if(mObj instanceof Moveable){
              ((Moveable) mObj).move();
            }

        }
        currentClockTime++;
        notifyObserver();
    }


    public void turnOnSound(boolean val){
        sound = val;
        notifyObserver();
    }

    public boolean isSound() {
        return sound;
    }

    public void switchSound() {
        if(sound){
            sound = false;
        } else {
            sound = true;
        }
        notifyObserver();
    }

    public void switchStrategies(){
        Iterator iter = this.getIterator();

        while(iter.hasNext()) {
            GameObject mObj = (GameObject) iter.getNext();
            if(mObj instanceof NPCCar){
                if(((NPCCar) mObj).returnCurrentStrategy() instanceof MoveTowardsPylonStrategy)
                {
                    ((NPCCar) mObj).setUpStrategy(followStr);
                }
                else if(((NPCCar)mObj).returnCurrentStrategy() instanceof FollowThePlayerCarStrategy){
                    ((NPCCar) mObj).setUpStrategy(moveToPylon);
                }
            }

        }
        notifyObserver();
    }

    /**
     * Update currentFuelLevel in the collection
     *
     * @param volume update the level of fuel in the main collection
     */
    public void setNewFuelLevel(float volume) {
        currentFuelLevel = volume;
        notifyObserver();
    }

    /**
     * Update the number of the lastPylon reached.
     *
     * @param seqNum update the sequential number of the last pylon
     *               reached in the main collection
     */
    public void updateLastPylonReached(int seqNum) {
        lastPylonReached = seqNum;
        notifyObserver();
    }

    public void updateDamageLevel(float newLevel) {
        damageLevel = newLevel;
        if (damageLevel == 100) {
            deleteOneLife();
        }

        notifyObserver();
    }

    /**
     * Decrease the number of lives and restart the game.
     * If the number of lives is equal to zero, then quit the game.
     */
    public void deleteOneLife() {
        livesRemaining--;
        if (livesRemaining == 0) {
            System.out.println("You're out of lives!");
            quitTheGame();
        }

        notifyObserver();
        resetTheGame();


    }

    /**
     * Reset the game after losing one life.
     */
    private void resetTheGame() {
        System.out.println("You've lost one life, your game has been reset");
        //init layout
        theWorldVector = null;
        Pylon.resetSequenceGeneratorTo(THE_FIRST_PYLON);
        initLayout();
        notifyObserver();
    }


    /**
     * Exit game
     */
    public void quitTheGame() {
        System.out.println("Quitting the game...");
        System.exit(0);
    }


    @Override
    public Iterator getIterator() {
        return new GameObjectsIterator();
    }



    @Override
    public void addObserver(IObserver obs) {
        observers.add(obs);
    }

    @Override
    public void notifyObserver() {
        GameWorldProxy proxy = new GameWorldProxy(this);

        for (int i = 0; i < observers.size(); i++){
            observers.elementAt(i).update(proxy,null);

        }
    }



    public int getCurrentClockTime() {
        return currentClockTime;
    }

    public int getLivesRemaining() {
        return livesRemaining;
    }

    public int getLastPylonReached() {
        return lastPylonReached;
    }

    public float getCurrentFuelLevel() {
        return currentFuelLevel;
    }

    public float getDamageLevel() {
        return damageLevel;
    }

    public Car getCharacterCar() {
        return car;
    }





    private class GameObjectsIterator implements Iterator {

        /**
         * Implicit initialization
         * with 0 for index. Feature of the
         * Java, initialize everything with
         * 0, null...
         * </p>
         */
        int index;

        /**
         * Return the next object from the
         * primary collection of the game objects.
         * Implicitly set the pointer to the next object,
         * so no need in (i++)
         * @return
         * object from the primary collection of the game objects
         */
        @Override
        public Object getNext() {
            if(this.hasNext()){
                return theWorldVector.elementAt(index++);
            }
            return null;
        }

        @Override
        public boolean hasNext() {
            if(index < theWorldVector.size()){
                return true;
            }
            return false;
        }
    }
}

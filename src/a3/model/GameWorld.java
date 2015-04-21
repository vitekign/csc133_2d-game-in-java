/**
 * Created by Victor Ignatenkov on 2/9/15.
 */

package a3.model;
import a3.app.strategies.FollowThePlayerCarStrategy;
import a3.app.strategies.MoveTowardsPylonStrategy;
import a3.app.utilities.Services;
import a3.app.utilities.Sound;
import a3.controller.IGameWorld;
import a3.objects.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.Vector;




public class GameWorld implements Container , IObservable, IGameWorld, ActionListener {


    /*
     *  Code here to hold and manipulate world objects, handle
     *  observer registration, invoke observer callbacks, etc.
     */




    /**
     * Some basic constants
     */

    public static final int FRAMES_PER_SECOND = 50;
    public static final int MILISEC_PER_FRAME = 1000/FRAMES_PER_SECOND;


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
    private boolean isAfterThePause;
    private MouseEvent lastMouseEvent = null;
    private static int time;





    Car car;

    Vector<GameObject> theWorldVector;
    Vector<IObserver> observers = new Vector<>();
    public Vector<GameObject> gameObjectsToDelete;

    FollowThePlayerCarStrategy followStr;
    MoveTowardsPylonStrategy moveToPylon;


    Timer timer;
    GameObjectsFactory factory;

    private int timeCounter;

    private  Sound backgroundMusic;

    public void initLayout() {

        /**
         * Collection with all game objects
         */
        theWorldVector = new Vector<>();
        gameObjectsToDelete = new Vector<>();
        //turnOnSound(false);

        factory = new GameObjectsFactory();
        factory.setTargetForGameWorld(this);

        backgroundMusic = new Sound("troops.wav");
        if(isSound()){
            backgroundMusic.loop();
        }
        /**
         * Initialize oll needed objects on the fly
         * and add them to the collection.
         * If one life is lost - make a hard reset and
         * initialize all objects again with the same data.
         */
        theWorldVector.add(factory.makePylonWithLocation(new Location(50, 50)));
        theWorldVector.add(factory.makePylonWithLocation(new Location(300, 100)));
        theWorldVector.add(factory.makePylonWithLocation(new Location(400, 50)));
        theWorldVector.add(factory.makePylonWithLocation(new Location(600, 600)));

                Services.supplyServicesWithGameWorld(this);

        /**
         * Find the pylon with the sequence number of THE_FIRST_PYLON
         * and create a car based on the location information
         */
        Pylon firstPylon = null;



        /**
         * Add another required objects with random data
         */
        theWorldVector.add(factory.makeOilSlickWithRandomData());
        theWorldVector.add(factory.makeOilSlickWithRandomData());

        theWorldVector.add(factory.makeFuelCanWithRandomData());
        theWorldVector.add(factory.makeFuelCanWithRandomData());
        theWorldVector.add(factory.makeFuelCanWithRandomData());
        theWorldVector.add(factory.makeFuelCanWithRandomData());
        theWorldVector.add(factory.makeFuelCanWithRandomData());
        theWorldVector.add(factory.makeFuelCanWithRandomData());
        theWorldVector.add(factory.makeFuelCanWithRandomData());
        theWorldVector.add(factory.makeFuelCanWithRandomData());



        NPCCar npcCar1 = factory.makeNPCCarWithRandomData();
        NPCCar npcCar2 = factory.makeNPCCarWithRandomData();
        NPCCar npcCar3 = factory.makeNPCCarWithRandomData();



         followStr = new FollowThePlayerCarStrategy();
         moveToPylon = new MoveTowardsPylonStrategy();

        npcCar1.setUpStrategy(moveToPylon);
        npcCar2.setUpStrategy(followStr);
        npcCar3.setUpStrategy(moveToPylon);


        theWorldVector.add(npcCar1);
        theWorldVector.add(npcCar2);
        theWorldVector.add(npcCar3);

        Location locationToPlaceCar = null;
        try {
            firstPylon = Services.findPylonWithIndexNumber(THE_FIRST_PYLON);
            locationToPlaceCar = firstPylon.getLocation();
            car = factory.makeCarWithLocation(locationToPlaceCar);
            theWorldVector.add(car);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }


        theWorldVector.add(factory.makeBirdWithRandomData());
        theWorldVector.add(factory.makeBirdWithRandomData());



        if(timer == null)
            timer = new Timer(MILISEC_PER_FRAME, this);


        System.out.println("The number of pylons is: " + Pylon.getCount());
    }



    public void startTimer(){

        //Logic for STOP
        if(timer.isRunning()){
            timer.stop();
            stopBGMusic();
        } else {
            //Logic for PLAY
            gameObjectsToDelete.removeAllElements();
            lastMouseEvent = null;

            Iterator iter = this.getIterator();
            while(iter.hasNext()){
                GameObject temp = (GameObject)iter.getNext();
                if(temp instanceof ISelectable){
                    ((ISelectable) temp).setSelected(false);
                }
            }
            notifyObserver();
            timer.start();

            if(sound == true) {
                startBGMusic();
            }

        }
    }



    public int getTimer(){

        return time;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Iterator iter = this.getIterator();
        timeCounter++;

        while(iter.hasNext()){
            GameObject obj = (GameObject)iter.getNext();
            if(obj instanceof Moveable){
                  ((Moveable)obj).move(MILISEC_PER_FRAME /10);
                  doCollisionChecking();
                  deleteUnnecessaryOjbects();
            }
        }

        //
        if(this.getTime()%50 == 0 && this.getTime()!= 0){
            resetTime();
            time++;
            if(time%10 == 0 && time != 0){
                switchStrategies();
            }
        }


        notifyObserver();
    }

    public int getTime(){
       return timeCounter;
    }

    public void resetTime(){
        timeCounter = 0;
    }

    public void doCollisionChecking(){
        Iterator iter = this.getIterator();
        while(iter.hasNext()){
            ICollider currObject = (ICollider)iter.getNext();

            //Check if the FuelCan's timer equals to zero
            if(currObject instanceof FuelCan){
                if (((FuelCan)currObject).getTimer() <= 0){
                    gameObjectsToDelete.add((GameObject)currObject);
                }
            }

            Iterator iter2 = this.getIterator();
            while (iter2.hasNext()) {
                ICollider otherObject = (ICollider) iter2.getNext();
                if(otherObject != currObject){
                    //check for collision
                    if(currObject.collidesWith(otherObject)){
                            /*
                            Check if two collided objects are in a collision state.
                             */
                           if( !((GameObject)currObject).objectsCollidedWith.contains((GameObject)otherObject)){
                            currObject.handleCollision(otherObject);
                           }
                    } else {
                        /*
                        Clear the collision state.
                         */
                        if(((GameObject)currObject).objectsCollidedWith.contains((GameObject)otherObject)){
                            if(((GameObject)otherObject) instanceof OilSlick && (GameObject)currObject instanceof Car &&
                                    !((GameObject)currObject instanceof NPCCar)){
                                this.leaveOilSlick();
                                System.out.println("Leaving oil slick");
                            }
                            ((GameObject)currObject).objectsCollidedWith.remove((GameObject)otherObject);
                        }
                    }
                }

            }
        }
    }

    private void deleteUnnecessaryOjbects(){

            for (int i = 0; i < gameObjectsToDelete.size(); i++) {
                for (int j = 0; j < theWorldVector.size(); j++) {
                   // System.out.println("gameObjectsToDelete size is: " + gameObjectsToDelete.size());
                   // System.out.println("theWorldVector size is: " + theWorldVector.size());
                    if (gameObjectsToDelete.elementAt(i) == theWorldVector.elementAt(j)) {
                        System.out.println("Im inside");
                        theWorldVector.remove(j);
                    }
                }
            }

        gameObjectsToDelete.clear();
    }


    public void createNewPylon(int seqNumberOfPylon){


        Vector<Pylon> allPylons = Services.getAllPylons();
        boolean isPylonInGW = false;
        for(Pylon pylon : allPylons){
            if(pylon.getIndexNumber() == seqNumberOfPylon)
                isPylonInGW = true;
        }

        if(isPylonInGW) {
            JOptionPane.showMessageDialog(null, "Sorry, the pylon with this number already exists...");
        }
        else {
            allPylons.sort( (Pylon p1, Pylon p2) -> {
                if(p1.getIndexNumber() < p2.getIndexNumber()){
                    return -1;
                } else {
                    return 1;
                }
            });

            if (isItInPause()) {
                if(lastMouseEvent == null){
                    JOptionPane.showMessageDialog(null, "Please click on the map and then press Add Pylon");
                } else {
                    theWorldVector.add(new Pylon(new Location(lastMouseEvent.getX(), lastMouseEvent.getY()), 50, new Color(64, 64, 64), this, seqNumberOfPylon));
                    notifyObserver();
                    lastMouseEvent = null;
                }
            }
        }

    }

    public void createNewFuelCan(int input){
        if(isItInPause() ){
            if(lastMouseEvent != null){
                theWorldVector.add(new FuelCan(new Location(lastMouseEvent.getX(), lastMouseEvent.getY()), input, new Color(255, 25, 5),this));
                notifyObserver();
                lastMouseEvent = null;
            }
            else {
                JOptionPane.showMessageDialog(null, "Please click on the map and then press Add FuelCan");
            }
        }

    }

    public void createNewFuelCan(Location location, int fuelLevel){
        theWorldVector.add(new FuelCan(location, fuelLevel, new Color(255, 25, 5), this));
        notifyObserver();
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
                rand.nextFloat() * 50, Services.generateRandomColor(), this);
        theWorldVector.addElement(oilSlick);

        notifyObserver();
    }

    /**
     * Create new oil slick with location
     * @param loc
     */
    public void addOilSlickWithLocation(Location loc) {
        OilSlick oilSlick = new OilSlick(loc, rand.nextFloat() * 50,
                rand.nextFloat() * 50, Services.generateRandomColor(), this);
        theWorldVector.addElement(oilSlick);

        notifyObserver();
    }


    /**
     * Pretend that the car has collided with
     * another car
     */

    public void carCollideWithCar(NPCCar theOtherCar) {


        car.increaseDamageLevel(DAMAGE_FOR_COLLIDING_WITH_CARS);
        if(theOtherCar instanceof NPCCar){
            theOtherCar.increaseDamageLevel(DAMAGE_FOR_COLLIDING_WITH_CARS/10);
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
     * @param fuelCan
     */
    public void pickUpFuelCan(FuelCan fuelCan) {

        FuelCan temp = null;
        try {
           // temp = Services.findTheFirstFuelCan();
            car.pickUpFuelCan(fuelCan);
          //  theWorldVector.remove(temp);
            temp = new FuelCan(new Location(new Random().nextInt(800), new Random().nextInt(800)), (rand.nextFloat() * 50) + 1, Services.generateRandomColor(),this);
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
              ((Moveable) mObj).move(MILISEC_PER_FRAME);
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

    @Override
    public int getFramesPerSecond() {
        return MILISEC_PER_FRAME;
    }





    /*
     Confirms to the IGameWorld
     */
    @Override
    public void addToTheDeleteObjectsCollection(GameObject obj) {

        gameObjectsToDelete.addElement(obj);
    }

    @Override
    public void eraseFromTheDeleteObjectsCollections(GameObject obj) {
       if(gameObjectsToDelete.contains(obj)){
           gameObjectsToDelete.removeElement(obj);
       }
    }

    @Override
    public boolean isItInPause(){
        return !timer.isRunning();
    }

    /*
    ********************************
     */

    public void deleteSelectedElements(){
       // System.out.println("Delete All Objects");
        this.deleteUnnecessaryOjbects();
       // gameObjectsToDelete.removeAllElements();
        this.setLastMouseEvent(null);
        notifyObserver();
    }



    public void startBGMusic(){
        if(!isItInPause())
            backgroundMusic.loop();
    }

    public void stopBGMusic(){
        backgroundMusic.stop();
    }

    public void setSound(boolean value){
        sound = value;
        notifyObserver();
    }

    public void switchSound() {
        if(sound) {
            setSound(false);
            stopBGMusic();

        } else {
            setSound(true);
            startBGMusic();
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


    public MouseEvent getLastMouseEvent() {
        return lastMouseEvent;
    }

    public void setLastMouseEvent(MouseEvent lastMouseEvent) {
        this.lastMouseEvent = lastMouseEvent;
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

    public void playPause() {
        startTimer();

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

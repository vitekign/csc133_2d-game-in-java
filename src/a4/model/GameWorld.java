/*Created by Victor Ignatenkov on 2/9/15.*/
package a4.model;
import a4.app.strategies.FollowThePlayerCarStrategy;
import a4.app.strategies.MoveTowardsPylonStrategy;
import a4.app.utilities.Services;
import a4.app.utilities.Sound;
import a4.controller.IGameWorld;
import a4.objects.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Optional;
import java.util.Random;
import java.util.Vector;

    /*   Code here to hold and manipulate world objects, handle    *
     *   observer registration, invoke observer callbacks, etc.    *
     *   Some basic constants                                      */

public class GameWorld implements Container, IObservable, IGameWorld, ActionListener {

    public static final int FRAMES_PER_SECOND = 50;
    public static final int MILISEC_PER_FRAME = 1000/FRAMES_PER_SECOND;

    public static final int GLOBAL_WIDTH = 1000;
    public static final int GLOBAL_HEIGHT = 800;

    public static final int THE_FIRST_PYLON = 1;
    public final static float DAMAGE_FOR_COLLIDING_WITH_CARS = 5;
    public static final int NUMBER_OF_LIVES = 3;

    private int currentClockTime;
    private int livesRemaining = NUMBER_OF_LIVES;
    private int lastPylonReached = THE_FIRST_PYLON;
    private float currentFuelLevel;
    private float damageLevel;
    private boolean sound;
    private MouseEvent lastMouseEvent = null;
    private static int time;
    private int timeCounter;


    Car car;
    Timer timer;
    Pylon firstPylonInTheGameWorld;
    ShockWave myShockWave;
    GameObjectsFactory factory;
    Random rand = new Random();

    Vector<GameObject> gameObjects;
    Vector<IObserver> observers = new Vector<>();
    public Vector<GameObject> gameObjectsToBeRemoved;

    FollowThePlayerCarStrategy followPlayer;
    MoveTowardsPylonStrategy moveFromPylonToPylon;

    private  Sound backgroundMusic =  new Sound("troops.wav");
    Sound lostLife = new Sound("losingLife.wav");

    public void initLayout() {
        /*Collection with all game object */
        gameObjects = new Vector<>();
        gameObjectsToBeRemoved = new Vector<>();
        factory = new GameObjectsFactory();
        factory.setTargetForGameWorld(this);

        /*   Initialize oll needed objects on the fly           *
         *   and add them to the collection.                    *
         *   If one life is lost - make a hard reset and        *
         *   initialize all objects again with the same data.   */

        gameObjects.add(factory.makePylonWithLocation(new Location(200, 200)));
        gameObjects.add(factory.makePylonWithLocation(new Location(300, 100)));
        gameObjects.add(factory.makePylonWithLocation(new Location(400, 50)));
        gameObjects.add(factory.makePylonWithLocation(new Location(600, 600)));

        Services.supplyServicesWithGameWorld(this);

        /*Add another required objects with random data */
        gameObjects.add(factory.makeOilSlickWithRandomData());
        gameObjects.add(factory.makeOilSlickWithRandomData());

        createFuelCansAndAddToGameWorld(factory, 7);

        NPCCar npcCar1 = factory.makeNPCCarWithRandomData();
        NPCCar npcCar2 = factory.makeNPCCarWithRandomData();
        NPCCar npcCar3 = factory.makeNPCCarWithRandomData();

         followPlayer = new FollowThePlayerCarStrategy();
         moveFromPylonToPylon = new MoveTowardsPylonStrategy();

        npcCar1.setUpStrategy(moveFromPylonToPylon);
        npcCar2.setUpStrategy(followPlayer);
        npcCar3.setUpStrategy(moveFromPylonToPylon);

        gameObjects.add(npcCar1);
        gameObjects.add(npcCar2);
        gameObjects.add(npcCar3);

        gameObjects.add(factory.makeBirdWithRandomData());
        gameObjects.add(factory.makeBirdWithRandomData());

        /* Place the car at the location of the first pylon */
        Location locationToPlaceCar;

        Optional<Pylon> locationOfFirstPylon = Services.findPylonWithIndexNumber(THE_FIRST_PYLON);
        if(locationOfFirstPylon.isPresent()){
            firstPylonInTheGameWorld = locationOfFirstPylon.get();
            locationToPlaceCar = firstPylonInTheGameWorld.getLocation();
            car = factory.makeCarWithLocation(locationToPlaceCar);
        } else {
            car = factory.makeCarWithLocation(new Location(300,300));
        }
        gameObjects.add(car);


        if(timer == null)
            timer = new Timer(MILISEC_PER_FRAME, this);


    }
    private void createFuelCansAndAddToGameWorld(GameObjectsFactory factory,
                                                        int numberOfCans) {
        while(numberOfCans > 0) {
            gameObjects.add(factory.makeFuelCanWithRandomData());
            numberOfCans--;
        }
    }


    /* Start timer */
    public void startTimer(){
        Iterator iterator = this.getIterator();
        //Logic for STOP
        if(timer.isRunning()){
            timer.stop();
            stopBGMusic();
        } else {
            //Logic for PLAY
            gameObjectsToBeRemoved.removeAllElements();
            lastMouseEvent = null;

            while(iterator.hasNext()){
                GameObject temp = (GameObject)iterator.getNext();
                if(temp instanceof ISelectable){
                    ((ISelectable) temp).setSelected(false);
                }
            }
            notifyObserver();
            timer.start();

            if(sound) {
                startBGMusic();
            }
        }
    }


    public int getTimer(){
        return time;
    }

    /* Listen to each tick */
    @Override
    public void actionPerformed(ActionEvent e) {
        Iterator iterator = this.getIterator();
        timeCounter++;

        while(iterator.hasNext()){
            GameObject obj = (GameObject)iterator.getNext();
            if(obj instanceof Moveable){
                  ((Moveable)obj).move(MILISEC_PER_FRAME /10);
                  doCollisionChecking();
                  doShockWaveChecking();
                  removeUnnecessaryObjects();
            }
        }
        if(this.getTime()%50 == 0 && this.getTime()!= 0){
            resetTime();
            time++;
            if(time%10 == 0 && time != 0){
                switchStrategies();
            }
        }
        notifyObserver();
    }


    public void doShockWaveChecking(){
        Iterator iterator = this.getIterator();
        while(iterator.hasNext()) {

            GameObject currObject = (GameObject) iterator.getNext();
            if(currObject instanceof ShockWave){
                if(Math.abs(currObject.getX()) > 1000 ||
                        Math.abs(currObject.getY())> 1000){
                    gameObjectsToBeRemoved.addElement(currObject);
                }
            }
        }
    }

    public void createNewShockWave(Location location){
        myShockWave = new ShockWave(location, 60,40, Color.BLACK, this);
        gameObjects.add(myShockWave);
    }

    /* Get time counter */
    public int getTime(){
       return timeCounter;
    }

    /* Set time to zero */
    public void resetTime(){
        timeCounter = 0;
    }

    /* Check if two game objects just collided */
    public void doCollisionChecking(){
        Iterator firstIterator = this.getIterator();
        while(firstIterator.hasNext()){
            ICollider currObject = (ICollider)firstIterator.getNext();

            /* Check if the FuelCan's timer equals to zero */
            if(currObject instanceof FuelCan){
                if (((FuelCan)currObject).getTimer() <= 0){
                    gameObjectsToBeRemoved.add((GameObject)currObject);
                }
            }
            Iterator secondIterator = this.getIterator();
            while (secondIterator.hasNext()) {
                ICollider anotherObject = (ICollider) secondIterator.getNext();
                if(anotherObject != currObject){
                    //check for collision
                    if(currObject.didCollideWithAnotherObject(anotherObject)){
                        /* Check if two collided objects are in a collision state */
                        if( !((GameObject)currObject).objectsCollidedWith.contains(anotherObject)){
                        currObject.handleCollision(anotherObject);}
                    } else {
                        /* Clear the collision state */
                        if(((GameObject)currObject).objectsCollidedWith.contains(anotherObject)){
                            if((anotherObject) instanceof OilSlick && currObject instanceof Car &&
                                    !(currObject instanceof NPCCar)){
                                this.leaveOilSlick();
                                System.out.println("Leaving oil slick");
                            }
                            ((GameObject)currObject).objectsCollidedWith.remove(anotherObject);
                        }
                    }
                }
            }
        }
    }

    /* Remove all objects which have been thrown away */
    private void removeUnnecessaryObjects(){
            for (int i = 0; i < gameObjectsToBeRemoved.size(); i++) {
                for (int j = 0; j < gameObjects.size(); j++) {
                    if (gameObjectsToBeRemoved.elementAt(i) == gameObjects.elementAt(j)) {
                        gameObjects.remove(j);
                    }
                }
            }
        gameObjectsToBeRemoved.clear();
    }


    /* Create a new Pylon and add it to the game */
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

                    Point2D mouseWorldLoc =  Services.applyInverseAndGetPoint(lastMouseEvent);

                    gameObjects.add(factory.makePylonWithLocationAndSequenceNumber(new Location((int)mouseWorldLoc.getX(), (int)mouseWorldLoc.getY()), seqNumberOfPylon));
                    notifyObserver();
                    lastMouseEvent = null;
                }
            }
        }

    }

    /* Create new fuel can and add it to the game */
    public void createNewFuelCan(int input){
        if(isItInPause() ){

            if(lastMouseEvent != null){

                Point2D mouseWorldLoc =  Services.applyInverseAndGetPoint(lastMouseEvent);

                gameObjects.add(factory.makeFuelCanWithLocation(new Location((int) mouseWorldLoc.getX(), (int) mouseWorldLoc.getY()), input));
                notifyObserver();
                lastMouseEvent = null;
            }
            else {
                JOptionPane.showMessageDialog(null, "Please click on the map and then press Add FuelCan");
            }
        }

    }

    /**
     * Additional methods to manipulate world
     * objects and related game date
     */

    public void accelerateTheCar() {
        car.accelerateTheCar(2.5f);
        notifyObserver();
    }

    public void showDownTheCar() {
        car.applyBreaks(2.5f);
        notifyObserver();
    }

    public void rotateSteeringToLeft() {
        car.changeCurrentHeadingToTheLeft();
        notifyObserver();

    }

    public void rotateSteeringToRight() {
        car.changeCurrentHeadingToTheRight();
        notifyObserver();
    }

    public void addOilSlick() {
        OilSlick oilSlick = new OilSlick(new Location(rand.nextFloat() * GLOBAL_WIDTH,
                rand.nextFloat() * GLOBAL_HEIGHT), rand.nextFloat() * 50,
                rand.nextFloat() * 50, Color.black, this);
        gameObjects.addElement(oilSlick);

        notifyObserver();
    }

    /* Create new oil slick with location */
    public void addOilSlickWithLocation(Location loc) {
        OilSlick oilSlick = factory.makeOilSlickWithLocatin(loc);
        gameObjects.addElement(oilSlick);
        notifyObserver();
    }

    public void carCollideWithCar(NPCCar theOtherCar) {
        car.increaseDamageLevelAndUpdateGameWorld(DAMAGE_FOR_COLLIDING_WITH_CARS);
        if(theOtherCar instanceof NPCCar){
            theOtherCar.increaseDamageLevelAndUpdateGameWorld(DAMAGE_FOR_COLLIDING_WITH_CARS/10);
        }
        notifyObserver();
    }


    public void carCollideWithPylon(int pylon) {
        car.collideWithPylonAndUpdateGameWorld(pylon);

        notifyObserver();
    }


    public void pickUpFuelCan() {
        FuelCan temp;
        try {
            temp = Services.findTheFirstFuelCan();
            car.pickUpFuelCan(temp);
            addToTheDeleteObjectsCollection(temp);


            temp = new FuelCan(new Location(new Random().nextInt(800), new Random().nextInt(800)), (rand.nextFloat() * 50) + 1, Services.generateRandomColor(),this);
            gameObjects.add(temp);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        notifyObserver();
    }


    /**
     * Tell the car to accept the increase in the fuel level and
     * then create a new fuel can.
     * @param fuelCan
     */
    public void pickUpFuelCan(FuelCan fuelCan) {

        FuelCan temp;
        try {
            car.pickUpFuelCan(fuelCan);

            temp = new FuelCan(new Location(new Random().nextInt(800), new Random().nextInt(800)), (rand.nextFloat() * 50) + 1, Services.generateRandomColor(),this);
            gameObjects.add(temp);
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
        car.increaseDamageLevelAndUpdateGameWorld(car.getAmountOfDamageForCollidingWithBirds());

        notifyObserver();
    }


    /**
     * Pretend that the car has entered
     * an oilSlick
     */


    public void enterOilSlick() {
        car.enterOilSlick();
        notifyObserver();
    }

    /**
     * Pretend that the car has left the
     * oilSlick
     */
    public void leaveOilSlick() {
        car.exitOilSlick();

        notifyObserver();
    }


    public void generateNewColors() {
        Iterator iterator = this.getIterator();

        while(iterator.hasNext()) {
            GameObject mObj = (GameObject) iterator.getNext();
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

    /**
     * Add objects, which are to be deleted, to the collection.
     * @param obj
     */
    @Override
    public void addToTheDeleteObjectsCollection(GameObject obj) {

        gameObjectsToBeRemoved.addElement(obj);
    }

    /**
     * Erase object from the collection
     * @param obj
     */
    @Override
    public void eraseFromTheDeleteObjectsCollections(GameObject obj) {
       if(gameObjectsToBeRemoved.contains(obj)){
           gameObjectsToBeRemoved.removeElement(obj);
       }
    }

    /**
     * Is the game in a pause mode.
     * @return
     */
    @Override
    public boolean isItInPause(){
        return !timer.isRunning();
    }


    /**
     * Part of delete unnecessary objects logic.
     */
    public void deleteSelectedElements(){
       // System.out.println("Delete All Objects");

        this.removeUnnecessaryObjects();
       // gameObjectsToBeRemoved.removeAllElements();
        this.setLastMouseEvent(null);
        notifyObserver();
    }

    public void startBGMusic(){
        if(!isItInPause())
            backgroundMusic.loop();
    }

    /**
     * Stop background music.
     */
    public void stopBGMusic(){
        backgroundMusic.stop();
    }

    /**
     * Part of turn on/off music logic.
     * @param value
     */
    public void setSound(boolean value){
        sound = value;
        notifyObserver();
    }

    /**
     * Part of turn on/off music logic.
     */
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

    /**
     * Switch to another strategy.
     */
    public void switchStrategies(){
        Iterator iter = this.getIterator();

        while(iter.hasNext()) {
            GameObject mObj = (GameObject) iter.getNext();
            if(mObj instanceof NPCCar){
                if(((NPCCar) mObj).returnCurrentStrategy() instanceof MoveTowardsPylonStrategy)
                {
                    ((NPCCar) mObj).setUpStrategy(followPlayer);
                }
                else if(((NPCCar)mObj).returnCurrentStrategy() instanceof FollowThePlayerCarStrategy){
                    ((NPCCar) mObj).setUpStrategy(moveFromPylonToPylon);
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

        if(isSound())
            lostLife.play();

        notifyObserver();
        resetTheGame();


    }

    /**
     * Reset the game after losing one life.
     */
    private void resetTheGame() {
        System.out.println("You've lost one life, your game has been reset");
        //init layout
        gameObjects = null;
        Pylon.resetSequenceGeneratorTo(THE_FIRST_PYLON);
        initLayout();
        car.setLastHighestPylonReachedToZero();
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
                return gameObjects.elementAt(index++);
            }
            return null;
        }

        @Override
        public boolean hasNext() {
            if(index < gameObjects.size()){
                return true;
            }
            return false;
        }
    }
}

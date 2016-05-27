/*Created by Victor Ignatenkov on 2/9/15.*/
package a4.model;
import a4.app.strategies.FollowThePlayerCarStrategy;
import a4.app.strategies.MoveTowardsPylonStrategy;
import a4.app.utilities.Utilities;
import a4.app.utilities.Sound;
import a4.controller.IGameWorld;
import a4.objects.*;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

    /*   Code here to hold and manipulate world objects, handle    *
     *   observer registration, invoke observer callbacks, etc.    *
     *   Some basic constants                                      */

public class GameWorld implements Container, IObservable, IGameWorld, ActionListener {

    public static final int FRAMES_PER_SECOND = 50;
    public static final int MILISEC_PER_FRAME = 1000 / FRAMES_PER_SECOND;

    public static final int GLOBAL_WIDTH = 1000;
    public static final int GLOBAL_HEIGHT = 800;

    public static final int THE_FIRST_PYLON = 1;
    public final static float DAMAGE_FOR_COLLIDING_WITH_CARS = 5;
    public static final int NUMBER_OF_LIVES = 3;

    public static boolean VK_S = false;
    public static boolean VK_F = false;
    public static boolean VK_LEFT = false;
    public static boolean VK_RIGHT = false;
    public static boolean VK_UP = false;

    private int currentClockTime;
    private int livesRemaining = NUMBER_OF_LIVES;
    private int lastPylonReached = THE_FIRST_PYLON;
    private float currentFuelLevel;
    private float damageLevel;
    private boolean sound;
    private MouseEvent lastMouseEvent = null;
    private static int seconds;
    private int timeCounterInTicks;


    Car car;
    Timer timer;
    Pylon firstPylonInTheGameWorld;
    ShockWave myShockWave;
    GameObjectsFactory factory;
    Random rand = new Random();

    List<GameObject> gameObjects;
    List<IObserver> observers = new ArrayList<>();
    public Vector<GameObject> gameObjectsToBeRemoved;

    FollowThePlayerCarStrategy followPlayer;
    MoveTowardsPylonStrategy moveFromPylonToPylon;

    private Sound backgroundMusic = new Sound("troops.wav");
    Sound lostLife = new Sound("losingLife.wav");

    public void initLayout() {
        /*Collection with all game object */
        gameObjects = new ArrayList<>();
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

        Utilities.supplyServicesWithGameWorld(this);

        /*Add another required objects with random data */
        gameObjects.add(factory.makeOilSlickWithRandomData());
        gameObjects.add(factory.makeOilSlickWithRandomData());

        createFuelCansDynamicallyAndAttachToGameWorld(factory, 7);

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

        Optional<Pylon> locationOfFirstPylon = Utilities.findPylonWithIndexNumber(THE_FIRST_PYLON);
        if (locationOfFirstPylon.isPresent()) {
            firstPylonInTheGameWorld = locationOfFirstPylon.get();
            locationToPlaceCar = firstPylonInTheGameWorld.getLocation();
            car = factory.makeCarWithLocation(locationToPlaceCar);
        } else {
            car = factory.makeCarWithLocation(new Location(600, 600));
        }
        gameObjects.add(car);

        if (timer == null)
            timer = new Timer(MILISEC_PER_FRAME, this);
    }

    public void playPause() {
        startTimer();
    }

    /* Start timer */
    public void startTimer() {
        Iterator iterator = this.getIterator();
        //Logic for STOP
        if (timer.isRunning()) {
            timer.stop();
            stopBGMusic();
        } else {
            //Logic for PLAY
            gameObjectsToBeRemoved.removeAllElements();
            lastMouseEvent = null;

            while (iterator.hasNext()) {
                GameObject temp = (GameObject) iterator.getNext();
                if (temp instanceof ISelectable) {
                    ((ISelectable) temp).setSelected(false);
                }
            }
            notifyObserver();
            timer.start();

            if (sound) {
                startBGMusic();
            }
        }
    }

    /* Listen to each tick */
    @Override
    public void actionPerformed(ActionEvent e) {
        Iterator iterator = this.getIterator();
        timeCounterInTicks++;

        while (iterator.hasNext()) {
            GameObject obj = (GameObject) iterator.getNext();
            if (obj instanceof Moveable) {
                ((Moveable) obj).move(MILISEC_PER_FRAME / 10);
                doCollisionChecking();
                doShockWaveChecking();
                removeUnnecessaryObjects();
            }
        }
        if (this.getTimeInTicks() % 50 == 0 && this.getTimeInTicks() != 0) {  /* 50 * 20 = 1000 (1 sec) */
            resetTimeCounterInTermsOfTicks();
            seconds++;
            if (seconds % 10 == 0 && seconds != 0) {
                switchStrategies();
            }
        }
        notifyObserver();
    }

    public int getTimeInSeconds() {
        return seconds;
    }

    public int getTimeInTicks() {
        return timeCounterInTicks;
    }

    public void resetTimeCounterInTermsOfTicks() {
        timeCounterInTicks = 0;
    }

    public void doShockWaveChecking() {
        Iterator iterator = this.getIterator();
        while (iterator.hasNext()) {

            GameObject currObject = (GameObject) iterator.getNext();
            if (currObject instanceof ShockWave) {
                if (Math.abs(currObject.getX()) > 1000 ||
                        Math.abs(currObject.getY()) > 1000) {
                    gameObjectsToBeRemoved.addElement(currObject);
                }
            }
        }
    }

    public void createNewShockWave(Location location) {
        for (int i = 0; i < 5; i++) {
            myShockWave = new ShockWave(location, 60, 40, Color.BLACK, this);
            gameObjects.add(myShockWave);
        }
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
                        if( !((GameObject)currObject).getObjectsCollidedWith().contains(anotherObject)){
                        currObject.handleCollision(anotherObject);}
                    } else {
                        /* Clear the collision state */
                        if(((GameObject)currObject).getObjectsCollidedWith().contains(anotherObject)){
                            if((anotherObject) instanceof OilSlick && currObject instanceof Car &&
                                    !(currObject instanceof NPCCar)){
                                this.leaveOilSlick();
                            }
                            ((GameObject)currObject).getObjectsCollidedWith().remove(anotherObject);
                        }
                    }
                }
            }
        }
    }

    /* Remove all objects which have been thrown away */
    private void removeUnnecessaryObjects() {
        for (int i = 0; i < gameObjectsToBeRemoved.size(); i++) {
            for (int j = 0; j < gameObjects.size(); j++) {
                if (gameObjectsToBeRemoved.elementAt(i) == gameObjects.get(j)) {
                    gameObjects.remove(j);
                }
            }
        }
        gameObjectsToBeRemoved.clear();
    }

    /* Create a new Pylon and add it to the game */
    public void createNewPylon(int seqNumberOfPylon) {
        List<Pylon> allPylons = Utilities.getAllPylons();
        boolean isPylonInGW = false;
        for (Pylon pylon : allPylons) {
            if (pylon.getIndexNumber() == seqNumberOfPylon)
                isPylonInGW = true;
        }
        if (isPylonInGW) {
            JOptionPane.showMessageDialog(null, "Sorry, the pylon with this number already exists...");
        } else {
            allPylons.sort((p1, p2) -> p1.getIndexNumber() < p2.getIndexNumber() ? -1 : 1);
            if (isItInPause()) {
                if (lastMouseEvent == null) {
                    JOptionPane.showMessageDialog(null, "Please click on the map and then press Add Pylon");
                } else {
                    Point2D mouseWorldLoc = Utilities.applyInverseAndGetPoint(lastMouseEvent);

                    gameObjects.add(factory.makePylonWithLocationAndSequenceNumber(new Location((int) mouseWorldLoc.getX(), (int) mouseWorldLoc.getY()), seqNumberOfPylon));

                    lastMouseEvent = null;
                }
            }
        }
    }

    private void addGameObjectToGameWorld(GameObject objectToBeAdded) {
        gameObjects.add(objectToBeAdded);
    }

    public boolean doesObjectCollidesWithAnother(GameObject gameObject) {
        ICollider objectToCheck = (ICollider) gameObject;
        Iterator secondIterator = this.getIterator();
        while (secondIterator.hasNext()) {
            ICollider anotherObject = (ICollider) secondIterator.getNext();
            if (anotherObject != objectToCheck) {
                //check for collision
                if (objectToCheck.didCollideWithAnotherObject(anotherObject))
                    return true;
            }
        }
        return false;
    }


    private void createFuelCansDynamicallyAndAttachToGameWorld(GameObjectsFactory factory, int numberOfCans) {
        while(numberOfCans > 0) {
            GameObject objectToAdd = factory.makeFuelCanWithRandomData();
            if(!doesObjectCollidesWithAnother(objectToAdd)) {
                addGameObjectToGameWorld(factory.makeFuelCanWithRandomData());
                numberOfCans--;
            }
        }
    }

    /* Create new fuel can and add it to the game */
    public void createNewFuelCanFromTheMenu(int input){
        if(isItInPause() ){

            if(lastMouseEvent != null){

                Point2D mouseWorldLoc =  Utilities.applyInverseAndGetPoint(lastMouseEvent);

                gameObjects.add(factory.makeFuelCanWithLocationAndSize(new Location((int) mouseWorldLoc.getX(), (int) mouseWorldLoc.getY()), input));
                
                lastMouseEvent = null;
            }
            else {
                JOptionPane.showMessageDialog(null, "Please click on the map and then press Add FuelCan");
            }
        }
    }

    /*  Additional methods to manipulate world
     *  objects and related game date          */

    public void accelerateTheCar() {
        car.accelerateTheCar(2.5f);
    }

    public void showDownTheCar() {
        car.applyBreaks(2.5f);
    }

    public void rotateSteeringToLeft() {
        car.changeCurrentHeadingToTheLeft();
    }

    public void rotateSteeringToRight() {
        car.changeCurrentHeadingToTheRight();
    }

    @Override
    public void rotateMuzzleToLeft(){
        car.changeCurrentDirectionOfMuzzleToLeft();
    }

    @Override
    public void rotateMuzzleToRight(){
        car.changeCurrentDirectionOfMuzzleToRight();
    }

    public void
    launchMissile() {
        Missile missile = new Missile(car.getLocationOfMuzzle(), car.getLocationOfCar(), car.getHeading(), car.getHeadingOfMuzzle(),1 , this);
        addGameObjectToGameWorld(missile);
    }

    public void addOilSlick() {
        OilSlick oilSlick = new OilSlick(new Location(rand.nextFloat() * GLOBAL_WIDTH,
                rand.nextFloat() * GLOBAL_HEIGHT), rand.nextFloat() * 50,
                rand.nextFloat() * 50, Color.black, this);
        gameObjects.add(oilSlick);
    }

    /* Create new oil slick with location */
    public void addOilSlickWithLocation(Location loc) {
        OilSlick oilSlick = factory.makeOilSlickWithLocation(loc);
        gameObjects.add(oilSlick);
    }

    public void carCollideWithCar(NPCCar theOtherCar) {
        car.increaseDamageLevelAndUpdateGameWorld(DAMAGE_FOR_COLLIDING_WITH_CARS);
        if(theOtherCar != null){
            theOtherCar.increaseDamageLevelAndUpdateGameWorld(DAMAGE_FOR_COLLIDING_WITH_CARS/10);
        }
    }

    public void carCollideWithPylon(int pylon) {
        car.collideWithPylonAndUpdateGameWorld(pylon);
    }

    //Got confused with this method
    public void pickUpFuelCan() {
        Optional<FuelCan> temp;
        temp = Utilities.findTheFirstFuelCan();
        if(temp.isPresent()){
            car.pickUpFuelCan(temp.get());
            addToTheDeleteObjectsCollection(temp.get());

            FuelCan newFuelCan = new FuelCan(new Location(new Random().nextInt(800), new Random().nextInt(800)), (rand.nextFloat() * 50) + 1, Utilities.generateRandomColor(),this);
            gameObjects.add(newFuelCan);
        } else {

        }
    }


    /* Tell the car to accept the increase in the fuel level and
     * then create a new fuel can.*/
    public void pickUpFuelCan(FuelCan fuelCan) {
        FuelCan temp;
        try {
            car.pickUpFuelCan(fuelCan);

            temp = new FuelCan(new Location(new Random().nextInt(800), new Random().nextInt(800)), (rand.nextFloat() * 50) + 1, Utilities.generateRandomColor(),this);
            gameObjects.add(temp);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /* Pretend that a bird has flown over the car,
     * thus making the car gain some damage */
    public void birdFlyOver() {
        car.increaseDamageLevelAndUpdateGameWorld(Car.getAmountOfDamageForCollidingWithBirds());
    }


    /* Pretend that the car has entered
     * an oilSlick */
    public void enterOilSlick() {
        car.enterOilSlick();
    }

    /* Pretend that the car has left the
     * oilSlick */
    public void leaveOilSlick() {
        car.exitOilSlick();
    }


    public void generateNewColors() {
        Iterator iterator = this.getIterator();

        while(iterator.hasNext()) {
            GameObject mObj = (GameObject) iterator.getNext();
            mObj.changeColor(Utilities.generateRandomColor());
        }
    }


    public boolean isSound() {
        return sound;
    }

    @Override
    public int getFramesPerSecond() {
        return MILISEC_PER_FRAME;
    }

    /* Confirms to the IGameWorld  */

    /* Add objects, which are to be deleted, to the collection */
    @Override
    public void addToTheDeleteObjectsCollection(GameObject obj) {

        gameObjectsToBeRemoved.addElement(obj);
    }

    /* Erase object from the collection */
    @Override
    public void eraseFromTheDeleteObjectsCollections(GameObject obj) {
       if(gameObjectsToBeRemoved.contains(obj)){
           gameObjectsToBeRemoved.removeElement(obj);
       }
    }

    /* Is the game in a pause mode. */
    @Override
    public boolean isItInPause(){
        return !timer.isRunning();
    }

    public void deleteSelectedElements(){
        this.removeUnnecessaryObjects();
        this.setLastMouseEvent(null);
    }

    public void startBGMusic(){
        if(!isItInPause())
            backgroundMusic.loop();
    }


    public void stopBGMusic(){
        backgroundMusic.stop();
    }

    /* Part of turn on/off music functionality */
    public void setSound(boolean value){
        sound = value;
        
    }

    /* Part of turn on/off music functionality */
    public void switchSound() {
        if(sound) {
            setSound(false);
            stopBGMusic();

        } else {
            setSound(true);
            startBGMusic();
        }
    }

    /* Switch to another strategy */
    public void switchStrategies(){
        Iterator iterator = this.getIterator();

        while(iterator.hasNext()) {
            GameObject mObj = (GameObject) iterator.getNext();
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
    }

    public void setNewFuelLevel(float volume) {
        currentFuelLevel = volume;
    }

    public void updateLastPylonReached(int seqNum) {
        lastPylonReached = seqNum;
        
    }

    public void updateDamageLevel(float newLevel) {
        damageLevel = newLevel;
        if (damageLevel == 100) {
            deleteOneLifeAndRestartLevel();
        }
    }

    /* Decrease one life and restart the level.
     * If the number of lives is equal to zero, then quit the game */
    public void deleteOneLifeAndRestartLevel() {
        livesRemaining--;
        if (livesRemaining == 0) {
            quitTheGame();
        }
        if(isSound())
            lostLife.play();
        
        resetLevel();
    }

    /* Reset level after losing one life. */
    private void resetLevel() {
        gameObjects = null;
        Pylon.resetSequenceGeneratorTo(THE_FIRST_PYLON);
        initLayout();
        car.setLastHighestPylonReachedToZero();
    }

    public void quitTheGame() {
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
        for (IObserver observer : observers) {
            observer.update(proxy, null);

        }
        if(getTimeInTicks() % 2 == 0 && getTimeInTicks() != 0) {
            if (VK_S) {
                car.changeCurrentDirectionOfMuzzleToLeft();
            }
            if (VK_F) {
                car.changeCurrentDirectionOfMuzzleToRight();
            }
            if (VK_LEFT) {
                car.changeCurrentHeadingToTheLeft();
            }
            if (VK_RIGHT) {
                car.changeCurrentHeadingToTheRight();
            }
            if (VK_UP) {
                car.accelerateTheCar((float) 0.3);
            } else {
                if(car.getSpeed() > 0)
                    car.applyBreaks((float)0.4);
            }
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


    private class GameObjectsIterator implements Iterator {
        int index;

        @Override
        public Object getNext() {
            if(this.hasNext()){
                return gameObjects.get(index++);
            }
            return null;
        }

        @Override
        public boolean hasNext() {
            return (index < gameObjects.size());
        }
    }
}

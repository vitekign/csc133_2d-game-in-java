/* Created by Victor Ignatenkov on 3/30/15 */

package a4.objects;
import a4.model.GameWorld;
import java.awt.*;
import java.util.Random;


public class GameObjectsFactory {

    GameWorld gw;
    Random rand;

    public void setTargetForGameWorld(GameWorld gw){
        this.gw = gw;
        orderObjectsWithZIndex();
    }

    /* Place objects in the right order,
     * so they are drawn in the right order */
    public void orderObjectsWithZIndex(){
        Pylon.zIndex    = 1;
        OilSlick.zIndex = 0;
        FuelCan.zIndex  = 2;
        NPCCar.zIndex   = 3;
        Bird.zIndex     = 5;
        Car.zIndex      = 4;
        ShockWave.zIndex= 6;
    }


    public Pylon makePylonWithLocation(Location location){
        return new Pylon(new Location(location.getX(), location.getY()), 50, new Color(64, 64, 64),gw);
    }

    public Pylon makePylonWithLocationAndSequenceNumber(Location location, int seqNumber){
        return new Pylon(location, 50, new Color(64, 64, 64), gw, seqNumber);
    }


    public OilSlick makeOilSlickWithRandomData(){
    rand = new Random();
        return new OilSlick(new Location(rand.nextFloat() * gw.GLOBAL_WIDTH,
            rand.nextFloat() * gw.GLOBAL_HEIGHT),
            rand.nextFloat() * 100,
            rand.nextFloat() * 100,
            new Color(0, 0, 0), gw);
    }

    public FuelCan makeFuelCanWithRandomData(){

        return new FuelCan(new Location(new Random().nextFloat()*800, new Random().nextFloat()*800),
                30, new Color(255, 25, 5),gw);
    }

    public FuelCan makeFuelCanWithLocationAndSize(Location location, int size){
        return  new FuelCan(location, size, new Color(255, 25, 5),gw);
    }

    public NPCCar makeNPCCarWithRandomData(){
        return new NPCCar(new Location(new Random().nextInt(100),new Random().nextInt(600)), gw, Color.BLACK,
                25,25,0,100,100,1,0,200,0);
    }

    public Bird makeBirdWithRandomData(){
        return  new Bird(new Location(300, //new Random().nextFloat() * gw.GLOBAL_WIDTH
                300),
                new Random().nextFloat() * 30 + 20,
                new Random().nextFloat() * 360,
                new Random().nextFloat() * 1, new Color(255, 0, 255),gw);
    }


    public Car makeCarWithLocation(Location location){
        return new Car(location, gw, new Color(240, 0, 0));
    }


    public OilSlick makeOilSlickWithLocation(Location location) {
        return new OilSlick(location, rand.nextFloat() * 50,
                rand.nextFloat() * 50, Color.BLACK, gw);
    }
}























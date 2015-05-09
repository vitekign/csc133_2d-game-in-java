package a4.objects;
import a4.app.utilities.Services;
import a4.model.GameWorld;


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Random;
import java.util.Vector;

/**
 * Created by Victor Ignatenkov on 5/8/15.
 */

public class ShockWave extends Moveable implements IDrawable,ICollider {


    public static int zIndex;
    public GameWorld gw;

    private int length;
    private int height;

    /**
     * Default ability of all objects to have their color changed.
     *
     * @param color
     */
    public ShockWave(Location location, float heading, float speed, Color color, GameWorld gw) {
        super(color);


        this.setX(location.getX());
        this.setY(location.getY());


        this.gw = gw;

        this.heading = new Random().nextFloat()*360;
        this.speed = (new Random().nextFloat()*3)+2;
        this.color = Services.generateRandomColor();



        length = (int) (50 + new Random().nextFloat()*10);
        height = (int) (50 + new Random().nextFloat()*10);

        objectsCollidedWith = new Vector<>();


        myRotationMatrix = new AffineTransform();
        myTranslationMatrix = new AffineTransform();
        myScaleMatrix = new AffineTransform();


    }




    @Override
    public void draw(Graphics2D g2d) {

        AffineTransform saveAt = g2d.getTransform();

        myTranslationMatrix.translate(this.getX(), this.getY());
        //myRotationMatrix.rotate(Math.toRadians(10));
        myRotationMatrix.rotate(Math.toRadians(90 - heading));

        g2d.transform(myTranslationMatrix);
        g2d.transform(myRotationMatrix);

        g2d.setColor(this.getColor());


        g2d.drawRect(0,0,
                (int) ((length+20)),
                (int) (height ));

        setToIdentity();
        g2d.setTransform(saveAt);

    }

    public int getZIndex(){
        return ShockWave.zIndex;
    }


    public void move(int framesPerSecond){
        float angle =  (90 - heading);
        float deltaY = (float) (Math.sin(Math.toRadians(angle))*speed);
        float deltaX = (float) (Math.cos(Math.toRadians(angle))*speed);

             Location temp = new Location(this.getX()+deltaX,
                this.getY()+deltaY);


        this.setX(temp.getX());
        this.setY(temp.getY());

    }




    @Override
    public boolean collidesWith(ICollider obj) {
        return false;
    }

    @Override
    public void handleCollision(ICollider otherObject) {

    }

    @Override
    public float getDistanceOfReference() {
        return 0;
    }
}

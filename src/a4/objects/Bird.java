package a4.objects;

/**
 * Created by Victor Ignatenkov on 2/14/15.
 */

import a4.app.utilities.Services;
import a4.model.GameWorld;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.util.Vector;

/**
 * All birds have certain properties:
 * Fixed color
 * Always fly in straight line
 * Damage caused by colliding with a bird equals to
 * half the damage of colliding with another car
 */


public class Bird extends Moveable implements IDrawable, ICollider {

    private float size;
    private GameWorld gw;
    Image imageRes;

    public static int zIndex;
    private int scaleX;
    private int scaleY;


    public Bird(Location location, float size, float heading, float speed, Color color, GameWorld gw){
        super(color);

        this.gw = gw;

        objectsCollidedWith = new Vector<>();

        this.X = location.getX();
        this.Y = location.getY();
        this.heading = heading;
        this.speed = speed;
        this.size = size;

        scaleX = 3;
        scaleY = 3;


        String pathToResources = Services.getPathToImgResources();
        File file = new File(pathToResources + "bird.png");

        try {
            imageRes = ImageIO.read(file);
        } catch (Exception e){
            System.out.println("The picture for Bird wasn't found");
        }


    }


    public int getZIndex(){
        return Bird.zIndex;
    }


    @Override
    public void changeColor(Color color) {

    }

    @Override
    public String toString() {
        return "Bird: " + super.toString() + ", " +
                "heading=" + (int)this.heading + "," +
                " size=" + (int)this.size;
    }


    /**
     * Logic for drawing the bird.
     * @param g2d
     */
    @Override
    public void draw(Graphics2D g2d) {

        int width = (int)size;
        int length = (int)size;

        myTranslationMatrix = new AffineTransform();
        myRotationMatrix = new AffineTransform();
        myScaleMatrix = new AffineTransform();


        AffineTransform saveAt = g2d.getTransform();
        myTranslationMatrix.translate((int) getX() - (int) (width / 2), (int) getY() - (int) (length / 2));
        myRotationMatrix.rotate(Math.toRadians(90));
        myRotationMatrix.rotate(Math.toRadians(90 - heading));
        scale(scaleX, scaleY);


        g2d.transform(myTranslationMatrix);
        g2d.transform(myRotationMatrix);
        g2d.transform(myScaleMatrix);

        g2d.setColor(this.getColor());
        g2d.drawImage(imageRes, 0, 0, 20, 20, null);
        g2d.setColor(Color.black);


        setToIdentity();
        g2d.setTransform(saveAt);
    }


    /**
     * Logic for moving the bird.
     * @param framesPerSecond
     */
    @Override
    public void move(int framesPerSecond){
        framesPerSecond *=3;
        float angle =  (90 - heading);
        float deltaY = (float) (Math.sin(Math.toRadians(angle))*speed * framesPerSecond);
        float deltaX = (float) (Math.cos(Math.toRadians(angle))*speed * framesPerSecond);
        Location temp = new Location(this.getLocation().getX() + deltaX,
                this.getLocation().getY() + deltaY);

        this.X = temp.getX();
        this.Y = temp.getY();


        //TODO find out how to make the change of the angle better
        if((getX() >= 1000) || (getX() < 0)){
           // speed = -speed;
          //  scaleY *= -1;
          //  scaleX *= -1;
            this.setHeading(this.getHeading() + 180);
        }

        if((getY() >= 1000) || (getY() < 0)){

            //scaleY *= -1;
          //  scaleY *= -1;
            //this.setHeading(this.getHeading() + 180);

            rotate(180);
        }
    }


    /**
     * The logic which detects the collision
     * with other object in the Game World
     * @param obj the other object
     * @return true if collision has happened
     */
    @Override
    public boolean collidesWith(ICollider obj) {

        float distX = this.getX() - ((GameObject)obj).getX();
        float distY = this.getY() - ((GameObject)obj).getY();
        float distanceBtwnCenters = (float) Math.sqrt(distX * distX + distY * distY);

        if((this.getDistanceOfReference() + obj.getDistanceOfReference() >
                distanceBtwnCenters)){
            return true;
        } else {
            return false;
        }
    }


    /**
     * Handle collision of the Bird with other Game Objects.
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


    @Override
    public float getDistanceOfReference() {
        return size;
    }
}



















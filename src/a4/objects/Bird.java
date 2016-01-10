/* Created by Victor Ignatenkov on 2/14/15 */
package a4.objects;

import a4.app.utilities.Services;
import a4.model.GameWorld;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;

/* All birds share the following properties:
 * Fixed color
 * Always fly in straight directions
 * Damage caused by colliding with a bird equals to
 * half the damage of colliding with another car */

public class Bird extends Moveable implements IDrawable, ICollider {

    public static int zIndex;

    private GameWorld gw;
    private Image imageRes;
    private float size;
    private int scaleX;
    private int scaleY;


    public Bird(Location location, float size, float heading, float speed, Color color, GameWorld gw){
        super(color);

        imageRes = Services.getImage("bird.png");

        this.gw = gw;

        objectsCollidedWith = new LinkedList<>();

        this.X = location.getX();
        this.Y = location.getY();
        this.heading = heading;
        this.speed = speed;
        this.size = size;

        scaleX = 2;
        scaleY = 2;

        myTranslationMatrix.translate(location.getX(), location.getY());

        myRotationMatrix.rotate(Math.toRadians(90));
        myRotationMatrix.rotate(Math.toRadians(90 - heading));
        scale(scaleX, scaleY);
    }

    public int getZIndex(){
        return Bird.zIndex;
    }

    /* Birds cannot change their color */
    @Override
    public void changeColor(Color color) {

    }

    @Override
    public void draw(Graphics2D g2d) {

        AffineTransform saveAt = g2d.getTransform();

        g2d.transform(myTranslationMatrix);
        g2d.transform(myRotationMatrix);
        g2d.transform(myScaleMatrix);

        g2d.setColor(this.getColor());
        g2d.drawImage(imageRes, 0, 0, 20, 20, null);
        g2d.setColor(Color.black);

        g2d.setTransform(saveAt);
    }

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

        translate(deltaX, deltaY);

        if((getX() >= 1000) || (getX() < 0)){
            this.setHeading(this.getHeading() + 180);
            rotate(180);
        }

        if((getY() >= 1000) || (getY() < 0)){
            this.setHeading(this.getHeading() + 180);
             rotate(180);
        }
    }

    /* Steps to detects collisions
     * with other objects in the Game World */
    @Override
    public boolean didCollideWithAnotherObject(ICollider obj) {

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

    @Override
    public String toString() {
        return "Bird: " + super.toString() + ", " +
                "heading=" + (int)this.heading + "," +
                " size=" + (int)this.size;
    }


    /* Handle collision of the Bird with other Game Objects */
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

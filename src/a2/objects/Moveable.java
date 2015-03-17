package a2.objects;

/**
 * Created by Victor Ignatenkov on 2/9/15.
 */

import java.awt.*;

/**
 * Moveable - abstract class for game objects
 * inheriting from the GameObject and having
 * properties concerning moving on the screen.
 */

public abstract class Moveable extends GameObject {


    Moveable(Color color) {
       super(color);

    }



    /**
     * heading specified by degrees, 0 North, 90 East
     */
    protected float heading;
    protected float speed;


    /**
     * Default behavior for the objects
     * which are moving in a straight line.
     * Ok for the current version; however,
     * it's very likely that in the future versions
     * the implementation will differ, and the method
     * will turn into an abstract method.
     */
     public void move(){

         float angle =  (90 - heading);
         float deltaY = (float) (Math.sin(Math.toRadians(angle))*speed);
         float deltaX = (float) (Math.cos(Math.toRadians(angle))*speed);
         Location temp = new Location(this.getLocation().getX() + deltaX,
                 this.getLocation().getY() + deltaY);

         this.X = temp.getX();
         this.Y = temp.getY();

     }


    public float getHeading() {
        return heading;
    }

    public float getSpeed() {
        return speed;
    }

    public void setHeading(float heading) {
        this.heading = heading;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}

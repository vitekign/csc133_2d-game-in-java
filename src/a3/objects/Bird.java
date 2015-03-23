package a3.objects;

/**
 * Created by Victor Ignatenkov on 2/14/15.
 */

import java.awt.*;

/**
 * All birds have certain properties:
 * Fixed color
 * Always fly in straight line
 * Damage caused by colliding with a bird equals to
 * half the damage of colliding with another car
 */


public class Bird extends Moveable implements IDrawable {

    private float size;

    public Bird(Location location, float heading, float speed, Color color){
        super(color);

        this.X = location.getX();
        this.Y = location.getY();
        this.heading = heading;
        this.speed = speed;
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

    @Override
    public void draw(Graphics g) {
        g.drawString("Bird", (int)getX(), (int)getY());
    }
}


















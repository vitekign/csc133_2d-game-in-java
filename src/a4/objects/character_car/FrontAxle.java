/**
 * Created by Victor Ignatenkov on 4/28/15.
 */

package a4.objects.character_car;
import java.awt.*;
import java.awt.geom.AffineTransform;


/**
 * FrontAxle - a part of hierarchical main character's car.
 */
public class FrontAxle extends AffineObject {

    Shaft shaft;
    Tire leftTire;
    Tire rightTire;

    private int width;
    private int height;
    private int steeringDirection;

    /**
     * Create a FrontAxle and define local coordinates.
     * @param width width of the front axle
     * @param height height of the front axle
     */
    public FrontAxle(int width, int height){
        this.width = width;
        this.height = height;

        myTranslation = new AffineTransform();
        myRotation = new AffineTransform();
        myScale = new AffineTransform();

        shaft = new Shaft(width, height);
        leftTire = new Tire(8,23);
        rightTire = new Tire(8,23);

        leftTire.translate(-25, 0);
        rightTire.translate(25, 0);
    }

    /**
     * Draw the current part of the main character's car.
     * @param g2d
     */
    @Override
    public void draw(Graphics2D g2d) {
        AffineTransform saveAt = g2d.getTransform();
        g2d.setColor(Color.BLACK);

        g2d.transform(myTranslation);

        leftTire.draw(g2d);
        rightTire.draw(g2d);
        shaft.draw(g2d);

        g2d.setTransform(saveAt);
    }

    /**
     * Change the steering direction of the tires.
     * @param steeringDirection
     */
    public void updateSteeringDirection(float steeringDirection) {

        this.steeringDirection = ((int)steeringDirection);

        leftTire.rotate(-steeringDirection);
        rightTire.rotate(-steeringDirection);

    }
}

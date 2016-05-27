/**
 * Created by Victor Ignatenkov on 4/28/15.
 */
package a4.objects.character_car;
import java.awt.*;
import java.awt.geom.AffineTransform;


/**
 * RearAxle a part of hierarchical main character's car.
 */
public class RearAxle extends AffineObject {

    Shaft shaft;
    Tire leftTire;
    Tire rightTire;

    private int width;
    private int height;


    /**
     * Create the object and define local coordinates.
     * @param width width of the RearAxle
     * @param height height of the RearAxle
     */
    public RearAxle(int width, int height) {

        this.width = width;
        this.height = height;

        myTranslation = new AffineTransform();
        myRotation = new AffineTransform();
        myScale = new AffineTransform();

        shaft = new Shaft(width, height);
        leftTire = new Tire(12,23);
        rightTire = new Tire(12,23);

        leftTire.translate(-27, 0);
        rightTire.translate(27, 0);

    }

    /**
     * Draw the current part of the hierarchical object.
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
}

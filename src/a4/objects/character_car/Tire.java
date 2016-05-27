/**
 * Created by Victor Ignatenkov on 4/28/15.
 */

package a4.objects.character_car;
import java.awt.*;
import java.awt.geom.AffineTransform;


/**
 * Tire - a part of the hierarchical object.
 */
public class Tire extends AffineObject {

    private int width;
    private int height;

    /**
     * Create a tire.
     * @param width width of the tire.
     * @param height height of the tire.
     */
    public Tire(int width, int height) {

        myTranslation = new AffineTransform();
        myRotation = new AffineTransform();
        myScale = new AffineTransform();

        this.width = width;
        this.height = height;
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
        g2d.transform(myRotation);

        g2d.drawRoundRect(-width / 2, -height / 2, width, height, 5, 5);

        g2d.setTransform(saveAt);
    }
}

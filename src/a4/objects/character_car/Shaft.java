/**
 * Created by Victor Ignatenkov on 4/28/15.
 */

package a4.objects.character_car;
import java.awt.*;

/**
 * Shaft - a part of the hierarchical object.
 */
public class Shaft extends AffineObject {

    private int width;
    private int height;

    /**
     * Create the object.
     * @param width width of Shaft.
     * @param height height of Shaft.
     */
    public Shaft(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Draw the current part of the hierarchical object.
     * @param g2d
     */
    @Override
    public void draw(Graphics2D g2d) {

        g2d.drawRoundRect(-width/2,-height/2,width,height,3,3);

    }
}

/**
 * Created by Victor Ignatenkov on 4/28/15.
 */
package a4.objects.character_car;
import a4.app.utilities.Utilities;

import java.awt.*;
import java.awt.geom.AffineTransform;


/**
 * Body - a part of hierarchical main character's car.
 */
public class Body extends AffineObject{

    private int width;
    private int height;
    Image bodyImage;
    /**
     * Define necessary boundaries in terms of Local Coordinates.
     */
    private int left, rightSide, bottom, top, narrowLeft, narrowRight, upToNarrow;


    /**
     * Create a body of the main character's car and
     * calculate local coordinates.
     * @param width width of the car
     * @param height height of the car
     */
    public Body(int width, int height){

        bodyImage = Utilities.loadImage("tank_body.png");

        myTranslation = new AffineTransform();
        myRotation = new AffineTransform();
        myScale = new AffineTransform();

        this.width = width;
        this.height = height;
    }

    /**
     * Draw the current part of hierarchical object.
     * @param g2d
     */
    @Override
    public void  draw(Graphics2D g2d) {

        g2d.setColor(new Color(206, 0, 7));
        g2d.drawOval(-1, -1, 2, 2);
        g2d.drawImage(bodyImage, -width/2, -height/2, width, height, null);
        g2d.setColor(Color.BLACK);
    }

}

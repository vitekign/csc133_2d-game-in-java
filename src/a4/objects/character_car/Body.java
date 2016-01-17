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
    private int leftSide, rightSide, bottom, top, narrowLeft, narrowRight, upToNarrow;


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

        leftSide =- width/2;
        rightSide =+ width/2;

        bottom =- height/2;
        top =+ height/2;

        narrowLeft =- (width/2) + 5;
        narrowRight =+ (width/2) -5;

        upToNarrow = (height/2) -30;

    }

    /**
     * Draw the current part of hierarchical object.
     * @param g2d
     */
    @Override
    public void  draw(Graphics2D g2d) {

        g2d.setColor(new Color(43, 43, 43));


        int[] xPoints = {leftSide, rightSide, rightSide, narrowRight, narrowLeft, leftSide};
        int[] yPoints = {bottom, bottom, upToNarrow, top , top, upToNarrow};


        g2d.fillPolygon(xPoints, yPoints, 6);
      //  g2d.drawImage(bodyImage, leftSide, bottom, rightSide+140, top+140, null);

        g2d.setColor(Color.BLACK);
    }

}

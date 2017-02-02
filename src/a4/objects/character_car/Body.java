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

    public enum TYPE_OF_IMAGE {
        DARK_IMAGE, LIGHT_IMAGE,
    }

    /**
     * Create a body of the main character's car and
     * calculate local coordinates.
     * @param width width of the car
     * @param height height of the car
     */
    public Body(int width, int height, TYPE_OF_IMAGE type_of_image){

        String name_of_img = "";
        if(type_of_image == TYPE_OF_IMAGE.DARK_IMAGE){
            name_of_img = "tank_body_dark.png";
        } else if(type_of_image ==TYPE_OF_IMAGE.LIGHT_IMAGE){
            name_of_img = "tank_body.png";
        }
        bodyImage = Utilities.loadImage(name_of_img);

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

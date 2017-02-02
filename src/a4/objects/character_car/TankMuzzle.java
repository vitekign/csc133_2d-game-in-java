package a4.objects.character_car;

import a4.app.utilities.Utilities;
import a4.objects.Location;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Created by Victor Ignatenkov on 1/17/16.
 */

    public class TankMuzzle extends AffineObject {

    Image tankMuzzle;

    private int width;
    private int height;

    private float headig;

    public enum TYPE_OF_IMAGE {
        DARK_IMAGE, LIGHT_IMAGE,
    }

    public TankMuzzle(float heading, TYPE_OF_IMAGE type_of_image){
        String name_of_img = "";
        if(type_of_image == TYPE_OF_IMAGE.DARK_IMAGE){
            name_of_img = "tank_muzzle_dark.png";
        } else if(type_of_image == TYPE_OF_IMAGE.LIGHT_IMAGE){
            name_of_img = "tank_muzzle_1.png";
        }
        myTranslation = new AffineTransform();
        myRotation = new AffineTransform();
        myScale = new AffineTransform();

        translate(0,12);

        width = 40;
        height = 88;

        this.headig = heading;

        tankMuzzle  = Utilities.loadImage(name_of_img);

    }

    @Override
    public void draw(Graphics2D g2d) {
        AffineTransform saveAt = g2d.getTransform();

        g2d.transform(myTranslation);
        g2d.transform(myRotation);

        g2d.drawImage(tankMuzzle, -20, -24, width, height, null);
        g2d.setBackground(Color.black);
        g2d.drawOval(0, 0, 2, 2);

        g2d.setTransform(saveAt);
    }

    public void changeDirection(int byRotationUnits) {
        this.rotate(byRotationUnits);
        headig -= byRotationUnits;
    }

    public float getHeading(){
        return this.headig;
    }


}

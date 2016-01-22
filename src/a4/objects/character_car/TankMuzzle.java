package a4.objects.character_car;

import a4.app.utilities.Utilities;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Created by Victor Ignatenkov on 1/17/16.
 */
public class TankMuzzle extends AffineObject {

    Image tankMuzzle;

    public TankMuzzle(){
        myTranslation = new AffineTransform();
        myRotation = new AffineTransform();
        myScale = new AffineTransform();

        tankMuzzle  = Utilities.loadImage("tank_muzzle_1.png");
    }

    @Override
    public void draw(Graphics2D g2d) {
        AffineTransform saveAt = g2d.getTransform();


        g2d.transform(myTranslation);
        g2d.transform(myRotation);

        g2d.drawImage(tankMuzzle, -17, -15, 35, 80, null);
        g2d.setBackground(Color.black);
        g2d.drawOval(0, 0, 2, 2);

        g2d.setTransform(saveAt);
    }

    public void changeDirection(int byRotationUnits) {
        this.rotate(byRotationUnits);
    }
}

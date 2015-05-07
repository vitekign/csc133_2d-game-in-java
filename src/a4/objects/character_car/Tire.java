package a4.objects.character_car;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Created by Victor Ignatenkov on 4/28/15.
 */
public class Tire extends AffineObject {

    private int width;
    private int height;

    public Tire(int width, int height) {


        myTranslation = new AffineTransform();
        myRotation = new AffineTransform();
        myScale = new AffineTransform();

        this.width = width;
        this.height = height;
    }

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

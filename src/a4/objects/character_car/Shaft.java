package a4.objects.character_car;

import java.awt.*;

/**
 * Created by Victor Ignatenkov on 4/28/15.
 */
public class Shaft extends AffineObject {

    private int width;
    private int height;

    public Shaft(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Graphics2D g2d) {

        g2d.drawRoundRect(-width/2,-height/2,width,height,3,3);

    }
}

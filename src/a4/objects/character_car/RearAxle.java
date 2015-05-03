/**
 * Created by Victor Ignatenkov on 4/28/15.
 */
package a4.objects.character_car;
import java.awt.*;
import java.awt.geom.AffineTransform;


public class RearAxle extends AffineObject {
    Shaft shaft;
    Tire tire1;
    Tire tire2;

    public RearAxle() {
        myTranslation = new AffineTransform();
        myRotation = new AffineTransform();
        myScale = new AffineTransform();

        shaft = new Shaft();
        tire1 = new Tire();
        tire2 = new Tire();
    }

    @Override
    public void draw(Graphics2D g2d) {

    }
}

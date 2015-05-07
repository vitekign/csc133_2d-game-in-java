/**
 * Created by Victor Ignatenkov on 5/3/15.
 */

package a4.objects.character_car;
import java.awt.*;
import java.awt.geom.AffineTransform;


public class HierCar extends AffineObject {

    Body myBody;
    FrontAxle myFrontAxle;
    RearAxle myRearAxle;

    public HierCar(){
        myTranslation = new AffineTransform();
        myRotation = new AffineTransform();
        myScale = new AffineTransform();

        myBody = new Body(30, 60);

        myFrontAxle = new FrontAxle(40, 9);
        myFrontAxle.translate(0,20);
        myRearAxle = new RearAxle(40,9);
        myRearAxle.translate(0,-20);

    }

    @Override
    public void draw(Graphics2D g2d) {
        AffineTransform saveAt = g2d.getTransform();

        translate(100, 100);
        rotate(-45);
        scale(1, 1);

        g2d.transform(myTranslation);
        g2d.transform(myRotation);
        g2d.transform(myScale);


        myBody.translate(0, 0);
        myBody.draw(g2d);
        myFrontAxle.draw(g2d);
        myRearAxle.draw(g2d);

        setToIdentity();
        g2d.setTransform(saveAt);
    }
}

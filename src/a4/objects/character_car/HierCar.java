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
    }

    @Override
    public void draw(Graphics2D g2d) {
        AffineTransform saveAt = g2d.getTransform();


       // translate(400, 400);

       // g2d.translate(400, 400);
        //g2d.rotate(Math.toRadians(-180));

        myTranslation.translate(400, 400);
       // myRotation.rotate(Math.toRadians(0));
        g2d.transform(myTranslation);
        g2d.transform(myRotation);

        myBody.draw(g2d);

        setToIdentity();
        g2d.setTransform(saveAt);
    }
}

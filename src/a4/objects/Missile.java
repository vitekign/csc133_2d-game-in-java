/* Created by Victor Ignatenkov on 1/23/16 */

package a4.objects;
import a4.app.utilities.Utilities;
import a4.model.GameWorld;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;

public class Missile extends Moveable implements IDrawable, ICollider {

    private GameWorld gw;
    private Image imageRes;
    private float size;
    private float scaleX;
    private float scaleY;
    private float muzzleHeading;

    private Location carLocation;
    private Location muzzleLocation;

    private boolean isFirstTime = true;

    public static int zIndex;
    public static final int DISTANCE_FROM_CENTER_OF_MUZZLE = 60;

    public Missile(Location muzzleLocation, Location carLocation,  float carHeading, float muzzleHeading, float speed, GameWorld gw){
        super(Color.RED);

        this.carLocation = carLocation;
        this.muzzleHeading = muzzleHeading;

        imageRes = Utilities.loadImage(Utilities.IMAGE_NAME_MISSILE);
        this.gw = gw;

        objectsCollidedWith = new LinkedList<>();
        this.X = muzzleLocation.getX();
        this.Y = muzzleLocation.getY();
        this.heading = carHeading;
        this.speed = speed;

        scaleX = 2f;
        scaleY = 1f;

//
       //   float angle =  (90 - heading + 180);
//        float deltaY = (float) (Math.sin(Math.toRadians(angle))*speed * framesPerSecond);
//        float deltaX = (float) (Math.cos(Math.toRadians(angle))*speed * framesPerSecond);
//


//        myTranslationMatrix.translate(location.getX() - 2, location.getY() + 79);


   //     myTranslationMatrix.translate(-2, 0);

//        myTranslationMatrix.translate(location.getX() + (Math.cos(Math.toRadians(90-carHeading))
//                * DISTANCE_FROM_CENTER_OF_MUZZLE),
//                location.getY() + (Math.sin(Math.toRadians(90-carHeading)) * DISTANCE_FROM_CENTER_OF_MUZZLE));

        myTranslationMatrix.translate(carLocation.getX(), carLocation.getY());
      // myTranslationMatrix.translate(location.getX()+50, location.getY()+50 );


       // myRotationMatrix.rotate(Math.toRadians(90 - heading + 180));
        myRotationMatrix.rotate(Math.toRadians(90 - carHeading));

    }


    @Override
    public boolean didCollideWithAnotherObject(ICollider obj) {
        return false;
    }

    @Override
    public void handleCollision(ICollider otherObject) {

    }

    @Override
    public float getDistanceOfReference() {
        return 0;
    }

    @Override
    public void draw(Graphics2D g2d) {
        AffineTransform saveAt = g2d.getTransform();

        g2d.transform(myTranslationMatrix);
        g2d.transform(myRotationMatrix);
        g2d.transform(myScaleMatrix);

        AffineTransform additionalTranslationOfMuzzle = new AffineTransform();
        additionalTranslationOfMuzzle.translate(12, 0);
        g2d.transform(additionalTranslationOfMuzzle);

        AffineTransform additionalRotation = new AffineTransform();
        additionalRotation.rotate(Math.toRadians(-muzzleHeading));
        g2d.transform(additionalRotation);


        AffineTransform additionalTransformationOnXAxis = new AffineTransform();
        additionalTransformationOnXAxis.translate(60, 2);
        g2d.transform(additionalTransformationOnXAxis);

        g2d.drawImage(imageRes, -5, -5, 10, 10, null);
        g2d.setColor(Color.black);

        g2d.setTransform(saveAt);
    }

    @Override
    public void move(int framesPerSecond) {
        framesPerSecond *=3;
        float angle =  (90 - heading - muzzleHeading);
        float deltaY = (float) (Math.sin(Math.toRadians(angle))*speed * framesPerSecond);
        float deltaX = (float) (Math.cos(Math.toRadians(angle))*speed * framesPerSecond);

        Location temp = new Location(this.getLocation().getX() + deltaX,
                this.getLocation().getY() + deltaY);

        this.X = temp.getX();
        this.Y = temp.getY();

        translate(deltaX, deltaY);
    }
}

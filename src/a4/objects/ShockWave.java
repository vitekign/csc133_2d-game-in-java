/* Created by Victor Ignatenkov on 5/8/15 */

package a4.objects;
import a4.app.utilities.Utilities;
import a4.model.GameWorld;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

/* ShockWave - bezier curve */
public class ShockWave extends Moveable implements IDrawable,ICollider {
    public static int zIndex;
    public GameWorld gw;

    Point2D.Double[] controlPoints = new Point2D.Double[4];

    public ShockWave(Location location, float heading, float speed, Color color, GameWorld gw) {
        super(color);
        this.gw = gw;

        this.setX(location.getX());
        this.setY(location.getY());

        //random heading, speed and color.
        this.heading = new Random().nextFloat()*360;
        this.speed = (new Random().nextFloat()*50)+200;
        //this.color = Utilities.generateRandomColor();
        this.color = Color.RED;

        objectsCollidedWith = new ArrayList<>();

        myRotationMatrix = new AffineTransform();
        myTranslationMatrix = new AffineTransform();
        myScaleMatrix = new AffineTransform();

        //define 4 random points
        Random rand = new Random();
        controlPoints[0] = new Point2D.Double(0,0);
        controlPoints[1] = new Point2D.Double(rand.nextInt(100),rand.nextInt(100));
        controlPoints[2] = new Point2D.Double(-rand.nextInt(100),-rand.nextInt(100));
        controlPoints[3] = new Point2D.Double(0,150);
    }

    private void drawBezierCurve (Point2D.Double[] controlPointVector, int level, Graphics2D g2d) {
        Point2D.Double[] LeftSubVector = new Point2D.Double[4];
        Point2D.Double[] RightSubVector = new Point2D.Double[4];
        if ( isStraightEnough(controlPointVector) || (level>15) ) {
            g2d.setColor(Color.BLACK);
            //draw one tiny line of the curve
            g2d.drawLine((int) controlPointVector[0].getX(), (int) controlPointVector[0].getY(),
                    (int) controlPointVector[3].getX(), (int) controlPointVector[3].getY());
        }  else {
            subdivideCurve(controlPointVector, LeftSubVector, RightSubVector) ;
            drawBezierCurve(LeftSubVector, level + 1, g2d) ;
            drawBezierCurve (RightSubVector,level+1, g2d) ;
        }
    }

    private void subdivideCurve (Point2D.Double[] Q,Point2D.Double[] R, Point2D.Double[] S ) {
        Point2D.Double T = calculateTwoBezierPoints(Q[1], Q[2]);
        R[0]=Q[0];
        R[1]= calculateTwoBezierPoints(Q[0], Q[1]);
        R[2]= calculateTwoBezierPoints(R[1], T);
        S[3]=Q[3];
        S[2]= calculateTwoBezierPoints(Q[3], Q[2]);
        S[1]= calculateTwoBezierPoints(S[2], T);
        R[3]= calculateTwoBezierPoints(R[2], S[1]);
        S[0]=R[3];
    }


    private boolean isStraightEnough(Point2D.Double[] controlPointVector) {
        // find length around control polygon
        double d1 = getLengthBetweenTwoPoints(controlPointVector[0], controlPointVector[1]) + getLengthBetweenTwoPoints(controlPointVector[1], controlPointVector[2]) + getLengthBetweenTwoPoints(controlPointVector[2], controlPointVector[3]);
       // find distance directly between first and last control point
        double d2 = getLengthBetweenTwoPoints(controlPointVector[0], controlPointVector[3]) ;

        return ( Math.abs(d1 - d2) < 0.001f );
    }

    private double getLengthBetweenTwoPoints(Point2D.Double firstPoint, Point2D.Double secondPoint) {
        double distX = firstPoint.getX() - secondPoint.getX();
        double distY = firstPoint.getY() - secondPoint.getY();
        return Math.sqrt((distX*distX)+(distY*distY));
    }

    private Point2D.Double calculateTwoBezierPoints(Point2D.Double p1, Point2D.Double p2){
        return new Point2D.Double(( p1.getX() / 2.0f + p2.getX() / 2.0f ),
                ( p1.getY() / 2.0f + p2.getY() / 2.0f ));
    }

    /* Draw the convex hull of the current bezier curve */
    @Override
    public void draw(Graphics2D g2d) {
        AffineTransform saveAt = g2d.getTransform();

        myTranslationMatrix.translate(this.getX(), this.getY());
        myRotationMatrix.rotate(Math.toRadians(90 - heading));

        g2d.transform(myTranslationMatrix);
        g2d.transform(myRotationMatrix);

        g2d.setColor(this.getColor());
        drawBezierCurve(this.controlPoints, 0, g2d);
        g2d.setColor(Color.RED);
        g2d.drawLine((int) controlPoints[0].getX(), (int) controlPoints[0].getY(),
                (int) controlPoints[1].getX(), (int) controlPoints[1].getY());
        g2d.setColor(Color.RED);
        g2d.drawLine((int) controlPoints[1].getX(), (int) controlPoints[1].getY(),
                (int) controlPoints[2].getX(), (int) controlPoints[2].getY());
        g2d.setColor(Color.RED);
        g2d.drawLine((int) controlPoints[2].getX(), (int) controlPoints[2].getY(),
                (int) controlPoints[3].getX(), (int) controlPoints[3].getY());
        g2d.setColor(Color.RED);
        g2d.drawLine((int) controlPoints[0].getX(), (int) controlPoints[0].getY(),
                (int) controlPoints[3].getX(), (int) controlPoints[3].getY());

        setToIdentity();
        g2d.setTransform(saveAt);

    }

    public int getZIndex(){
        return ShockWave.zIndex;
    }

    /* Move the curve on the screen */
    public void move(int framesPerSecond){
        float angle =  (90 - heading);
        float deltaY = (float) (Math.sin(Math.toRadians(angle))*speed);
        float deltaX = (float) (Math.cos(Math.toRadians(angle))*speed);

        Location temp = new Location((float)this.getX()+deltaX,
                     (float) this.getY()+deltaY);
        this.setX(temp.getX());
        this.setY(temp.getY());
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
}

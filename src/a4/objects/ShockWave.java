/**
 * Created by Victor Ignatenkov on 5/8/15.
 */

package a4.objects;
import a4.app.utilities.Utilities;
import a4.model.GameWorld;


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.Vector;


/**
 * ShockWave - bezier curve.
 */
public class ShockWave extends Moveable implements IDrawable,ICollider {


    public static int zIndex;
    public GameWorld gw;

    private int length;
    private int height;

    Point2D.Double[] controlPoint = new Point2D.Double[4];


    /**
     *
     * @param location location of bezier curve
     * @param heading heading of bezier curve
     * @param speed speed of bezier curve
     * @param color
     * @param gw
     */
    public ShockWave(Location location, float heading, float speed, Color color, GameWorld gw) {
        super(color);


        this.setX(location.getX());
        this.setY(location.getY());


        this.gw = gw;

        //random heading, speed and color.
        this.heading = new Random().nextFloat()*360;
        this.speed = (new Random().nextFloat()*3)+2;
        this.color = Utilities.generateRandomColor();

        //size of the curve
        length = (int) (50 + new Random().nextFloat()*10);
        height = (int) (50 + new Random().nextFloat()*10);

        objectsCollidedWith = new Vector<>();


        myRotationMatrix = new AffineTransform();
        myTranslationMatrix = new AffineTransform();
        myScaleMatrix = new AffineTransform();

        //define 4 random points
        Random rand = new Random();
        controlPoint[0] = new Point2D.Double(0,0);
        controlPoint[1] = new Point2D.Double(rand.nextInt(100),rand.nextInt(100));
        controlPoint[2] = new Point2D.Double(-rand.nextInt(100),-rand.nextInt(100));
        controlPoint[3] = new Point2D.Double(0,150);
    }

    /**
     * Logic of the bezier curve algorithm.
     * @param controlPointVector
     * @param level
     * @param g2d
     */
    private void drawBezierCurve (Point2D.Double[] controlPointVector, int level, Graphics2D g2d) {
        Point2D.Double[] LeftSubVector = new Point2D.Double[4];
        Point2D.Double[] RightSubVector = new Point2D.Double[4];
        if ( straightEnough (controlPointVector) || (level>15) ) {
            g2d.setColor(Color.BLACK);
            //draw one tiny line of the curve
            g2d.drawLine((int) controlPointVector[0].getX(), (int) controlPointVector[0].getY(), (int) controlPointVector[3].getX(), (int) controlPointVector[3].getY());

        }  else {
            subdivideCurve(controlPointVector, LeftSubVector, RightSubVector) ;
            drawBezierCurve(LeftSubVector, level + 1, g2d) ;
            drawBezierCurve (RightSubVector,level+1, g2d) ;
        }
    }

    /**
     * Auxiliary function for bezier curve.
     * @param Q
     * @param R
     * @param S
     */
    private void subdivideCurve (Point2D.Double[] Q,Point2D.Double[] R, Point2D.Double[] S ) {
        Point2D.Double T = caclBezierPoints(Q[1], Q[2]);
        R[0]=Q[0];
        R[1]= caclBezierPoints(Q[0], Q[1]);
        R[2]= caclBezierPoints(R[1], T);
        S[3]=Q[3];
        S[2]= caclBezierPoints(Q[3], Q[2]);
        S[1]= caclBezierPoints(S[2], T);
        R[3]= caclBezierPoints(R[2], S[1]);
        S[0]=R[3];
    }

    /**
     * Auxiliary function for bezier curve.
     * @param controlPointVector
     * @return
     */
    private boolean straightEnough (Point2D.Double[] controlPointVector) {
        // find length around control polygon
        double d1 = getLengthBtwnPoints(controlPointVector[0], controlPointVector[1]) + getLengthBtwnPoints(controlPointVector[1], controlPointVector[2]) + getLengthBtwnPoints(controlPointVector[2], controlPointVector[3]);
       // find distance directly between first and last control point
        double d2 = getLengthBtwnPoints(controlPointVector[0], controlPointVector[3]) ;
        if ( Math.abs(d1 - d2) < 0.001f )
            return true ;
        else
            return false ;
    }

    /**
     * Auxiliary function for bezier curve.
     * @param firstPoint
     * @param secondPoint
     * @return
     */
    private double getLengthBtwnPoints(Point2D.Double firstPoint, Point2D.Double secondPoint) {
        double distX = firstPoint.getX() - secondPoint.getX();
        double distY = firstPoint.getY() - secondPoint.getY();
        return Math.sqrt((distX*distX)+(distY*distY));
    }

    /**
     * Auxiliary function for bezier curve.
     * @param p1
     * @param p2
     * @return
     */
    private Point2D.Double caclBezierPoints(Point2D.Double p1, Point2D.Double p2){
        return new Point2D.Double(( p1.getX() / 2.0f + p2.getX() / 2.0f ),
                ( p1.getY() / 2.0f + p2.getY() / 2.0f ));
    }


    /**
     * Draw the convex hull of the current bezier curve.
     * @param g2d
     */
    @Override
    public void draw(Graphics2D g2d) {

        AffineTransform saveAt = g2d.getTransform();

        myTranslationMatrix.translate(this.getX(), this.getY());
        myRotationMatrix.rotate(Math.toRadians(90 - heading));

        g2d.transform(myTranslationMatrix);
        g2d.transform(myRotationMatrix);

        g2d.setColor(this.getColor());
        drawBezierCurve(this.controlPoint, 0, g2d);
        g2d.setColor(Color.GREEN);
        g2d.drawLine((int) controlPoint[0].getX(), (int) controlPoint[0].getY(), (int) controlPoint[1].getX(), (int) controlPoint[1].getY());
        g2d.setColor(Color.RED);
        g2d.drawLine((int) controlPoint[1].getX(), (int) controlPoint[1].getY(), (int) controlPoint[2].getX(), (int) controlPoint[2].getY());
        g2d.setColor(Color.BLUE);
        g2d.drawLine((int) controlPoint[2].getX(), (int) controlPoint[2].getY(), (int) controlPoint[3].getX(), (int) controlPoint[3].getY());
        g2d.setColor(Color.ORANGE);
        g2d.drawLine((int) controlPoint[0].getX(), (int) controlPoint[0].getY(), (int) controlPoint[3].getX(), (int) controlPoint[3].getY());


        setToIdentity();
        g2d.setTransform(saveAt);

    }

    public int getZIndex(){
        return ShockWave.zIndex;
    }


    /**
     * Move the curve on the screen.
     * @param framesPerSecond
     */
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

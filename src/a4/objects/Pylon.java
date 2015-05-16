package a4.objects;

/**
 * Created by Victor Ignatenkov on 2/14/15.
 */

import a4.app.utilities.Services;
import a4.model.GameWorld;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.Vector;

/**
 * Pylon class.
 *
 * Not allowed to change color once they're created
 */


public class Pylon extends Fixed implements IDrawable, ICollider, ISelectable {

    private float radius;
    final private int sequenceNumber;
    static private int count = 1;

    Image imageRes;
    Image imagePressed;

    private AffineTransform tr;

    private GameWorld gw;

    private boolean isSelected;

    public static int zIndex;

    public void initObject(){
        objectsCollidedWith = new Vector<>();
    }



    @Override
    public Location getLocation() {
        return new Location((float)myTranslationMatrix.getTranslateX(), (float)myTranslationMatrix.getTranslateY());
    }

    @Override
    public double getX() {
        return myTranslationMatrix.getTranslateX();
    }

    @Override
    public double getY() {
        return myTranslationMatrix.getTranslateY();
    }

    public Pylon(Location location, float radius, Color color, GameWorld gw){
            super(color);
            this.gw = gw;



        initObject();
         //   this.X = location.getX();
          //  this.Y = location.getY();
            this.radius = radius;


        tr = new AffineTransform();
        myTranslationMatrix.translate(location.getX(), (int) location.getY());
        scale(1,1);

        sequenceNumber = count++;


        String pathToResources = Services.getPathToImgResources();
        File file = new File(pathToResources + "pylon.png");

        try {
            imageRes = ImageIO.read(file);
        } catch (Exception e){
            System.out.println("The picture for Bird wasn't found");
        }

        File fileImagePressed = new File(pathToResources + "pylon_pressed.png");
        try {
            imagePressed = ImageIO.read(fileImagePressed);
        } catch (Exception e){
            System.out.println("The picture for Bird wasn't found");
        }



    }

    public Pylon(Location location, float radius, Color color, GameWorld gw, int seqNumberOfPylon){
        super(color);

        this.gw = gw;

        initObject();
        //this.X = location.getX();
       // this.Y = location.getY();




        tr = new AffineTransform();
        //tr.translate(location.getX() - (int) radius / 2, (int) location.getY() - (int) radius / 2);
        myTranslationMatrix.translate(location.getX(), (int) location.getY());

        this.radius = radius;

        sequenceNumber = seqNumberOfPylon;
                count++;





    }

    public int getZIndex(){
        return Pylon.zIndex;
    }

    public static int getzIndex() {
        return zIndex;
    }

    public static void setzIndex(int zIndex) {
        Pylon.zIndex = zIndex;
    }

    @Override
    public String toString() {
        return "Pylon " +
                super.toString() +
                " radius " + (int)this.radius +
                " seqNum " + (int)this.sequenceNumber;
    }

    /**
     *
     * @return
     * current index number
     */
    public int getIndexNumber() {
        return sequenceNumber;
    }

    /**
     * Set the first index number for Pylon Class
     * @param numberOfTheFirstPylon
     * index number
     */
    public static void resetSequenceGeneratorTo(int numberOfTheFirstPylon) {
        count = numberOfTheFirstPylon;
    }

    /**
     * Pylons don't have the ability to have their
     * color changed after creation.
     * @param color
     */
    @Override
    public void changeColor(Color color) {

    }

    /**
     *
     * @return
     * number of pylons created + 1
     */
    public static int getCount(){
        return count - 1;
    }


    /****************************************/
    /* Confirms to ISelectable              */
    /****************************************/
    @Override
    public void setSelected(boolean yesNo) {
        this.isSelected = yesNo;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public boolean contains(Point2D p) {


        int xLoc = (int)getX();
        int yLoc = (int)getY();



        //get inverse
        //public void concatenate(AffineTransform Tx)
        //Cx'(p) = Cx(Tx(p))
        //first transforming p by Tx and then transforming the result by the original transform Cx

        AffineTransform temp = new AffineTransform();
        temp.setTransform(myTranslationMatrix);
        temp.concatenate(myScaleMatrix);
        //apply the inverse to the point
        AffineTransform inverseVTM = null;
        try {
            inverseVTM = temp.createInverse();
        } catch (NoninvertibleTransformException e){
            System.out.println("Cannot inverse the matrix: " + e.getMessage());
        }

        Point2D mouseScreenLocation = new Point();
        mouseScreenLocation.setLocation(p.getX(), p.getY());

        //  mouseScreenLocation = theVTM.transform(mouseScreenLocation, null);
        mouseScreenLocation = inverseVTM.transform(mouseScreenLocation,null);


        int px = (int) Math.abs(mouseScreenLocation.getX());
        int py = (int) Math.abs(mouseScreenLocation.getY());



        int radiusMouseInput = (int) Math.sqrt((px*px)+(py*py));

/*
        if((px >= xLoc - (this.getDistanceOfReference()/2)) && (px <= xLoc + (this.getDistanceOfReference()/2))
                && (py >= yLoc - (this.getDistanceOfReference()/2))&& (py <= yLoc + (this.getDistanceOfReference()/2))) {
*/
        if(radiusMouseInput <= this.getDistanceOfReference()/2){
            return true;
        }
        else{
            return false;
        }
        }

    /****************************************/




    @Override
    public void draw(Graphics2D g2d) {

        AffineTransform saveAt = g2d.getTransform();
        //myTranslationMatrix.translate((int) getX() - (int) radius / 2, (int) getY() - (int) radius / 2);
        g2d.transform(myTranslationMatrix);
        g2d.transform(myScaleMatrix);

    if(isSelected){
        g2d.setColor(Color.gray);
        g2d.drawImage(imagePressed, (int) -(radius/2), (int) -(radius/2), (int)radius, (int)radius, null);
        //g2d.fillOval((int) -(radius/2), (int) -(radius/2), (int) radius, (int) radius);
        g2d.setColor(Color.white);
        g2d.fillOval((int) -(radius/2), (int) -(radius/2), 1, 1);
        g2d.setColor(Color.white);

        AffineTransform addTranslate = new AffineTransform();
        addTranslate.translate(15, 15);

        myScaleMatrix.scale(1, -1);
        g2d.transform(addTranslate);
        g2d.transform(myScaleMatrix);

        g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        g2d.setColor(new Color(219, 219, 219));
        g2d.drawString(String.valueOf(getIndexNumber()), (int) -(radius / 2), (int) (radius / 2));


        scale(1,-1);
    } else {
        g2d.setColor(new Color(14, 40, 3));
        g2d.drawImage(imageRes, (int) -(radius/2), (int) -(radius/2), (int)radius, (int)radius, null);
        //g2d.fillOval((int) -(radius/2), (int) -(radius/2), (int) radius, (int) radius);
        g2d.setColor(Color.white);
        g2d.fillOval((int) -(radius/2), (int) -(radius/2), 1, 1);
        g2d.setColor(Color.white);



        AffineTransform addTranslate = new AffineTransform();
        addTranslate.translate(15,15);


        myScaleMatrix.scale(1, -1);
        g2d.transform(addTranslate);
        g2d.transform(myScaleMatrix);

        g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        g2d.setColor(new Color(164, 164, 164));
        g2d.drawString(String.valueOf(getIndexNumber()), (int) -(radius / 2), (int) (radius / 2));
        g2d.setColor(Color.black);

        scale(1, -1);

    }
      //  setToIdentity();

        g2d.setTransform(saveAt);
}



    /**
     * The logic which detects the collision
     * with other object in the Game World
     * @param obj the other object
     * @return true if collision has happened
     */
    @Override
    public boolean collidesWith(ICollider obj) {
        double distX = this.getX() - ((GameObject)obj).getX();
        double distY = this.getY() - ((GameObject)obj).getY();
        float distanceBtwnCenters = (float) Math.sqrt(distX * distX + distY * distY);

        if((this.getDistanceOfReference() + obj.getDistanceOfReference() >
                distanceBtwnCenters)){
            return true;
        } else {
            return false;
        }
    }

    /* Handle collision of the Pylon with other Game Objects.
       * @param otherObject
    */
    @Override
    public void handleCollision(ICollider otherObject) {
        if(otherObject instanceof Car && !(otherObject instanceof NPCCar)){
            if(!objectsCollidedWith.contains((GameObject)otherObject)){
                objectsCollidedWith.add((GameObject)otherObject);

            }
        }
    }

    @Override
    public float getDistanceOfReference() {
        return radius;
    }


}

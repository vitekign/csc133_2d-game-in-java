package a3.objects;

/**
 * Created by Victor Ignatenkov on 2/14/15.
 */

import a3.model.GameWorld;

import java.awt.*;
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

    private GameWorld gw;

    private boolean isSelected;

    public Pylon(Location location, float radius, Color color, GameWorld gw){
            super(color);

            this.gw = gw;

            objectsCollidedWith = new Vector<>();

            this.X = location.getX();
            this.Y = location.getY();
            this.radius = radius;

        sequenceNumber = count++;
    }

    public Pylon(Location location, float radius, Color color, GameWorld gw, int seqNumberOfPylon){
        super(color);

        this.gw = gw;

        objectsCollidedWith = new Vector<>();

        this.X = location.getX();
        this.Y = location.getY();
        this.radius = radius;

        sequenceNumber = seqNumberOfPylon;
                count++;
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
    public boolean contains(Point p) {
        int px = (int) p.getX();
        int py = (int) p.getY();
        int xLoc = (int)getX();
        int yLoc = (int)getY();

        if((px >= xLoc - (this.getDistanceOfReference()/2)) && (px <= xLoc + (this.getDistanceOfReference()/2))
                && (py >= yLoc - (this.getDistanceOfReference()/2))&& (py <= yLoc + (this.getDistanceOfReference()/2)))
            return true;
        else
            return false;
    }
    /****************************************/




    @Override
    public void draw(Graphics g) {

        if(isSelected){
            g.setColor(Color.gray);
            g.fillOval((int) getX() - (int) radius / 2, (int) getY() - (int) radius / 2, (int) radius, (int) radius);
            g.setColor(Color.white);
            g.fillOval((int) getX(), (int) getY(), 1, 1);
            g.drawString(String.valueOf(getIndexNumber()), (int) getX() - (int) radius / 2, (int) getY() - (int) radius / 2);
            g.setColor(Color.black);
        } else {
            g.setColor(Color.black);
            g.fillOval((int) getX() - (int) radius / 2, (int) getY() - (int) radius / 2, (int) radius, (int) radius);
            g.setColor(Color.white);
            g.fillOval((int) getX(), (int) getY(), 1, 1);
            g.drawString(String.valueOf(getIndexNumber()), (int) getX() - (int) radius / 2, (int) getY() - (int) radius / 2);
            g.setColor(Color.black);
        }
    }


    /****************************************/
    /* Confirms to ICollider                */
    /****************************************/
    @Override
    public boolean collidesWith(ICollider obj) {
        float distX = this.getX() - ((GameObject)obj).getX();
        float distY = this.getY() - ((GameObject)obj).getY();
        float distanceBtwnCenters = (float) Math.sqrt(distX * distX + distY * distY);

        if((this.getDistanceOfReference() + obj.getDistanceOfReference() >
                distanceBtwnCenters)){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void handleCollision(ICollider otherObject) {
        if(otherObject instanceof Car && !(otherObject instanceof NPCCar)){
            System.out.println("Just collided Pylon" + this.getIndexNumber());
           // gw.gameObjectsToDelete.add((GameObject)this);
            if(!objectsCollidedWith.contains((GameObject)otherObject)){
                objectsCollidedWith.add((GameObject)otherObject);
                ((GameObject)otherObject).objectsCollidedWith.add(this);
            }
            gw.carCollideWithPylon(this.getIndexNumber());
        }
    }

    @Override
    public float getDistanceOfReference() {
        return radius;
    }

    /****************************************/
}

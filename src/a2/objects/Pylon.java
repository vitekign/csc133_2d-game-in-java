package a2.objects;

/**
 * Created by Victor Ignatenkov on 2/14/15.
 */

import java.awt.*;
//TODO Pylon doesn't generate new colors ???
/**
 * Pylon class.
 *
 * Not allowed to change color once they're created
 */
public class Pylon extends Fixed {

    private float radius;
    final private int sequenceNumber;
    static private int count = 1;

    public Pylon(Location location, float radius, Color color){

            super(color);

            this.X = location.getX();
            this.Y = location.getY();
            this.radius = radius;

        sequenceNumber = count++;
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
        return count-1;
    }
}

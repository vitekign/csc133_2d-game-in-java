package a4.objects;

/**
 * Created by Victor Ignatenkov  on 2/9/15.
 */
public interface ISteerable {

    //TODO return rotation_unit back to 5
    public final int ROTATION_UNIT = 10;
    public final int MAX_HEADING_DEFLECTION = 40;

    void changeCurrentHeadingToTheLeft();
    void changeCurrentHeadingToTheRight();

}

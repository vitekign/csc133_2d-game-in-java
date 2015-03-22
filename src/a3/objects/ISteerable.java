package a3.objects;

/**
 * Created by Victor Ignatenkov  on 2/9/15.
 */
public interface ISteerable {

    public final int ROTATION_UNIT = 5;
    public final int MAX_HEADING_DEFLECTION = 40;

    void changeCurrentHeadingToTheLeft();
    void changeCurrentHeadingToTheRight();

}

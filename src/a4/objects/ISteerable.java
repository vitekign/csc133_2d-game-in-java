/* Created by Victor Ignatenkov  on 2/9/15 */

package a4.objects;

public interface ISteerable {

    int ROTATION_UNIT = 10;
    int MAX_HEADING_DEFLECTION = 40;

    void changeCurrentHeadingToTheLeft();
    void changeCurrentHeadingToTheRight();

}

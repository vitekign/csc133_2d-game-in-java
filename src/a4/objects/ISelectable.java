/* Created by Victor Ignatenkov on 3/26/15 */

package a4.objects;
import java.awt.*;
import java.awt.geom.Point2D;

public interface ISelectable {

     void setSelected(boolean yesNo);
     boolean isSelected();
     boolean contains(Point2D p);
     void draw(Graphics2D g);
}

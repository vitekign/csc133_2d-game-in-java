package a4.objects;

/**
 * Created by Victor Ignatenkov on 2/20/15.
 */

/**
 * Interface to add the ability to have
 * a location changed.
 *
 * Only the classes with the ability to have their
 * location changed must confirm to this interface.
 */
public interface ILocationChangeable {
    void setNewLocation(Location location);
}

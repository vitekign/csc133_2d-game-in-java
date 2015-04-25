package a4.objects;

/**
 * Created by Victor Ignatenkov on 3/23/15.
 */


public interface ICollider {

    public boolean collidesWith(ICollider obj);
    public void handleCollision(ICollider otherObject);
    public float getDistanceOfReference();


}

package a4.objects;

/* Created by Victor Ignatenkov on 3/23/15 */

public interface ICollider {

    boolean didCollideWithAnotherObject(ICollider obj);
    void handleCollision(ICollider otherObject); /* apply appropriate response algorithm */
    float getDistanceOfReference();
}

package a4.model;

/**
 * Created by Victor Ignatenkov on 3/6/15.
 */

public interface IObserver {
    public void update (GameWorldProxy o, Object arg);
}

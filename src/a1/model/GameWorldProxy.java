package a1.model;

import a1.objects.IGameWorld;

/**
 * Created by Victor Ignatenkov on 3/6/15.
 */
public class GameWorldProxy implements IObservable, IGameWorld{
    @Override
    public void addObserver(IObserver obs) {

    }

    @Override
    public void notifyObserver() {

    }
    /**
     * code here to accept and hold a GameWorld, provide implementations
     * of all the public methods in a GameWorld, forward allowed
     * calls to the actual GameWorld, and reject calls to methods
     * which the outside should not be able to access in the GameWorld.
     */
}

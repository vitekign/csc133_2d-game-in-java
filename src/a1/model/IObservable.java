package a1.model;

/**
 * Created by Victor Ignatenkov on 3/6/15.
 */
public interface IObservable {
    public void addObserver(IObserver obs);
    public void notifyObserver();

}

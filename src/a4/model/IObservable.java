/*Created by Victor Ignatenkov on 3/6/15.*/
package a4.model;

public interface IObservable {
    void addObserver(IObserver obs);
    void notifyObserver();

}

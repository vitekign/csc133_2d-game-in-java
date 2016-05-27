/* Created by Victor Ignatenkov on 3/10/15. */
package a4.app.strategies;
import a4.model.GameWorld;
import a4.objects.NPCCar;


/* IStrategy Interface for all Strategy classes */
public interface IStrategy {
     void performStrategy(NPCCar car, GameWorld gw);
     String toString();
}

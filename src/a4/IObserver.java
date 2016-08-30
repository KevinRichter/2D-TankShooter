/** This interface provides the public methods required to
 *  implement an Observer.class.
 */
package a4;

public interface IObserver {
	public void update (IObservable proxy, Object arg);
}

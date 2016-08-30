/** This interface provides the public methods required to
 *  implement an Observable.class.
 */
package a4;

public interface IObservable {
		public void addObserver (IObserver obs);
		public void notifyObservers();
}

/** This interface provides the public methods needed to
 *  implement an Iterator.class.
 */
package a4;

public interface Iterator {
	public boolean hasNext();
	public Object getNext();
	public int size();
	public void remove();
	public Object getRandom();
	public void shuffle();
}

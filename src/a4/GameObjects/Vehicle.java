/** Vehicle.java extends MoveableItems.java and
 *  implements ISteerable, but implements none of 
 *  its inherited methods, including the ISteerable
 *  method steer().
 */

package a4.GameObjects;

import a4.Strategy.Strategy;

public abstract class Vehicle extends MoveableItems implements ISteerable{

	protected Strategy curStrategy;
	public abstract void setStrategy(Strategy s);
	public abstract void invokeStrategy();
	public abstract void steer(String in);
	public abstract void move(int delay);
}

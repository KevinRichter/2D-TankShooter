/** MoveableItems.java is an abstract class that inherits
 *  one method setLocation() from Vehicle() and creates
 *  a new abstract method for objects that are movable, move().
 */
package a4.GameObjects;

public abstract class MoveableItems extends GameObjects {
	// 
	
	private int direction;
	private int speed;
	private boolean isBlocked;
	private boolean isMarkedForDeletion;

	// Abstract methods provided for all subclasses
	public abstract void move(int delay);
	
//	// Checks to make sure object is still on the screen before
//	// updating object location
//	public void checkBounds(float newX, float newY, float oldX, float oldY) {				
//		if (newX > getScreenWidth()) {
//			newX = getScreenWidth();
//			setIsBlocked(true);
//			setLocation(newX, oldY);
//			return;
//		}
//		else if (newX < 10) {
//			newX = 10;
//			setIsBlocked(true);
//			setLocation(newX, oldY);
//			return;
//		}
//		else if (newY > getScreenHeight()) {
//			newY = getScreenHeight();
//			setIsBlocked(true);
//			setLocation(oldX, newY);
//			return;
//		}
//		else if (newY < 10) {
//			newY = 10;
//			setIsBlocked(true);
//			setLocation(oldX, newY);
//			return;
//		}
//		else {
//			setLocation(newX, newY);
//		}
//		
//	}
	
	
	
	// Allows outside object to see if it is blocked
	public Boolean getIsBlocked() {
		// Allows outside objects to see if a tank is currently blocked
		return isBlocked;
	}
	
	// Allows outside object to set the blocked flag.
	public void setIsBlocked(Boolean isBlocked) {
		// Allows outside objects to set a tank to blocked
		this.isBlocked = isBlocked;
	}
	
	// Implementation of getter for direction.	
	public int getDirection() {
		return direction;
	}
	
	// Implementation of setter for direction.
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	//Implementation of getter for speed.
	public int getSpeed() {
		return speed;
	}
	
	// Implementation of setter for speed..
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	// All Movable objects can be deleted from game. A true
	// value will be used to check for what objects need deleting
	// from the collection.
	public boolean isMarkedForDeletion() {
		return isMarkedForDeletion;
	}
	public void setMarkedForDeletion(boolean isMarkedForDeletion) {
		this.isMarkedForDeletion = isMarkedForDeletion;
	}
	
}

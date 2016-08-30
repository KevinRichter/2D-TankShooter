/** Projectile.java class extends the abstract class
 *  MoveableItems(). It inherits two methods, but
 *  does not implement them.
 */
package a4.GameObjects;

public abstract class Projectile extends MoveableItems{
	
	private int lifetime;
	private int myTanksID;

	public abstract void move(int delay);

	public int getMyTanksID() {
		return myTanksID;
	}
	public void setMyTanksID(int ID) {
		myTanksID = ID;		
	}	

	// Setter and getter for lifetime.
	public int getLifetime() {
		return lifetime;
	}

	public void setLifetime(int lifetime) {
		this.lifetime = lifetime;
	}


}

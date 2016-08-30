/** Tank.java class creates a Tank object and requires
 *  no arguments to generate an enemy tank. A java.awt.Color needs
 *  to be passed to generate a player tank. A color different from
 *  the red enemy tanks is advised. Tank.java inherits
 *  from ISteerable the steer() method and implements it.
 *  It also implements move() and setLocation from 
 *  GameObjects().
 */
package a4.GameObjects;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.Random;


//import a4.Strategy.FireEveryTick;
//import a4.Strategy.FireEveryotherTick;


public abstract class Tank extends Vehicle implements ISteerable, ISelectable{
	
	protected int width, turretWidth;
	protected int height, turretLength;
	
	private int tankID;    	// this is used to let a missile know what tank it belongs to.
//	private int ticks = 1;
	private int missileCount;
	private int armorStrength;	
	private boolean isSelected = false;
	protected static Random generator = new Random();	
	
	public Tank() {	}
	

	abstract public void fireMissile();
//	// AT methods
//	public AffineTransform getMyTranslation() {
//		return myTranslation;
//	}
//	public AffineTransform getMyScale() {
//		return myScale;
//	}
//	public AffineTransform getMyRotation() {
//		return myRotation;
//	}
//	public void rotate (double degrees) {
//		myRotation.rotate (Math.toRadians(degrees));
//	}
//	public void scale (double sx, double sy) {
//		myScale.scale (sx, sy);
//	}
//	public void translate (double dx, double dy) {
//		myTranslation.translate (dx, dy);
//	}
		
//	public void steer(String direction2) {
//		// Alters the heading of the player's tank. "l" for left
//		// and "r" for right.
//		if ("l".equals(direction2)) {
//			if(getDirection() == 360) {
//				this.setDirection(5);
//				this.rotate(5);
//			}
//			else {
//				this.setDirection(getDirection() - 5);
//				this.rotate(getDirection() - 5);
//			}
//		}
//		if ("r".equals(direction2)) {
//			if(getDirection() == 0){
//				this.setDirection(355);
//				this.rotate(355);
//			}
//			else {
//				this.setDirection(getDirection() + 5);
//				this.rotate(getDirection() + 5);
//			}
//		}
//		// Clear the blocked flag
//		this.setIsBlocked(false);
//	}

//	@Override
//	public void move(int delay) {
//		// Updates all tank locations
//		// Use delay to get a fraction of time that can be used to weigh speed and direction. Every 1 second should see the
//		// same distance as the Tick() command used to produce.	
//		ticks++; // Keeps track of the number of times move() is called.
//		if (this instanceof EnemyTank) {
//			AffineTransform enemyAT = new AffineTransform();
//			EnemyTank enemyTank = (EnemyTank) this;
//			enemyAT = enemyTank.getMyTranslation();
//			double dist = (((double)delay)/1000) * getSpeed();
//			float deltaX = (float) ((float) (Math.cos( Math.toRadians(enemyTank.getDirection()))) * dist);
//			float deltaY = (float) ((float) (Math.sin( Math.toRadians(enemyTank.getDirection()))) * dist);
//			float newX = (float) (enemyAT.getTranslateX() + deltaX);
//			float newY = (float) (enemyAT.getTranslateY() + deltaY);
//			enemyTank.translate(newX,  newY);
//		}
//		else {
//			AffineTransform playerAT = new AffineTransform();
//			PlayerTank playerTank = (PlayerTank) this;
//			playerAT = playerTank.getMyTranslation();
//			double dist = (((double)delay)/1000) * getSpeed();
//			float deltaX = (float) ((float) (Math.cos( Math.toRadians(playerTank.getDirection()))) * dist);
//			float deltaY = (float) ((float) (Math.sin( Math.toRadians(playerTank.getDirection()))) * dist);
//			float newX = (float) (playerAT.getTranslateX() + deltaX);
//			float newY = (float) (playerAT.getTranslateY() + deltaY);
//			playerTank.translate(newX, newY);
//		}
////		// If object is not blocked, then update position
////		if (!this.getIsBlocked()) {
////			this.checkBounds(newX, newY, getX(), getY());
////		}
//		
//		// Change enemy tank strategy every 30 seconds
//		if (this instanceof EnemyTank) {			
//			if (ticks % 1500 == 0){	
//				String current = ((EnemyTank)this).getStrategy().substring(0, 25);
//				if (current.equals("a3.Strategy.FireEveryTick")) {
//					this.setStrategy(new FireEveryotherTick((EnemyTank) this));
//				} else {
//				this.setStrategy(new FireEveryTick((EnemyTank) this));
//				}  
//			} 
//		}
		
		
//	}
		
	public int getMissileCount() {
		// Allows outside objects to get the current missile count of tank
		return missileCount;
	}

	public void setMissileCount(int missleCount) {
		// Allows outside objects to set the missile count of the tank
		this.missileCount = missleCount;
	}

	public int getArmorStrength() {
		// Allows outside objects to get the armor value of tank
		return armorStrength;
	}

	public void setArmorStrength(int armorStrength) {
		// Allows outside objects to set the armor of the tank
		this.armorStrength = armorStrength;
	}
				
	// These methods find the left, right, top and bottom most points
	// of the tank.
//	public float getLeft() {
//		// The -5 adjusts for center of tank
//		AffineTransform tankAT = new AffineTransform();
//		if (this instanceof PlayerTank) {
//			PlayerTank playerTank = (PlayerTank) this;
//			tankAT = playerTank.getMyTranslation();
//		} else {
//			EnemyTank enemyTank = (EnemyTank) this;
//			tankAT = enemyTank.getMyTranslation();
//		}
//		return (float) (tankAT.getTranslateX() - width/2);
//	}
//
//	public float getRight() {
//		// The +5 adjusts for center of tank
//		AffineTransform tankAT = new AffineTransform();
//		if (this instanceof PlayerTank) {
//			PlayerTank playerTank = (PlayerTank) this;
//			tankAT = playerTank.getMyTranslation();
//		} else {
//			EnemyTank enemyTank = (EnemyTank) this;
//			tankAT = enemyTank.getMyTranslation();
//		}
//		return (float) (tankAT.getTranslateX() + width/2);
//	}
//	
//	public float getTop() {
//		// The -5 adjusts for center of tank
//		AffineTransform tankAT = new AffineTransform();
//		if (this instanceof PlayerTank) {
//			PlayerTank playerTank = (PlayerTank) this;
//			tankAT = playerTank.getMyTranslation();
//		} else {
//			EnemyTank enemyTank = (EnemyTank) this;
//			tankAT = enemyTank.getMyTranslation();
//		}
//		return (float) (tankAT.getTranslateY() - height/2);
//	}
//	
//	public float getBottom() {
//		// The +5 adjusts for center of tank
//		AffineTransform tankAT = new AffineTransform();
//		if (this instanceof PlayerTank) {
//			PlayerTank playerTank = (PlayerTank) this;
//			tankAT = playerTank.getMyTranslation();
//		} else {
//			EnemyTank enemyTank = (EnemyTank) this;
//			tankAT = enemyTank.getMyTranslation();
//		}
//		return (float) (tankAT.getTranslateY() + height/2);
//	}

	public int getTankID() {
		return tankID;
	}

	public void setTankID(int tankID) {
		this.tankID = tankID;
	}	
	
	public void setSelected (boolean yesNo) {
		isSelected = yesNo;
	}
	
	public boolean isSelected() {
		return isSelected;
		
	}
	
	public boolean contains(Point2D p) {
		boolean isIn;
		
		AffineTransform inverseTankLocal = new AffineTransform();
		AffineTransform inverseRotateLocal = new AffineTransform();
		AffineTransform inverseTransLocal = new AffineTransform();
		Point2D mouseLocalLoc;
		if (this instanceof PlayerTank) {
			PlayerTank playerTank = (PlayerTank) this;
			try {
				inverseRotateLocal = playerTank.getMyRotation().createInverse();
				inverseTransLocal = playerTank.getMyTranslation().createInverse();	
				inverseTankLocal = (AffineTransform) inverseRotateLocal.clone();
				inverseTankLocal.concatenate(inverseTransLocal);
			} catch (NoninvertibleTransformException e) { 
				System.out.println("InverseVTM could not be created!!");
			}
			mouseLocalLoc = inverseTankLocal.transform(p,null);
		} else {
			EnemyTank enemyTank = (EnemyTank) this;
			try {
				inverseRotateLocal = enemyTank.getMyRotation().createInverse();
				inverseTransLocal = enemyTank.getMyTranslation().createInverse();	
				inverseTankLocal = (AffineTransform) inverseRotateLocal.clone();
				inverseTankLocal.concatenate(inverseTransLocal);
			} catch (NoninvertibleTransformException e) { 
				System.out.println("InverseVTM could not be created!!");
			}
			mouseLocalLoc = inverseTankLocal.transform(p,null);
		}
		
		if (((mouseLocalLoc.getX() > (- width/2) && mouseLocalLoc.getX() < (width/2)) &&
			(mouseLocalLoc.getY() > (- height/2) && mouseLocalLoc.getY() < (height/2))) ||
			((mouseLocalLoc.getX() > (- turretWidth/2) && mouseLocalLoc.getX() < (turretWidth/2)) &&
			(mouseLocalLoc.getY() > (height/2) && mouseLocalLoc.getY() < (height/2 + turretLength)))){
			isIn = true;
			System.out.println("Point (" + mouseLocalLoc.getX() + "," + mouseLocalLoc.getY() + " is in the square.");
		} else {
			isIn = false;
			System.out.println("Point (" + mouseLocalLoc.getX() + "," + mouseLocalLoc.getY() + " is NOT in the square.");
		}
		
		return isIn;		
	}
}

/** Missile.java class is a subclass of Projectile.java
 *  and implements move() and setLocation(). It needs two
 *  int's and two float's to be properly instantiated.
 */ 
package a4.GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;

import a4.ICollider;

public class Missile extends Projectile{	
	
	private static int count;
	private Point lowerLeft, lowerRight, top, midLeft, midRight, upperLeft, 
			upperRight;
	private int [] xCoord, yCoord;
	private int base = 8, height = 16;
	private AffineTransform myTranslation ;
	private AffineTransform myRotation ;
	private AffineTransform myScale ;

	public Missile(int direction, int speed, int ID) {
		// Overloaded constructor sets color, lifetime, direction, 
		// speed and location.
		count = 1;
		this.setMyTanksID(ID);
		setIsBlocked(false);
		this.setMarkedForDeletion(false);
		this.setLifetime(5);
		setC(Color.red);
		this.setDirection(direction);
		this.setSpeed(speed + 30);  // missiles will go 30 faster than the tank that created it	
		
		// Setup parameters to build missile
		lowerLeft = new Point(-base/2, -height/2);
		lowerRight = new Point(base/2, -height/2);
		midLeft = new Point(-base/4, -height/4);
		midRight = new Point(base/4, -height/4);
		upperLeft = new Point(-base/4, height/3);
		upperRight = new Point(base/4, height/3);
		top = new Point(0, height/2);
		Point centerObject = new Point();
		centerObject.x = base/2;
		centerObject.y = (height)/2;
		setCenter(centerObject);
		setRadius((height)/2);
		
		xCoord = new int[7];
		yCoord = new int[7];
		xCoord[0] = lowerLeft.x;
		xCoord[1] = midLeft.x;
		xCoord[2] = upperLeft.x;		
		xCoord[3] = top.x;
		xCoord[4] = upperRight.x;
		xCoord[5] = midRight.x;
		xCoord[6] = lowerRight.x;
		yCoord[0] = lowerLeft.y;
		yCoord[1] = midLeft.y;
		yCoord[2] = upperLeft.y;
		yCoord[3] = top.y;
		yCoord[4] = upperRight.y;
		yCoord[5] = midRight.y;
		yCoord[6] = lowerRight.y;
		
		
		// Initialize the transformations applied to the missile
		myTranslation = new AffineTransform();
		myRotation = new AffineTransform();
		myScale = new AffineTransform();
	}
	
	public void move(int delay) {
		// Implements move() from Moveable(). Updates location of object.		
		// Use delay to get a fraction of time that can be used to weigh speed and direction. Every 1 second should see the
		// same distance as the Tick() command used to produce
		double dist = (((double)delay)/1000) * getSpeed();
		
		float deltaX = (float) ((float) (Math.cos( Math.toRadians(this.getDirection()))) * dist);
		float deltaY = (float) ((float) (Math.sin( Math.toRadians(this.getDirection()))) * dist);
		translate(deltaX,  deltaY);
		// If missile still has life left, decrement lifetime when 1 second has passed
		if (count % 50 == 0){
			if (getLifetime() > 0) {
				setLifetime(getLifetime() - 1);				
			}				
		}
		
		if (getLifetime() <= 0) {
				this.setMarkedForDeletion(true);
		}
		count++;
	}
	
	// AT methods
	public void rotate (double degrees) {
		myRotation.rotate (Math.toRadians(degrees));
	}
	public void scale (double sx, double sy) {
		myScale.scale (sx, sy);
	}
	public void translate (double dx, double dy) {
		myTranslation.translate (dx, dy);
		Point center = new Point();
		center.x = (int) myTranslation.getTranslateX();
		center.y = (int) myTranslation.getTranslateY();
		setCenter(center);
	}
	public void resetRotation(double degrees) {
		myRotation.setToIdentity();
		myRotation.rotate (Math.toRadians(degrees));
	}
	public AffineTransform getMyTranslation(){
		return myTranslation;
	}	
			
	public void draw (Graphics2D g2d) {
		// save the current graphics transform for later restoration
		AffineTransform saveAT = g2d.getTransform();
		// Append this shape's transforms to the graphics object's transform. Note the 
		// ORDER: Translation will be done FIRST, then Scaling, and lastly Rotation
		g2d.transform(myTranslation);
		g2d.transform(myScale);
		g2d.transform(myRotation);
		g2d.setColor(Color.black);
		
		g2d.fillPolygon(xCoord, yCoord, 7);
		
		// restore the old graphics transform (remove this shape's transform)
		g2d.setTransform (saveAT) ;
	}
	
	@Override
	public boolean collidesWith(ICollider otherObj) {
		// Checks all incoming objects against itself to see if a collision has occurred.
		// Missiles can collide only with other tanks and projectiles.
		boolean result = false;
		// Check to see if incoming object is something other than a MoveableItems.class
		if (otherObj instanceof LandscapeItems) {
			// Missiles don't collide with LandscapeItems.class. Return false. 
			return result;
		}
		// Check to see if this is the tank that fired the missile
		if (otherObj instanceof Tank) {
			if (((Tank) otherObj).getTankID() == this.getMyTanksID()) {
				// Don't want tanks' own missile to collide with itself
				return result;
			}
		}
		//Check to see if this is a projectile that was fired by your tank
		if (otherObj instanceof Projectile) {
			if (((Projectile) otherObj).getMyTanksID() == this.getMyTanksID()) {
				// No projectile should collide with another from the same tank
				return result;
			}
		}
		double dist = Math.pow((this.getCenter().x - ((GameObjects)otherObj).getCenter().x),2) + 
				Math.pow((this.getCenter().y - ((GameObjects)otherObj).getCenter().y),2);
		
		if (dist <= (Math.pow(getRadius() +((GameObjects)otherObj).getRadius(), 2))) {
			result = true;
		}
		
		return result;
	}

	@Override
	public void handleCollision(ICollider obj) {
		// If a missile collides with a tank or another missile, this missile is destroyed.
		if (obj instanceof LandscapeItems) {
			// Do nothing
		} else {
			this.setMarkedForDeletion(true);	
		}
	}
}

package a4.GameObjects;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;

import a4.ICollider;

public class SpikedGrenade extends Projectile{
	
	private Grenade myGrenade;
	private Spike [] mySpikes;	private int diameter;
	private static int count;
	private AffineTransform myTranslation, myRotation, myScale ;
	private double spikeOffset = 0 ; // current flame distance from fireball
	private double spikeIncrement = +0.05 ; // change in flame distance each tick
	private double maxSpikeOffset = 0.5 ; // max distance before reversing

	public SpikedGrenade(int direction, int speed, int ID) {	
		count = 1;
		this.setMyTanksID(ID);
		setIsBlocked(false);
		this.setMarkedForDeletion(false);
		this.setLifetime(5);
		this.setDirection(direction);
		this.setSpeed(speed + 20); 
		
		// Instantiate Transforms
		myTranslation = new AffineTransform() ;
		myRotation = new AffineTransform() ;
		myScale = new AffineTransform() ;
		
		// Initializes components of Spiked Grenade
		myGrenade = new Grenade();
		myGrenade.scale(1.2,1.2);
		mySpikes = new Spike [4];
		diameter = 12;
		Point centerObject = new Point();
		centerObject.x = diameter/2;
		centerObject.y = (diameter)/2;
		setCenter(centerObject);
		setRadius((diameter)/2);
		
		// Create four spikes, each translated “up” in Y and then scaled, and rotated into
		// position relative to the Body's origin
		Spike f0 = new Spike(); f0.translate(0, 12); f0.scale (0.7, 0.8);
		mySpikes[0] = f0 ;
		Spike f1 = new Spike(); f1.translate(0, 12);f1.rotate(-90);f1.scale(0.6, 0.7);
		mySpikes[1] = f1 ;
		Spike f2 = new Spike(); f2.translate(0, 12);f2.rotate(180);f2.scale(0.7, 0.8);
		mySpikes[2] = f2 ;
		Spike f3 = new Spike(); f3.translate(0, 12);f3.rotate(90);f3.scale(0.6, 0.7);
		mySpikes[3] = f3;		
	}
		
	public void update () {
		// update the SpikedGrenade position and orientation
		rotate(1) ;
		// update the flame positions (move them along their local Y axis)
		spikeOffset += spikeIncrement ;
		for (Spike s: mySpikes) {
			s.translate (0, spikeOffset); // this is why flames are TRANSLATED 1st
		}
		// reverse direction of flame movement for next time if we've hit the max
		if (Math.abs(spikeOffset) >= maxSpikeOffset) {
		spikeIncrement *= -1 ;
		}
	}

	@Override
	public void handleCollision(ICollider obj) {
		// If a spiked grenade collides with a tank or another missile, this grenade is destroyed.
		if (obj instanceof LandscapeItems) {
			// Do nothing
		} else {
			this.setMarkedForDeletion(true);	
		}
	}

	@Override
	public boolean collidesWith(ICollider otherObj) {
		// Checks all incoming objects against itself to see if a collision has occurred.
		// Missiles can collide only with other tanks and missiles.
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
	public void draw(Graphics2D g2d) {
		// save the current graphics transform for later restoration
		AffineTransform saveAT = g2d.getTransform() ;
		// append this shape's transforms to the graphics object's transform
		g2d.transform(myTranslation);
		g2d.transform(myRotation);
		g2d.transform(myScale);
		// draw this shape's components, modified by the updated g2d transformation
		myGrenade.draw(g2d);
		for (Spike f : mySpikes) { // draw each Flame. Recall that each Flame
			f.draw(g2d); // applies its translation first, causing it to
		} // move “up and down” on its own Y axis
		// restore the old graphics transform (remove this shape's transform)
		g2d.setTransform (saveAT) ;		
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
	
	@Override
	public void move(int delay) {
		// Implements move() from Moveable(). Updates location of object.		
		// Use delay to get a fraction of time that can be used to weigh speed and direction. Every 1 second should see the
		// same distance as the Tick() command used to produce
		double dist = (((double)delay)/1000) * getSpeed();
		
		float deltaX = (float) ((float) (Math.cos( Math.toRadians(this.getDirection()))) * dist);
		float deltaY = (float) ((float) (Math.sin( Math.toRadians(this.getDirection()))) * dist);
		float newX = (float) (myTranslation.getTranslateX() + deltaX);
		float newY = (float) (myTranslation.getTranslateY() + deltaY);
		System.out.println("The missile newX is: " + newX + " and the newY is: " + newY);
		System.out.println("Missile Location on move # " + count  + ":" + "(" + myTranslation.getTranslateX() +","+ myTranslation.getTranslateY() + ")");

		translate(deltaX,  deltaY);
		update(); // makes the spikes move in and out
		
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

}

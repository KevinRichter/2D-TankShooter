package a4.GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.File;

import a4.GameWorld;
import a4.ICollider;
import a4.Sound;
import a4.Strategy.Strategy;

public class PlayerTank extends Tank{
	
	private GameWorld gw;
	private int ticks, missileOffset;
	private Point lowerLeft, upperLeft, lowerRight, upperRight, 
				  bottomLeftTurret, bottomRightTurret, upperLeftTurret, 
				  upperRightTurret;
	private int [] xCoord, yCoord;
	private Sound myTankCrashSound;
	private Sound myMissileHitSound;
	private Sound myFireCannonSound;
	private Sound myFireGrenadeSound;
	private Sound myFirePlasmaWaveSound;
	
	private AffineTransform myTranslation;
	private AffineTransform myRotation;
	private AffineTransform myScale;
	
	
	public PlayerTank(GameWorld gameWorld, double dir, int height, int width, int turretWidth, int turretLength) {
		// All player tanks are blue
		gw = gameWorld;
		String soundDir = "." + File.separator + "sounds" + File.separator;
		String tankCrashSoundFile = "tankcrash.wav";
		String soundPath = soundDir + tankCrashSoundFile;
		myTankCrashSound = new Sound(soundPath);
		String missileHitSoundFile = "missilehit.wav";
		soundPath = soundDir + missileHitSoundFile;
		myMissileHitSound = new Sound(soundPath);
		String fireCannonSoundFile = "firecannon.wav";
		soundPath = soundDir + fireCannonSoundFile;
		myFireCannonSound = new Sound(soundPath);
		String fireGrenadeSoundFile = "grenadeLauncher.wav";
		soundPath = soundDir + fireGrenadeSoundFile;
		myFireGrenadeSound = new Sound(soundPath);
		String firePlasmaWaveSoundFile = "firePlasmaWave.wav";
		soundPath = soundDir + firePlasmaWaveSoundFile;
		myFirePlasmaWaveSound = new Sound(soundPath);
		this.setC(Color.BLUE);
		this.setIsBlocked(false);
		this.setDirection(90 - (int) dir);
		this.setSpeed(0);
		this.setMarkedForDeletion(false);	
		this.setTankID(1);
		setMissileCount(30);
		setArmorStrength(10);
		ticks = 1;
		missileOffset = height + turretLength;
		
		// Save tank dimensions
		Point ctrObject = new Point();
		this.width = width;
		this.height = height;
		this.turretLength = turretLength;
		this.turretWidth = turretWidth;
		ctrObject.x = width/2;
		ctrObject.y = (height + turretLength)/2;
		setCenter(ctrObject);
		setRadius((height + turretLength)/2);
		
		// Setup parameters to draw Tank in local space
		lowerLeft = new Point(-width/2, -height/2);
		upperLeft = new Point(-width/2, height/2);
		lowerRight = new Point(width/2, -height/2);
		upperRight = new Point(width/2, height/2);
		bottomLeftTurret = new Point(-turretWidth/2, height/2);
		bottomRightTurret = new Point(turretWidth/2, height/2);
		upperLeftTurret = new Point(-turretWidth/2, height/2 + turretLength);		
		upperRightTurret = new Point(turretWidth/2, height/2 + turretLength);
		
		xCoord = new int[8];
		yCoord = new int[8];
		xCoord[0] = lowerLeft.x;
		xCoord[1] = upperLeft.x;
		xCoord[2] = bottomLeftTurret.x;
		xCoord[3] = upperLeftTurret.x;
		xCoord[4] = upperRightTurret.x;
		xCoord[5] = bottomRightTurret.x;
		xCoord[6] = upperRight.x;
		xCoord[7] = lowerRight.x;
		yCoord[0] = lowerLeft.y;
		yCoord[1] = upperLeft.y;
		yCoord[2] = bottomLeftTurret.y;
		yCoord[3] = upperLeftTurret.y;
		yCoord[4] = upperRightTurret.y;
		yCoord[5] = bottomRightTurret.y;
		yCoord[6] = upperRight.y;
		yCoord[7] = lowerRight.y;
		
		// Instantiate AffineTransforms
		myTranslation = new AffineTransform();
		myRotation = new AffineTransform();
		myScale = new AffineTransform() ;		
	}
	
	@Override
	public void fireMissile() {
		if(getMissileCount() > 0){
			gw.createMissile(this);
			setMissileCount(getMissileCount() - 1);
			if(gw.isSound()) {
				myFireCannonSound.play();
			}
		}
		else
			System.out.println("Tank is out of missiles.");
	}

	@Override
	public void setStrategy(Strategy s) {
		// No strategy for PlayerTank at this time		
	}

	@Override
	public void invokeStrategy() {
		// No strategy for PlayerTank at this time		
	}
					
	public void draw (Graphics2D g2d) {
		// save the current graphics transform for later restoration
		AffineTransform saveAT = g2d.getTransform();
		// Append this shape's transforms to the graphics object's transform. Note the 
		// ORDER: Rotation will be done FIRST, then Scaling, and lastly Translation
		g2d.transform(myTranslation);
		g2d.transform(myScale);
		g2d.transform(myRotation);
		g2d.setColor(Color.blue);
		if (this.isSelected()) {
			// Draw a filled rectangle if tank is selected
			g2d.fillPolygon(xCoord, yCoord, 8);
		} else {
			g2d.drawPolygon(xCoord, yCoord, 8);
		}
		// restore the old graphics transform (remove this shape's transform)
		g2d.setTransform (saveAT) ;
	}

	@Override
	public boolean collidesWith(ICollider otherObj) {
		// Checks all incoming objects against itself to see if a collision has occurred.
		// Tanks can collide with anything.
		boolean result = false;
		// Check to see if this missile was fired by this tank.
		if (otherObj instanceof Missile) {
			if (((Missile) otherObj).getMyTanksID() == this.getTankID()) {
				// Don't want tanks' to collide with its own missile
				return result;
			}
		}
		double dist = Math.pow((this.getCenter().x - ((GameObjects)otherObj).getCenter().x),2) + 
				Math.pow((this.getCenter().y - ((GameObjects)otherObj).getCenter().y),2);
		
		if (dist <= (Math.pow((getRadius() +((GameObjects)otherObj).getRadius()), 2))) {
			result = true;
		}
		
		return result;
	}

	@Override
	public void handleCollision(ICollider otherObj) {
		// Response to collision with various objects		
		
		// Check to see if this is an enemy tank
		if (otherObj instanceof EnemyTank) {
			// This is an enemy tank, bounce off it
			this.setDirection(this.getDirection() - 135);
			this.resetRotation(getDirection()-90);
			if (ticks % 50 == 0) {
				this.setArmorStrength(this.getArmorStrength() - 1); 
			}
		}
		
		// Check to see if collision with LandscapeItems
		if (otherObj instanceof LandscapeItems) {
			// Tank becomes blocked. No damage to tank
			this.setDirection(this.getDirection() - 180);
			this.resetRotation(getDirection()-90);
			if(gw.isSound()) {
				myTankCrashSound.play();
			}
		}
		
		// Check to see if player missile hit enemy tank to score points
		if (otherObj instanceof Missile) {
			Missile missile = (Missile) otherObj;
			if (missile.getMyTanksID() != 1) {
				// This is an enemy missile destroy missile and dec armor
				this.setArmorStrength(this.getArmorStrength() - 2); // Lose 2 armor points
				((Projectile) otherObj).setMarkedForDeletion(true);
				if (gw.isSound()) {
					myMissileHitSound.play();
				}
			}
		}
		// Check to see if armor is at zero
		if (this.getArmorStrength() <= 0) {
			this.setMarkedForDeletion(true);
			gw.createPlayerTank();
			gw.setRemainingLives(gw.getRemainingLives() - 1);
			gw.setNewGameFlag(true);
		}
	}

	@Override
	public void move(int delay) {
		// Updates all tank locations
		// Use delay to get a fraction of time that can be used to weigh speed and direction. Every 1 second should see the
		// same distance as the Tick() command used to produce.	
		ticks++; // Keeps track of the number of times move() is called.
		
		double dist = (((double)delay)/1000) * getSpeed();
		float deltaX = (float) ((float) (Math.cos( Math.toRadians(this.getDirection()))) * dist);
		float deltaY = (float) ((float) (Math.sin( Math.toRadians(this.getDirection()))) * dist);
		
		this.translate(deltaX,  deltaY);
		
	}
	

	public void moveToLoc(Point2D p2d) {
		// Allows player to control tank direction by using the mouse
		
		float direction;
		// Rotate player tank to the point mouse clicked
		direction = (float)Math.toDegrees(Math.atan2(p2d.getY() - this.myTranslation.getTranslateY(), p2d.getX() - this.myTranslation.getTranslateX()));		
		
		this.setDirection((int)direction);
		this.resetRotation((int)direction-90);		
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
	public AffineTransform getMyTranslation() {
		// Returns the player tank world coordinates
		return myTranslation;
	}
	public AffineTransform getMyRotation() {
		// Returns the player tank world coordinates
		return myRotation;
	}
	
	public void steer(String direction2) {
		// Alters the heading of the player's tank. "l" for left
		// and "r" for right.
		if ("l".equals(direction2)) {
			if(getDirection() == 360) {
				this.setDirection(5);
				this.rotate(5);
			}
			else {
				this.setDirection(getDirection() + 5);
				this.rotate(5);
			}
		}
		if ("r".equals(direction2)) {
			if(getDirection() == 0){
				this.setDirection(355);
				this.rotate(-5);
			}
			else {
				this.setDirection(getDirection() - 5);
				this.rotate(-5);
			}
		}
		// Clear the blocked flag
		this.setIsBlocked(false);
	}

	public int getMissileOffset() {
		return missileOffset;
	}

	public void fireSpikedGrenade() {
		// Calls GameWorld and tells it to create a new spiked grenade
		gw.createSpikedGrenade(this);
		myFireGrenadeSound.play();
	}

	public void firePlasmaWave() {
		// Calls GameWorld and tells it to create a new spiked grenade
		gw.createPlasmaWave(this);
		myFirePlasmaWaveSound.play();		
	}

}

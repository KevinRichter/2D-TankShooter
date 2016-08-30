package a4.GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.util.Random;

import a4.GameWorld;
import a4.ICollider;
import a4.Iterator;
import a4.Sound;
import a4.Strategy.FireEveryTick;
import a4.Strategy.FireEveryotherTick;
import a4.Strategy.Strategy;

public class EnemyTank extends Tank{
	
	private GameWorld gw;
	private int ticks, missileOffset;
	private Random rand;
//	private static final int DEGREES = 360;
	private static final int MAX_SPEED = 20;
	private Point lowerLeft, upperLeft, lowerRight, upperRight, 
	  			  bottomLeftTurret, bottomRightTurret, upperLeftTurret, 
	  			  upperRightTurret;
	private int [] xCoord, yCoord; // Stores the points to draw the tank
	private Sound myTankCrashSound;
	private Sound myMissileHitSound;
	private Sound myFireCannonSound;
	private Sound myGrenadeHitSound;
	private Sound myPlasmaHitSound;
	private boolean initialCrash = true;
	private AffineTransform myTranslation;
	private AffineTransform myRotation;
	private AffineTransform myScale;
	
	public EnemyTank(GameWorld gameWorld, double dir, int height, int width, int turretWidth, int turretLength) {
		// All enemy tanks are red
		rand = new Random();
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
		String grenadeHitSoundFile = "grenadeExplosion.wav";
		soundPath = soundDir + grenadeHitSoundFile;
		myGrenadeHitSound = new Sound(soundPath);
		String plasmaHitSoundFile = "plasmahit.wav";
		soundPath = soundDir + plasmaHitSoundFile;
		myPlasmaHitSound = new Sound(soundPath);
		this.setC(Color.magenta);
		this.setStrategy(new FireEveryTick(this));
		this.setIsBlocked(false);
		this.setDirection(90 - (int) dir);
		this.setSpeed(rand.nextInt(MAX_SPEED -1) + 5);
		this.setMarkedForDeletion(false);	
		this.setTankID(rand.nextInt(10000000) + 2);
		setMissileCount(30);
		setArmorStrength(10);
		ticks = 1;
		missileOffset = height + turretLength; // Used when creating missiles, places them at end of turret
		
		// Save tank dimensions
		Point ctrObject = new Point();
		this.width = width;
		this.height = height;
		this.turretLength = turretLength;
		this.turretWidth = turretWidth;
		ctrObject.x = this.width/2;
		ctrObject.y = (this.height + this.turretLength)/2;
		setCenter(ctrObject);
		setRadius((this.height + this.turretLength)/2);

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
	public void setStrategy(Strategy s) {
		curStrategy = s;		
	}
	
	public String getStrategy() {
		String current = curStrategy.toString();
		return current;
	}

	@Override
	public void invokeStrategy() {
		curStrategy.apply();		
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
	
	public void draw (Graphics2D g2d) {
		// save the current graphics transform for later restoration		
		AffineTransform saveAT = g2d.getTransform();

		// Append this shape's transforms to the graphics object's transform. Note the 
		// ORDER: Rotation will be done FIRST, then Scaling, and lastly Translation
		g2d.transform(myTranslation);
		g2d.transform(myScale);		
		g2d.transform(myRotation);
		g2d.setColor(Color.red);
		if (this.isSelected()) {
			// Draw a filled rectangle if tank is selected
			g2d.fillPolygon(xCoord, yCoord, 8);
//			g2d.drawRect(0,0,20,20);
		} else {
			g2d.drawPolygon(xCoord, yCoord, 8);
//			g2d.drawRect(0,0,20,20);
		}
		// restore the old graphics transform (remove this shape's transform)
		g2d.setTransform (saveAT) ;
	}
	
	@Override
	public void handleCollision(ICollider otherObj) {
		// Response to collision with various objects		
		
		// Check to see if this is the player tank
		if (otherObj instanceof Tank) {
			// This can be any tank, bounce off it
			this.setDirection(this.getDirection() - 135);
			this.resetRotation(getDirection()-90);
			if (otherObj instanceof PlayerTank) {
				// If tank hits the players tank, lose 1 armor point per second
				if (ticks % 50 == 0) {
					this.setArmorStrength(this.getArmorStrength() - 1); 
				}
			}
		}
		
		// Check to see if collision with LandscapeItems
		if (otherObj instanceof LandscapeItems) {
			// Tank becomes blocked. No damage to tank			
			this.setIsBlocked(true);
			
			if(gw.isSound() && initialCrash) {
				myTankCrashSound.play();
				initialCrash = false;
			}
		}
		
		// Check to see if player missile hit enemy tank to score points
		if (otherObj instanceof Missile) {
			Missile missile = (Missile) otherObj;
			if (missile.getMyTanksID() == 1) {
				// This is players missile, score points and decrement enemy tank armor
				this.setArmorStrength(this.getArmorStrength() - 2); // Lose 2 armor points
				gw.setCurrentScore(gw.getCurrentScore() + 20);
				((Missile) otherObj).setMarkedForDeletion(true);
				if (gw.isSound()) {
					myMissileHitSound.play();
				}
			}
			else {
				// This is a missile from another enemy tank, no points for player
				this.setArmorStrength(this.getArmorStrength() - 2); // Lose 2 armor points				
				((Missile) otherObj).setMarkedForDeletion(true);
				if (gw.isSound()) {
					myMissileHitSound.play();
				}
			}				
		}
		
		// Check to see if player spiked grenade hit enemy tank to score points
		if (otherObj instanceof SpikedGrenade) {
			SpikedGrenade grenade = (SpikedGrenade) otherObj;
			if (grenade.getMyTanksID() == 1) {
				// This is players grenade, score points and decrement enemy tank armor
				this.setArmorStrength(this.getArmorStrength() - 3); // Lose 2 armor points
				gw.setCurrentScore(gw.getCurrentScore() + 15);
				((SpikedGrenade) otherObj).setMarkedForDeletion(true);
				if (gw.isSound()) {
					myGrenadeHitSound.play();
				}
			}
			else {
				// This is a missile from another enemy tank, no points for player
				this.setArmorStrength(this.getArmorStrength() - 3); // Lose 2 armor points				
				((SpikedGrenade) otherObj).setMarkedForDeletion(true);
				if (gw.isSound()) {
					myGrenadeHitSound.play();
				}
			}
				
		}
		
		// Check to see if player spiked grenade hit enemy tank to score points
		if (otherObj instanceof PlasmaWave) {
			PlasmaWave grenade = (PlasmaWave) otherObj;
			if (grenade.getMyTanksID() == 1) {
				// This is players grenade, score points and decrement enemy tank armor
				this.setArmorStrength(this.getArmorStrength() - 10); // Lose 2 armor points
				gw.setCurrentScore(gw.getCurrentScore() + 35);
				((PlasmaWave) otherObj).setMarkedForDeletion(true);
				if (gw.isSound()) {
					myPlasmaHitSound.play();
				}
			}
			else {
				// This is a plasma wave from another enemy tank, no points for player
				this.setArmorStrength(this.getArmorStrength() - 10); // Lose 2 armor points				
				((PlasmaWave) otherObj).setMarkedForDeletion(true);
				if (gw.isSound()) {
					myPlasmaHitSound.play();
				}
			}
				
		}
		// Check to see if armor is at zero
		if (this.getArmorStrength() <= 0) {
			this.setMarkedForDeletion(true);
			gw.createEnemyTank();
		}
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
		
		if (dist <= (Math.pow(getRadius() +((GameObjects)otherObj).getRadius(), 2))) {
			result = true;
		}		
		return result;
	}
	
	public void stalkPlayer() {
		// Used by strategies to redirect enemy tank to move to player
		// tank position
		Point playerLoc = new Point();
		float direction;
		Iterator iter = gw.passIterator();		
		
		// Find player tank
		while (iter.hasNext()) {
			GameObjects obj = (GameObjects) iter.getNext();
			if (obj instanceof PlayerTank) {
				PlayerTank player = (PlayerTank)obj;
				AffineTransform playerAT = (AffineTransform) player.getMyTranslation();
				playerLoc.x = (int) playerAT.getTranslateX();
				playerLoc.y = (int) playerAT.getTranslateY();
				break;
			}
		}
		// Rotate enemy tank at the player tank
		direction = (float)Math.toDegrees(Math.atan2(playerLoc.y - this.myTranslation.getTranslateY(), playerLoc.x - this.myTranslation.getTranslateX()));		
		
		this.setDirection((int)direction);
		this.resetRotation((int)direction-90);
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
		
		// Change enemy tank strategy every 30 seconds
		if (ticks % 1500 == 0){	
			String current = ((EnemyTank)this).getStrategy().substring(0, 25);
			if (current.equals("a3.Strategy.FireEveryTick")) {
				this.setStrategy(new FireEveryotherTick((EnemyTank) this));
			} else {
			this.setStrategy(new FireEveryTick((EnemyTank) this));
			}  
		} 
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
		// Returns the translation AffineTransform of EnemyTank
		return myTranslation;
	}
	public AffineTransform getMyRotation() {
		// Returns the translation AffineTransform of EnemyTank
		return myRotation;
	}
	
	@Override
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

	// Returns distance from center of tank to end of turret
	public int getMissileOffset() {
		return missileOffset;
	}
	
}

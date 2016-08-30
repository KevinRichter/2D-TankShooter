package a4.GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import a4.ICollider;

public class PlasmaWave extends Projectile {
	
	private static final int MaxLevel = 5;
	private int count;
	private Vector<Point> controlPoints;//, leftSubVector, rightSubVector; // Stores control points of polygon
	int [] xCoord, yCoord; // Stores values from controlPoints to enable drawing of polygon
	private AffineTransform myTranslation ;
	private AffineTransform myRotation ;
	private AffineTransform myScale ;
	
	public PlasmaWave(int direction, int speed, int ID) {
		controlPoints = new Vector<Point>();
new ArrayList<Point>();
		xCoord = new int[4];
		yCoord = new int[4];
		setControlPoints();
//		for (int i = 0; i < 4; i++){
//			Point p = new Point(0,0);
//			leftSubVector[i] = p;
//			rightSubVector[i] = p;
//		}
		
		count = 1;
		this.setMyTanksID(ID);
		setIsBlocked(false);
		this.setMarkedForDeletion(false);
		this.setLifetime(7); // Waves will last 7 seconds		
		this.setDirection(direction);
		this.setSpeed(speed + 60);  // plasma waves will go 60 faster than the tank that created it	
		Point centerObject = new Point();
		centerObject.x = 0; 	// Sets center of wave to (0,0)
		centerObject.y = 0;
		setCenter(centerObject);
		
		// Initialize the transformations applied to the missile
		myTranslation = new AffineTransform();
		myRotation = new AffineTransform();
		myScale = new AffineTransform();
	}

	@Override
	public void handleCollision(ICollider obj) {
		// If a plasma wave collides with a tank it is destroyed.
		if (obj instanceof Tank) {
			this.setMarkedForDeletion(true);				
		}
		
	}

	@Override
	public boolean collidesWith(ICollider otherObj) {
		// Checks all incoming objects against itself to see if a collision has occurred.
		// Waves can collide only with other tanks and projectiles.
		boolean result = false;
		// Check to see if incoming object is something other than a LandscapeItems
		if (otherObj instanceof LandscapeItems) {
			// Missiles don't collide with LandscapeItems. Return false. 
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
		AffineTransform saveAT = g2d.getTransform();
		// Append this shape's transforms to the graphics object's transform. Note the 
		// ORDER: Rotation will be done FIRST, then Scaling, and lastly Translation 
		g2d.transform(myTranslation);
		g2d.transform(myScale);
		g2d.transform(myRotation);
		g2d.setColor(new Color(0,51,0));
		g2d.drawPolygon(xCoord, yCoord, 4);
		g2d.setColor(new Color(255,102,78));
		this.drawBezierCurve(g2d, controlPoints, 0);
		// restore the old graphics transform (remove this shape's transform)
		g2d.setTransform (saveAT) ;		
	}

	@Override
	public void move(int delay) {
		// Implements move() from Moveable(). Updates location of object.		
		// Use delay to get a fraction of time that can be used to weigh speed and direction. Every 1 second should see the
		// same distance as the Tick() command used to produce
		double dist = (((double)delay)/1000) * getSpeed();
		
		float deltaX = (float) ((float) (Math.cos( Math.toRadians(this.getDirection()))) * dist);
		float deltaY = (float) ((float) (Math.sin( Math.toRadians(this.getDirection()))) * dist);
		translate(deltaX,  deltaY);
		// If wave still has life left, decrement lifetime when 1 second has passed
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
	
	private void setControlPoints() {
		Random rand = new Random();
		// Create 4 random points inside a 40 by 40 box
		for (int i = 0; i < 4; i++){
			xCoord[i] = rand.nextInt(20) + (i*20);
			yCoord[i] = rand.nextInt(40);
			Point p = new Point(xCoord[i], yCoord[i]);
			controlPoints.add(p);
		}
		// Find distance between two longest points and set radius to half that distance.
		// Used to determine bounding circle in collisions
		double radius = 0;
		for (int i = 0; i < 3; i++) {
			if (radius < lengthOf(controlPoints.get(i), controlPoints.get(i+1))) {
				radius = lengthOf(controlPoints.get(i), controlPoints.get(i+1));
			}
		}
		this.setRadius(radius);
	}
	

	private boolean straightEnough (Vector<Point> controlPoints) {
		double epsilon = .001;
		// find length around control polygon
		double d1 = lengthOf(controlPoints.get(0), controlPoints.get(1)) + lengthOf(controlPoints.get(1), controlPoints.get(2)) 
				+ lengthOf(controlPoints.get(2), controlPoints.get(3)); 
		// find distance directly between first and last control point
		double d2 = lengthOf(controlPoints.get(0), controlPoints.get(3)) ; 
		System.out.println("The distance from first point to last point: " + d2);

		if ( Math.abs(d1-d2) < epsilon )  {// epsilon (“tolerance”) = (e.g.) .001
			System.out.println("The difference is less than episilon");
			return true ;
		}
		else
			return false ;
	}
	
	private double lengthOf(Point p1, Point p2) {
		// Find distance between two points
		double result;
		result = Math.sqrt(((p2.x - p1.x)*(p2.x - p1.x)) + ((p2.y - p1.y)*(p2.y - p1.y)));
		return result;
	}

	private void drawBezierCurve(Graphics2D g2d, Vector<Point> controlPoints, int level){
		if(straightEnough(controlPoints) || level > MaxLevel){
			g2d.drawLine((int) ((Point) controlPoints.get(0)).getX(), (int)((Point) controlPoints.get(0)).getY(), 
					(int)((Point) controlPoints.get(3)).getX(), (int)((Point) controlPoints.get(3)).getY());
		}else{
			Vector<Point> leftSubVector = new Vector<Point>();
			leftSubVector.setSize(4);
			Vector<Point> rightSubVector = new Vector<Point>();
			rightSubVector.setSize(4);
			subDivideCurve(controlPoints, leftSubVector, rightSubVector);
			drawBezierCurve(g2d, leftSubVector, level+1);
			drawBezierCurve(g2d, rightSubVector, level+1);
		}
	}
	private void subDivideCurve(Vector<Point> Q, Vector<Point> R, Vector<Point> S){
		// Takes in a controlPoints subset = Q, and divides it in two halves so that the
	    // both left half subset, R and right half subset S make up the original curve, Q.
		// Here is the algorithm symbolically
//		R(0) = Q(0) ;
//		R(1) = (Q(0)+Q(1)) / 2.0 ;
//		R(2) = (R(1)/2.0) + (Q(1)+Q(2))/4.0 ;
//		S(3) = Q(3) ;
//		S(2) = (Q(2)+Q(3)) / 2.0 ;
//		S(1) = (Q(1)+Q(2))/4.0 + S(2)/2.0 ;
//		R(3) = (R(2)+S(1)) / 2.0 ;
//		S(0) = R(3) ;
		R.set(0, Q.get(0).getLocation());		
		Point tempPoint = new Point();
		tempPoint.setLocation((((Point) Q.get(0)).getX() + ((Point) Q.get(1)).getX()) / 2.0,
								(((Point) Q.get(0)).getY() + ((Point) Q.get(1)).getY()) / 2.0);
		R.set(1, tempPoint.getLocation());		
		tempPoint.setLocation(((Point) R.get(1)).getX() / 2.0 + ((((Point) Q.get(1)).getX() + ((Point) Q.get(2)).getX()) / 4.0),
					((Point) R.get(1)).getY() / 2.0 + ((((Point) Q.get(1)).getY() + ((Point) Q.get(2)).getY()) / 4.0));
		R.set(2, tempPoint.getLocation());		
		S.set(3, Q.get(3).getLocation());		
		tempPoint.setLocation((((Point) Q.get(2)).getX() + ((Point) Q.get(3)).getX()) / 2.0,
								(((Point) Q.get(2)).getY() + ((Point) Q.get(3)).getY()) / 2.0);
		S.set(2, tempPoint.getLocation());		
		tempPoint.setLocation(((Point) S.get(2)).getX() / 2.0 + ((((Point) Q.get(1)).getX() + ((Point) Q.get(2)).getX()) / 4.0),
					((Point) S.get(2)).getY() / 2.0 + ((((Point) Q.get(1)).getY() + ((Point) Q.get(2)).getY()) / 4.0));
		S.set(1, tempPoint.getLocation());
		tempPoint.setLocation((((Point) R.get(2)).getX() + ((Point) S.get(1)).getX()) / 2.0,
				(((Point) R.get(2)).getY() + ((Point) S.get(1)).getY()) / 2.0);
		R.set(3, tempPoint.getLocation());
		S.set(0,tempPoint.getLocation());
	}

}

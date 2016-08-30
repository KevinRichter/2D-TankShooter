/** Rocks.java is a concrete class that implements
 *  setLocation() and move() that it inherited. 
 */
package a4.GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.Random;

import a4.ICollider;

public class Rocks extends LandscapeItems {
	
	private int height;
	private int width;
	private Point centerObject;
	private static Random generator = new Random();
	private AffineTransform myTranslation ;
	private AffineTransform myRotation ;
	private AffineTransform myScale ;

	public Rocks(int transX, int transY, int rotate) {
		// Height and width should be random numbers between 1-20 inclusive
		height = (generator.nextInt(29)) + 10;
		width = (generator.nextInt(29)) + 10;
		setColor();
		
		// Define center and radius of rock for collision detection
		centerObject = new Point();
		centerObject.x = width/2;
		centerObject.y = height/2;
		setCenter(centerObject);
		if (height > width) {
			setRadius(height/2);
		} else {
			setRadius(width/2);
		}
		
		// Initialize the transformations applied to the Rock
		myTranslation = new AffineTransform();
		myRotation = new AffineTransform();
		myScale = new AffineTransform();	
		
		this.translate(transX,  transY);
		this.rotate(rotate);
	}

	private void setColor() {
		// all rocks will be gray in color
		setC(Color.GRAY);
	}

	@Override
	public void setLocation(float x, float y) {
		// Do nothing here. Rocks cannot be moved after being created.
	}
		
	// AT methods
	public void rotate (double degrees) {
		myRotation.rotate (Math.toRadians(degrees));
	}
	public void scale (double sx, double sy) {
		myScale.scale (sx, sy);
	}
	public void translate (double dx, double dy) {
//		System.out.println("Rock dx: " + dx + " dy: " + dy);
		myTranslation.translate (dx, dy);
		Point center = new Point();
		center.x = (int) myTranslation.getTranslateX();
		center.y = (int) myTranslation.getTranslateY();
		setCenter(center);
	}
	public AffineTransform getMyTranslation() {
		return myTranslation;
	}

	@Override
	public void draw (Graphics2D g2d) {
		// save the current graphics transform for later restoration
//		System.out.println("Rocks Location1: (" + g2d.getTransform().getTranslateX() +","+ g2d.getTransform().getTranslateY() + ")");

		AffineTransform saveAT = g2d.getTransform();
		// Append this shape's transforms to the graphics object's transform. Note the 
		// ORDER: Translation will be done FIRST, then Scaling, and lastly Rotation
		g2d.transform(myTranslation);
		g2d.transform(myScale);
		g2d.transform(myRotation);
		
		g2d.setColor(getC());
//		System.out.println("Rocks Location2: (" + g2d.getTransform().getTranslateX() +","+ g2d.getTransform().getTranslateY() + ")");

		g2d.fillRect(-width/2, -height/2, width, height);
		// restore the old graphics transform (remove this shape's transform)
		g2d.setTransform (saveAT) ;			
	}

	@Override
	public boolean collidesWith(ICollider otherObj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handleCollision(ICollider obj) {
		// TODO Auto-generated method stub
		
	}
	
//	// These methods find the left, right, top and bottom most points
//	// of the rock.
//	public float getLeft() {
//		// The -width/2 adjusts for center of rock
//		return (float) (myTranslation.getTranslateX() - width/2);
//	}
//
//	public float getRight() {
//		// The +width/2 adjusts for center of rock
//		return (float) (myTranslation.getTranslateX() + width/2);
//	}
//	
//	public float getTop() {
//		// The -height/2 adjusts for center of rock
//		return (float) (myTranslation.getTranslateY() - height/2);
//	}
//	
//	public float getBottom() {
//		// The +height/2 adjusts for center of rock
//		return (float) (myTranslation.getTranslateY() + height/2);
//	}
		
}

/** Trees.java class instantiates a tree game object. 
 *  It implements setColor() and overrides setLocation() that it
 *  inherited from GameObjects. It is a subclass of 
 *  LandscapeItems.java.
 */

package a4.GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.Random;

import a4.ICollider;

public class Trees extends LandscapeItems{
	
	private int diameter;
	
	private static Random generator = new Random();
	private Point centerObject;
	private AffineTransform myTranslation ;
	private AffineTransform myRotation ;
	private AffineTransform myScale ;
	
	// Constructor sets diameter, location and color
	public Trees() {		
		diameter = (generator.nextInt(24)) + 10;
		// Initialize the transformations applied to the Tree
		myTranslation = new AffineTransform();
		myRotation = new AffineTransform();
		myScale = new AffineTransform();
		setColor();
		
		// Define center and radius of rock for collision detection
		centerObject = new Point();
		centerObject.x = 0;
		centerObject.y = 0;
		setCenter(centerObject);
		setRadius(diameter/2);
	}

	private void setColor() {
		// all trees will be gray in color
		setC(Color.GREEN);
	}

	@Override
	public void setLocation(float x, float y) {
		// Do nothing here. Trees cannot be moved after being created.
	}
	
//	public String toString() {
//		// Overrides toString() to return description of object.
//		String color = this.getC().toString();
//		color = color.substring(14);
//		String myDesc = "Tree: loc=" + getX() + "," + getY() + " color=" + color + " diameter=" + diameter;
//		return myDesc;
//	}
	
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
		center.y = (int) myTranslation.getTranslateY() + diameter;
		setCenter(center);
	}
	public AffineTransform getMyTranslation() {
		return myTranslation;
	}

	@Override
	public void draw (Graphics2D g2d) {
		// save the current graphics transform for later restoration
		AffineTransform saveAT = g2d.getTransform();
//		System.out.println("Tree Location1: (" + g2d.getTransform().getTranslateX() +","+ g2d.getTransform().getTranslateY() + ")");
//		System.out.println("Tree Scale1: (" + g2d.getTransform().getScaleX() +","+ g2d.getTransform().getScaleY() + ")");

		// Append this shape's transforms to the graphics object's transform. Note the 
		// ORDER: Translation will be done FIRST, then Scaling, and lastly Rotation
		g2d.transform(myTranslation);
		g2d.transform(myScale);
		g2d.transform(myRotation);
//		System.out.println("Tree Location2: (" + g2d.getTransform().getTranslateX() +","+ g2d.getTransform().getTranslateY() + ")");
//		System.out.println("Tree Scale2: (" + g2d.getTransform().getScaleX() +","+ g2d.getTransform().getScaleY() + ")");

		g2d.setColor(Color.green);
		g2d.fillOval(-diameter/2, diameter/2, diameter, diameter);		
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
//	// of the tree. Detects a bounding rectangle.
//	public float getLeft() {
//		// The -diameter/2 adjusts for center of missile
//		return (float) (myTranslation.getTranslateX() - diameter/2);
//	}
//
//	public float getRight() {
//		// The +diameter/2 adjusts for center of missile
//		return (float) (myTranslation.getTranslateX() + diameter/2);
//	}
//	
//	public float getTop() {
//		// The -diameter/2 adjusts for center of missile
//		return (float) (myTranslation.getTranslateY() - diameter/2);
//	}
//	
//	public float getBottom() {
//		// The +diameter/2 adjusts for center of missile
//		return (float) (myTranslation.getTranslateY() + diameter/2);
//	}	
}

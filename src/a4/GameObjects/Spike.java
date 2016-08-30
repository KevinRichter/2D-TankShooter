package a4.GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;

public class Spike {
	
	private AffineTransform myTranslation, myRotation, myScale ;
	private Point lowerLeft, lowerRight, top;
	private int [] xCoord, yCoord;
	private int base = 5, height = 12;

	public Spike() {
		myTranslation = new AffineTransform() ;
		myRotation = new AffineTransform() ;
		myScale = new AffineTransform() ;
		
		// Setup parameters to build missile
		lowerLeft = new Point(-base/2, -height/2);
		lowerRight = new Point(base/2, -height/2);
		top = new Point(0, height/2);
		
		xCoord = new int[3];
		yCoord = new int[3];
		xCoord[0] = lowerLeft.x;		
		xCoord[1] = top.x;
		xCoord[2] = lowerRight.x;
		yCoord[0] = lowerLeft.y;
		yCoord[1] = top.y;
		yCoord[2] = lowerRight.y;
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
	}
	public void resetRotation(double degrees) {
		myRotation.setToIdentity();
		myRotation.rotate (Math.toRadians(degrees));
	}
	public AffineTransform getMyTranslation(){
		return myTranslation;
	}
	
	public void draw(Graphics2D g2d) {
		// save the current graphics transform for later restoration
		AffineTransform saveAT = g2d.getTransform() ;
		// Append this shape's transforms to the graphics object's transform. Note the 
		// ORDER: Translation will be done FIRST, then Scaling, and lastly Rotation
		g2d.transform(myRotation);
		g2d.transform(myScale);
		g2d.transform(myTranslation); 
		// draw this object in the defined color
		g2d.setColor(new Color(160,160,160));
		g2d.fillPolygon(xCoord, yCoord,3);
		// restore the old graphics transform (remove this shape's transform)
		g2d.setTransform (saveAT) ;
		
	}
}

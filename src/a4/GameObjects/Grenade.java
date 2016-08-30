package a4.GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Grenade {
	
	private AffineTransform myTranslation, myRotation, myScale ;
	private int diameter;

	public Grenade() {		
		diameter = 10;
		
		myTranslation = new AffineTransform() ;
		myRotation = new AffineTransform() ;
		myScale = new AffineTransform() ;
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
		g2d.transform(myTranslation);
		g2d.transform(myScale);
		g2d.transform(myRotation); 
		// draw this object in the defined color
		g2d.setColor(new Color(102,51,0));
		g2d.fillOval(-diameter/2, -diameter/2, diameter, diameter);
//		g2d.drawLine (top.x, top.y, bottomLeft.x, bottomLeft.y) ;
//		g2d.drawLine (bottomLeft.x, bottomLeft.y, bottomRight.x, bottomRight.y);
//		g2d.drawLine (bottomRight.x, bottomRight.y, top.x, top.y) ;
		// restore the old graphics transform (remove this shape's transform)
		g2d.setTransform (saveAT) ;
		
	}	
}

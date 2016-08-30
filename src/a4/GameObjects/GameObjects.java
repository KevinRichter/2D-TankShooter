/** GameObjects.java class contains the data and 
 *  one method setLocation(), that is usable 
 *  by all subclasses.
 */

package a4.GameObjects;

import java.awt.Color;
import java.awt.Point;

import a4.ICollider;

public abstract class GameObjects implements ICollider, IDrawable{

//	private float x;
//	private float y;
	private Point centerObject = new Point();
	private double radius;
	private Color c;
//	private final float SCREEN_WIDTH = 880;
//	private final float SCREEN_HEIGHT = 650;
	
	public Point getCenter() {
		return centerObject;
	}
	public void setCenter(Point p) {
		centerObject.x = p.x;
		centerObject.y = p.y;
	}
	public double getRadius() {
		return radius;
	}
	public void setRadius(double r) {
		radius = r;
	}
	   
	public Color getC() {
		return c;
	}

	public void setC(Color c) {
		this.c = c;
	}		
}

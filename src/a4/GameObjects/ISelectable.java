package a4.GameObjects;

import java.awt.geom.Point2D;

public interface ISelectable {
	// A way to mark an object as selected
	public void setSelected(boolean yesNo);
	
	// A way to test whether an object is selected
	public boolean isSelected();
	
	// A way to determine if a mouse point is "in" an object
	public boolean contains(Point2D mouseWorldLoc);
		
}

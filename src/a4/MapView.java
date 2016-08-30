/** This class extends JPanel and is responsible for
 *  displaying the game objects (Future functionality).
 *  It gets a ProxyGameWorld passed into it so that it
 *  cannot inadvertently put GameWorld.class into an invalid state.
 */
package a4;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.LineBorder;

import a4.GameObjects.GameObjects;
import a4.GameObjects.ISelectable;

public class MapView extends JPanel implements IObserver, MouseListener, MouseWheelListener, MouseMotionListener {
	
	private static final long serialVersionUID = 1L;
	private GameWorld gw;
	private Point curMousePos, prevMousePos;
	private AffineTransform worldToND, ndToScreen, theVTM, inverseVTM ;
	// world window boundaries
	private double winLeft, winWidth, winHeight, winBottom;
//	private final Dimension MAX_MAP_SIZE = new Dimension(800, 600);
	// Create a list to hold all objects to be set selectable.
	ArrayList<ISelectable> selected = new ArrayList<ISelectable>();
	
	public MapView(GameWorld gameWorld) {
//		this.setMaximumSize(MAX_MAP_SIZE);
//		setSize(MAX_MAP_SIZE);
		gw = gameWorld;
		this.setBorder(new LineBorder(Color.red));
		this.setLayout(new GridBagLayout());
		this.setBackground(new Color(255,255,204));
		addMouseListener(this);
		addMouseWheelListener(this);	
		addMouseMotionListener(this);
		
		// Instantiate all AffineTransforms
		worldToND = new AffineTransform(); 
		ndToScreen = new AffineTransform(); 
		theVTM = new AffineTransform();
	}
	
	public void setDimensions() {
		// When game starts, the window is mapped to the panel. The bottom left
		// of window is (0,0).
		winLeft = 0;
		winBottom = 0;
		winWidth = getWidth();
		winHeight = getHeight();
	}
		
	@Override
	public void update(IObservable o, Object arg) {
		if (gw.getNewGameFlag()) {
			setDimensions();
			gw.setNewGameFlag(false);
		}
		repaint();
	}
		
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g ;		
		
		// save the current graphics transform for later restoration
		AffineTransform saveAT = g2d.getTransform();
//		g2d.translate(0, getHeight());
//		g2d.scale(1, -1);
		
		
		// update the Viewing Transformation Matrix
		worldToND = buildWorldToNDXform(winWidth,winHeight,winLeft,winBottom);
		ndToScreen = buildNDToScreenXform (getWidth(),getHeight());
		theVTM = (AffineTransform) ndToScreen.clone(); 
		theVTM.concatenate(worldToND); // matrix multiply – note order!
		// concatenate the VTM onto the g2d’s current transformation
		g2d.transform (theVTM);
		
		// draw each shape, letting the g2d apply its transform (including the VTM)
		Iterator it = gw.passIterator();
		while (it.hasNext()) {
			GameObjects gwo = (GameObjects) it.getNext();
			gwo.draw(g2d);
//			System.out.println("This is a: " + gwo);
		}
		
		// restore the old graphics transform (remove this shape's transform)
		g2d.setTransform (saveAT) ;
	}

	private AffineTransform buildNDToScreenXform(double panelWidth2,
			double panelHeight2) {
		// Builds the AT that goes from Normalized Device to Screen
		ndToScreen.setToIdentity();
		ndToScreen.translate(0, panelHeight2);
		ndToScreen.scale(panelWidth2, -panelHeight2);
		return ndToScreen;
	}

	private AffineTransform buildWorldToNDXform(double winWidth2,
			double winHeight2, double winLeft2, double winBottom2) {
		// Build the AT that goes form World coordinates to Normalized Device.
		worldToND.setToIdentity();
		worldToND.scale(1/winWidth, 1/winHeight);
		worldToND.translate(-winLeft, -winBottom);		
		return worldToND;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// Moves tank to the location clicked
		if (!gw.isPaused()){
			Point2D moveToLoc = arg0.getPoint();
			try {
				inverseVTM = theVTM.createInverse();
			} catch (NoninvertibleTransformException e) { 
				System.out.println("InverseVTM could not be created!!");
			}
			Point2D mouseWorldLoc = inverseVTM.transform(moveToLoc,null);
			gw.redirectPlayer(mouseWorldLoc);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		System.out.println("mousePressed entered.");
		
		prevMousePos = arg0.getPoint();
		Point2D p = arg0.getPoint();
		try {
			inverseVTM = theVTM.createInverse();
		} catch (NoninvertibleTransformException e) { 
			System.out.println("InverseVTM could not be created!!");
		}
		Point2D mouseWorldLoc = inverseVTM.transform(p,null);
		boolean foundOne = false;
		Iterator iter = gw.passIterator();
		// Check to see if game is paused. If current in play mode, just return.
		if (!gw.isPaused()){
			return;
		}
		
		// Check to see if any objects were selected
		while (iter.hasNext()) {
			GameObjects obj = (GameObjects)iter.getNext();	
			if (obj instanceof ISelectable) {
				if (((ISelectable) obj).contains(mouseWorldLoc)) {
					foundOne = true;
					break; // Get out of loop once one is found
				}
			}
		}
		
		// Check to see if it's the first object or multiple objects
		iter = gw.passIterator();
		while (iter.hasNext()) {
			GameObjects obj = (GameObjects)iter.getNext();
			if (obj instanceof ISelectable) {
				if (((ISelectable) obj).contains(mouseWorldLoc) && selected.isEmpty()) {
					selected.add((ISelectable) obj); // This is the first selected object
				} else if (((ISelectable) obj).contains(mouseWorldLoc) && arg0.isControlDown()) {
						selected.add((ISelectable) obj); // Adds another object if CTRL is being held down
				} else if (((ISelectable) obj).contains(mouseWorldLoc) && foundOne){
					// This is the case where user clicks in a tank, but hasn't held CTRL. Same as clicking outside
					// a tank/selectable object.
					for (int i = 0; i < selected.size(); i++) {
						selected.get(i).setSelected(false);
					}
					selected.removeAll(selected);
					selected.add((ISelectable) obj);
				}
			}
		}
		
		// Go through list and set objects to selected/unselected so they can get redrawn as such.
		for (int i = 0; i < selected.size(); i++) {
			if (foundOne) {
			selected.get(i).setSelected(true);
			} else {
				selected.get(i).setSelected(false);
			}
		}
		
		// If there were no object found where mouse is then clear the list of selected
		if(!foundOne){
			selected.removeAll(selected); // If mouse clicked outside of a selectable object, clear list.
		}

		foundOne = false;
		this.repaint();		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// Zoom in/out method
		
		int numClicks = e.getWheelRotation();

		double h = winHeight;
		double w = winWidth;
		if (numClicks < 0) {			
				winLeft += w*0.05;
				winWidth -= w*0.10;
				winHeight -= h*0.10;
				winBottom += h*0.05;
		} else {
				winLeft -= w*0.05;
				winWidth += w*0.10;
				winHeight += h*0.10;
				winBottom -= h*0.05;
		}
			
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		// Pans the map
		// Get current mouse location to be able to calculate
		// new window translation.
		
		curMousePos = me.getPoint();
		int deltaX = curMousePos.x - prevMousePos.x; 
		int deltaY = curMousePos.y - prevMousePos.y; 
		winLeft -= deltaX;
		winBottom += deltaY;
		repaint();		
		prevMousePos = curMousePos;			
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		
	}

}

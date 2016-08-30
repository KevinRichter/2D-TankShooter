/** This command calls the GameWorld method that slows
 *  the players tank down by 1.
 */
package a4.Commands;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import a4.GameWorld;

public class DecreaseSpeedCommand extends AbstractAction{
	
	
	private static final long serialVersionUID = 1L;
	private GameWorld gw;

	public DecreaseSpeedCommand(GameWorld gameWorld) {
		gw = gameWorld;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(!gw.isPaused()) {
			System.out.println("The DecreaseSpeedCommand has been invoked.");
			gw.decreaseSpeed();	
		}
	}

}

/** This command calls the GameWorld method that speeds
 *  the players tank up by 1.
 */
package a4.Commands;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import a4.GameWorld;

public class IncreaseSpeedCommand extends AbstractAction{

	
	private static final long serialVersionUID = 1L;
	private GameWorld gw;
	
	public IncreaseSpeedCommand(GameWorld gameWorld) {
		gw = gameWorld;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Loops through GameObjects collection to find player tank and
		// increases speed by 1.
		if(!gw.isPaused()) {
			System.out.println("The IncreaseSpeedCommand has been invoked.");
			gw.increaseSpeed();		
		}
	}

}

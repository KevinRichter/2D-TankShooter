/** This command calls the GameWorld method that 
 *  causes the player tank to turn left.
 */
package a4.Commands;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import a4.GameWorld;

public class LeftTurnCommand extends AbstractAction{
		
	private static final long serialVersionUID = 1L;
	GameWorld gw;

	public LeftTurnCommand(GameWorld gameWorld) {
		super("Left Turn");
		gw = gameWorld;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(!gw.isPaused()) {
			System.out.println("The LeftTurnCommand has been invoked.");
			gw.leftTurn();
		}
	}
		
}



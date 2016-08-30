/** This command calls the GameWorld method that 
 *  makes the player tank fire a missile.
 */
package a4.Commands;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import a4.GameWorld;

public class FirePlayerMissileCommand extends AbstractAction {
	
	private static final long serialVersionUID = 1L;
	private GameWorld gw;

	public FirePlayerMissileCommand(GameWorld gameWorld) {
		gw = gameWorld;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(!gw.isPaused()) {
			System.out.println("The FirePlayerMissileCommand has been invoked.");
			gw.firePlayerMissile();
		}
	}

}

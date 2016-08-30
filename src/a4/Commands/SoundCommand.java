/** This command toggles the game sound on/off.
 * 
 */
package a4.Commands;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import a4.GameWorld;

public class SoundCommand extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private GameWorld gw;
	
	public SoundCommand(GameWorld gameWorld) {
		super("Sound");
		gw = gameWorld;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Toggles the sounds on and off
		gw.setTempSound(!gw.isTempSound());
		if (gw.isPaused()) {
			// If game is paused, only change the temp sound setting
			gw.setTempSound(gw.isTempSound());	
		} else {
			// In live game play, set actual sound to current temp sound
			gw.setSound(gw.isTempSound());			
		}
	}
}

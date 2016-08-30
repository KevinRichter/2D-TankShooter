/** This command currently does nothing, except report that 
 *  it has been invoked.  
 */
package a4.Commands;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import a4.GameWorld;

public class NewCommand extends AbstractAction {

	
	private static final long serialVersionUID = 1L;
	GameWorld gw;

	public NewCommand(GameWorld gameWorld) {
		super("New");
		gw = gameWorld;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// Future functionality will go here
		int result =JOptionPane.showConfirmDialog(null, "Would you like to play again?", 
				  "Confirm Exit",
				   JOptionPane.YES_NO_OPTION,
				   JOptionPane.QUESTION_MESSAGE);	
		if (result == JOptionPane.NO_OPTION) {
			System.exit(0);
		} else {
			gw.newGame();				
		}
		
	}
}

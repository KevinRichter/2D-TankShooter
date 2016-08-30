/** This command displays a set of key descriptions
 *  to the console.
 */
package a4.Commands;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

public class HelpCommand extends AbstractAction{

	private static final long serialVersionUID = 1L;

	public HelpCommand() {
		super("Help");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/** Help menu to describe game controls
		 */
		JOptionPane.showMessageDialog(null,	
											"KEY                      Description\n" +
											"LeftArrow          Makes the player's tank turn right.\n" +
											"RightArrow        Makes the player's tank turn left.\n" +
											"UpArrow             Increases player's tank speed by 1.\n" +
											"DownArrow       Decreases player's tank speed by 1.\n" +
											"Spacebar           Fire a missile from player's tank.\n" +
											"G                           Fires a Spiked Grenade.\n" +
											"P                           Fires a Plasma Wave.\n" +
											"Left Click            Rotates player tank to that position");
						
	}

}

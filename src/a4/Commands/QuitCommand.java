/** This command displays a JOptionPane and asks 
 *  the user if they want to quit the game or not.
 */
package a4.Commands;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

public class QuitCommand extends AbstractAction  {

	
	private static final long serialVersionUID = 1L;

	public QuitCommand(){
		super("Quit");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Displays a JOptionPane and prompts the user to quit or not.
		System.out.println("The QuitCommand has been invoked.");
		int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit  ?", 
														  "Confirm Exit",
														   JOptionPane.YES_NO_OPTION,
														   JOptionPane.QUESTION_MESSAGE);	
		if (result == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
		return;
	}

}


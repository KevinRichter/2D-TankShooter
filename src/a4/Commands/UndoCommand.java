/** This command currently does nothing, except report that 
 *  it has been invoked.  
 */
package a4.Commands;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class UndoCommand extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public UndoCommand() {
		super("Undo");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// Future functionality to go here
		System.out.println("The " + e.getActionCommand() + " command has been invoked.");		
	}
}

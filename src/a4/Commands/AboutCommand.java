/** AboutCommand displays an info box that details
 *  author info and version number
 */
package a4.Commands;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class AboutCommand extends AbstractAction  {

	
	private static final long serialVersionUID = 1L;

	public AboutCommand(){
		super("About");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("The AboutCommand has been invoked.");
		JFrame frame = new JFrame();
		JOptionPane.showMessageDialog(frame, "Game Programmer: Kevin Richter\n" +
											 "Assignment for Class CSc 133\n" +
											 "Software Version 1.0");		
	}

}

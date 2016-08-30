/** This class extends JPanel and is used by Game.class.
 *  It provides a set of JButtons and links the commands
 *  that belong to them.
 */
package a4;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;

import a4.Commands.HelpCommand;
import a4.Commands.PlayPauseCommand;
import a4.Commands.QuitCommand;
import a4.Commands.ReverseCommand;
public class CommandPanel extends JPanel{
	
	private GameWorld gw;
	private Game gm;
	private static final long serialVersionUID = 1L;
	
	private JButton playPause;
	private JButton reverse;
	private JButton help;
	private JButton quit;
	
	CommandPanel(GameWorld GameWorld, Game game) {
		gw = GameWorld;
		gm = game;
		this.setBorder(new TitledBorder(" Options: "));
		this.setLayout(new GridLayout (20,1));
		
		playPause = new JButton("Pause");
		playPause.getInputMap().put(KeyStroke.getKeyStroke("SPACE"),"none");
		this.add(playPause);
		reverse = new JButton("Reverse");
		reverse.setEnabled(false);
		reverse.getInputMap().put(KeyStroke.getKeyStroke("SPACE"),"none");
		this.add(reverse);
		help = new JButton("Help");
		help.getInputMap().put(KeyStroke.getKeyStroke("SPACE"),"none");
		this.add(help);
		quit = new JButton("Quit");
		quit.getInputMap().put(KeyStroke.getKeyStroke("SPACE"),"none");
		this.add(quit);	
		
		playPause.addActionListener(new PlayPauseCommand(gw, this, gm));
		reverse.addActionListener(new ReverseCommand(gw));
		help.addActionListener(new HelpCommand());
		quit.addActionListener(new QuitCommand());
		
		setVisible(true);
	}
	
	public JButton getReverseButton() {
		return reverse;
	}
	
	public JButton getPlayPauseButton() {
		return playPause;
	}

}

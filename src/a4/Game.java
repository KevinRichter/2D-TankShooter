/** Game.java is the Controller for the Tanks game.
 *  It is responsible for getting user commands and
 *  making the corresponding calls to GameWorld.java. 
 */
package a4;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.*;

import a4.Commands.AboutCommand;
import a4.Commands.DecreaseSpeedCommand;
import a4.Commands.FirePlayerMissileCommand;
import a4.Commands.GrenadeCommand;
import a4.Commands.IncreaseSpeedCommand;
import a4.Commands.LeftTurnCommand;
import a4.Commands.NewCommand;
import a4.Commands.PlasmaWaveCommand;
import a4.Commands.QuitCommand;
import a4.Commands.RightTurnCommand;
import a4.Commands.SaveCommand;
import a4.Commands.SoundCommand;
import a4.Commands.UndoCommand;

public class Game extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final int DELAY_IN_MSEC = 20;
	private GameWorld gw;
	private MapView mv;
	private ScoreView sv;
	private CommandPanel cp;
	private Timer timer;
//	private int panelWidth, panelHeight;
	
	public Game(){
		
		gw = new GameWorld();
			
		// Create the main frame and it's components
		setTitle ("Tank Game");
		setSize (1000,800);
		setLocation(200, 20);
		
		cp = new CommandPanel(gw, this);
		mv = new MapView(gw);		
		gw.addObserver(mv); // Controller registers view
		sv = new ScoreView(); 	
		gw.addObserver(sv); // Controller registers view
		
		// Create and place new components
		// Score view will occupy the top of the screen
		this.add(sv,BorderLayout.NORTH);
		// Map view will be in the center of the layout
		this.add(mv,BorderLayout.CENTER);
		// Command panel that holds buttons, located on left of frame
		this.add(cp,BorderLayout.WEST);
		
		setDefaultCloseOperation (EXIT_ON_CLOSE);
		buildMenuBar();		
		
		
		timer = new Timer(DELAY_IN_MSEC, this);
		timer.start();		
				
		setVisible(true);
		mv.setDimensions(); // Sets game window to starting position
		
		// Set up key bindings
		
		int mapName = JComponent.WHEN_IN_FOCUSED_WINDOW;
		InputMap imap = mv.getInputMap(mapName);
			
		KeyStroke spaceKey = KeyStroke.getKeyStroke("SPACE");
		imap.put(spaceKey,"fire");
		KeyStroke upKey = KeyStroke.getKeyStroke("UP");
		imap.put(upKey, "faster");
		KeyStroke downKey = KeyStroke.getKeyStroke("DOWN");
		imap.put(downKey, "slower");
		KeyStroke leftKey = KeyStroke.getKeyStroke("LEFT");
		imap.put(leftKey, "left");
		KeyStroke rightKey = KeyStroke.getKeyStroke("RIGHT");
		imap.put(rightKey, "right");
		KeyStroke gKey = KeyStroke.getKeyStroke('g');
		imap.put(gKey, "grenade");
		KeyStroke pKey = KeyStroke.getKeyStroke('p');
		imap.put(pKey, "plasma");
				
		ActionMap amap = mv.getActionMap();
		amap.put("fire",  new FirePlayerMissileCommand(gw));
		amap.put("faster", new IncreaseSpeedCommand(gw));
		amap.put("slower", new DecreaseSpeedCommand(gw));
		amap.put("left", new LeftTurnCommand(gw));
		amap.put("right", new RightTurnCommand(gw));
		amap.put("grenade", new GrenadeCommand(gw));
		amap.put("plasma", new PlasmaWaveCommand(gw));
		this.requestFocus();
			
		
	}	
	
		
	private void buildMenuBar() {
		JMenuBar bar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		bar.add(fileMenu);
		JMenu commandMenu = new JMenu("Command");
		bar.add(commandMenu);
		JMenuItem newItem = new JMenuItem("New");
		JMenuItem saveItem = new JMenuItem("Save");
		JMenuItem undoItem = new JMenuItem("Undo");
		JCheckBoxMenuItem soundItem = new JCheckBoxMenuItem("Sound");
		soundItem.setMnemonic(KeyEvent.VK_S);
		soundItem.setSelected(true);
		JMenuItem aboutItem = new JMenuItem("About");
		JMenuItem quitItem = new JMenuItem("Quit");
				
		fileMenu.add(newItem);
		fileMenu.add(saveItem);
		fileMenu.add(undoItem);
		fileMenu.add(soundItem);
		fileMenu.add(aboutItem);
		fileMenu.add(quitItem);
		setJMenuBar(bar);
		
		NewCommand newCommand = new NewCommand(gw);
		newItem.setAction(newCommand);
		SaveCommand saveCommand = new SaveCommand();
		saveItem.setAction(saveCommand);
		UndoCommand undoCommand = new UndoCommand();
		undoItem.setAction(undoCommand);
		SoundCommand soundCommand = new SoundCommand(gw);
		soundItem.setAction(soundCommand);
		AboutCommand aboutCommand = new AboutCommand();
		aboutItem.setAction(aboutCommand);
		QuitCommand quitCommand = new QuitCommand();
		quitItem.setAction(quitCommand);
		
		this.setJMenuBar(bar);
			
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		// Tick() method in GameWorld moves all movable objects,
		// then checks for any collisions, then notifies MapView,
		// which then repaints all objects.
		gw.tick(DELAY_IN_MSEC); 
		
	}
	
	// Allows for the PlayPauseCommand to start and stop timer.
	public void stopTimer() {
		timer.stop();
		gw.setIsPaused(true);
	}
	
	public void startTimer() {
		timer.start();
		gw.setIsPaused(false);
	}
		
}


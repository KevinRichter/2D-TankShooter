package a4.Commands;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;

import a4.CommandPanel;
import a4.Game;
import a4.GameWorld;

public class PlayPauseCommand extends AbstractAction {
	
	private static final long serialVersionUID = 1L;
	private CommandPanel cp;
	private GameWorld gw;
	private Game gm;
	
	public PlayPauseCommand(GameWorld gameWorld, CommandPanel commandPanel, Game game) {
		cp = commandPanel;
		gw = gameWorld;
		gm = game;	
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// Note: The test of the button reflects the opposite of the current game mode.
		JButton playPause = cp.getPlayPauseButton();
		JButton reverse = cp.getReverseButton();
		if (playPause.getText().equals("Pause")) { // This means the game is currently playing
			playPause.setText("Play"); // Change label to indicate game is paused
			reverse.setEnabled(true); // Enables the reverse button during paused game play.
			gw.setTempSound(gw.isSound()); // Save current sound value before pausing
			gw.setSound(false); // then turns sound off when paused
			gm.stopTimer(); // Stops game timer, no more tick events
			gw.setIsPaused(true); // Lets Game know that it is paused
		} else {
			playPause.setText("Pause"); // Change label to indicate game is in play
			gw.setIsPaused(false); // Lets Game know that it is not paused
			gw.setSound(gw.isTempSound()); // Restores sound to either pre-paused status or if change was made during paused play
			reverse.setEnabled(false); // Disables the reverse button during live game play.
			gw.setDeselectedAll();
			gm.startTimer();
		}		
	}

}

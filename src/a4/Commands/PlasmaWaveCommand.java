package a4.Commands;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import a4.GameWorld;

public class PlasmaWaveCommand extends AbstractAction{
	
	private static final long serialVersionUID = 1L;
	private GameWorld gw;
	
	public PlasmaWaveCommand(GameWorld gameWorld) {
		gw = gameWorld;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(!gw.isPaused()) {
			System.out.println("The PlasmaWaveCommand has been invoked.");
			gw.firePlasmaWave();
		}
		
	}

}

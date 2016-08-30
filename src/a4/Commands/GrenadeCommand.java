package a4.Commands;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import a4.GameWorld;

public class GrenadeCommand extends AbstractAction {
	
	private static final long serialVersionUID = 1L;
	private GameWorld gw;

	public GrenadeCommand(GameWorld gameWorld) {
		gw = gameWorld;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(!gw.isPaused()) {
			System.out.println("The GrenadeCommand has been invoked.");
			gw.fireSpikedGrenade();
		}
	}

}
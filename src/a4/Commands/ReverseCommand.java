package a4.Commands;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import a4.GameWorld;
import a4.Iterator;
import a4.GameObjects.EnemyTank;
import a4.GameObjects.GameObjects;
import a4.GameObjects.PlayerTank;
import a4.GameObjects.Tank;

public class ReverseCommand extends AbstractAction {

	
	private static final long serialVersionUID = 1L;
	GameWorld gw;
	
	public ReverseCommand(GameWorld gameWorld) {
		gw = gameWorld;
		
		this.setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (gw.isPaused()) {
			this.setEnabled(true);
			Iterator iter = gw.passIterator();
			
			// Loop through GameWorld collection and find tanks and reverse their direction.
			while(iter.hasNext()) {
				GameObjects obj = (GameObjects) iter.getNext();
				if (obj instanceof Tank) {
					((Tank) obj).setDirection(((Tank) obj).getDirection() - 180);
					if (obj instanceof PlayerTank) {
						((PlayerTank) obj).rotate(180);
					} else {
						((EnemyTank) obj).rotate(180);
					}
				}
			}
		} else {
			this.setEnabled(false);
		}

	}

}

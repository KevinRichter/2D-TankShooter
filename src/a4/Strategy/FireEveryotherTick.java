/** This is a strategy that is used by the EnemyTank.class.
 *  Its purpose is to have the tank fire a missile at every
 *  other clock tick.
 */
package a4.Strategy;

import a4.GameObjects.EnemyTank;
import a4.GameObjects.Tank;


public class FireEveryotherTick implements Strategy {
	private int ticks = 1;
	private final int DELAY = 400; // Equals an 8 second delay
	private Tank tank;
	
	public FireEveryotherTick(EnemyTank t) {		
		tank = t;
	}

	@Override
	public void apply() {
		// Fire a missile every DELAY number of clock ticks
		if (ticks % DELAY == 0) {
			tank.fireMissile();
		}
		
		// Check every tick to see if tank is blocked
		if(tank.getIsBlocked()) {
			tank.steer("r");
			tank.steer("r");
			tank.steer("r");
			tank.move(50);
		}
		ticks++;
	}

}

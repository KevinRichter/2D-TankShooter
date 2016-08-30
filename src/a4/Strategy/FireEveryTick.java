/** This is a strategy that is used by the EnemyTank.class.
 *  Its purpose is to have the tank fire a missile at every
 *  clock tick.
 */
package a4.Strategy;

import a4.GameObjects.EnemyTank;
import a4.GameObjects.Tank;

public class FireEveryTick implements Strategy{

	private Tank tank;
	private int tick = 1;
	private final int DELAY = 200; // Equals 4 second delay
	
	public FireEveryTick(EnemyTank t) {
		tank = t;
	}

	@Override
	public void apply() {
		// Fire a missile every DELAY number of clock ticks
		if (tick % DELAY == 0) {
			tank.fireMissile();
		}
		
		// Have Enemy tanks adjust course once every second
		if (tick % DELAY/100 == 0) {
			((EnemyTank) tank).stalkPlayer(); // Makes enemy tanks track the player
		}
		
		// Check every tick to see if tank is blocked
		if(tank.getIsBlocked()) {
			tank.steer("l");
			tank.steer("l");
			tank.steer("l");
			tank.move(50);
		}
		
		
		tick++;
	}

}

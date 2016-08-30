/** GameWorld.java is the model for the Tanks game.
 *  The constructor initializes the game data and
 *  creates a collection of game objects. The methods allow 
 *  the controller to manipulate the game world.
 */
package a4;

import java.awt.geom.Point2D;
import java.io.File;
import java.util.Random;
import java.util.Vector;

import javax.swing.JOptionPane;

import a4.GameObjects.EnemyTank;
import a4.GameObjects.GameObjects;
import a4.GameObjects.ISelectable;
import a4.GameObjects.Missile;
import a4.GameObjects.MoveableItems;
import a4.GameObjects.PlasmaWave;
import a4.GameObjects.PlayerTank;
import a4.GameObjects.Rocks;
import a4.GameObjects.SpikedGrenade;
import a4.GameObjects.Tank;
import a4.GameObjects.Trees;
import a4.Strategy.FireEveryTick;
import a4.Strategy.FireEveryotherTick;

public class GameWorld implements IObservable, IGameWorld{
	/** code here to hold and manipulate 
	*   world objects and related game state data
	*/ 
	
	private int elapsedTime;
	private int remainingLives;
	private int currentScore;
	private boolean newGame; // Used to let MapView know to reset game window
	private boolean sound; // flag for sound on/off
	private boolean tempSound; // Keeps track of sound while game is paused
	private static int count; // Used by move to track number of times called.
	private boolean isPaused;
	private Random randomWorldLocX, randomWorldLocY, randomWorldDir;

	private Sound myBackgroundSound;  // Plays background loop
	
	private GameObjectCollection goc;; 
	private Vector<IObserver> myObserverList;
			
	public GameWorld(){
		myObserverList = new Vector<IObserver>();
		goc = new GameObjectCollection();
		randomWorldLocX = new Random();
		randomWorldLocY = new Random();
		randomWorldDir = new Random();
		elapsedTime = 0;
		isPaused = false;
		remainingLives = 3;
		currentScore = 0;
		count = 1;
		String soundDir = "." + File.separator + "sounds" + File.separator;
		String backgroundSoundFile = "background.wav";
		String soundPath = soundDir + backgroundSoundFile;
		myBackgroundSound = new Sound(soundPath);
		this.setSound(true); // Starts game with sounds on
		this.setTempSound(true); // Parallels Sound at start of program
		
		// Create GameObjects
		int rocks = 60;
		int trees = 50;
		int enemyTanks = 10;
		double direction;
		// Place one tank close to player
		direction = randomWorldDir.nextDouble() * 360;
		EnemyTank eTanks = new EnemyTank(this, direction, 25, 20, 8, 14);
		eTanks.translate(200, 200);
		eTanks.rotate(direction);
		eTanks.setStrategy(new FireEveryTick(eTanks));
		goc.add(eTanks);
		for(int i = 0; i < enemyTanks; i++){
			// Create enemy tanks 
			direction = randomWorldDir.nextDouble() * 360;
			eTanks = new EnemyTank(this, direction, 25, 20, 8, 14);
			// Place tank in the world with random location and direction
			eTanks.translate((randomWorldLocX.nextInt(6000) - 3000), (randomWorldLocY.nextInt(6000) - 3000));
			eTanks.rotate(direction);
			
			eTanks.setStrategy(new FireEveryTick(eTanks));
			goc.add(eTanks);
		}
		
		for(int i = 0; i < rocks; i++){
			// Create rocks 
			Rocks rock = new Rocks(randomWorldLocX.nextInt(6000) - 3000, randomWorldLocY.nextInt(6000) - 3000, (int)randomWorldDir.nextDouble() * 360 );
			// Place rocks in world at random locations and random angles.
			rock.rotate(randomWorldDir.nextDouble() * 360 );		
			goc.add(rock);			
		}
		
		for(int i = 0; i < trees; i++){
			// Create trees
			Trees tree = new Trees();
			// Place trees in world at random locations.
			tree.translate(randomWorldLocX.nextInt(6000) - 3000, randomWorldLocY.nextInt(6000) - 3000);
			goc.add(tree);			
		}
		
		// Create a player tank and add to gameObjects
		// Player tank is designated a different color from enemy tanks.
		direction = 0;
		PlayerTank playerTank = new PlayerTank(this, direction, 25, 20, 8, 14);
		playerTank.translate(400, 300);
		playerTank.rotate(0);
		
		goc.add(playerTank);
	}	
	
	public void createMissile(Tank tank) {
		// Creates a missile for the tank that was passed into method
		Missile missile = new Missile(tank.getDirection(), tank.getSpeed(), tank.getTankID());
		if (tank instanceof PlayerTank) {
			PlayerTank player = (PlayerTank) tank;
			missile.translate(player.getMyTranslation().getTranslateX() + (Math.cos(Math.toRadians(player.getDirection()))
					* player.getMissileOffset()), player.getMyTranslation().getTranslateY() 
					+ (Math.sin(Math.toRadians(player.getDirection()))* player.getMissileOffset()));
			missile.rotate(tank.getDirection()-90);
//			System.out.println("Missile location: (" + missile.getMyTranslation().getTranslateX() + "," 
//					+ missile.getMyTranslation().getTranslateY());
		} else {
			EnemyTank enemy = (EnemyTank) tank;
			missile.translate(enemy.getMyTranslation().getTranslateX() + (Math.cos(Math.toRadians(enemy.getDirection()))
					* enemy.getMissileOffset()), enemy.getMyTranslation().getTranslateY() 
					+ (Math.sin(Math.toRadians(enemy.getDirection()))* enemy.getMissileOffset()));
			missile.rotate(tank.getDirection()-90);
		}
		goc.add(missile);
	}
	
	public void createSpikedGrenade(Tank tank) {
		// Creates a spiked grenade for the tank that was passed into method
		SpikedGrenade sg = new SpikedGrenade(tank.getDirection(), tank.getSpeed(), tank.getTankID());
		if (tank instanceof PlayerTank) {
			PlayerTank player = (PlayerTank) tank;
			sg.translate(player.getMyTranslation().getTranslateX() + (Math.cos(Math.toRadians(player.getDirection()))
					* player.getMissileOffset()), player.getMyTranslation().getTranslateY() 
					+ (Math.sin(Math.toRadians(player.getDirection()))* player.getMissileOffset()));
			sg.rotate(tank.getDirection()-90);
//			System.out.println("SpikedGrenade location: (" + sg.getMyTranslation().getTranslateX() + "," 
//					+ sg.getMyTranslation().getTranslateY());
		} else {
			EnemyTank enemy = (EnemyTank) tank;
			sg.translate(enemy.getMyTranslation().getTranslateX() + (Math.cos(Math.toRadians(enemy.getDirection()))
					* enemy.getMissileOffset()), enemy.getMyTranslation().getTranslateY() 
					+ (Math.sin(Math.toRadians(enemy.getDirection()))* enemy.getMissileOffset()));
			sg.rotate(tank.getDirection()-90);
		}
		goc.add(sg);
	}
	
	public void createPlasmaWave(PlayerTank tank) {
		// Creates a plasma wave for the tank that was passed into method
		System.out.println("%%%%%%%%%%%%%%%%%%% A PLASMA WAVE WAS CREATED %%%%%%%%%%%%%%%%%%%%%%%%");
		PlasmaWave pw = new PlasmaWave(tank.getDirection(), tank.getSpeed(), tank.getTankID());
		
		pw.translate(tank.getMyTranslation().getTranslateX() + (Math.cos(Math.toRadians(tank.getDirection()))
				* tank.getMissileOffset()), tank.getMyTranslation().getTranslateY() 
				+ (Math.sin(Math.toRadians(tank.getDirection()))* tank.getMissileOffset()));
		pw.rotate(tank.getDirection()-90);
		goc.add(pw);
	}
	
	public void decreaseSpeed() {
		// Finds player tank and decreases speed by 1.
		Iterator gameObjects = goc.getIterator();
		for (int i = 0; i < gameObjects.size(); i++) {
			GameObjects player = (GameObjects) gameObjects.getNext();
			if (player instanceof PlayerTank) {
				PlayerTank player1 = (PlayerTank) player;
				if (!player1.getIsBlocked()) {
					player1.setSpeed(player1.getSpeed() - 1);
				}
				else{
					System.out.println("ERROR: You are blocked. You must turn to get unblocked!");
				}
			}
		}
	}
	
//	public void displayMap() {
//		// Used by ScoreView to display map in console.
//		Iterator gameObjects = goc.getIterator();
//		while (gameObjects.hasNext()) {
//			GameObjects gmo = (GameObjects) gameObjects.getNext();
//			
//		}		
//	}
	
	public void enemySwitch() {
		// Finds all enemy tanks and switches their strategy.
		Iterator enemyTanks = goc.getIterator();
		while (enemyTanks.hasNext()) {
			GameObjects enemy = (GameObjects) enemyTanks.getNext();
			if(enemy instanceof EnemyTank) {
				EnemyTank tank = (EnemyTank)enemy;
				String current = ((EnemyTank)enemy).getStrategy().substring(0, 25);
				if (current.equals("a2.Strategy.FireEveryTick")){
					((EnemyTank)enemy).setStrategy(new FireEveryotherTick(tank));
					System.out.println("The strategy has been set to FireEveryotherTick.");
				} 
				else {
					((EnemyTank)enemy).setStrategy(new FireEveryTick(tank));
					System.out.println("The strategy has been set to FireEveryTick.");
				}
			}			
		}
	}
	
	public void firePlayerMissile() {
		// Finds the player tank and calls its fireMissile() method.
		Iterator gameObjects = goc.getIterator();
		while (gameObjects.hasNext()) {
			Object player = gameObjects.getNext();
			if (player instanceof PlayerTank) {
				((PlayerTank) player).fireMissile();
			}
		}
	}	
	
	public void increaseSpeed() {
		// Finds player tank and increases speed by 1.
		Iterator gameObjects = goc.getIterator();
		for (int i = 0; i < gameObjects.size(); i++) {
			GameObjects player = (GameObjects) gameObjects.getNext();
			if (player instanceof PlayerTank) {
				PlayerTank player1 = (PlayerTank) player;
				if (!player1.getIsBlocked()) {
					player1.setSpeed(player1.getSpeed() + 1);
				}
				else{
					System.out.println("ERROR: You are blocked. You must turn to get unblocked!");
				}
			}
		}
	}
	
	public void leftTurn(){
		// Finds player tank and calls its steer() method to turn it left
		Iterator gameObjects = goc.getIterator();
		while (gameObjects.hasNext()) {
			Object player = gameObjects.getNext();
			if (player instanceof PlayerTank) {
				((PlayerTank) player).steer("l");
			}
		}
	}
	
	public void missileCollision() {
		/** goc will destroy 2 random missiles, if there are at least two
		 *  present in the game. If not, an error message will be produced.
		 */
		int missileCount = 0;
		Iterator gameObjects = goc.getIterator();
		// Check to make sure there are at least 2 missiles currently in the game.
		for (int i = 0; i < gameObjects.size(); i++) {
			Object missile = gameObjects.getNext();
			if (missile instanceof Missile) {
				missileCount++;
			}
		}
		if (missileCount < 2) {
			System.out.println("ERROR: Fewer than two missiles are deployed!");
		}
		else {
			// Randomize gameObjects and remove the first 2 missiles.
			missileCount = 2;
			Iterator gameObjects1 = goc.getIterator();
			gameObjects1.shuffle();
			while (gameObjects1.hasNext() && (missileCount > 0)) {
				Object missile = gameObjects1.getNext();
				if (missile instanceof Missile) {
					gameObjects1.remove();
					--missileCount;
				}
			}
		}
	}
	
	public void tankHit() {
		/** A random tank is chosen to be hit by a missile. Tank 
		 * loses 1 armorStrength or if 0 is destroyed and respawned
		 * in a random location. Missile is destroyed.
		 */
		Iterator gameObjects = goc.getIterator();
		Boolean isMissile = new Boolean(false);
		
		// Check to make sure a missile is currently in the game.
		for (int i = 0; i < gameObjects.size(); i++) {
			if (gameObjects.getNext() instanceof Missile) {
				isMissile = true;
			}
		}
		if (!isMissile) {
			System.out.println("ERROR: There are currently no missiles deployed!");
		}
		else {
			// Randomize ArrayList and destroy first missile
			Iterator gameObjects1 = goc.getIterator();
			gameObjects1.shuffle();
			Boolean isFound = new Boolean(false);
			while (gameObjects1.hasNext() && !isFound) {
				Object missile = gameObjects1.getNext();
				if (missile instanceof Missile){
					gameObjects1.remove();
					isFound = true;
				}
			}
			
			isFound = false;
			/** Get first tank from randomized ArrayList and decrement 
			* armorStrength by 1 or destroy tank if ArmorStrength is 0.
			* If the tank hit was an enemy, the player gets 20 points.
			*/
			Iterator gameObjects2 = goc.getIterator();
			while (gameObjects2.hasNext() && !isFound) {
				Object tank = gameObjects2.getNext();
				if (tank instanceof Tank){
					// If armor is already zero, destroy tank
					if (((Tank) tank).getArmorStrength() == 0) {
						// Looking to see if it's player tank
						if (tank instanceof PlayerTank) {
							gameObjects2.remove();
							// If lives remain, decrement remainingLives and spawn new player tank. 
							if (this.getRemainingLives() > 1) {
								this.setRemainingLives(this.getRemainingLives() - 1);
								System.out.println("You have died! Only " + this.getRemainingLives() + " lives left!");
								int direction = 0;
								PlayerTank newPlayerTank = new PlayerTank(this, direction, 25, 20, 8, 14);
								newPlayerTank.translate(400, 300);
								newPlayerTank.rotate(0);
								goc.add(newPlayerTank);
								isFound = true;
							}
							else {
								// Player out of lives, end game.
								System.out.print("Game Over!");
								System.exit(0);
							}
						}
						else {
							// Destroy enemy tank and spawn a new one.
							System.out.println("Enemy tank destroyed!");
							gameObjects2.remove();
							// Create enemy tanks 
							int direction = (int)randomWorldDir.nextDouble() * 360;
							EnemyTank newEnemyTank = new EnemyTank(this, direction, 25, 20, 8, 14);
							// Place tank in the world with random location and direction	
							newEnemyTank.translate(randomWorldLocX.nextInt(6000) - 3000, randomWorldLocY.nextInt(6000) - 3000);
							newEnemyTank.rotate(direction);
							goc.add(newEnemyTank);
							this.setCurrentScore(this.getCurrentScore() + 20);
							isFound = true;
						}
					}
				  	else {
					    // armorStrength > 0, so decrement tank armorStrength by 1
				  		if (tank instanceof EnemyTank) { //Enemy hit, player gets 20 more points
				  			((Tank) tank).setArmorStrength(((Tank) tank).getArmorStrength() - 1);
				  			this.setCurrentScore(this.getCurrentScore() + 20);
							isFound = true;
				  		}
				  		else { // Player tank hit, decrement armor and award no points
				  			((Tank) tank).setArmorStrength(((Tank) tank).getArmorStrength() - 1);
				  			isFound = true;
				  		}
					}
				}
			}
		}		
	}
	
	public Iterator passIterator() {
		Iterator it = goc.getIterator();
		return it;
	}
	
	public void rightTurn () {
		// Finds player tank and calls its steer() method to turn it right.
		Iterator gameObjects = goc.getIterator();
		while (gameObjects.hasNext()) {
			Object player = gameObjects.getNext();
			if (player instanceof PlayerTank) {
				((PlayerTank) player).steer("r");
			}
		}
//		notifyObservers();
	}
	
	public void tankBlocked () {
		// goc will randomly select a tank and flag it as blocked.
		// It will then set the speed of that tank to 0.
		Iterator gameObjects = goc.getIterator();
		gameObjects.shuffle();
		for (int i = 0; i < gameObjects.size(); i++) {
			GameObjects tank = (GameObjects) gameObjects.getNext();
			if (tank instanceof Tank) {
				((Tank) tank).setIsBlocked(true);
				((Tank) tank).setSpeed(0);
			}
		}
	}
	
	public void tick(int delay) {
		// The tick() method is called for every timer event from Game.java
		// actionPerformed() method. tick() updates all movable objects to 
		// update their location, has all Missiles check to see if they have
		// expired or are blocked and if so removes them from the game. It has all
		// enemy tanks invoke their strategies. It then checks to see if there were 
		// any collisions between objects and if so will make calls to those objects 
		// to handle the collision. It has all enemy tanks invoke their strategies. 
		// It then notifies MapView that a change has occurred, which then causes all
		// objects to be repainted. It also increments elapsedTime by 1.		
		
		Iterator theGameObjects = goc.getIterator();
		while (theGameObjects.hasNext()) {
			GameObjects gmo = (GameObjects) theGameObjects.getNext();
			if(gmo instanceof MoveableItems) {
				((MoveableItems) gmo).move(delay); // Passing delay to move() will require move()
												   // to use delay in it's calculation's				
				if(gmo instanceof EnemyTank) {  
					((EnemyTank)gmo).invokeStrategy(); // Invoke strategy every tick. Strategies account for real time.
				}
			}
		}		
		
		// This nested while loop will compare all combinations
		// of game objects. It will then check to see if they have collided
		// and if so, they get handled by response().
		Iterator it1 = goc.getIterator();
		while (it1.hasNext()) {
			ICollider curObj = (ICollider) it1.getNext();
			Iterator it2 = goc.getIterator();
			while (it2.hasNext()) {
				ICollider otherObj = (ICollider) it2.getNext();//insure it's not the SAME object
				if (otherObj != curObj) { //check for collision and handle it
					if (curObj.collidesWith(otherObj)) {
						curObj.handleCollision(otherObj);
					}
				}
			}
		}	
		
		// Remove all objects marked for deletion
		it1 = goc.getIterator();
		while(it1.hasNext()) {
			GameObjects obj = (GameObjects) it1.getNext();
			if(obj instanceof MoveableItems) {
				// Objects are marked for deletion within collision handling
				if(((MoveableItems) obj).isMarkedForDeletion()) {
					it1.remove();
				}
			}
		}
	
		if (count % 50 == 0) { // Check to see if 1 second has passed
			setElapsedTime(getElapsedTime() + 1); // will trigger notifyObservers()
		}
		else {
			notifyObservers();
		}
		count++; // increment count each time Tick() is called
	}
	
	@Override
	public void addObserver(IObserver obs) {
		myObserverList.add(obs);		
	}

	@Override
	public void notifyObservers() {
		ProxyGameWorld proxy = new ProxyGameWorld(this);
		for (IObserver obs : myObserverList) {
			obs.update(proxy, null);
		}
	}
	
	public int getElapsedTime() {
		return elapsedTime;
	}
	
	public void setElapsedTime(int time) {
		elapsedTime = time;
		notifyObservers();
	}
	
	public int getRemainingLives() {
		return remainingLives;
	}
	
	public void setRemainingLives(int lives) {
		remainingLives = lives;
		if (remainingLives == 0) {
			setSound(false);
			int result =JOptionPane.showConfirmDialog(null, "GAME OVER! Would you like to play again?", 
					  "Confirm Exit",
					   JOptionPane.YES_NO_OPTION,
					   JOptionPane.QUESTION_MESSAGE);	
			if (result == JOptionPane.NO_OPTION) {
				System.exit(0);
			} else {
				newGame();				
			}
		}
		notifyObservers();
	}
	
	public int getCurrentScore() {
		return currentScore;
	}
	
	public void setCurrentScore(int score) {
		currentScore = score;
		notifyObservers();
	}

	public boolean isSound() {
		return sound;
	}

	public void setSound(boolean sound) {
		this.sound = sound;
		if (sound && !isPaused) {
			myBackgroundSound.loop();
		}
		else {
			myBackgroundSound.stop();
		}
		notifyObservers();
	}

	public void createEnemyTank() {
		// Creates a new player tank
		int direction = (int)randomWorldDir.nextDouble() * 360;
		EnemyTank newEnemyTank = new EnemyTank(this, direction, 25, 20, 8, 14);
		// Place tank in the world with random location and direction	
		newEnemyTank.translate(randomWorldLocX.nextInt(6000) - 3000, randomWorldLocY.nextInt(6000) - 3000);
		newEnemyTank.rotate(direction);
		goc.add(newEnemyTank);
	}
	
	public void createPlayerTank() {
		// Creates a new player tank
		int direction = 0;
		PlayerTank newPlayerTank = new PlayerTank(this, direction, 25, 20, 8, 14);
		newPlayerTank.translate(400, 300);
		newPlayerTank.rotate(0);
		goc.add(newPlayerTank);		
	}

	public boolean isPaused() {
		// This is a flag that tells whether the GameWorld is paused or not.
		return isPaused;
	}
	
	public void setIsPaused (boolean status) {
		// Sets the game paused flag.
		isPaused = status;
	}

	public boolean isTempSound() {
		// Returns a boolean for a temporary sound flag
		return tempSound;
	}

	public void setTempSound(boolean tempSound) {
		this.tempSound = tempSound;
	}
	
	public void newGame() {
		goc = new GameObjectCollection();
		elapsedTime = 0;
		isPaused = false;
		remainingLives = 3;
		currentScore = 0;
		count = 1;
		this.setSound(true); // Starts game with sounds on
		this.setTempSound(true); // Parallels Sound at start of program
		this.setNewGameFlag(true);
		
		// Create GameObjects
		int rocks = 50;
		int trees = 40;
		int enemyTanks = 10;
		double direction;
		// Place one tank close to player
		direction = randomWorldDir.nextDouble() * 360;
		EnemyTank eTanks = new EnemyTank(this, direction, 25, 20, 8, 14);
		eTanks.translate(200, 200);
		eTanks.rotate(direction);
		eTanks.setStrategy(new FireEveryTick(eTanks));
		goc.add(eTanks);		
		for(int i = 0; i < enemyTanks; i++){
			// Create enemy tanks 
			direction = randomWorldDir.nextDouble() * 360;
			eTanks = new EnemyTank(this, direction, 25, 20, 8, 14);
			// Place tank in the world with random location and direction	
			eTanks.translate(randomWorldLocX.nextInt(6000) - 3000, randomWorldLocY.nextInt(6000) - 3000);
			eTanks.rotate(direction);
			
			eTanks.setStrategy(new FireEveryTick(eTanks));
			goc.add(eTanks);
		}
		
		for(int i = 0; i < rocks; i++){
			// Create rocks 
			Rocks rock = new Rocks(randomWorldLocX.nextInt(6000) - 3000, randomWorldLocY.nextInt(6000) - 3000, (int)randomWorldDir.nextDouble() * 360 );
			// Place rocks in world at random locations and random angles.
			rock.rotate(randomWorldDir.nextDouble() * 360 );		
			goc.add(rock);			
		}
		
		for(int i = 0; i < trees; i++){
			// Create trees
			Trees tree = new Trees();
			// Place trees in world at random locations.
			tree.translate(randomWorldLocX.nextInt(6000) - 3000, randomWorldLocY.nextInt(6000) - 3000);
			goc.add(tree);			
		}
		
		// Create a player tank and add to gameObjects
		// Player tank is designated a different color from enemy tanks.
		direction = 0;
		PlayerTank playerTank = new PlayerTank(this, direction, 25, 20, 8, 14);
		playerTank.translate(400, 300);
		playerTank.rotate(0);
		
		goc.add(playerTank);
	}

	public void setNewGameFlag(boolean b) {
		newGame = b;		
	}
	public boolean getNewGameFlag() {
		return newGame;
	}

	public void fireSpikedGrenade() {
		// Finds player tank and calls its fireSpikedGrenade() method
		Iterator gameObjects = goc.getIterator();
		while (gameObjects.hasNext()) {
			Object player = gameObjects.getNext();
			if (player instanceof PlayerTank) {
				((PlayerTank) player).fireSpikedGrenade();
			}
		}
		
	}
	
	public void firePlasmaWave() {
		// Finds player tank and calls its firePlasmaWave() method
		Iterator gameObjects = goc.getIterator();
		while (gameObjects.hasNext()) {
			Object player = gameObjects.getNext();
			if (player instanceof PlayerTank) {
				((PlayerTank) player).firePlasmaWave();
			}
		}
	}
	

	public void setDeselectedAll() {
		// When game is coming out of "pause", all selected objects must be set 
		// back to unselected.
		Iterator gameObjects = goc.getIterator();
		while (gameObjects.hasNext()) {
			GameObjects gmo = (GameObjects) gameObjects.getNext();
			if (gmo instanceof ISelectable) { 
				((ISelectable) gmo).setSelected(false);
			}
		}
	}

	public void redirectPlayer(Point2D moveToLoc) {
		
		Iterator iter = goc.getIterator();
		while (iter.hasNext()) {
			GameObjects gmo = (GameObjects) iter.getNext();
			if(gmo instanceof PlayerTank) {
				PlayerTank player= (PlayerTank) gmo;
				player.moveToLoc(moveToLoc);
			}
		}
		
	}
}


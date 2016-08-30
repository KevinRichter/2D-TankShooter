/** This is a proxy for GameWorld.java. Please see documentation
 *  in GameWorld.java for explanation of methods.
 */
package a4;

public class ProxyGameWorld implements IObservable, IGameWorld{
	
	private int elapsedTime;
	private int remainingLives;
	private int currentScore;
	private boolean sound = false; // new for a2. flag for sound on/off
	GameWorld gw;
	
	public ProxyGameWorld(GameWorld gameWorld) {
		gw = gameWorld;
		elapsedTime = gw.getElapsedTime();
		remainingLives = gw.getRemainingLives();
		currentScore = gw.getCurrentScore();
		sound = gw.isSound();
	}
	
	public void addObserver(IObserver obs) {}
	public void notifyObservers() {}
	public int getElapsedTime() { return elapsedTime; }
	public int getRemainingLives() { return remainingLives; }
	public int getCurrentScore() { return currentScore; }
	public boolean isSound() { return sound; }
	public void setSound(boolean s) {}	
	public void setElapsedTime(int time) {}
	public void setRemainingLives(int lives) {}
	public void setCurrentScore(int score) {}
//	public void displayMap() { gw.displayMap();	}	
	//public Iterator passIterator() { return gw.passIterator(); }
}

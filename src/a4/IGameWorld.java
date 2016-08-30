/** This interface provides the public methods required to
 *  implement an GameWorld.class.
 */
package a4;

public interface IGameWorld {
	
	public void addObserver(IObserver obs);
	public void notifyObservers(); 
	public int getElapsedTime(); 
	public void setElapsedTime(int time);
	public int getRemainingLives();
	public void setRemainingLives(int lives);
	public int getCurrentScore();
	public void setCurrentScore(int score);
	public boolean isSound();
	public void setSound(boolean sound);
}

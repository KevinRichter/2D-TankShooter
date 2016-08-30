/** This class implements ICollectiion and provides the 
 *  ability for another object to blindly create a 
 *  collection of Objects. The GameObjectIterator.class
 *  provides a custom Iterator to work on the collection. 
 */
package a4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GameObjectCollection implements ICollection{
	
	private ArrayList<Object> gameObjects;

	public GameObjectCollection() {
		
		gameObjects = new ArrayList<Object>();
	}

	// Implement the Collection methods to allow adding and getting of GameObjects
		public void add(Object newObject) {
			gameObjects.add(newObject);
		}
			
		public Iterator getIterator() {
			return new GameObjectIterator();
		}
		
		// Implement a private class GameObjectIterator
			private class GameObjectIterator implements Iterator {
				private int currElementIndex;
				
				public GameObjectIterator() {
					currElementIndex = -1;
				}
				
				public boolean hasNext() {
					if (gameObjects.size() <= 0) return false;
					if (currElementIndex == gameObjects.size() -1)
						return false;
					return true;
				}
				
				public Object getNext() {
					currElementIndex ++;
					return(gameObjects.get(currElementIndex));
				}
				
				public Object getRandom() {
					Random generator = new Random();
					int num = generator.nextInt(gameObjects.size());
					return gameObjects.get(num);
				}
				
				public void shuffle() {
					Collections.shuffle(gameObjects);
				}
				
				@Override
				public int size() {
					// TODO Auto-generated method stub
					return gameObjects.size();
				}
				
				@Override
				public void remove() {
					// TODO Auto-generated method stub
					gameObjects.remove(currElementIndex);
					currElementIndex--;
				}			
	    	}

}

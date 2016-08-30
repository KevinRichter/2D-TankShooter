/** This class extends JPanel and is a View for Game.class.
 *  It provides a score board for the game and should get
 *  updated whenever there is a change to GameWorld.class.
 */
package a4;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class ScoreView extends JPanel implements IObserver{
	
	private static final long serialVersionUID = 1L;
	private JLabel time;
	private JLabel score;
	private JLabel livesLeft;
	private JLabel sound;

	public ScoreView() {
				
		int i = 2;
		int j = 6;
		JPanel[][] panelHolder = new JPanel[i][j];    
		this.setLayout(new GridLayout(i,j));

		for(int m = 0; m < i; m++) {
		   for(int n = 0; n < j; n++) {
		      panelHolder[m][n] = new JPanel();
		      add(panelHolder[m][n]);
		   }
		}
		
		
		this.setBorder(new LineBorder(Color.BLUE,2));
		
		time = new JLabel("Elaspsed Time: 0");
		panelHolder[0][1].add(time);
		
		livesLeft = new JLabel("Lives Left: 3");
		panelHolder[0][2].add(livesLeft);
						
		score = new JLabel("Score: 0");
		panelHolder[0][3].add(score);
		
		sound = new JLabel("Sound: OFF");
		panelHolder[0][4].add(sound);	
		
		setVisible(true);
	}


	@Override
	public void update(IObservable o, Object arg) {
		// TODO Auto-generated method stub
		time.setText("ElapsedTime: " + ((ProxyGameWorld) o).getElapsedTime());
		livesLeft.setText("Lives Left: " + ((ProxyGameWorld) o).getRemainingLives());
		score.setText("Score: " + ((ProxyGameWorld) o).getCurrentScore());
		if (((ProxyGameWorld) o).isSound())
			sound.setText("Sound: ON");	
		else
			sound.setText("Sound: OFF");	
	}
}

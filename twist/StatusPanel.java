package twist;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author blackm0k
 *
 * Panel containing status information and buttons to control aspects of the game.
 */
class StatusPanel extends JPanel {
    static final long serialVersionUID = 8370369782373015293L;
 
	private int     score = 0;
	private int     timer = 0;
	private JLabel  scoreDisplay;
	private JLabel  timerDisplay;
	private JButton startButton;
	private JButton shuffleButton;

	/**
	 * Create a new StatusPanel, feeding ActionEvents generated by the buttons to the
	 * ActionListener specified.
	 * 
	 * The "Start new game" button generates an action command of "start, while the "Shuffle"
	 * button generates an action command of "shuffle".
	 * 
	 * @param al ActionLister to handle the button presses.
	 */
	StatusPanel( ActionListener al )
	{
		setLayout( new BoxLayout( this, BoxLayout.X_AXIS ) );

		scoreDisplay = new JLabel( "Score: " + String.valueOf( score ) );
		timerDisplay = new JLabel( "Time: " + String.valueOf( timer ) );

		startButton = new JButton( "Start new game" );
		startButton.setFocusable( false );
		startButton.setActionCommand( "start" );
		startButton.addActionListener( al );
		shuffleButton = new JButton( "Shuffle" );
		shuffleButton.setFocusable( false );
		shuffleButton.setActionCommand( "shuffle" );
		shuffleButton.addActionListener( al );
		
		add( startButton );
		add( Box.createRigidArea( new Dimension( 50, 0 ) ) );
		add( scoreDisplay );
		add( Box.createRigidArea( new Dimension( 50, 0 ) ) );
		add( timerDisplay );
		add( Box.createRigidArea( new Dimension( 50, 0 ) ) );
		add( shuffleButton );

		setPreferredSize( new Dimension( 300, 100 ) );
	}
	
	/**
	 * Update the score display.
	 * 
	 * @param score New score to display.
	 */
	void setScore( int score )
	{
		this.score = score;
		
		scoreDisplay.setText( "Score: " + String.valueOf( score ) );
	}
	
	/**
	 * Update the timer display.
	 * 
	 * @param timer New timer value to display.
	 */
	void setTimer( int timer )
	{
		this.timer = timer;
		
		timerDisplay.setText( "Timer: " + String.valueOf( timer ) );
	}
}
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Iterator;

/**
 * Main class for the Twist game. Contains the main method as well as the vast majority of
 * game processing.
 * 
 * @author blackm0k
 *
 */
public class Twist extends JFrame
	implements KeyListener, ActionListener
{
    static final long        serialVersionUID = -8940617936892412173L;

    // Display elements
	private WordPanel        wPanel;
	private LetterPanel      letterPool, guessArea;
	private StatusPanel      statusArea;

	// Dictionary handling objects
	private Dictionary	     dict;
	private DictionaryEntry  currentEntry;
	
	// High score manager
	private HighScoreManager highScores;

	// Elements for tracking progress within a single round
	private WordManager      wordManager;	
	private StringBuilder    poolLetters, guessLetters;
	private int              guessWordLength;
	private Timer            updateInterval;
	private boolean          longWordGuessed;
	private int              countdown;

	// Current score across all rounds
	private int              score;

	// Defined length of longest words, has implications in most aspects of the game.
	// Notably it used by the Dictionary class when generating a dictionary from a word list
	final static int         WORD_LENGTH = 7;

	// Timer length in seconds
	private final int        TIMER_LENGTH = 120;
	// Bonus for getting all the words in a set
	private final int        FINISHING_BONUS = 1024;

	/**
	 * Create a new Twist object
	 * 
	 * @param args Not used.
	 */
	public static void main( String [] args )
	{
		new Twist();
	}
	
	/**
	 * Create a new instance of the Twist game. Initialise all the display and game elements
	 * and start a new game playing.
	 */
	public Twist()
	{
		super( "Twist" );
		
		JPanel    top;

		setDefaultCloseOperation( EXIT_ON_CLOSE );
		
		createMenu();
		
		setLayout( new BorderLayout() );

		top = new JPanel();
		top.setLayout( new BoxLayout( top, BoxLayout.Y_AXIS ) );

		wPanel = new WordPanel();
		top.add( wPanel );

		top.add( Box.createRigidArea( new Dimension( 0, 30 ) ) );

		guessArea = new LetterPanel( WORD_LENGTH );
		top.add( guessArea );

		top.add( Box.createRigidArea( new Dimension( 0, 30 ) ) );

		letterPool = new LetterPanel( WORD_LENGTH );
		top.add( letterPool );

		letterPool.addKeyListener( this );
		letterPool.setFocusable( true );
		letterPool.grabFocus();
		
		statusArea = new StatusPanel( this );
		top.add( statusArea );

		add( top, BorderLayout.NORTH );

		pack();
		
		updateInterval = new Timer( 1000, this );
		updateInterval.setRepeats( true );
		updateInterval.setActionCommand( "timer0" );

		dict = new Dictionary();
		dict.loadDictionary();

		poolLetters = new StringBuilder( WORD_LENGTH );
		guessLetters = new StringBuilder( WORD_LENGTH );

		for( int i = 0; i < WORD_LENGTH; i++ )
		{
			poolLetters.append('-');
			guessLetters.append(' ');
		}
		
		highScores = new HighScoreManager();
		highScores.readFromFile();

		setVisible( true );
	}
	
	private void createMenu()
	{
		JMenuBar  menuBar;
		JMenu     menu;
		JMenuItem menuItem;

		menuBar = new JMenuBar();
		
		menu = new JMenu( "Game" );
		menu.setMnemonic( 'g' );
		menuItem = new JMenuItem( "Start new game");
		menuItem.setMnemonic( 'n' );
		menuItem.setActionCommand( "start" ); // Action common with "Start new game" button
		menuItem.addActionListener( this );
		menu.add( menuItem );
		menuItem = new JMenuItem( "Show high scores");
		menuItem.setMnemonic( 'h' );
		menuItem.setActionCommand( "highscores" );
		menuItem.addActionListener( this );
		menu.add( menuItem );
		menuItem = new JMenuItem( "Exit" );
		menuItem.setMnemonic( 'x' );
		menuItem.setActionCommand( "exit" );
		menuItem.addActionListener( this );
		menu.add( menuItem );
		menuBar.add( menu );

		menu = new JMenu( "Help" );
		menu.setMnemonic( 'h' );
		menuItem = new JMenuItem( "About" );
		menuItem.setMnemonic( 'b' );
		menuItem.setActionCommand( "about" );
		menuItem.addActionListener( this );
		menu.add( menuItem );
		menuBar.add( menu );
		
		setJMenuBar( menuBar );
	}
	
	private void showAboutDialog()
	{
		JOptionPane.showMessageDialog( this,
				                       "Twist by blackm0k",
				                       "About Twist", JOptionPane.INFORMATION_MESSAGE );
	}

	/**
	 * Start a new game with a score of zero.
	 */
	private void startNewGame()
	{
		score = 0;
		statusArea.setScore(score);

		startNewRound();
	}

	/**
	 * Give the option to start a new game when the player loses.
	 */
	private void gameOver()
	{
		JOptionPane pane;
		JDialog     dialog;
		Object      selectedValue;

		wPanel.showRemainingWords();

		// Use a custom option pane to avoid hitting enter meaning that a new game is
		// immediately started without the player being able to see what the words were
		pane = new JOptionPane( "You lose. Bad luck. Play again?",
				                JOptionPane.QUESTION_MESSAGE,
				                JOptionPane.YES_NO_OPTION,
				                null,
				                new String[] {"Yes", "No"}, null );

		dialog = pane.createDialog( "Game Over" );
		dialog.setVisible( true );

		selectedValue = pane.getValue();

		// If this is a high score, get the user's name and submit the high score
		if( highScores.isHighScore( score ) )
		{
			highScores.submitScore( HighScoreWindow.getNameFromUser(this), score );
			highScores.writeToFile();
		}

		if( selectedValue != null && selectedValue.equals( "Yes" ) ) 
			startNewGame();

/*
		int option = JOptionPane.showConfirmDialog( this,
				"You lose. Bad luck. Play again?", "Game Over",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE );



		if( option == JOptionPane.YES_OPTION )
			startNewGame();
 */
	}

	/**
	 * Begin a new round without resetting the score.
	 */
	private void continueGame()
	{
		JOptionPane pane;
		JDialog     dialog;

		wPanel.showRemainingWords();

		// Use a custom option pane to avoid hitting enter meaning that a new game is
		// immediately started without the player being able to see what the words were
		pane = new JOptionPane( "Congratulations! You made it through this round.",
				                JOptionPane.INFORMATION_MESSAGE,
				                JOptionPane.OK_OPTION,
				                null,
				                new String[] {"Okay"}, null );

		dialog = pane.createDialog( "You Win" );
		dialog.setVisible( true );

		startNewRound();

		/*
		JOptionPane.showMessageDialog( this,
				"Congratulations! You made it through this round.",
				"You Win", JOptionPane.INFORMATION_MESSAGE );

		startNewRound();
		 */
	}

	/**
	 * Begin a new round.
	 */
	private void startNewRound()
	{
		// Select a new word
		currentEntry = selectWord();

		// Create  word manager from the selected entry
		wordManager = new WordManager( currentEntry, wPanel );

		// Put the letters from the entry into the pool and shuffle them
		poolLetters.replace( 0, WORD_LENGTH, currentEntry.getHeadWord() );
		shuffleLetters( poolLetters );

		// Reset the guess area
		for( int i = 0; i < WORD_LENGTH; i++ )
			guessLetters.setCharAt( i, ' ' );

		// Update the display of both letter panels
		letterPool.update( poolLetters.toString() );
		guessArea.update( guessLetters.toString() );

		// Reset the necessary parts of the game state
		guessWordLength = 0;
		longWordGuessed = false;		

		// Start the clock ticking
		startTimer();		
	}

	/**
	 * Select an entry at random from the dictionary.
	 * 
	 * @return The selected entry
	 */
	private DictionaryEntry selectWord()
	{
		int entry = (int) (Math.random() * dict.getEntryCount());
		
		return dict.getEntry( entry );
	}

	/**
	 * Shuffle the letters in the given StringBuilder by repeatedly swapping the letters at
	 * two positions.
	 * 
	 * @param letters The StringBuilder to shuffle
	 */
	private void shuffleLetters( StringBuilder letters )
	{
		int  swap;
		char intermediate;

		for( int passes = 0; passes < 10; passes++ )
		    for( int i = 0; i < WORD_LENGTH; i++ )
			{
				swap = (int) (Math.random() * WORD_LENGTH);
				swap %= WORD_LENGTH;
				intermediate = letters.charAt(i);
				letters.setCharAt( i, letters.charAt(swap) );
				letters.setCharAt( swap, intermediate );
			}
	}

	/**
	 * Start the countdown timer.
	 */
	private void startTimer()
	{
		countdown = TIMER_LENGTH;
		updateInterval.start();
	}
	
	/**
	 * Clear all letters from the guess area
	 */
	private void deleteAllLetters() {
		// Run loop in reverse since deleteLetter will modify guessWordLength
		for( int i = guessWordLength; i > 0; i-- )
			deleteLetter();
	}

	/**
	 * Clear a single letter from the guess area
	 */
	private void deleteLetter() {

		// Do nothing if there is no letter to delete
		if( guessWordLength == 0 )
			return;
		
		for( int i = 0; i < WORD_LENGTH; i++ )
			if( poolLetters.charAt(i) == '-' )
			{
				poolLetters.setCharAt( i, guessLetters.charAt(guessWordLength - 1) );
				break;
			}

		guessLetters.setCharAt( --guessWordLength, ' ' );
	}


	private void processWord()
	{
		Iterator<String> iter;
		String           currentWord;
		String           guessedWord;
		boolean          matchFound;

		if( guessWordLength == 0 )
			return;
		
		// Remove the trailing spaces from the guessLetters string to find the guessed word
		guessedWord = guessLetters.substring(0, guessWordLength);

		iter = currentEntry.getLinkedWords().iterator();
		matchFound = false;

		// Check the guessed word against each linked word for the current entry, until a
		// match is found
		while( iter.hasNext() )
		{
			currentWord = iter.next();

			if( guessedWord.equals(currentWord) )
			{
				matchFound = true;
				break;
			}
		}

		// If a match was found and the word hasn't already been guessed, mark the word as
		// guessed, update the game state (this could potentially have finished the round),
		// and clear all letters from the guess area
		if( matchFound &&
		    ! wordManager.isWordGuessed(guessedWord) )
		{
			wordManager.setWordGuessed(guessedWord);

			updateGameState( guessedWord );

			deleteAllLetters();
		}
		// If no match found, or the word was already guessed, set the guess area's warning
		// state
		else
			guessArea.setWarning( true );
		
		// Update the display for the letter pool and the guess area
		letterPool.update( poolLetters.toString() );
		guessArea.update( guessLetters.toString() );
	}

	/**
	 * Update the game state, based on new information - either a word guessed or a change
	 * in the countdown state
	 */
	private void updateGameState()
	{
		// If all words guessed or the countdown has hit zero, the round is over
		if( wordManager.allWordsGuessed() || countdown == 0 )
		{
			updateInterval.stop();

			if( countdown == 0 && ! longWordGuessed )
				gameOver();
			else
				continueGame();
		}		
	}

	/**
	 * Update the game state when a new word is guessed. Calls updateGameState() to do the
	 * generic game state (not word-related) game state processing.
	 * 
	 * @param word The word that was guessed
	 */
	private void updateGameState( String word )
	{
		if( word.length() == WORD_LENGTH )
			longWordGuessed = true;
		
		updateScore( word );

		updateGameState();
	}

	/**
	 * Update the score given a word that has been successfully guessed.
	 * 
	 * @param word The word on which to base the score
	 */
	private void updateScore( String word )
	{
		score += Math.pow( 2, word.length() );
		
		if( wordManager.allWordsGuessed() )
			score += FINISHING_BONUS;
		
		statusArea.setScore( score );
	}

	/**
	 * Method implemented as part of KeyListener.
	 * 
	 * On entry of a character, check if it is in the letter pool and move it to the guess
	 * area if it is.
	 * On entry of a carriage return, process the guessed word - check if it is valid and
	 * handle the knock-ons dependent on that.
	 */
	public void keyTyped( KeyEvent e )
	{
		// If the timer has run out, don't process keyboard input
		if( countdown < 1 )
			return;
		
		// If the enter key was pressed, process the guessed word and do nothing else
		if( e.getKeyChar() == '\n' )
		{
			processWord();
			return;
		}

		// Shuffle the letters in the pool when spacebar is pressed
		if( e.getKeyChar() == ' ' )
		{
			shuffleLetters( poolLetters );
			letterPool.update( poolLetters.toString() );
		}

		// If the guess are word is already at maximum length, don't do any more processing
		if( guessWordLength == WORD_LENGTH )
			return;

		// Don't allow non-alphabetic input
		if( ! Character.isLetter( e.getKeyChar() ) )
			return;	

		// Clear the warning status on new input
		guessArea.setWarning( false );

		// Check if the input character is in the pool, if so move it to the guess area
		for( int i = 0; i < WORD_LENGTH; i++ )
			if( Character.toUpperCase(e.getKeyChar()) == poolLetters.charAt(i) )
			{
				guessLetters.setCharAt( guessWordLength++, poolLetters.charAt(i) );
				poolLetters.setCharAt( i, '-' );
				break;
			}

		// Update the display of both letter panels
		letterPool.update( poolLetters.toString() );
		guessArea.update( guessLetters.toString() );
	}


	/**
	 * Method implemented as part of KeyListener.
	 * 
	 * Handle a backspace press - delete a letter from the guess area
	 */
	public void keyPressed( KeyEvent e )
	{
		// If the round is over, don't allow any more changes to the guess area
		if( countdown < 1 )
			return;

		if( e.getKeyCode() == KeyEvent.VK_BACK_SPACE )
		{
			deleteLetter();

			// Clear warning on change in the guess area 
			guessArea.setWarning( false );

			letterPool.update( poolLetters.toString() );
			guessArea.update( guessLetters.toString() );
		}
	}

	/**
	 * Implemented as part of KeyListener.
	 * 
	 * Not used.
	 */
	public void keyReleased( KeyEvent e )
	{
	}

	/**
	 * Handler for ActionEvents generated by the countdown timer and the buttons in the
	 * status area.
	 */
	public void actionPerformed( ActionEvent e )
	{
		// If handling an event generated by the timer, decrement the countdown by one.
		// If the countdown reaches zero, stop the timer and handle the end of the round
		if( e.getActionCommand().equals( "timer0" ) )
		{
			countdown--;
			statusArea.setTimer( countdown );
			
			updateGameState();
		}
		// Generated by the "Start new game" button
		else if( e.getActionCommand().equals( "start" ) )
		{
			startNewGame();
		}
		// Generated by the "Shuffle" button
		else if( e.getActionCommand().equals( "shuffle" ) )
		{
			shuffleLetters( poolLetters );
			letterPool.update( poolLetters.toString() );
		}
		else if( e.getActionCommand().equals( "highscores" ) )
		{
			HighScoreWindow.showHighScoreWindow( this, highScores.getScores() );
		}
		else if( e.getActionCommand().equals( "exit" ) )
		{
			System.exit(0);
		}
		else if( e.getActionCommand().equals( "about" ) )
		{
			showAboutDialog();
		}
	}
}

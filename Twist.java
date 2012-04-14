import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Iterator;

public class Twist extends JFrame
	implements KeyListener, ActionListener
{
    static final long       serialVersionUID = -8940617936892412173L;
    
	private Dictionary	    dict;
	private WordPanel       wPanel;
	private LetterPanel     letterPool, guessArea;
	private StatusPanel     statusArea;
	private DictionaryEntry currentEntry;
	private WordManager     wordManager;
	private JPanel          top;
	private Timer           updateInterval;
	private StringBuffer    poolLetters, guessLetters;
	private int             guessWordLength;
	
	private int             score;
	private boolean         longWordGuessed;
	private int             countdown;

	private final int       WORD_LENGTH = 7;
	private final int       TIMER_LENGTH = 120;
	private final int       FINISHING_BONUS = 1024;

	public static void main( String args[] )
	{
		Twist mainWindow = new Twist();

		mainWindow.setVisible( true );
	}

	public Twist()
	{
		super( "Twist" );

		setDefaultCloseOperation( EXIT_ON_CLOSE );

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

		poolLetters = new StringBuffer( WORD_LENGTH );
		guessLetters = new StringBuffer( WORD_LENGTH );

		for( int i = 0; i < WORD_LENGTH; i++ )
		{
			poolLetters.append('-');
			guessLetters.append(' ');
		}
		
		startNewGame();
	}
	
	private void startNewGame()
	{
		score = 0;

		beginGame();
	}
	
	private void gameOver()
	{
		wPanel.showAllWords();

		int option = JOptionPane.showConfirmDialog( this,
				"You lose. Bad luck. Play again?", "Game Over",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE );
		
		if( option == JOptionPane.YES_OPTION )
			startNewGame();
	}
	
	private void continueGame()
	{
		wPanel.showAllWords();
		
		JOptionPane.showMessageDialog( this,
				"Congratulations! You made it through this round.",
				"You Win", JOptionPane.INFORMATION_MESSAGE );

		beginGame();
	}
	
	private void beginGame()
	{
		currentEntry = selectWord();
		
		wordManager = new WordManager( currentEntry, wPanel );

		poolLetters.replace( 0, WORD_LENGTH, currentEntry.getHeadWord() );
		shuffleLetters( poolLetters );
		
		for( int i = 0; i < WORD_LENGTH; i++ )
			guessLetters.setCharAt( i, ' ' );
		
		letterPool.update( poolLetters.toString() );
		guessArea.update( guessLetters.toString() );
		
		guessWordLength = 0;
		longWordGuessed = false;		
		
		initialiseTimer();		
	}
	
	private DictionaryEntry selectWord()
	{
		int entry = (int) (Math.random() * dict.getEntryCount());
		
		return dict.getEntry( entry );
	}
	
	private void shuffleLetters( StringBuffer letters )
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
	
	private void initialiseTimer()
	{
		countdown = TIMER_LENGTH;
		updateInterval.start();
	}

	public void keyTyped( KeyEvent e )
	{
		if( guessWordLength > WORD_LENGTH )
			return;
		
		if( countdown < 1 )
			return;
		
		if( e.getKeyChar() == '\n' )
		{
			processWord();
			return;
		}
		
		guessArea.setWarning( false );

		for( int i = 0; i < WORD_LENGTH; i++ )
			if( Character.toUpperCase(e.getKeyChar()) == poolLetters.charAt(i) )
			{
				guessLetters.setCharAt( guessWordLength++, poolLetters.charAt(i) );
				poolLetters.setCharAt( i, '-' );
				break;
			}

		letterPool.update( poolLetters.toString() );
		guessArea.update( guessLetters.toString() );
	}

	private void processWord()
	{
		Iterator<String> iter = currentEntry.getLinkedWords().iterator();
		String           currentWord;
		String           guessedWord;
		int              wordLength;
		boolean          matchFound = false;

		if( guessWordLength == 0 )
			return;
		
		guessedWord = guessLetters.substring(0, guessWordLength);

		while( iter.hasNext() )
		{
			currentWord = iter.next();

			if( guessedWord.equals(currentWord) )
			{
				matchFound = true;
				break;
			}
		}

		if( matchFound &&
		    ! wordManager.isWordGuessed(guessedWord) )
		{
			wordLength = guessWordLength;

			for( int i = 0; i < wordLength; i++ )
				deleteLetter();
			
			score += Math.pow( 2, wordLength );
			statusArea.setScore( score );
			
			if( wordLength == WORD_LENGTH )
				longWordGuessed = true;
			
			wordManager.setWordGuessed(guessedWord);
			
			if( wordManager.allWordsGuessed() )
			{
				updateInterval.stop();

				score += FINISHING_BONUS;
				statusArea.setScore( score );

				continueGame();
			}
		}
		else
			guessArea.setWarning( true );
		
		letterPool.update( poolLetters.toString() );
		guessArea.update( guessLetters.toString() );
	}

	public void keyPressed( KeyEvent e )
	{
		if( e.getKeyCode() == KeyEvent.VK_BACK_SPACE )
		{
			if( guessWordLength == 0 )
				return;

			deleteLetter();
			
			letterPool.update( poolLetters.toString() );
			guessArea.update( guessLetters.toString() );
			
			guessArea.setWarning( false );
		}
	}

	private void deleteLetter() {
		for( int i = 0; i < WORD_LENGTH; i++ )
			if( poolLetters.charAt(i) == '-' )
			{
				poolLetters.setCharAt( i, guessLetters.charAt(guessWordLength - 1) );
				break;
			}

		guessLetters.setCharAt( --guessWordLength, ' ' );
	}

	public void keyReleased( KeyEvent e )
	{
	}

	public void actionPerformed( ActionEvent e )
	{
		if( e.getActionCommand().equals( "timer0" ) )
		{
			countdown--;
			statusArea.setTimer( countdown );
			
			if( countdown == 0 )
			{
				updateInterval.stop();

				if( longWordGuessed )
					continueGame();
				else
					gameOver();
			}
		}
		else if( e.getActionCommand().equals( "start" ) )
		{
			startNewGame();
		}
		else if( e.getActionCommand().equals( "shuffle" ) )
		{
			shuffleLetters( poolLetters );
			letterPool.update( poolLetters.toString() );
		}
	}
}

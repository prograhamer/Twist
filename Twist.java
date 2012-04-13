import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Iterator;

public class Twist extends JFrame
	implements KeyListener, ActionListener
{
	private Dictionary	    dict;
	private LetterPanel     letterPool, guessArea;
	private WordPanel       wPanel;
	private DictionaryEntry currentEntry;
	private JPanel          top;
	private StringBuffer    poolLetters, guessLetters;
	private int             guessWordLength;

	private final int       WORD_LENGTH = 7;

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

		add( top, BorderLayout.NORTH );

		pack();

		dict = new Dictionary();
		dict.loadDictionary();

		currentEntry = dict.getEntry(0);

		wPanel.setWordList( currentEntry.getLinkedWords() );
		letterPool.update( currentEntry.getHeadWord() );

		poolLetters = new StringBuffer( currentEntry.getHeadWord() );

		guessLetters = new StringBuffer( WORD_LENGTH );
		guessWordLength = 0;

		for( int i = 0; i < WORD_LENGTH; i++ )
			guessLetters.append(' ');
	}

	public void keyTyped( KeyEvent e )
	{
		if( guessWordLength > WORD_LENGTH )
			return;
		
		if( e.getKeyChar() == '\n' )
		{
			processWord();
			return;
		}

		for( int i = 0; i < WORD_LENGTH; i++ )
			if( e.getKeyChar() == poolLetters.charAt(i) )
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
		if( guessWordLength == 0 )
			return;

		Iterator<String> iter = currentEntry.getLinkedWords().iterator();
		String           currentWord;

		while( iter.hasNext() )
		{
			currentWord = iter.next();

			if( guessLetters.substring(0, guessWordLength).equals(currentWord) )
				wPanel.showWord( currentWord );
		}
		
		for( int i = 0; i < guessWordLength; i++ )
			deleteLetter();
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
	}
}

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.util.Collection;
import java.util.Iterator;

/**
 * Panel containing a collection of words, arranged into a number of columns.
 * 
 * @author blackm0k
 *
 */
class WordPanel extends JPanel
{
    static final long serialVersionUID = -7766686790120866435L;

    // Collection of words
	Word [] words;
	// Constant declaration of number of columns 
	private final int NO_COLUMNS = 5;
	// Panel that contains a panel for each column
	private JPanel hBox;

	/**
	 * Create a new instance of word panel, ready to have a word list assigned to it.
	 */
	WordPanel()
	{
		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
		setPreferredSize( new Dimension( 400, 300 ) );
		// Create a new panel to contain the columns, with a border and pad that from the
		// window edge
		hBox = new JPanel();
		hBox.setLayout( new BoxLayout( hBox, BoxLayout.X_AXIS ) );
		hBox.setBorder( new LineBorder( Color.black, 1, true ) );
		add( Box.createRigidArea( new Dimension( 0, 4 ) ) );
		add( hBox );
	}

	/**
	 * Set the words to be displayed by the panel. These can be later accessed by a numerical
	 * index, so the order they are present in the collection is important if this form of
	 * access is intended to be used later. Alternatively the words can be accessed by the
	 * string value of the word.
	 * 
	 * @param wordList A string collection of words - typically used with Vector<String>
	 */
	void setWordList( Collection<String> wordList )
	{
		Iterator<String> iter = wordList.iterator();
		JPanel           currentPanel;
		int              nRows;

		// First remove all the words before starting with some new one
		hBox.removeAll();

		// currentPanel is used for a column in progress. All words necessary are added to
		// this panel and then it added to the hBox and a new JPanel created and assigned to
		// currentPanel
		currentPanel = new JPanel();
	    currentPanel.setLayout(	new BoxLayout( currentPanel, BoxLayout.Y_AXIS ) );

	    currentPanel.add( Box.createRigidArea( new Dimension( 0, 10 ) ) );

		words = new Word[ wordList.size() ];

		// Calculate the number of rows
		nRows = (wordList.size() / NO_COLUMNS) + 1;

		for( int i = 0; iter.hasNext(); i++ )
		{
			words[i] = new Word( iter.next() );

			// If this word would go beyond the number of rows we calculated, put it in a
			// new column
			if( i % nRows == 0 && i != 0 )
			{
				hBox.add( currentPanel );
				currentPanel = new JPanel();
				currentPanel.setLayout( new BoxLayout( currentPanel, BoxLayout.Y_AXIS ) );
				currentPanel.add( Box.createRigidArea( new Dimension( 0, 10 ) ) );
			}

			currentPanel.add( words[i] );
		}

		hBox.add( currentPanel );
	}

	/**
	 * Show the word at the specified index.
	 * 
	 * @param index Numerical index of the word to show.
	 */
	void showWord( int index )
	{
		words[index].update(true);
	}

	/**
	 * Hide the word at the specified index.
	 * 
	 * @param index Numerical index of the word to show.
	 */
	void hideWord( int index )
	{
		words[index].update(false);
	}

	/**
	 * Show the word (all instances of that word) with the given string value.
	 * 
	 * @param word The word to show
	 */
	void showWord( String word )
	{
		for( int i = 0; i < words.length; i++ )
			if( word.equals( words[i].toString() ) )
				words[i].update(true);
	}

	/**
	 * Hide the word (all instances of that word) with the given string value.
	 * 
	 * @param word The word to hide
	 */
	void hideWord( String word )
	{
		for( int i = 0; i < words.length; i++ )
			if( word.equals( words[i].toString() ) )
				words[i].update(false);
	}

	/**
	 * Show all words in the collection.
	 */
	void showAllWords()
	{
		for( int i = 0; i < words.length; i++ )
			words[i].update(true);
	}

	/**
	 * Hide all words in the collection.
	 */
	void hideAllWords()
	{
		for( int i = 0; i < words.length; i++ )
			words[i].update(false);
	}
	
	/**
	 * Show remaining words, highlighted in red.
	 */
	void showRemainingWords()
	{
		for( int i = 0; i < words.length; i++ )
			if( ! words[i].getVisibility() )
				words[i].update( true, Color.red );
	}
}

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.util.Collection;
import java.util.Iterator;

class WordPanel extends JPanel
{
	Word [] words;
	private final int NO_COLUMNS = 5;

	WordPanel()
	{
		setLayout( new BoxLayout( this, BoxLayout.X_AXIS ) );
		setPreferredSize( new Dimension( 400, 300 ) );
		setBorder( new LineBorder( Color.black, 1, true ) );
	}

	void setWordList( Collection<String> wordList )
	{
		Iterator<String> iter = wordList.iterator();
		JPanel           currentPanel;
		int              nRows;

		currentPanel = new JPanel();
	  currentPanel.setLayout(	new BoxLayout( currentPanel, BoxLayout.Y_AXIS ) );

		words = new Word[ wordList.size() ];

		nRows = (wordList.size() / NO_COLUMNS) + 1;

		for( int i = 0; iter.hasNext(); i++ )
		{
			words[i] = new Word( iter.next() );

			if( i % nRows == 0 && i != 0 )
			{
				add( currentPanel );
				currentPanel = new JPanel();
				currentPanel.setLayout( new BoxLayout( currentPanel, BoxLayout.Y_AXIS ) );
			}

			currentPanel.add( words[i] );
		}

		add( currentPanel );
	}

	void showWord( int index )
	{
		words[index].update(true);
	}

	void hideWord( int index )
	{
		words[index].update(false);
	}

	void showWord( String word )
	{
		for( int i = 0; i < words.length; i++ )
			if( word.equals( words[i].toString() ) )
				words[i].update(true);
	}

	void hideWord( String word )
	{
		for( int i = 0; i < words.length; i++ )
			if( word.equals( words[i].toString() ) )
				words[i].update(false);
	}
}

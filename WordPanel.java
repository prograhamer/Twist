import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.util.Collection;
import java.util.Iterator;

class WordPanel extends JPanel
{
    static final long serialVersionUID = -7766686790120866435L;

	Word [] words;
	private final int NO_COLUMNS = 5;
	private JPanel hBox;

	WordPanel()
	{
		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
		setPreferredSize( new Dimension( 400, 300 ) );
		hBox = new JPanel();
		hBox.setLayout( new BoxLayout( hBox, BoxLayout.X_AXIS ) );
		hBox.setBorder( new LineBorder( Color.black, 1, true ) );
		add( Box.createRigidArea( new Dimension( 0, 4 ) ) );
		add( hBox );
	}

	void setWordList( Collection<String> wordList )
	{
		Iterator<String> iter = wordList.iterator();
		JPanel           currentPanel;
		int              nRows;
		
		hBox.removeAll();

		currentPanel = new JPanel();
	    currentPanel.setLayout(	new BoxLayout( currentPanel, BoxLayout.Y_AXIS ) );

	    currentPanel.add( Box.createRigidArea( new Dimension( 0, 10 ) ) );

		words = new Word[ wordList.size() ];

		nRows = (wordList.size() / NO_COLUMNS) + 1;

		for( int i = 0; iter.hasNext(); i++ )
		{
			words[i] = new Word( iter.next() );

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
	
	void showAllWords()
	{
		for( int i = 0; i < words.length; i++ )
			words[i].update(true);
	}
	
	void hideAllWords()
	{
		for( int i = 0; i < words.length; i++ )
			words[i].update(false);
	}
}

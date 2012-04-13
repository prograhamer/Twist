import java.awt.*;
import javax.swing.*;

class Word extends JPanel
{
	private JLabel [] letters;
	private String text;

	Word( String text )
	{
		super( new FlowLayout( FlowLayout.CENTER, 5, 0 ) );

		letters = new JLabel[ text.length() ];

		for( int i = 0; i < text.length(); i++ )
		{
			letters[i] = new JLabel( " ", JLabel.CENTER );
			letters[i].setOpaque( true );
			letters[i].setBackground( Color.white );
			letters[i].setPreferredSize( new Dimension( 12, 15 ) );
			letters[i].setMinimumSize( new Dimension( 12, 15 ) );
			add( letters[i] );
		}

		this.text = text;
	}

	void update( boolean wordVisible )
	{
		for( int i = 0; i < text.length(); i++ )
		{
			if( wordVisible )
				letters[i].setText( "" + text.charAt(i) );
			else
				letters[i].setText( " " );
		}
	}

	public String toString()
	{
		return text;
	}
}

import java.awt.*;
import javax.swing.*;
import javax.swing.border.SoftBevelBorder;

public class LetterPanel extends JPanel
{
	private JLabel [] letters;
	private final Color standardBackground, warningBackground;

	LetterPanel( int size )
	{
		super( new FlowLayout( FlowLayout.CENTER, 20, 0 ) );
		
		standardBackground = getBackground();
		warningBackground = Color.red;

		letters = new JLabel[size];

		for( int i = 0; i < size; i++ )
		{
			letters[i] = new JLabel();
			letters[i].setFont( Font.decode( "Arial-BOLD-40" ) );
			letters[i].setBorder( new SoftBevelBorder( SoftBevelBorder.RAISED ) );
			letters[i].setPreferredSize( new Dimension( 50, 50 ) );
			letters[i].setHorizontalAlignment( JLabel.CENTER );
			letters[i].setOpaque( true );
			add( letters[i] );
		}
	}

	void update( String newLetters )
	{
		for( int i = 0; i < letters.length; i++ )
			letters[i].setText( "" + newLetters.charAt(i) );
	}

	void update( char [] newLetters )
	{
		for( int i = 0; i < letters.length; i++ )
			letters[i].setText( "" + newLetters[i] );
	}
	
	void setWarning( boolean warn )
	{
		for( int i = 0; i < letters.length; i++ )
		{
			if( warn )
				letters[i].setBackground( warningBackground );
			else
				letters[i].setBackground( standardBackground );
		}
	}
}

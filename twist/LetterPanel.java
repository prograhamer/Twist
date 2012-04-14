package twist;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.SoftBevelBorder;

/**
 * @author blackm0k
 *
 * A panel that contains a number of characters on embossed tiles. Designed for use as the
 * pool of letters from which to make words and the area where guessed words are constructed.
 */
public class LetterPanel extends JPanel
{
    static final long serialVersionUID = 869907805526468996L;

    // Collection of JLabel objects, on which the letters are rendered
	private JLabel [] letters;

	private final Color standardBackground, warningBackground;

	/**
	 * Create a new LetterPanel of the desired size.
	 * 
	 * @param size The required number of letter tiles.
	 */
	LetterPanel( int size )
	{
		super( new FlowLayout( FlowLayout.CENTER, 20, 0 ) );
		
		// Save the current background and store the warning colour for later use
		standardBackground = getBackground();
		warningBackground = Color.red;

		letters = new JLabel[size];

		// Initialise a letter for each element in the letters[] array
		// Set up the visual appearance of each "tile" as necessary.
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

	/**
	 * Update the panel with new letters, as specified by the parameter.
	 * 
	 * @param newLetters String specifying new letters to display.
	 */
	void update( String newLetters )
	{
		for( int i = 0; i < letters.length; i++ )
			letters[i].setText( "" + newLetters.charAt(i) );
	}

	/**
	 * Update the panel with new letters, as specified by the character array.
	 * 
	 * @param newLetters Character array specifying new letters to display.
	 */
	void update( char [] newLetters )
	{
		for( int i = 0; i < letters.length; i++ )
			letters[i].setText( "" + newLetters[i] );
	}
	
	/**
	 * Set/clear the warning state. Currently this causes the background of each tile to be
	 * set to red when warning state is enabled.
	 * 
	 * @param warn Warning state activated if true, deactivated if false.
	 */
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

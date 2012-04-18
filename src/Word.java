import java.awt.*;
import javax.swing.*;

/**
 * Panel containing a word, separated into word tiles with a distinct background, so as to
 * visually emphasise the number of letters in the given word without the letters being
 * displayed.
 *  
 * @author blackm0k
 *
 */
class Word extends JPanel
{
    static final long serialVersionUID = -1117969609654322967L;
 
    // Array of JLabel objects to display the individual letters of a word
	private JLabel [] letters;
	// The text of the word
	private String text;
	private final Color background = Color.white;
	private boolean visible;

	/**
	 * Create a new Word instance from the word specified in the parameter.
	 * 
	 * @param text The word used to create the instance.
	 */
	Word( String text )
	{
		super( new FlowLayout( FlowLayout.CENTER, 5, 0 ) );
		
		visible = false;

		letters = new JLabel[ text.length() ];

		// Create a label for each character in the string parameter and set up the necessary
		// visual requirements
		for( int i = 0; i < text.length(); i++ )
		{
			letters[i] = new JLabel( " ", JLabel.CENTER );
			letters[i].setOpaque( true );
			letters[i].setBackground( background );
			letters[i].setForeground( background );
			letters[i].setPreferredSize( new Dimension( 12, 15 ) );
			letters[i].setMinimumSize( new Dimension( 12, 15 ) );
			letters[i].setText( "" + text.charAt(i) );
			add( letters[i] );
		}

		this.text = text;
	}

	/**
	 * Update the visibility of the word.
	 * 
	 * @param wordVisible Set the word visible if true, hidden if false.
	 */
	void update( boolean wordVisible )
	{
		update( wordVisible, Color.black );
	}

	/**
	 * Update the visibility and color of the word.
	 * 
	 * @param wordVisible Set the word visible if true, hidden if false
	 * @param color Only relevant if true, the color in which to display the word
	 */
	void update( boolean wordVisible, Color color )
	{
		visible = wordVisible;

		for( int i = 0; i < text.length(); i++ )
		{
			if( wordVisible )
				letters[i].setForeground( color );
			else
				letters[i].setForeground( background );
		}
	}

	/**
	 * Get the visibility of the word.
	 * 
	 * @return True if the word is visible, false if not
	 */
	boolean getVisibility()
	{
		return visible;
	}

	/**
	 * Render to string, simply as the word text.
	 */
	public String toString()
	{
		return text;
	}
}

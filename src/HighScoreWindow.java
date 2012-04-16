import java.awt.*;
import javax.swing.*;

/**
 * Class to display high scores and request name information.
 *
 * @author blackm0k
 *
 */
class HighScoreWindow {
	/**
	 * Pop up a dialog to show the high score rankings.
	 * 
	 * @param owner Used as the parentComponent for JOptionPane - provide the top-level frame
	 * @param scores An array of HighScore instances to display
	 */
	static void showHighScoreWindow( Frame owner, HighScore [] scores )
	{
		String message;

		// Special message if there are no high scores set
		if( scores[0] == null )
			message = "No high scores.";
		else
			message = "";

		// Show all non-null high scores
		for( int i = 0; i < scores.length && scores[i] != null; i++ )
			message += scores[i].getName() + ": " + scores[i].getScore() + "\n";

		JOptionPane.showMessageDialog( owner, message, "High Scores",
				                       JOptionPane.INFORMATION_MESSAGE );
	}

	/**
	 * Pop up a dialog to prompt the user for their name.
	 * 
	 * @param owner Used as the parentComponent for JOptionPane - provide the top-level frame
	 * @return
	 */
	static String getNameFromUser( Frame owner )
	{
		return JOptionPane.showInputDialog( owner,
				                            "You are a high scorer! Please enter your name.",
				                            "Congratulations!",
				                            JOptionPane.QUESTION_MESSAGE );
	}
}
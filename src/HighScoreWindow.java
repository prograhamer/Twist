import java.awt.*;
import javax.swing.*;

/**
 * Class to display high scores and request name information etc.
 *
 * @author blackm0k
 *
 */
class HighScoreWindow {
	static void showHighScoreWindow( Frame owner, HighScore [] scores )
	{
		String message;
		
		if( scores[0] == null )
			message = "No high scores.";
		else
			message = "";

		for( int i = 0; i < scores.length && scores[i] != null; i++ )
			message += scores[i].getName() + ": " + scores[i].getScore() + "\n";

		JOptionPane.showMessageDialog( owner, message, "High Scores",
				                       JOptionPane.INFORMATION_MESSAGE );
	}
	
	static String getNameFromUser( Frame owner )
	{
		return JOptionPane.showInputDialog( owner,
				                            "You are a high scorer! Please enter your name.",
				                            "Congratulations!",
				                            JOptionPane.QUESTION_MESSAGE );
	}
}
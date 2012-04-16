import java.io.*;

/**
 * Class to handle high-scores and the high-score file.
 * 
 * @author blackm0k
 *
 */
class HighScoreManager {
	private final static int    NO_SCORES = 10;
	private final static String DEFAULT_FILENAME = ".twistscores";

	private HighScore [] highScores;

	/**
	 * Create a new high score manager instance.
	 */
	HighScoreManager()
	{
		highScores = new HighScore[ NO_SCORES ];
	}

	/**
	 * Read high scores in from the default file.
	 */
	void readFromFile()
	{
		String fileName;
		
		fileName = System.getProperty( "user.home" ) +
		           System.getProperty( "file.separator" ) +
		           DEFAULT_FILENAME;
		
		readFromFile( fileName );
	}

	/**
	 * Read the high scores in from the file specified.
	 * 
	 * @param fileName The filename of the high scores file to read
	 */
	void readFromFile( String fileName )
	{
		String line;
		int position;

		try
		{
			FileReader fr = new FileReader( fileName );
			BufferedReader br = new BufferedReader( fr );
			
			position = 0;

			// For each line, create a new high score instance
			while( (line = br.readLine()) != null )
			{
				highScores[position++] = new HighScore( line );

				// If the file is too long, just ignore the extra scores
				if( position >= NO_SCORES )
					break;
			}

			br.close();
			fr.close();
		}
		// If the file doesn't exist, then we simply don't have any high scores to load
		catch( FileNotFoundException fnfe )
		{
			return;
		}
		catch( IOException ioe )
		{
			System.err.println( "I/O exception: " + ioe.getMessage() );
		}
	}

	/**
	 * Write the high scores to the default file.
	 */
	void writeToFile()
	{
		String fileName;
		
		fileName = System.getProperty( "user.home" ) +
		           System.getProperty( "file.separator" ) +
		           DEFAULT_FILENAME;
		
		writeToFile( fileName );
	}

	/**
	 * Write the high scores to the file specified.
	 * 
	 * @param fileName The filename of the high scores file to write
	 */
	void writeToFile( String fileName )
	{
		try
		{
			PrintWriter pw = new PrintWriter( fileName );

			// Write out all non-null entries
			for( int i = 0; i < NO_SCORES && highScores[i] != null; i++ )
				pw.println( highScores[i] );
			
			pw.close();
		}
		catch ( IOException ioe )
		{
			System.err.println( "I/O exception: " + ioe.getMessage() );
		}	
	}

	/**
	 * Check if the given score would make it into the high scores table.
	 * 
	 * @param score The score to check
	 * @return True if the score would be a high score, false otherwise
	 */
	boolean isHighScore( int score )
	{
		return ( highScores[NO_SCORES-1] == null ||
				 score > highScores[NO_SCORES-1].getScore() );
	}

	/**
	 * Submit the given name and score to be entered into the high score rankings.
	 * 
	 * @param name The name of the high-scoring user
	 * @param score The score attained
	 */
	void submitScore( String name, int score )
	{
		int position = -1;

		// Find the insertion position
		for( int i = 0; i < NO_SCORES; i++ )
			if( highScores[i] == null || score > highScores[i].getScore() )
			{
				position = i;
				break;
			}
		
		// This was not a high score
		if( position == -1 )
			return;

		// Move existing entries below this one down the list, losing the last one
		for( int i = NO_SCORES - 2; i >= position; i-- )
			highScores[i + 1] = highScores[i];

		// Save this score into the table
		highScores[position] = new HighScore( name, score );
	}

	/**
	 * Get the scores held in the rankings.
	 * 
	 * @return An array of HighScore instances containing all the high scores currently held
	 */
	HighScore [] getScores()
	{
		return (HighScore []) (highScores.clone());
	}
}
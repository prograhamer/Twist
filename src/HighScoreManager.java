import java.io.*;

/**
 * Class to handle high-scores and the high-score file.
 * 
 * @author blackm0k
 *
 */
class HighScoreManager {
	private final static int NO_SCORES = 10;

	private HighScore [] highScores;

	HighScoreManager()
	{
		highScores = new HighScore[ NO_SCORES ];
	}
	
	void readFromFile()
	{
		String fileName;
		
		fileName = System.getProperty( "user.home" ) +
		           System.getProperty( "file.separator" ) +
		           ".twistscores";
		
		readFromFile( fileName );
	}

	void readFromFile( String fileName )
	{
		String line;
		int position;

		try
		{
			FileReader fr = new FileReader( fileName );
			BufferedReader br = new BufferedReader( fr );
			
			position = 0;

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
	
	void writeToFile()
	{
		String fileName;
		
		fileName = System.getProperty( "user.home" ) +
		           System.getProperty( "file.separator" ) +
		           ".twistscores";
		
		writeToFile( fileName );
	}
	
	void writeToFile( String fileName )
	{
		try
		{
			PrintWriter pw = new PrintWriter( fileName );
			
			for( int i = 0; i < NO_SCORES && highScores[i] != null; i++ )
				pw.println( highScores[i] );
			
			pw.close();
		}
		catch ( IOException ioe )
		{
			System.err.println( "I/O exception: " + ioe.getMessage() );
		}	
	}
	
	boolean isHighScore( int score )
	{
		for( int i = 0; i < NO_SCORES; i++ )
			if( highScores[i] == null || score > highScores[i].getScore() )
				return true;
		
		return false;
	}
	
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

		highScores[position] = new HighScore( name, score );
	}
	
	HighScore [] getScores()
	{
		return (HighScore []) (highScores.clone());
	}
}
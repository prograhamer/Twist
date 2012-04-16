/**
 * Class to contain a single high-score entry.
 *
 * @author blackm0k
 *
 */


class HighScore {
	private final String name;
	private final int    score;

	/**
	 * Create a high score instance from a name/score pair
	 * 
	 * @param name Name for the high score entry
	 * @param score Score for the high score entry
	 */
	HighScore( String name, int score )
	{
		this.name = name;
		this.score = score;
	}

	/**
	 * Create a high score instance from the string representation as created by toString()
	 * 
	 * @param scoreDef String representation of a HighScore object
	 */
	HighScore( String scoreDef )
	{
		String [] parts;
		
		parts = scoreDef.split(":");
		
		if( parts.length != 2 )
			throw( new IllegalArgumentException("Too many fields ") );
		
		name = parts[0]; 
		score = Integer.parseInt( parts[1] );
	}

	/**
	 * Get the name of the high score entry.
	 * 
	 * @return The name held in this high score entry
	 */
	String getName()
	{
		return name;
	}

	/**
	 * Get the score of the high score entry
	 * 
	 * @return The score held in this high score entry
	 */
	int getScore()
	{
		return score;
	}

	public String toString()
	{
		return name + ":" + score;
	}
}
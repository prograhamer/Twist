/**
 * Class to contain a single high-score entry.
 *
 * @author blackm0k
 *
 */


class HighScore {
	private final String name;
	private final int    score;
	
	HighScore( String name, int score )
	{
		this.name = name;
		this.score = score;
	}
	
	HighScore( String scoreDef )
	{
		String [] parts;
		
		parts = scoreDef.split(":");
		
		if( parts.length != 2 )
			throw( new IllegalArgumentException("Too many fields ") );
		
		name = parts[0]; 
		score = Integer.parseInt( parts[1] );
	}

	String getName()
	{
		return name;
	}
	
	int getScore()
	{
		return score;
	}
	
	public String toString()
	{
		return name + ":" + score;
	}
}
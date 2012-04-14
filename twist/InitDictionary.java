package twist;

/**
 * 
 * @author blackm0k
 *
 * Simple program to load a word list from a file and save it to the default dictionary data
 * file.
 */
class InitDictionary
{
	public static void main( String [] args )
	{
		Dictionary.createFromWordList( "dictionary/words" );
	}
}

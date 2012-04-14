package twist;

import java.util.Vector;
import java.util.Iterator;
import java.io.*;

/**
 * 
 * @author blackm0k
 *
 * Dictionary class that provides access to word lists containing words that are anagrams
 * of a word, or a subset of the characters in that word.
 *
 * This is a achieved with a collection of DictionaryEntry objects, each one representing
 * a heading word and a collection of all the anagrams of it and its parts.
 *
 */
class Dictionary
{
	// The collection of dictionary entries
	private Vector<DictionaryEntry> entries;
	
	// Default filename for the dictionary data file, used by save/load methods
	private final String            DEFAULT_FILENAME = "dict.data";

	/**
	 * Create a dictionary data structure from a list of words and save it to the default
	 * dictionary data filename.
	 * 
	 * WARNING: This will have undefined results with lists contained words longer than the
	 * number of characters defined in Twist.WORD_LENGTH
	 * 
	 * @param fileName The file from which to load the word list. This works well with a
	 *                 pruned version of the list kept in /usr/share/dict
	 */
	public static void createFromWordList( String fileName )
	{
		Dictionary dict;

		dict = new Dictionary();

		dict.loadFromWordList( fileName );

		dict.saveDictionary();
	}

	Dictionary()
	{
		entries = new Vector<DictionaryEntry>();
	}

	/**
	 * Load a word list and use it to create a set of words and their (partial) anagrams.
	 * 
	 * WARNING: This will have undefined results with lists contained words longer than the
	 * number of characters defined in Twist.WORD_LENGTH
	 * 
	 * @param fileName The file from which to load the word list.
	 */
	void loadFromWordList( String fileName )
	{
		FileReader                fRead;
		BufferedReader            buffRead;
		// Storage for all the words in the word list
		Vector<String>            words;
		// Iterator to pass between all the head words (words of maximum length)
		Iterator<DictionaryEntry> entryIterator;

		// Open the file
		try
		{
			fRead = new FileReader( fileName );
		}
		catch( FileNotFoundException fnfe )
		{
			System.err.println( "File not found" );
			return;
		}

		words = new Vector<String>();

		buffRead = new BufferedReader( fRead );


		// Read the words a line at a time into the words Vector, creating a new
		// DictionaryEntry for each word of maximum (WORD_LENGTH) length
		try
		{
			String line;

			while( (line = buffRead.readLine()) != null )
			{
				if( line.length() == Twist.WORD_LENGTH )
					entries.add( new DictionaryEntry( line.toUpperCase() ) );
				
				words.add( line.toUpperCase() );
			}
		}
		catch( IOException ioe )
		{
			System.out.println( "IO exception: " + ioe.getMessage() );
		}

		entryIterator = entries.iterator();

		// Iterate through all the entries created and find all the other words that can be
		// made with a subset of each entry's letters
		while( entryIterator.hasNext() )
		{
			DictionaryEntry  entry;
			char []          entryArray;
			Iterator<String> shortIter;
			String           shortWord;
			boolean          matchFound;

			entry = entryIterator.next();
			
			shortIter = words.iterator();

			// Iterate through all the words, finding any which can be formed from the
			// letters in the current dictionary entry
			while( shortIter.hasNext() )
			{
				entryArray = entry.getHeadWord().toCharArray();

				shortWord = shortIter.next();

				matchFound = false;

				// Systematically search for each character in the word we are trying to fit
				// into the entry, removing the character matched at each step to prevent
				// double-matching
				for( int i = 0; i < shortWord.length(); i++ )
				{
					matchFound = false;

					for( int j = 0; j < entryArray.length; j++ )
					{
						if( entryArray[j] == shortWord.charAt(i) )
						{
							entryArray[j] = ' ';
							matchFound = true;
							break;
						}
					}

					if( ! matchFound )
						break;
				}

				if( matchFound )
					entry.addLinkedWord( shortWord );
			}

			// Perform some clean-up after processing the entries
			entry.finalise();
		}

		// Prune entries with more than 50 words - these aren't so fun to play and don't
		// render well with the program as it is
		// This is done using a for loop, since the iterator returned by the Vector class
		// does not work if modifications are made while it is in use.
		for( int i = 0; i < entries.size(); i++ )
			if( entries.elementAt(i).getLinkedWords().size() > 50 )
				entries.remove(i--);

		entries.trimToSize();
	}

	/**
	 * Save the dictionary data using the default filename.
	 */
	void saveDictionary()
	{
		saveDictionary( DEFAULT_FILENAME );
	}

	/**
	 * Save the dictionary data using the filename provided.
	 * 
	 * @param fileName Filename to use for saving the dictionary data.
	 */
	void saveDictionary( String fileName )
	{
		Iterator<DictionaryEntry> entryIterator = entries.iterator();

		// Write the size of the entries Vector, followd by each element of the Vector
		try
		{
			FileOutputStream fos = new FileOutputStream( fileName );
			ObjectOutputStream oos = new ObjectOutputStream( fos );

			oos.writeInt( entries.size() );

			while( entryIterator.hasNext() )
				oos.writeObject( entryIterator.next() );

			oos.close();
		}
		catch( IOException ioe )
		{
			System.out.println( "I/O exception: " + ioe.getMessage() );
			return;
		}
	}

	/**
	 * Load the dictionary data using the default filename.
	 */
	void loadDictionary()
	{
		loadDictionary( DEFAULT_FILENAME );
	}

	/**
	 * Load the dictionary data using the filename provided.
	 * 
	 * @param fileName Filename to use for loading the dictionary data.
	 */
	void loadDictionary( String fileName )
	{
		int	entryCount;

		// First, make sure we are starting with a clean slate
		entries.removeAllElements();

		try
		{
			FileInputStream fis = new FileInputStream( fileName );
			ObjectInputStream ois = new ObjectInputStream( fis );

			// Read the number of objects in the file
			entryCount = ois.readInt();

			// Read that many objects from the file into the entries Vector
			for( int i = 0; i < entryCount; i++ )
				entries.add( (DictionaryEntry) ois.readObject() );

			ois.close();
		}
		catch( IOException ioe )
		{
			System.out.println( "I/O exception: " + ioe.getMessage() );
		}
		catch( ClassNotFoundException cnfe )
		{
			System.out.println( "Class not found: " + cnfe.getMessage() );
		}
	}

	/**
	 * Get the number of entries (head words) in the dictionary.
	 * 
	 * @return The number of entries in the dictionary.
	 */
	int getEntryCount()
	{
		return entries.size();
	}

	/**
	 * Get a specific entry from the dictionary.
	 * 
	 * @param index The entry, identified by its index in storage. Valid values are between
	 *              0 and (getEntryCount() - 1). 
	 * @return The DictionaryEntry object stored at the specified index.
	 */
	DictionaryEntry getEntry( int index )
	{
		return entries.elementAt( index );
	}
}
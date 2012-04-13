import java.util.Vector;
import java.util.Iterator;
import java.io.*;

class Dictionary
	implements Serializable
{
	private Vector<DictionaryEntry> entries;
	private final String            DEFAULT_FILENAME = "dict.data";

	public static void createFromWordList( String fileName )
	{
		Dictionary dict;

		dict = new Dictionary();

		dict.loadFromWordList( fileName );

		dict.saveDictionary();

		Dictionary dict2 = new Dictionary();

		dict2.loadDictionary();
	}

	Dictionary()
	{
		entries = new Vector<DictionaryEntry>();
	}

	void loadFromWordList( String fileName )
	{
		FileReader                fRead;
		BufferedReader            buffRead;
		Vector<String>            shortWords;
		Iterator<DictionaryEntry> entryIterator;

		try
		{
			fRead = new FileReader( fileName );
		}
		catch( FileNotFoundException fnfe )
		{
			System.out.println( "File not found" );
			return;
		}

		shortWords = new Vector<String>();

		buffRead = new BufferedReader( fRead );

		try
		{
			String line;

			while( (line = buffRead.readLine()) != null )
			{
				if( line.length() < 7 )
					shortWords.add( line.toUpperCase() );
				else
					entries.add( new DictionaryEntry( line.toUpperCase() ) );
			}
		}
		catch( IOException ioe )
		{
			System.out.println( "IO exception: " + ioe.getMessage() );
		}

		entryIterator = entries.iterator();

		while( entryIterator.hasNext() )
		{
			DictionaryEntry  entry;
			char []          entryArray;
			Iterator<String> shortIter;
			String           shortWord;
			boolean          matchFound;

			entry = entryIterator.next();

			shortIter = shortWords.iterator();

			while( shortIter.hasNext() )
			{
				entryArray = entry.getHeadWord().toCharArray();

				shortWord = shortIter.next();

				matchFound = false;

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

			entry.addLinkedWord( entry.getHeadWord() );

			entry.finalise();
		}

		entries.trimToSize();
	}

	void saveDictionary()
	{
		saveDictionary( DEFAULT_FILENAME );
	}

	void saveDictionary( String fileName )
	{
		Iterator<DictionaryEntry> entryIterator = entries.iterator();

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

	void loadDictionary()
	{
		loadDictionary( DEFAULT_FILENAME );
	}

	void loadDictionary( String fileName )
	{
		int	entryCount;

		entries.removeAllElements();

		try
		{
			FileInputStream fis = new FileInputStream( fileName );
			ObjectInputStream ois = new ObjectInputStream( fis );

			entryCount = ois.readInt();

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

	int getEntryCount()
	{
		return entries.size();
	}

	DictionaryEntry getEntry( int index )
	{
		return entries.elementAt( index );
	}
}

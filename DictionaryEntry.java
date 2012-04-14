import java.util.Vector;
import java.util.Collections;
import java.util.Iterator;
import java.io.Serializable;

class DictionaryEntry
	implements Serializable
{
    static final long serialVersionUID = -72359880169567976L;
 
	private String headWord;
	private Vector<String> linkedWords;

	DictionaryEntry( String headWord )
	{
		this.headWord = headWord;

		linkedWords = new Vector<String>();
	}

	void addLinkedWord( String word )
	{
		linkedWords.add( word );
	}

	public String toString()
	{
		String           returnString;
		Iterator<String> iter;

		returnString = headWord + ":";

		iter = linkedWords.iterator();

		while( iter.hasNext() )
		{
			returnString += " " + iter.next();
		}

		return returnString;
	}

	String getHeadWord()
	{
		return headWord;
	}

	Vector<String> getLinkedWords()
	{
		return linkedWords;
	}

	void finalise()
	{
		Collections.sort( linkedWords, new DictionaryEntryComparator() );
		linkedWords.trimToSize();
	}
}

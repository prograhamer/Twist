import java.util.Vector;
import java.util.Collections;
import java.util.Iterator;
import java.io.Serializable;

/**
 * 
 * @author blackm0k
 * 
 * Dictionary entry class, designed to store a "head word" and a list of all the anagrams of
 * that word, or part of that word.
 *
 */
class DictionaryEntry
	implements Serializable
{
    static final long serialVersionUID = -72359880169567976L;
 
	private String headWord;
	private Vector<String> linkedWords;

	/**
	 * Create a new DictionaryEntry instance from the word specified in headWord.
	 * 
	 * @param headWord The head word of the new instance.
	 */
	DictionaryEntry( String headWord )
	{
		this.headWord = headWord;

		linkedWords = new Vector<String>();
	}

	/**
	 * Add a linked word to the existing entry for a head word.
	 * 
	 * @param word The word to add to the linked word list.
	 */
	void addLinkedWord( String word )
	{
		linkedWords.add( word );
	}

	/**
	 * Render the entry to a string of the form headWord: word1 word2 ... wordN
	 */
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

	/**
	 * Get the head word of the entry.
	 * 
	 * @return The head word of the entry.
	 */
	String getHeadWord()
	{
		return headWord;
	}

	/**
	 * Get the linked word list of the entry.
	 * 
	 * @return A Vector<String> containing an ordered list of the linked words.
	 */
	Vector<String> getLinkedWords()
	{
		return linkedWords;
	}

	/**
	 * Finalise the entry - to be called once the entry has been fully loaded.
	 * 
	 * Sort the linked words and trim the collection to the minimum required size.
	 */
	void finalise()
	{
		Collections.sort( linkedWords, new DictionaryEntryComparator() );
		linkedWords.trimToSize();
	}
}

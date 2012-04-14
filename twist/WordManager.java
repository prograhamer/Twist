/**
 * Class to manage the status of the words with respect to whether they have been guessed or
 * not and to update their display based thereupon.
 * 
 * @author blackm0k
 *
 */
class WordManager {
	private boolean [] wordGuessed;
	private final      DictionaryEntry entry;
	private WordPanel  wPanel;

	/**
	 * Create a new word manager based on the given DictionaryEntry and use the provided
	 * WordPanel to display the status of the words in the entry.
	 * 
	 * @param entry DictionaryEntry on which to base the WordManager
	 * @param wPanel WordPanel to use to display the words and their status
	 */
	WordManager( DictionaryEntry entry, WordPanel wPanel )
	{
		wordGuessed = new boolean[entry.getLinkedWords().size()];
		this.entry = entry;
		this.wPanel = wPanel;
		
		wPanel.setWordList( entry.getLinkedWords() );
	}

	/**
	 * Mark the word as guessed and update its display.
	 * 
	 * @param word The word to mark as guessed.
	 */
	void setWordGuessed( String word )
	{
		for( int i = 0; i < entry.getLinkedWords().size(); i++ )
			if( entry.getLinkedWords().elementAt(i).equals(word) )
			{
				wordGuessed[i] = true;
				wPanel.showWord(i);
			}
	}

	/**
	 * Check the status of the word to see if it has been marked guessed.
	 * 
	 * @param word The word of which to check the status
	 * @return The status of the word: true if guessed, false if not
	 */
	boolean isWordGuessed( String word )
	{
		for( int i = 0; i < entry.getLinkedWords().size(); i++ )
			if( entry.getLinkedWords().elementAt(i).equals(word) )
					return wordGuessed[i];

		return false;
	}

	/**
	 * Check to see if all words in the DictionaryEntry have been guessed.
	 * 
	 * @return true if all words guessed, false if not
	 */
	boolean allWordsGuessed()
	{
		for( int i = 0; i < entry.getLinkedWords().size(); i++ )
			if( ! wordGuessed[i] )
				return false;
		
		return true;
	}
}

import java.util.Comparator;
import java.io.Serializable;

class DictionaryEntryComparator
	implements Comparator<String>, Serializable
{
	public int compare( String o1, String o2 )
	{
		if( o1.length() == o2.length() )
			return o1.compareTo( o2 );

		return (o1.length() - o2.length());
	}

	public boolean equals( String o1, String o2 )
	{
		return (o1.equals(o2));
	}
}

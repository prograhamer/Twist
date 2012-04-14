import java.util.Comparator;
import java.io.Serializable;

class DictionaryEntryComparator
	implements Comparator<String>, Serializable
{
    static final long serialVersionUID = -1961320352213572133L;
 
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

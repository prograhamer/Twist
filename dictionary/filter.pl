#!/usr/bin/perl -W

open( $input, "<", "/usr/share/dict/words" ) or die "Cannot open file";

while( <$input> )
{
	if( m/^[a-z]{3,7}$/ )
	{
		print;
	}
}

close( $input );

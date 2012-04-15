#!/usr/bin/perl -w

if( $#ARGV != 0 ) { print "Usage: filter.pl <filename>\n"; exit 1; }

open( $input, "<", "$ARGV[0]" ) or die "Cannot open file: $ARGV[0]";

while( <$input> )
{
  # Remove accents from characters in the input word list. Uses repeated s///
	# because UTF-8 characters don't play nicely with tr///
	s/à/a/g;
	s/á/a/g;
	s/â/a/g;
	s/ã/a/g;
	s/ä/a/g;
	s/å/a/g;
	s/ç/c/g;
	s/è/e/g;
	s/é/e/g;
	s/ê/e/g;
	s/ë/e/g;
	s/ì/i/g;
	s/í/i/g;
	s/î/i/g;
	s/ï/i/g;
	s/ñ/n/g;
	s/ò/o/g;
	s/ó/o/g;
	s/ô/o/g;
	s/õ/o/g;
	s/ö/o/g;
	s/ù/u/g;
	s/ú/u/g;
	s/û/u/g;
	s/ü/u/g;
	s/æ/ae/g;

  # If the word is made up of 3-7 lowercase characters, include it
  # This exclude proper nouns and possessives
	if( m/^[a-z]{3,7}$/ )
	{
		print;
	}
}

close( $input );

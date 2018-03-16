grammar Times;

import LexerEN, NumbersParser;

time : hour time_sep min (time_sep sec)? ;

time_sep : DOT | COLON | SPACE_SEP AND SPACE_SEP ;

hour : d_0_24 ;
min  : d_0_59 ;
sec  : d_0_59 ;

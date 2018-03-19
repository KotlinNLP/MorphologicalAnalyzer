grammar Times;

import LexerEN, NumbersParser;

time
    : hour DOT min (DOT sec)?
    | hour COLON min (COLON sec)?
    | hour SPACE_SEP AND SPACE_SEP min (SPACE_SEP AND SPACE_SEP sec)?
    ;

hour : d_0_24 ;
min  : d_0_59 ;
sec  : d_0_59 ;

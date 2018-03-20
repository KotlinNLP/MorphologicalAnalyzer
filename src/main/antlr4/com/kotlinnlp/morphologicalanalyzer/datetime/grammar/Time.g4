grammar Time;

import LexerEN, NumbersParser;

time
    : time_canonical time_suffix
    | hour COLON min time_suffix
    | hour DOT min (DOT sec (DOT millisec)?)?
    ;

time_canonical : hour_00 COLON min_00 COLON sec_00 (DOT millisec_000)? ;

time_suffix : (SPACE_SEP? TIME_SUFFIX)? ;

hour     : d_0_24 ;
min      : d_0_59 ;
sec      : d_0_59 ;
millisec : d_0_999 ;

hour_00      : d_00_24 ;
min_00       : d_00_59 ;
sec_00       : d_00_59 ;
millisec_000 : d_000_999 ;

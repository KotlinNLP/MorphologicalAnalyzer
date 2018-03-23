grammar Time;

import LexerEN, NumbersParser;

time
    : time_canonical (WS? (time_suffix | time_zone))?
    | hour COLON min (WS? (time_suffix | time_zone))?
    | (TIME_H | HOUR) WS hour
    | hour DOT min (DOT sec (DOT millisec)?)?
    | (hour | hour_str) WS? (time_suffix | O_CLOCK) (WS? time_suffix | O_CLOCK)? (WS? time_zone)?
    ;

time_canonical : hour_00 COLON min_00 COLON sec_00 (DOT millisec_000)? ;

time_suffix : TIME_SUFFIX ;
time_zone : TIME_ZONE ;

hour     : n_0_24 ;
min      : n_0_59 ;
sec      : n_0_59 ;
millisec : n_0_999 ;

hour_00      : n_00_24 ;
min_00       : n_00_59 ;
sec_00       : n_00_59 ;
millisec_000 : n_000_999 ;

hour_str
    : ns_1  | ns_2  | ns_3  | ns_4  | ns_5  | ns_6  | ns_7  | ns_8  | ns_9  | ns_10
    | ns_11 | ns_12 | ns_13 | ns_14 | ns_15 | ns_16 | ns_17 | ns_18 | ns_19 | ns_20
    | ns_21 | ns_22 | ns_23 | ns_24
    ;

// -----
// -- LITERALS
// -----

time_unit_literal : hour_lit | min_lit | sec_lit ;

hour_lit : HOUR ;
min_lit  : MIN ;
sec_lit  : SEC ;

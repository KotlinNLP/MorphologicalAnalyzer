grammar Time;

import LexerEN, NumbersParser;

time
    : time_canonical (WS? (time_suffix | time_zone))?
    | hour COLON min (WS? (time_suffix | time_zone))?
    | (TIME_H | HOUR) WS hour
    | hour DOT min (DOT sec (DOT millisec)?)?
    | (hour | hour_str) WS? (time_suffix | O_CLOCK) (WS? time_suffix | O_CLOCK)? (WS? time_zone)?
    | half_hour
    | quarter_hour
    | three_quarters_hour
    | IN WS generic_time // specific for EN with generic time (e.g. "in the morning")
    | OF WS generic_time // specific for IT with generic time (e.g. "di sera")
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
// -- GENERIC
// -----

generic_time : morning | lunch | afternoon | evening | night ;

morning : MORNING ;
lunch : LUNCH ;
afternoon : AFTERNOON ;
evening : EVENING ;
night : NIGHT ;

// -----
// -- SPECIALS
// -----

half_hour           : HALF_HOUR | N_1 (SLASH | BACKSLASH) N_2 WS? HOUR ;
quarter_hour        : QUARTER_HOUR | N_3 (SLASH | BACKSLASH) N_4 WS? HOUR ;
three_quarters_hour : ns_3 WS? QUARTER_HOUR ; // specific for IT

// -----
// -- LITERALS
// -----

time_unit_literal : hour_lit | quarter_hour_lit | half_hour_lit | min_lit | sec_lit ;

quarter_hour_lit : QUARTER_HOUR ;
half_hour_lit    : HALF_HOUR ;
hour_lit         : HOUR ;
min_lit          : MIN ;
sec_lit          : SEC ;

grammar Time;

import LexerEN, NumbersParser;

time
    : time_canonical (SPACE_SEP? (time_suffix | time_zone))?
    | hour COLON min (SPACE_SEP? (time_suffix | time_zone))?
    | TIME_H SPACE_SEP? hour
    | hour DOT min (DOT sec (DOT millisec)?)?
    | (hour | hour_str) SPACE_SEP? (time_suffix | O_CLOCK) (SPACE_SEP? time_suffix | O_CLOCK)? (SPACE_SEP? time_zone)?
    ;

time_canonical : hour_00 COLON min_00 COLON sec_00 (DOT millisec_000)? ;

time_suffix : TIME_SUFFIX ;
time_zone : TIME_ZONE ;

hour     : d_0_24 ;
min      : d_0_59 ;
sec      : d_0_59 ;
millisec : d_0_999 ;

hour_00      : d_00_24 ;
min_00       : d_00_59 ;
sec_00       : d_00_59 ;
millisec_000 : d_000_999 ;

hour_str
    : h_s_1  | h_s_2  | h_s_3  | h_s_4  | h_s_5  | h_s_6  | h_s_7  | h_s_8  | h_s_9  | h_s_10
    | h_s_11 | h_s_12 | h_s_13 | h_s_14 | h_s_15 | h_s_16 | h_s_17 | h_s_18 | h_s_19
    | h_s_20 | h_s_21 | h_s_22 | h_s_23 | h_s_24
    ;

h_s_1  : S_1 ;
h_s_2  : S_2 ;
h_s_3  : S_3 ;
h_s_4  : S_4 ;
h_s_5  : S_5 ;
h_s_6  : S_6 ;
h_s_7  : S_7 ;
h_s_8  : S_8 ;
h_s_9  : S_9 ;
h_s_10 : S_10 ;
h_s_11 : S_11 ;
h_s_12 : S_12 ;
h_s_13 : S_13 ;
h_s_14 : S_14 ;
h_s_15 : S_15 ;
h_s_16 : S_16 ;
h_s_17 : S_17 ;
h_s_18 : S_18 ;
h_s_19 : S_19 ;
h_s_20 : S_20 ;
h_s_21 : S_21 ;
h_s_22 : S_22 ;
h_s_23 : S_23 ;
h_s_24 : S_24 ;

grammar Time;

import LexerEN, NumbersParser;

time
    : time_canonical (SPACE_SEP? (time_suffix | time_zone))?
    | hour COLON min (SPACE_SEP? (time_suffix | time_zone))?
    | TIME_H SPACE_SEP hour
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
    : n_s_1  | n_s_2  | n_s_3  | n_s_4  | n_s_5  | n_s_6  | n_s_7  | n_s_8  | n_s_9  | n_s_10
    | n_s_11 | n_s_12 | n_s_13 | n_s_14 | n_s_15 | n_s_16 | n_s_17 | n_s_18 | n_s_19 | n_s_20
    | n_s_21 | n_s_22 | n_s_23 | n_s_24
    ;

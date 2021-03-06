grammar DTDateTimeSimple;

import DTLexerEN, DTDate, DTTime;

date_time_simple
    : date WS (time | date_time_simple_generic_time)
    | date WS AT WS time
    | datetime_utc
    | date TIME_T time
    | (time | date_time_simple_generic_time) WS OF WS date
    | (time | date_time_simple_generic_time) WS ON WS date // specific for EN
    ;

datetime_utc : date TIME_T time TIME_Z ;

date_time_simple_generic_time : generic_time | generic_time_expr ;

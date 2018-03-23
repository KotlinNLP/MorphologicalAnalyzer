grammar DateTime;

import LexerEN, Date, Time, DateOrdinal, Offset;

// -----
// -- Input text
// -----

root : WS? text EOF? ;

text : text_atomic | text_complex ;

text_complex : text_ws+? text_atomic? ;
text_atomic  : datetime | string ;
text_ws      : datetime punct* WS | string WS ;

string : (CHAR_NO_WS | ~WS)+ ;
punct  : DOT | COMMA | COLON | SEMICOLON | APEX | DASH | SLASH | DEGREE | CIRCUMFLEX | OTHER_SYMBOLS ;

// -----
// -- Date-times
// -----

datetime
    : date_offset
    | offset
    | date_time_simple
    | time
    | date
    | date_ordinal
    ;

date_offset : date_offset_date_ref WS (OF WS)? offset ;
date_offset_date_ref : date ;

date_time_simple
    : date WS time
    | date WS AT WS time
    | datetime_utc
    | date TIME_T time
    | time WS OF WS date
    | time WS ON WS date // specific for EN
    ;

datetime_utc : date TIME_T time TIME_Z ;

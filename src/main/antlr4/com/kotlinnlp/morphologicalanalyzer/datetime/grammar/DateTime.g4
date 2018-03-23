grammar DateTime;

import LexerEN, Date, Time, DateOrdinal, Offset;

// -----
// -- Input text
// -----

root : SPACE_SEP? text EOF? ;

text : text_atomic | text_complex ;

text_complex : text_ws+? text_atomic? ;
text_atomic  : datetime | string ;
text_ws      : datetime punct* SPACE_SEP | string SPACE_SEP ;

string : (CHAR_NO_WS | ~SPACE_SEP)+ ;
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

date_offset : date_offset_date_ref SPACE_SEP (OF SPACE_SEP)? offset ;
date_offset_date_ref : date ;

date_time_simple
    : date SPACE_SEP time
    | date SPACE_SEP AT SPACE_SEP time
    | datetime_utc
    | date TIME_T time
    | time SPACE_SEP OF SPACE_SEP date
    | time SPACE_SEP ON SPACE_SEP date // specific for EN
    ;

datetime_utc : date TIME_T time TIME_Z ;

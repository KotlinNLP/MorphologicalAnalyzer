grammar DateTime;

import LexerEN, Date, Time, Offset;

// -----
// -- Input text
// -----

root : ws? text EOF? ;

text : text_atomic | text_complex ;

text_complex : text_ws+? text_atomic? ;
text_atomic  : datetime | string ;
text_ws      : datetime punct* ws | string ws ;

string : (CHAR_NO_WS | ~(SPACE_SEP | OTHER_SPACES))+ ;
punct  : DOT | COMMA | COLON | SEMICOLON | APEX | DASH | SLASH | OTHER_SYMBOLS ;
ws     : (SPACE_SEP | OTHER_SPACES)+ ;

// -----
// -- Date-times
// -----

datetime
    : date_offset
    | offset
    | date_time_simple
    | time
    | date
    ;

date_offset : date SPACE_SEP OF SPACE_SEP offset ;

date_time_simple
    : date SPACE_SEP time
    | date ws AT ws time
    | datetime_utc
    | date TIME_T time
    | time SPACE_SEP OF SPACE_SEP date
    | time SPACE_SEP ON SPACE_SEP date // specific for EN
    ;

datetime_utc : date TIME_T time TIME_Z ;

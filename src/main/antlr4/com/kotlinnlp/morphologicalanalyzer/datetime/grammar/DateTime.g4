grammar DateTime;

import LexerEN, Dates, Times;

// -----
// -- Input text
// -----

root : ws? text EOF? ;

text : text_atomic | text_complex ;

text_complex : text_ws+? text_atomic? ;
text_atomic  : datetime | string ;
text_ws      : datetime punct* ws | string ws ;

string : (CHAR_NO_WS | ~(SPACE_SEP | OTHER_SPACES))+ ;
punct  : DOT | COMMA | COLON | SEMICOLON ;
ws     : (SPACE_SEP | OTHER_SPACES)+ ;

// -----
// -- Date-times
// -----

datetime : complex_datetime | time | date ;
complex_datetime : date ws AT ws time ;

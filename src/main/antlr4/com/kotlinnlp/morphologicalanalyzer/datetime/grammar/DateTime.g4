grammar DateTime;

import Symbols, Dates, Times, LexerEN, ImportLast;

// -----
// -- Input text
// -----

root : ws? text ws? EOF? ;

text : atomic_text | complex_text ;

atomic_text  : datetime | string ;
complex_text : text_ws+ atomic_text? ;

text_ws : datetime punct* ws | string ws ;

string : (CHAR_NO_WS | ~(SPACE_SEP | OTHER_SPACES))+ ;
punct  : DOT | COMMA | COLON | SEMICOLON ;
ws     : (SPACE_SEP | OTHER_SPACES)+ ;

// -----
// -- Date-times
// -----

datetime : complex_datetime | date | time ;
complex_datetime : date ws AT ws time ;

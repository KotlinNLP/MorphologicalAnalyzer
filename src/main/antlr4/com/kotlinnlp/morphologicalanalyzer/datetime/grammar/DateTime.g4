grammar DateTime;

import Symbols, Dates, Times, ImportLast;

root : ws? text ws? EOF? ;

text : atomic_text | complex_text ;

atomic_text  : datetime | string ;
complex_text : text_ws+ atomic_text? ;

text_ws : datetime punct* ws | string ws ;

datetime : date | time ;

string : (CHAR_NO_WS | ~(SPACE_SEP | OTHER_SPACES))+ ;
punct  : DOT | COMMA | COLON | SEMICOLON ;
ws     : (SPACE_SEP | OTHER_SPACES)+ ;

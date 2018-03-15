grammar Datetime;

import Symbols, Dates, Times, ImportLast;

root : ws? text ws? EOF? ;

text : atomic_text | complex_text ;

atomic_text  : datetime | string ;
complex_text : text_ws+ atomic_text? ;

text_ws : datetime ws | string ws ;

datetime : date | time ;

string : (CHAR_NO_WS | ~(SPACE_SEP | OTHER_SPACES))+ ;
ws     : (SPACE_SEP | OTHER_SPACES)+ ;

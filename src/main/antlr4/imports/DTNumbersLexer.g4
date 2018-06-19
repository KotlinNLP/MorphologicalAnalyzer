lexer grammar DTNumbersLexer;

import DTSymbols, DTLetters;

// -----
// -- Ordinal suffix
// -----

ORD_SUFFIX : (O | A | DEGREE | CIRCUMFLEX) ;

// -----
// -- Numbers with 1 digit
// -----

N_0 : '0' ;
N_1 : '1' ;
N_2 : '2' ;
N_3 : '3' ;
N_4 : '4' ;
N_5 : '5' ;
N_6 : '6' ;
N_7 : '7' ;
N_8 : '8' ;
N_9 : '9' ;

fragment DIGIT : [0-9] ;

// -----
// -- Numbers with 2 digits
// -----

N_00    : N_0 N_0 ;
N_01    : N_0 N_1 ;
N_02    : N_0 N_2 ;
N_03    : N_0 N_3 ;
N_04_09 : N_0 [4-9] ;
N_10_12 : N_1 [0-2] ;
N_13_19 : N_1 [3-9] ;
N_20    : N_2 N_0 ;
N_21    : N_2 N_1 ;
N_22    : N_2 N_2 ;
N_23    : N_2 N_3 ;
N_24    : N_2 N_4 ;
N_25_29 : N_2 [5-9] ;
N_30    : N_3 N_0 ;
N_31    : N_3 N_1 ;
N_32_39 : N_3 [2-9] ;
N_40_59 : [4-5] DIGIT ;
N_60_99 : [6-9] DIGIT ;

// -----
// -- Numbers with more then 2 digits
// -----

N_000_099   : N_0 DIGIT DIGIT ;
N_100_999   : [1-9] DIGIT DIGIT ;
N_1000_1899 : N_1 [0-8] DIGIT DIGIT ;
N_1900_2099 : (N_1 N_9 | N_2 N_0) DIGIT DIGIT ;
N_2100_9999 : [2-9] [1-9] DIGIT DIGIT ;

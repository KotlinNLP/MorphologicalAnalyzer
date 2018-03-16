lexer grammar NumbersLexer;

// -----
// -- Numbers with 1 digit
// -----

DIGIT : [0-9] ;

D_0 : '0' ;
D_1 : '1' ;
D_2 : '2' ;
D_3 : '3' ;
D_4 : '4' ;
D_5 : '5' ;
D_6 : '6' ;
D_7 : '7' ;
D_8 : '8' ;
D_9 : '9' ;

// -----
// -- Numbers with 2 digits
// -----

D_00_09 : '0' DIGIT ;
D_10_12 : '1' [12] ;
D_13_19 : '1' [3-9] ;
D_20_24 : '2' [0-4] ;
D_25_29 : '2' [5-9] ;
D_30_31 : '3' [01] ;
D_32_39 : '3' [2-9] ;
D_40_59 : [4-5] DIGIT ;
D_60_99 : [6-9] DIGIT ;

// -----
// -- Numbers with more then 2 digits
// -----

D_100_9999 : [1-9] DIGIT DIGIT DIGIT? ;
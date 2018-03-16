lexer grammar NumbersLexer;

// -----
// -- Numbers with 1 digit
// -----

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

fragment DIGIT : [0-9] ;

// -----
// -- Numbers with 2 digits
// -----

D_00    : D_0 D_0 ;
D_01    : D_0 D_1 ;
D_02    : D_0 D_2 ;
D_03    : D_0 D_3 ;
D_04_09 : D_0 [4-9] ;
D_10_12 : D_1 [12] ;
D_13_19 : D_1 [3-9] ;
D_20    : D_2 D_0 ;
D_21    : D_2 D_1 ;
D_22    : D_2 D_2 ;
D_23    : D_2 D_3 ;
D_24    : D_2 D_4 ;
D_25_29 : D_2 [5-9] ;
D_30    : D_3 D_0 ;
D_31    : D_3 D_1 ;
D_32_39 : D_3 [2-9] ;
D_40_59 : [4-5] DIGIT ;
D_60_99 : [6-9] DIGIT ;

// -----
// -- Numbers with more then 2 digits
// -----

D_100_999   : [1-9] DIGIT DIGIT ;
D_1000_1899 : D_1 [0-8] DIGIT DIGIT ;
D_1900_2099 : (D_1 D_9 | D_2 D_0) DIGIT DIGIT ;
D_2100_9999 : [2-9] [1-9] DIGIT DIGIT ;

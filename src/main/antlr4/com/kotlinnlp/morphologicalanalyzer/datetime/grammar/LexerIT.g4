lexer grammar LexerIT;

import Letters, Symbols, NumbersLexer, TimeLexer, LexerImportLast;

// -----
// -- Functional words
// -----

DAY_ST : NOT_DEFINED ;
DAY_ND : NOT_DEFINED ;
DAY_RD : NOT_DEFINED ;
DAY_TH : E S I M (O | I | A | E) ;

AND : E ;
AT  : A L | A L L E ;
THE : I L ;
OF  : D I | D E (L | L L APEX | L L O | I | G L I | L L A | L L E) ;
ON  : NOT_DEFINED ;
IN  : I N | N E (L | I | G L I | L L A | L L E) | T R A | F R A ;

THIS : Q U E S T (O | I | A | E) ;
LAST : U L T I M (O | I | A | E) ;
PREV : P R E C E D E N T (E | I) ;
NEXT : P R O S S I M (O | I | A | E) ;

AFTER : D O P O SPACE_SEP THE ;
BEFORE : P R I M A SPACE_SEP OF ;

FROM : D A | D A L ;

// -----
// -- Special expressions
// -----

O_CLOCK : I N SPACE_SEP P U N T O ;

// -----
// -- Words
// -----

YEAR    : A N N (O | I) ;
MONTH   : M E S (E | I) ;
WEEK    : S E T T I M A N (A | E) ;
WEEKEND : F I N E SPACE_SEP WEEK ;
DAY     : G I O R N (O | I) ;
HOUR    : O R (A | E) ;
MIN     : M I N U T (O | I) ;
SEC     : S E C ; // 'secondo' is also an ordinal number

AGO : F (A | AA | A APEX) ; // "fà" and "fa'" are two common errors

// -----
// -- Holidays
// -----

CHRISTMAS     : N A T A L E ;
CHRISTMAS_EVE : V I G I L I A SPACE_SEP OF SPACE_SEP CHRISTMAS ;
EASTER        : P A S Q U A ;

// -----
// -- Days
// -----

MON : L U N E D II ;
TUE : M A R T E D II ;
WED : M E R C O L E D II ;
THU : G I O V E D II ;
FRI : V E N E R D II ;
SAT : S A B A T O ;
SUN : D O M E N I C A ;

MON_ABBR : L U N DOT? ;
TUE_ABBR : M A R DOT? ;
WED_ABBR : M E R DOT? ;
THU_ABBR : G I O DOT? ;
FRI_ABBR : V E N DOT? ;
SAT_ABBR : S A B DOT? ;
SUN_ABBR : D O M DOT? ;

// -----
// -- Months
// -----

JAN : G E N N A I O ;
FEB : F E B B R A I O ;
MAR : M A R Z O ;
APR : A P R I L E ;
MAY : M A G G I O ;
JUN : G I U G N O ;
JUL : L U G L I O ;
AUG : A G O S T O ;
SEP : S E T T E M B R E ;
OCT : O T T O B R E ;
NOV : N O V E M B R E ;
DEC : D I C E M B R E ;

JAN_ABBR : G E N DOT? ;
FEB_ABBR : F E B DOT? ;
MAR_ABBR : M A R DOT? ;
APR_ABBR : A P R DOT? ;
MAY_ABBR : M A G ;
JUN_ABBR : G I U DOT? ;
JUL_ABBR : L U G DOT? ;
AUG_ABBR : A G O DOT? ;
SEP_ABBR : S E T T? DOT? ;
OCT_ABBR : O T T DOT? ;
NOV_ABBR : N O V DOT? ;
DEC_ABBR : D I C DOT? ;

// -----
// -- Cardinal Numbers
// -----

NS_1  : U N O ;
NS_2  : D U E ;
NS_3  : T R E ;
NS_4  : Q U A T T R O ;
NS_5  : C I N Q U E ;
NS_6  : S E I ;
NS_7  : S E T T E ;
NS_8  : O T T O ;
NS_9  : N O V E ;
NS_10 : D I E C I ;
NS_11 : U N D I C I ;
NS_12 : D O D I C I ;
NS_13 : T R E D I C I ;
NS_14 : Q U A T T O R D I C I ;
NS_15 : Q U I N D I C I ;
NS_16 : S E D I C I ;
NS_17 : D I C I A S NS_7 ;
NS_18 : D I C I NS_8 ;
NS_19 : D I C I A N NS_9 ;
NS_20 : V E N T I ;
NS_21 : V E N T NS_1 ;
NS_22 : NS_20 NS_2 ;
NS_23 : NS_20 NS_3 ;
NS_24 : NS_20 NS_4 ;
NS_25 : NS_20 NS_5 ;
NS_26 : NS_20 NS_6 ;
NS_27 : NS_20 NS_7 ;
NS_28 : V E N T NS_8 ;
NS_29 : NS_20 NS_9 ;
NS_30 : T R E N T A ;
NS_31 : T R E N T NS_1 ;

// -----
// -- Ordinal Numbers
// -----

fragment TH : DAY_TH ;

NS_ORD_1  : P R I M (O | I | A | E) ;
NS_ORD_2  : S E C O N D (O | I | A | E) ;
NS_ORD_3  : T E R Z (O | I | A | E) ;
NS_ORD_4  : Q U A R T (O | I | A | E) ;
NS_ORD_5  : Q U I N T (O | I | A | E) ;
NS_ORD_6  : S E S T (O | I | A | E) ;
NS_ORD_7  : S E T T I M (O | I | A | E) ;
NS_ORD_8  : O T T A V (O | I | A | E) ;
NS_ORD_9  : N O N (O | I | A | E) ;
NS_ORD_10 : D E C I M (O | I | A | E) ;
NS_ORD_11 : U N D I C TH ;
NS_ORD_12 : D O D I C TH ;
NS_ORD_13 : T R E D I C TH ;
NS_ORD_14 : Q U A T T O R D I C TH ;
NS_ORD_15 : Q U I N D I C TH ;
NS_ORD_16 : S E D I C TH ;
NS_ORD_17 : D I C I A S S E T T TH ;
NS_ORD_18 : D I C I O T T TH ;
NS_ORD_19 : D I C I A N N O V TH ;
NS_ORD_20 : V E N T TH ;
NS_ORD_21 : V E N T U N TH ;
NS_ORD_22 : NS_20 D U TH ;
NS_ORD_23 : NS_20 T R E TH ;
NS_ORD_24 : NS_20 Q U A T T TH ;
NS_ORD_25 : NS_20 C I N Q U TH ;
NS_ORD_26 : NS_20 S E I TH ;
NS_ORD_27 : NS_20 S E T T TH ;
NS_ORD_28 : V E N T O T T TH ;
NS_ORD_29 : NS_20 N O V TH ;
NS_ORD_30 : T R E N T TH ;
NS_ORD_31 : T R E N T U N TH ;

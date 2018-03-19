lexer grammar LexerIT;

import Letters, Symbols, NumbersLexer, LexerImportLast;

// -----
// -- Functional words
// -----

DAY_ST : DOT ; // not defined for IT
DAY_ND : DOT ; // not defined for IT
DAY_RD : DOT ; // not defined for IT
DAY_TH : E S I M O ;

AND : E ;
AT  : A L | A L L E ;
THE : I L ;
OF : D I | D E L ;

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

S_1  : U N O ;
S_2  : D U E ;
S_3  : T R E ;
S_4  : Q U A T T R O ;
S_5  : C I N Q U E ;
S_6  : S E I ;
S_7  : S E T T E ;
S_8  : O T T O ;
S_9  : N O V E ;
S_10 : D I E C I ;
S_11 : U N D I C I ;
S_12 : D O D I C I ;
S_13 : T R E D I C I ;
S_14 : Q U A T T O R D I C I ;
S_15 : Q U I N D I C I ;
S_16 : S E D I C I ;
S_17 : D I C I A S S_7 ;
S_18 : D I C I S_8 ;
S_19 : D I C I A N S_9 ;
S_20 : V E N T I ;
S_21 : V E N T S_1 ;
S_22 : S_20 S_2 ;
S_23 : S_20 S_3 ;
S_24 : S_20 S_4 ;
S_25 : S_20 S_5 ;
S_26 : S_20 S_6 ;
S_27 : S_20 S_7 ;
S_28 : V E N T S_8 ;
S_29 : S_20 S_9 ;
S_30 : T R E N T A ;
S_31 : T R E N T S_1 ;

// -----
// -- Ordinal Numbers
// -----

fragment TH : DAY_TH ;

S_ORD_1  : P R I M O ;
S_ORD_2  : S E C O N D O ;
S_ORD_3  : T E R Z O ;
S_ORD_4  : Q U A R T O ;
S_ORD_5  : Q U I N T O ;
S_ORD_6  : S E S T O ;
S_ORD_7  : S E T T I M O ;
S_ORD_8  : O T T A V O ;
S_ORD_9  : N O N O ;
S_ORD_10 : D E C I M O ;
S_ORD_11 : U N D I C TH ;
S_ORD_12 : D O D I C TH ;
S_ORD_13 : T R E D I C TH ;
S_ORD_14 : Q U A T T O R D I C TH ;
S_ORD_15 : Q U I N D I C TH ;
S_ORD_16 : S E D I C TH ;
S_ORD_17 : D I C I A S S E T T TH ;
S_ORD_18 : D I C I O T T TH ;
S_ORD_19 : D I C I A N N O V TH ;
S_ORD_20 : V E N T TH ;
S_ORD_21 : V E N T U N TH ;
S_ORD_22 : S_20 D U TH ;
S_ORD_23 : S_20 T R E TH ;
S_ORD_24 : S_20 Q U A T T TH ;
S_ORD_25 : S_20 C I N Q U TH ;
S_ORD_26 : S_20 S E I TH ;
S_ORD_27 : S_20 S E T T TH ;
S_ORD_28 : V E N T O T T TH ;
S_ORD_29 : S_20 N O V TH ;
S_ORD_30 : T R E N T TH ;
S_ORD_31 : T R E N T U N TH ;

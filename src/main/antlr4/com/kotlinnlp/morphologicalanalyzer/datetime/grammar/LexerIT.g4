lexer grammar LexerIT;

import Letters, Symbols, NumbersLexer, LexerImportLast;

// -----
// -- Functional words
// -----

DAY_ST : DOT ; // not defined for IT
DAY_ND : DOT ; // not defined for IT
DAY_RD : DOT ; // not defined for IT
DAY_TH : DOT ; // not defined for IT

AND : E ;
AT  : A L | A L L E ;

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

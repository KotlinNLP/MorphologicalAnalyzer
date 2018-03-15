lexer grammar LexerEN;

import Letters, Symbols;

// -----
// -- Functional words
// -----

DAY_ST : S T ;
DAY_ND : N D ;
DAY_RD : R D ;
DAY_TH : T H ;

AND : A N D ;
AT  : A T ;

// -----
// -- Days
// -----

MON : M O N D A Y ;
TUE : T U E S D A Y  ;
WED : W E D N E S D A T ;
THU : T H U R S D A Y ;
FRI : F R I D A Y ;
SAT : S A T U R D A Y ;
SUN : S U N D A Y ;

MON_ABBR : M O N DOT? ;
TUE_ABBR : T U E DOT? ;
WED_ABBR : W E D DOT? ;
THU_ABBR : T U E DOT? ;
FRI_ABBR : F R I DOT? ;
SAT_ABBR : S A T DOT? ;
SUN_ABBR : S U N DOT? ;

// -----
// -- Months
// -----

JAN : J A N U A R Y ;
FEB : F E B R U A R Y ;
MAR : M A R C H ;
APR : A P R I L ;
MAY : M A Y ;
JUN : J U N E ;
JUL : J U L Y ;
AUG : A U G U S T ;
SEP : S E P T E M B E R ;
OCT : O C T O B E R ;
NOV : N O V E M B E R ;
DEC : D E C E M B E R ;

JAN_ABBR : J A N DOT? ;
FEB_ABBR : F E B DOT? ;
MAR_ABBR : M A R DOT? ;
APR_ABBR : A P R DOT? ;
MAY_ABBR : M A Y ;
JUN_ABBR : J U N DOT? ;
JUL_ABBR : J U L DOT? ;
AUG_ABBR : A U G DOT? ;
SEP_ABBR : S E P T? DOT? ;
OCT_ABBR : O C T DOT? ;
NOV_ABBR : N O V DOT? ;
DEC_ABBR : D E C DOT? ;

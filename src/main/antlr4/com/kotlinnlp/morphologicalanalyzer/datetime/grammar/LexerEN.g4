lexer grammar LexerEN;

import Letters, Symbols, NumbersLexer, TimeLexer, LexerImportLast;

// -----
// -- Functional words
// -----

DAY_ST : S T ;
DAY_ND : N D ;
DAY_RD : R D ;
DAY_TH : T H ;

AND : A N D ;
AT  : A T ;
THE : T H E ;
OF  : O F (SPACE_SEP THE)? ;
ON  : O N (SPACE_SEP THE)? ;
IN  : I N (SPACE_SEP THE)? ;

THIS : T H I S ;
LAST : L A S T ;
PREV : P R E V DOT? | P R E V I O U S ;
NEXT : N E X T | C O M I N G ;

// -----
// -- Special expressions
// -----

O_CLOCK : O SPACE_SEP? APEX? SPACE_SEP? C L O C K ;
TIME_H : H ;

// -----
// -- Words
// -----

YEAR : Y E A R S? ;
MONTH : M O N T H S? ;
WEEK : W E E K S? ;
WEEKEND : W E E K DASH? E N D S? ;
DAY : D A Y S? ;

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
THU_ABBR : T H U DOT? ;
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

// -----
// -- Cardinal Numbers
// -----

S_1  : O N E ;
S_2  : T W O ;
S_3  : T H R E E ;
S_4  : F O U R ;
S_5  : F I V E ;
S_6  : S I X ;
S_7  : S E V E N ;
S_8  : E I G H T ;
S_9  : N I N E ;
S_10 : T E N ;
S_11 : E L E V E N ;
S_12 : T W E L V E ;
S_13 : T H I R T E E N ;
S_14 : F O U R T E E N ;
S_15 : F I F T E E N ;
S_16 : S I X T E E N ;
S_17 : S E V E N T E E N ;
S_18 : E I G H T E E N ;
S_19 : N I N E T E E N ;
S_20 : T W E N T Y ;
S_21 : T W E N T Y DASH? S_1 ;
S_22 : T W E N T Y DASH? S_2 ;
S_23 : T W E N T Y DASH? S_3 ;
S_24 : T W E N T Y DASH? S_4 ;
S_25 : T W E N T Y DASH? S_5 ;
S_26 : T W E N T Y DASH? S_6 ;
S_27 : T W E N T Y DASH? S_7 ;
S_28 : T W E N T Y DASH? S_8 ;
S_29 : T W E N T Y DASH? S_9 ;
S_30 : T H I R T Y ;
S_31 : T H I R T Y DASH? S_1 ;

// -----
// -- Ordinal Numbers
// -----

fragment TH : DAY_TH ;

S_ORD_1  : F I R S T ;
S_ORD_2  : S E C O N D ;
S_ORD_3  : T H I R D ;
S_ORD_4  : S_4 TH ;
S_ORD_5  : F I F TH ;
S_ORD_6  : S_6 TH ;
S_ORD_7  : S_7 TH ;
S_ORD_8  : E I G H TH ;
S_ORD_9  : N I N TH | N I N E TH ; // actually 'nineth' is a mispelling
S_ORD_10 : S_10 TH ;
S_ORD_11 : S_11 TH ;
S_ORD_12 : T W E L F TH ;
S_ORD_13 : S_13 TH ;
S_ORD_14 : S_14 TH ;
S_ORD_15 : S_15 TH ;
S_ORD_16 : S_16 TH ;
S_ORD_17 : S_17 TH ;
S_ORD_18 : S_18 TH ;
S_ORD_19 : S_19 TH ;
S_ORD_20 : T W E N T I E TH ;
S_ORD_21 : S_20 S_ORD_1 ;
S_ORD_22 : S_20 S_ORD_2 ;
S_ORD_23 : S_20 S_ORD_3 ;
S_ORD_24 : S_20 S_ORD_4 ;
S_ORD_25 : S_20 S_ORD_5 ;
S_ORD_26 : S_20 S_ORD_6 ;
S_ORD_27 : S_20 S_ORD_7 ;
S_ORD_28 : S_20 S_ORD_8 ;
S_ORD_29 : S_20 S_ORD_9 ;
S_ORD_30 : T H I R T I E TH ;
S_ORD_31 : S_30 S_ORD_1 ;

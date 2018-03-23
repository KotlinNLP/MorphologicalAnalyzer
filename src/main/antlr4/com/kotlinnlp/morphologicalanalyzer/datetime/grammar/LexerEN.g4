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
OF  : O F (WS THE)? ;
ON  : O N (WS THE)? ;
IN  : I N (WS THE)? ;

THIS : T H I S ;
LAST : L A S T ;
PREV : P R E V DOT? | P R E V I O U S ;
NEXT : N E X T | C O M I N (APEX | G)? ;
PAST : P A S T ;

AGO : A G O ;
HENCE : H E N C E ;

AFTER : A F T E R ;
BEFORE : B E F O R E ;

FROM : F R O M ;
TO   : T O ;

// -----
// -- Special expressions
// -----

O_CLOCK : O WS? APEX? WS? C L O C K ;

// -----
// -- Words
// -----

YEAR    : Y E A R S? ;
MONTH   : M O N T H S? ;
WEEK    : W E E K S? ;
WEEKEND : W E E K DASH? E N D S? ;
DAY     : D A Y S? ;
HOUR    : H O U R S? ;
MIN     : M I N (U T E) S? ;
SEC     : S E C S? ; // 'second' is also an ordinal number

// -----
// -- Holidays
// -----

CHRISTMAS     : C H R I S T M A S ;
CHRISTMAS_EVE : CHRISTMAS WS E V E ;
EASTER        : E A S T E R ;

// -----
// -- Days
// -----

MON : M O N D A Y ;
TUE : T U E S D A Y  ;
WED : W E D N E S D A Y ;
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

NS_1  : O N E ;
NS_2  : T W O ;
NS_3  : T H R E E ;
NS_4  : F O U R ;
NS_5  : F I V E ;
NS_6  : S I X ;
NS_7  : S E V E N ;
NS_8  : E I G H T ;
NS_9  : N I N E ;
NS_10 : T E N ;
NS_11 : E L E V E N ;
NS_12 : T W E L V E ;
NS_13 : T H I R T E E N ;
NS_14 : F O U R T E E N ;
NS_15 : F I F T E E N ;
NS_16 : S I X T E E N ;
NS_17 : S E V E N T E E N ;
NS_18 : E I G H T E E N ;
NS_19 : N I N E T E E N ;
NS_20 : T W E N T Y ;
NS_21 : T W E N T Y DASH? NS_1 ;
NS_22 : T W E N T Y DASH? NS_2 ;
NS_23 : T W E N T Y DASH? NS_3 ;
NS_24 : T W E N T Y DASH? NS_4 ;
NS_25 : T W E N T Y DASH? NS_5 ;
NS_26 : T W E N T Y DASH? NS_6 ;
NS_27 : T W E N T Y DASH? NS_7 ;
NS_28 : T W E N T Y DASH? NS_8 ;
NS_29 : T W E N T Y DASH? NS_9 ;
NS_30 : T H I R T Y ;
NS_31 : T H I R T Y DASH? NS_1 ;

// -----
// -- Ordinal Numbers
// -----

fragment TH : DAY_TH ;

NS_ORD_1  : F I R S T ;
NS_ORD_2  : S E C O N D ;
NS_ORD_3  : T H I R D ;
NS_ORD_4  : NS_4 TH ;
NS_ORD_5  : F I F TH ;
NS_ORD_6  : NS_6 TH ;
NS_ORD_7  : NS_7 TH ;
NS_ORD_8  : E I G H TH ;
NS_ORD_9  : N I N TH | N I N E TH ; // actually 'nineth' is a mispelling
NS_ORD_10 : NS_10 TH ;
NS_ORD_11 : NS_11 TH ;
NS_ORD_12 : T W E L F TH ;
NS_ORD_13 : NS_13 TH ;
NS_ORD_14 : NS_14 TH ;
NS_ORD_15 : NS_15 TH ;
NS_ORD_16 : NS_16 TH ;
NS_ORD_17 : NS_17 TH ;
NS_ORD_18 : NS_18 TH ;
NS_ORD_19 : NS_19 TH ;
NS_ORD_20 : T W E N T I E TH ;
NS_ORD_21 : NS_20 NS_ORD_1 ;
NS_ORD_22 : NS_20 NS_ORD_2 ;
NS_ORD_23 : NS_20 NS_ORD_3 ;
NS_ORD_24 : NS_20 NS_ORD_4 ;
NS_ORD_25 : NS_20 NS_ORD_5 ;
NS_ORD_26 : NS_20 NS_ORD_6 ;
NS_ORD_27 : NS_20 NS_ORD_7 ;
NS_ORD_28 : NS_20 NS_ORD_8 ;
NS_ORD_29 : NS_20 NS_ORD_9 ;
NS_ORD_30 : T H I R T I E TH ;
NS_ORD_31 : NS_30 NS_ORD_1 ;

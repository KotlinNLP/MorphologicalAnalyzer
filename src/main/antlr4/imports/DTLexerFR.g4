lexer grammar DTLexerFR;

import DTLetters, DTSymbols, DTNumbersLexer, DTTimeLexer, DTImportLast;

// -----
// -- Functional words
// -----

DAY_ST : NOT_DEFINED ;
DAY_ND : NOT_DEFINED ;
DAY_RD : NOT_DEFINED ;
DAY_TH : I EE M E ;

AND : E T ;
AT  : AA (WS THE)? | A U | A U X ;
THE : L E | L APEX | L A | L E S ;
OF  : D E (WS THE)? | D U | D E S ;
ON  : NOT_DEFINED ;
IN  : D A N S ;

THIS : C E (T (T E)?)? ;
LAST : D E R N I (EE | E) R E? ;
PREV : P R EE C EE D E N T E? ;
NEXT : S U I V A N T E? ;
PAST : P A S S EE E? ;

AGO : NOT_DEFINED ;
AGO_PREFIX : I L WS Y WS A ;
HENCE : AA WS P A R T I R WS D E WS NOW ;

AFTER : A P R EE S ;
BEFORE : A V A N T ;

FROM : NOT_DEFINED ;
TO   : NOT_DEFINED ;

ABOUT : E N V I R O N | V E R S ;

EN_POSSESSIVE : NOT_DEFINED ;

// -----
// -- Special expressions
// -----

O_CLOCK : NOT_DEFINED ;

// -----
// -- Words
// -----

NOW                  : M A I N T E N A N T ; // 'heure' (= 'now') is equal to HOUR
TODAY                : A U J O U R D APEX WS? H U I ;
YESTERDAY            : H I E R ;
TOMORROW             : D E M A I N ;
DAY_AFTER_TOMORROW   : AFTER WS? TOMORROW ;
DAY_BEFORE_YESTERDAY : BEFORE DASH YESTERDAY ;

YEAR         : A N N EE E S? ;
MONTH        : M O I S ;
WEEK         : S E M A I N E S? ;
WEEKEND      : F I N WS D E WS WEEK | W E E K DASH? E N D S? ;
DAY          : J O U R (N EE E S)? ;
HOUR         : H E U R E S? ;
HALF_HOUR    : D E M I DASH HOUR ;
QUARTER_HOUR : Q U A R T WS D (APEX | APEX? WS) HOUR ; // flexible for mispellings
MIN          : M I N U T E S? ;
SEC          : S E C O N D E S? ;

MORNING   : M A T I N (EE E)? ;
NOON      : D EE J E U N E R | M I D I ;
AFTERNOON : AFTER DASH M I D I ;
EVENING   : (C E WS)? S O I R (EE E)? ;
NIGHT     : N U I T ;
TONIGHT   : C E T T E WS NIGHT ;

// -----
// -- Holidays
// -----

CHRISTMAS     : N O EE L ;
CHRISTMAS_EVE : V E I L L E WS OF WS CHRISTMAS ;
EASTER        : P AA Q U E S ;

// -----
// -- Days
// -----

MON : L U N D I ;
TUE : M A R D I ;
WED : M E R C R E D I ;
THU : J E U D I ;
FRI : V E N D R E D I ;
SAT : S A M E D I ;
SUN : D I M A N C H E ;

MON_ABBR : L U N DOT? ;
TUE_ABBR : M A R DOT? ;
WED_ABBR : M E R DOT? ;
THU_ABBR : J E U DOT? ;
FRI_ABBR : V E N DOT? ;
SAT_ABBR : S A M DOT? ;
SUN_ABBR : D I M DOT? ;

// -----
// -- Months
// -----

JAN : J A N V I E R ;
FEB : F EE V R I E R ;
MAR : M A R S ;
APR : A V R I L ;
MAY : M A I ;
JUN : J U I N ;
JUL : J U I L L E T ;
AUG : A O UU T ;
SEP : S E P T E M B R E ;
OCT : O C T O B R E ;
NOV : N O V E M B R E ;
DEC : D E C E M B R E ;

JAN_ABBR : J A N V DOT? ;
FEB_ABBR : F EE V R DOT? ;
MAR_ABBR : M A R DOT? ;
APR_ABBR : A V R DOT? ;
MAY_ABBR : MAY ;
JUN_ABBR : JUN ;
JUL_ABBR : J U I L DOT? ;
AUG_ABBR : AUG ;
SEP_ABBR : S E P T? DOT? ;
OCT_ABBR : O C T DOT? ;
NOV_ABBR : N O V DOT? ;
DEC_ABBR : D EE C DOT? ;

// -----
// -- Cardinal Numbers
// -----

NS_1  : U N E? ;
NS_2  : D E U X | U N WS C O U P L E WS D E ;
NS_3  : T R O I S ;
NS_4  : Q U A T R E ;
NS_5  : C I N Q ;
NS_6  : S I X ;
NS_7  : S E P T ;
NS_8  : H U I T ;
NS_9  : N E U F ;
NS_10 : D I X ;
NS_11 : O N Z E ;
NS_12 : D O U Z E ;
NS_13 : T R E I Z E ;
NS_14 : Q U A T O R Z E ;
NS_15 : Q U I N Z E ;
NS_16 : S E I Z E ;
NS_17 : NS_10 DASH NS_7 ;
NS_18 : NS_10 DASH NS_8 ;
NS_19 : NS_10 DASH NS_9 ;
NS_20 : V I N G T ;
NS_21 : NS_20 WS AND WS NS_1 ;
NS_22 : NS_20 DASH NS_2 ;
NS_23 : NS_20 DASH NS_3 ;
NS_24 : NS_20 DASH NS_4 ;
NS_25 : NS_20 DASH NS_5 ;
NS_26 : NS_20 DASH NS_6 ;
NS_27 : NS_20 DASH NS_7 ;
NS_28 : NS_20 DASH NS_8 ;
NS_29 : NS_20 DASH NS_9 ;
NS_30 : T R E N T E ;
NS_31 : NS_30 WS AND WS NS_1 ;

// -----
// -- Ordinal Numbers
// -----

fragment TH : DAY_TH ;

NS_ORD_1  : P R E M I (E R | EE R E) ;
NS_ORD_2  : NS_2 TH ;
NS_ORD_3  : NS_3 TH ;
NS_ORD_4  : Q U A T R TH ;
NS_ORD_5  : NS_5 U TH ;
NS_ORD_6  : NS_6 TH ;
NS_ORD_7  : NS_7 TH ;
NS_ORD_8  : NS_8 TH ;
NS_ORD_9  : N E U V TH ;
NS_ORD_10 : NS_10 TH ;
NS_ORD_11 : O N Z TH ;
NS_ORD_12 : D O U X TH ;
NS_ORD_13 : T R E I Z TH ;
NS_ORD_14 : Q U A T O R Z TH ;
NS_ORD_15 : Q U I N Z TH ;
NS_ORD_16 : S E I Z TH ;
NS_ORD_17 : NS_ORD_7 TH ;
NS_ORD_18 : NS_ORD_8 TH ;
NS_ORD_19 : NS_ORD_9 TH ;
NS_ORD_20 : NS_20 TH ;
NS_ORD_21 : NS_20 WS AND WS U N TH ;
NS_ORD_22 : NS_20 NS_ORD_2 ;
NS_ORD_23 : NS_20 NS_ORD_3 ;
NS_ORD_24 : NS_20 NS_ORD_4 ;
NS_ORD_25 : NS_20 NS_ORD_5 ;
NS_ORD_26 : NS_20 NS_ORD_6 ;
NS_ORD_27 : NS_20 NS_ORD_7 ;
NS_ORD_28 : NS_20 NS_ORD_8 ;
NS_ORD_29 : NS_20 NS_ORD_9 ;
NS_ORD_30 : T R E N T TH ;
NS_ORD_31 : NS_30 WS AND WS U N TH ;

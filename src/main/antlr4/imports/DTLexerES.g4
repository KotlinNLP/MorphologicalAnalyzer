lexer grammar DTLexerES;

import DTLetters, DTSymbols, DTNumbersLexer, DTTimeLexer, DTImportLast;

// -----
// -- Functional words
// -----

fragment DECLINATION_SUFFIX : O | O S | A | A S | E | E S ;

DAY_ST : NOT_DEFINED ;
DAY_ND : NOT_DEFINED ;
DAY_RD : NOT_DEFINED ;
DAY_TH : E R DECLINATION_SUFFIX ;

AND : Y ;
AT  : NOT_DEFINED ;
THE : E L | L A | L O S | L A S ;
OF  : D E L? ;
ON  : NOT_DEFINED ;
IN  : P O R WS THE | E N ;

THIS : E S T DECLINATION_SUFFIX ;
LAST : U L T I M DECLINATION_SUFFIX ;
PREV : A N T E R I O R DECLINATION_SUFFIX? ;
NEXT : S I G U I E N T DECLINATION_SUFFIX ;
PAST : P A S A D DECLINATION_SUFFIX ;

AGO : NOT_DEFINED ;
AGO_PREFIX : H A C E ;
HENCE : Q U E WS V I E N E | C O M E N Z A N D O WS A H O R A ;

AFTER : D E S P U (E | EE) S ;
BEFORE : A N T E S WS OF ;

FROM : D E S D E (WS THE)? ;
TO   : A WS THE | A L ;

ABOUT : (A C E R C A | A L R E D E D O R) WS D E ;

EN_POSSESSIVE : NOT_DEFINED ;

// -----
// -- Special expressions
// -----

O_CLOCK : E N WS P U N T O ;

// -----
// -- Words
// -----

NOW                  : A H O R A ;
TODAY                : H O Y ;
YESTERDAY            : A Y E R ;
TOMORROW             : NOT_DEFINED ; // equal to morning
DAY_AFTER_TOMORROW   : AFTER WS D E WS TOMORROW ;
DAY_BEFORE_YESTERDAY : L APEX? WS? A L T R O WS YESTERDAY ;

YEAR         : A NN O S? ;
MONTH        : M E S (E S)? ;
WEEK         : S E M A N A S? ;
WEEKEND      : F I N WS D E WS WEEK | W E E K DASH? E N D S? ;
DAY          : D II A S? ;
HOUR         : H O R A S? ;
HALF_HOUR    : M E D I A WS HOUR ;
QUARTER_HOUR : C U A R T O S? WS D E WS HOUR ;
MIN          : M I N U T O S? ;
SEC          : S E C ; // 'secundo' is also an ordinal number

MORNING   : M A NN A N A ;
NOON      : M E D I O D II A ;
AFTERNOON : T A R D E ;
EVENING   : NOT_DEFINED ;
NIGHT     : N O C H E ;
TONIGHT   : THIS WS NIGHT ;

// -----
// -- Holidays
// -----

CHRISTMAS     : N A V I D A D ;
CHRISTMAS_EVE : N O C H E B U E N A | V I G I L I A WS OF WS CHRISTMAS ;
EASTER        : P A S C U A ;

// -----
// -- Days
// -----

MON : L U N E S ;
TUE : M A R T E S ;
WED : M I EE R C O L E S ;
THU : J U E V E S ;
FRI : V I E R N E S ;
SAT : S AA B A D O ;
SUN : D O M I N G O ;

MON_ABBR : L U N DOT? ;
TUE_ABBR : M A R DOT? ;
WED_ABBR : M I EE DOT? ;
THU_ABBR : J U E DOT? ;
FRI_ABBR : V I E DOT? ;
SAT_ABBR : S AA B DOT? ;
SUN_ABBR : D O M DOT? ;

// -----
// -- Months
// -----

JAN : E N E R O ;
FEB : F E B R E R O ;
MAR : M A R Z O ;
APR : A B R I L ;
MAY : M A Y O ;
JUN : J U N I O ;
JUL : J U L I O ;
AUG : A G O S T O ;
SEP : S E P T I E M B R E ;
OCT : O C T U B R E ;
NOV : N O V I E M B R E ;
DEC : D I C I E M B R E ;

JAN_ABBR : E N DOT? ;
FEB_ABBR : F E B DOT? ;
MAR_ABBR : M A R DOT? ;
APR_ABBR : A B R DOT? ;
MAY_ABBR : M A Y DOT? ;
JUN_ABBR : J U N DOT? ;
JUL_ABBR : J U L DOT? ;
AUG_ABBR : A G O? DOT? ;
SEP_ABBR : S E P T DOT? ;
OCT_ABBR : O C T DOT? ;
NOV_ABBR : N O V DOT? ;
DEC_ABBR : D I C DOT? ;

// -----
// -- Cardinal Numbers
// -----

NS_1  : U N (O | A) ;
NS_2  : D O S | U N WS P A R (WS D E)? ;
NS_3  : T R E S ;
NS_4  : C U A T R O ;
NS_5  : C I N C O ;
NS_6  : S (E | EE) I S ;
NS_7  : S I E T E ;
NS_8  : O C H O ;
NS_9  : N U E V E ;
NS_10 : D I E Z ;
NS_11 : O N C E ;
NS_12 : D O C E ;
NS_13 : T R E C E ;
NS_14 : C A T O R C E ;
NS_15 : Q U I N C E ;
NS_16 : D I E C I NS_6 ;
NS_17 : D I E C I NS_7 ;
NS_18 : D I E C I NS_8 ;
NS_19 : D I E C I NS_9 ;
NS_20 : V E I N T E ;
NS_21 : V E I N T I NS_1 ;
NS_22 : V E I N T I NS_2 ;
NS_23 : V E I N T I NS_3 ;
NS_24 : V E I N T I NS_4 ;
NS_25 : V E I N T I NS_5 ;
NS_26 : V E I N T I NS_6 ;
NS_27 : V E I N T I NS_7 ;
NS_28 : V E I N T I NS_8 ;
NS_29 : V E I N T I NS_9 ;
NS_30 : T R E I N T A ;
NS_31 : NS_30 WS AND WS NS_1 ;

// -----
// -- Ordinal Numbers
// -----

NS_ORD_1  : P R I M E R DECLINATION_SUFFIX ;
NS_ORD_2  : S E G U N D DECLINATION_SUFFIX ;
NS_ORD_3  : T E R C E R DECLINATION_SUFFIX ;
NS_ORD_4  : C U A R T DECLINATION_SUFFIX ;
NS_ORD_5  : Q U I N T DECLINATION_SUFFIX ;
NS_ORD_6  : S E X T DECLINATION_SUFFIX ;
NS_ORD_7  : S (E | EE) P T I M DECLINATION_SUFFIX ;
NS_ORD_8  : O C T A V DECLINATION_SUFFIX ;
NS_ORD_9  : (N O V E N | N O N) DECLINATION_SUFFIX ;
NS_ORD_10 : D (E | EE) C I M DECLINATION_SUFFIX ;
NS_ORD_11 : U N D (E | EE) C I M DECLINATION_SUFFIX ;
NS_ORD_12 : D U O D (E | EE) C I M DECLINATION_SUFFIX ;
NS_ORD_13 : NS_ORD_10 WS? NS_ORD_3 ;
NS_ORD_14 : NS_ORD_10 WS? NS_ORD_4 ;
NS_ORD_15 : NS_ORD_10 WS? NS_ORD_5 ;
NS_ORD_16 : NS_ORD_10 WS? NS_ORD_6 ;
NS_ORD_17 : NS_ORD_10 WS? NS_ORD_7 ;
NS_ORD_18 : NS_ORD_10 WS? NS_ORD_8 ;
NS_ORD_19 : NS_ORD_10 WS? NS_ORD_9 ;
NS_ORD_20 : V I G (E | EE) S I M DECLINATION_SUFFIX ;
NS_ORD_21 : NS_ORD_20 WS? NS_ORD_1 ;
NS_ORD_22 : NS_ORD_20 WS? NS_ORD_2 ;
NS_ORD_23 : NS_ORD_20 WS? NS_ORD_3 ;
NS_ORD_24 : NS_ORD_20 WS? NS_ORD_4 ;
NS_ORD_25 : NS_ORD_20 WS? NS_ORD_5 ;
NS_ORD_26 : NS_ORD_20 WS? NS_ORD_6 ;
NS_ORD_27 : NS_ORD_20 WS? NS_ORD_7 ;
NS_ORD_28 : NS_ORD_20 WS? NS_ORD_8 ;
NS_ORD_29 : NS_ORD_20 WS? NS_ORD_9 ;
NS_ORD_30 : T R I G (E | EE) S I M DECLINATION_SUFFIX ;
NS_ORD_31 : NS_ORD_30 WS? NS_ORD_1 ;

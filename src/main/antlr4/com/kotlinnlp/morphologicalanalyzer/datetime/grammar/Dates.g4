grammar Dates;

import Symbols, Numbers, LexerEN;

// -----
// -- DATE
// -----

date 
    : day date_sep month date_sep year
    | year date_sep month date_sep day
    | month date_sep day
    | day date_sep month
    ;

date_sep : SPACE_SEP | DASH | SLASH | DOT ;

// -----
// -- YEAR
// -----

year : APEX d_0_99 | d_0_9999 ;

// -----
// -- MONTH
// -----

month : d_0_12 | month_full | month_abbr ;

month_full : JAN | FEB | MAR | APR | MAY | JUN | JUL | AUG | SEP | OCT | NOV | DEC ;
month_abbr : JAN_ABBR | FEB_ABBR | MAR_ABBR | APR_ABBR | MAY_ABBR | JUN_ABBR | JUL_ABBR | AUG_ABBR | SEP_ABBR | OCT_ABBR | NOV_ABBR | DEC_ABBR ;

// -----
// -- DAY
// -----

day
    : ('1' | '01' | '21' | '31') DAY_ST?
    | ('2' | '02' | '22') DAY_ND?
    | ('3' | '03' | '23') DAY_RD?
    | d_0_31 DAY_TH?
    | day_week
    | day_week_abbr
    ;

day_week : MON | TUE | WED | THU | FRI | SAT | SUN;
day_week_abbr : MON_ABBR | TUE_ABBR | WED_ABBR | THU_ABBR | FRI_ABBR | SAT_ABBR | SUN_ABBR;

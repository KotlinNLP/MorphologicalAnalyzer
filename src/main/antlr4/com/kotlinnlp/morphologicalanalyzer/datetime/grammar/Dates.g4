grammar Dates;

import LexerEN, NumbersParser;

// -----
// -- DATE
// -----

date 
    : day date_sep month date_sep year // DMY
    | day SPACE_SEP month_str SPACE_SEP year // DMY
    | month date_sep day date_sep year // MDY
    | month_str SPACE_SEP day SPACE_SEP year // MDY
    | year date_sep month date_sep day // YMD
    | year SPACE_SEP month_str SPACE_SEP day // YMD
    | day date_sep month // DM
    | day SPACE_SEP month_str // DM
    | month date_sep day // MD
    | month_str SPACE_SEP day // MD
    | year_APEX
    | year_modern
    | month_str
    | day_str
    ;

date_sep : DASH | SLASH | DOT ;

// -----
// -- YEAR
// -----

year : year_APEX | d_0_9999 ;

year_APEX : APEX d_0_99 ;
year_modern : D_1900_2099 ;

// -----
// -- MONTH
// -----

month : month_num | month_str ;

month_num : d_0_12 ;
month_str : month_abbr | month_full ;

month_full : JAN | FEB | MAR | APR | MAY | JUN | JUL | AUG | SEP | OCT | NOV | DEC ;
month_abbr : JAN_ABBR | FEB_ABBR | MAR_ABBR | APR_ABBR | MAY_ABBR | JUN_ABBR | JUL_ABBR | AUG_ABBR | SEP_ABBR | OCT_ABBR | NOV_ABBR | DEC_ABBR ;

// -----
// -- DAY
// -----

day
    : day_str SPACE_SEP? day_num
    | day_str COMMA SPACE_SEP? day_num
    | day_str SPACE_SEP COMMA day_num
    | day_num
    | day_str
    ;



day_num
    : (D_1 | D_01 | D_21 | D_31) (SPACE_SEP? DAY_ST)?
    | (D_2 | D_02 | D_22) (SPACE_SEP? DAY_ND)?
    | (D_3 | D_03 | D_23) (SPACE_SEP? DAY_RD)?
    | d_0_31 DAY_TH?
    ;

day_str : day_week | day_week_abbr;

day_week      : MON | TUE | WED | THU | FRI | SAT | SUN;
day_week_abbr : MON_ABBR | TUE_ABBR | WED_ABBR | THU_ABBR | FRI_ABBR | SAT_ABBR | SUN_ABBR;

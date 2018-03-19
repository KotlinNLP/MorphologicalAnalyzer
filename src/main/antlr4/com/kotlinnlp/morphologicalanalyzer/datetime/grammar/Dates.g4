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

year : year_APEX | year_num ;

year_APEX : APEX d_0_99 ;

year_num    : year_modern | year_abbr | year_full ;
year_modern : D_1900_2099 ;
year_abbr   : d_0_99 ;
year_full   : d_100_9999 ;

// -----
// -- MONTH
// -----

month : month_num | month_str ;

month_num : d_0_12 ;

month_str
    : month_jan
    | month_feb
    | month_mar
    | month_apr
    | month_may
    | month_jun
    | month_jul
    | month_aug
    | month_sep
    | month_oct
    | month_nov
    | month_dec
    ;

month_jan : JAN | JAN_ABBR ;
month_feb : FEB | FEB_ABBR ;
month_mar : MAR | MAR_ABBR ;
month_apr : APR | APR_ABBR ;
month_may : MAY | MAY_ABBR ;
month_jun : JUN | JUN_ABBR ;
month_jul : JUL | JUL_ABBR ;
month_aug : AUG | AUG_ABBR ;
month_sep : SEP | SEP_ABBR ;
month_oct : OCT | OCT_ABBR ;
month_nov : NOV | NOV_ABBR ;
month_dec : DEC | DEC_ABBR ;

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

day_str : day_mon | day_tue | day_wed | day_thu | day_fri | day_sat | day_sun ;

day_mon : MON | MON_ABBR ;
day_tue : TUE | TUE_ABBR ;
day_wed : WED | WED_ABBR ;
day_thu : THU | THU_ABBR ;
day_fri : FRI | FRI_ABBR ;
day_sat : SAT | SAT_ABBR ;
day_sun : SUN | SUN_ABBR ;
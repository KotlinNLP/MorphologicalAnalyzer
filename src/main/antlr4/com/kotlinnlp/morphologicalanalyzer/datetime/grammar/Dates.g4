grammar Dates;

import LexerEN, NumbersParser;

// -----
// -- DATE
// -----

date 
    : day date_sep month date_sep year // D/M/Y
    | day_week COMMA? SPACE_SEP? month_str SPACE_SEP? day_num // WeekDay ,? Month D
    | day SPACE_SEP month SPACE_SEP year_modern // D M YYYY(19XX-20XX)
    | day SPACE_SEP? month_str SPACE_SEP? year // D Month Y
    | day SPACE_SEP of_sep month_str SPACE_SEP of_sep year // D of? Month of? Y
    | month date_sep day date_sep year // M/D/Y
    | month SPACE_SEP day SPACE_SEP year_modern // M D YYYY(19XX-20XX)
    | month_str SPACE_SEP day (SPACE_SEP? COMMA SPACE_SEP? | SPACE_SEP) of_sep? year // Month D ,? of? Y
    | year date_sep month date_sep day // Y/M/D
    | year SPACE_SEP month_str of_sep? day // Y Month of? D
    | day date_sep month // D/M
    | month date_sep day // M/D
    | day SPACE_SEP of_sep? month_str // D of? Month
    | month_str SPACE_SEP? day // Month D
    | month_str SPACE_SEP? year // Month Y
    | year_APEX // 'YY
    | year_modern // YYYY(19XX-20XX)
    | month_str // Month
    | day_week // WeekDay
    ;

date_sep : DASH | SLASH | DOT ;
of_sep : OF SPACE_SEP ;

// -----
// -- YEAR
// -----

year : year_APEX | year_num ;

year_APEX : APEX SPACE_SEP? d_0_99 ;

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
    : day_week (SPACE_SEP (THE SPACE_SEP)?)? (day_num | day_s_str)
    | day_week SPACE_SEP? COMMA (SPACE_SEP (THE SPACE_SEP)?)? (day_num | day_s_str)
    | (THE SPACE_SEP)? (day_num | day_s_str)
    ;

day_num
    : (D_1 | D_01 | D_21 | D_31) (SPACE_SEP? DAY_ST)?
    | (D_2 | D_02 | D_22) (SPACE_SEP? DAY_ND)?
    | (D_3 | D_03 | D_23) (SPACE_SEP? DAY_RD)?
    | d_0_31 (SPACE_SEP? DAY_TH)?
    ;

day_s_str
    : day_s_1  | day_s_2  | day_s_3  | day_s_4  | day_s_5  | day_s_6  | day_s_7  | day_s_8  | day_s_9 | day_s_10
    | day_s_11 | day_s_12 | day_s_13 | day_s_14 | day_s_15 | day_s_16 | day_s_17 | day_s_18 | day_s_19 | day_s_20
    | day_s_21 | day_s_22 | day_s_23 | day_s_24 | day_s_25 | day_s_26 | day_s_27 | day_s_28 | day_s_29 | day_s_30
    | day_s_31
    ;

day_s_1 : S_1 | S_ORD_1 ;
day_s_2 : S_2 | S_ORD_2 ;
day_s_3 : S_3 | S_ORD_3 ;
day_s_4 : S_4 | S_ORD_4 ;
day_s_5 : S_5 | S_ORD_5 ;
day_s_6 : S_6 | S_ORD_6 ;
day_s_7 : S_7 | S_ORD_7 ;
day_s_8 : S_8 | S_ORD_8 ;
day_s_9 : S_9 | S_ORD_9 ;
day_s_10 : S_10 | S_ORD_10 ;
day_s_11 : S_11 | S_ORD_11 ;
day_s_12 : S_12 | S_ORD_12 ;
day_s_13 : S_13 | S_ORD_13 ;
day_s_14 : S_14 | S_ORD_14 ;
day_s_15 : S_15 | S_ORD_15 ;
day_s_16 : S_16 | S_ORD_16 ;
day_s_17 : S_17 | S_ORD_17 ;
day_s_18 : S_18 | S_ORD_18 ;
day_s_19 : S_19 | S_ORD_19 ;
day_s_20 : S_20 | S_ORD_20 ;
day_s_21 : S_21 | S_ORD_21 ;
day_s_22 : S_22 | S_ORD_22 ;
day_s_23 : S_23 | S_ORD_23 ;
day_s_24 : S_24 | S_ORD_24 ;
day_s_25 : S_25 | S_ORD_25 ;
day_s_26 : S_26 | S_ORD_26 ;
day_s_27 : S_27 | S_ORD_27 ;
day_s_28 : S_28 | S_ORD_28 ;
day_s_29 : S_29 | S_ORD_29 ;
day_s_30 : S_30 | S_ORD_30 ;
day_s_31 : S_31 | S_ORD_31 ;

day_week : day_mon | day_tue | day_wed | day_thu | day_fri | day_sat | day_sun ;

day_mon : MON | MON_ABBR ;
day_tue : TUE | TUE_ABBR ;
day_wed : WED | WED_ABBR ;
day_thu : THU | THU_ABBR ;
day_fri : FRI | FRI_ABBR ;
day_sat : SAT | SAT_ABBR ;
day_sun : SUN | SUN_ABBR ;
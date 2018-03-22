grammar Date;

import LexerEN, NumbersParser;

// -----
// -- DATE
// -----

date 
    : date_canonical // Y-M-D
    | day date_sep month date_sep year // D/M/Y
    | month date_sep day date_sep year // M/D/Y
    | year date_sep month date_sep day // Y/M/D
    | day_week COMMA? SPACE_SEP? month_str SPACE_SEP? day_num ((SPACE_SEP? COMMA)? SPACE_SEP year)? // WeekDay ,? Month D Y?
    | day SPACE_SEP month SPACE_SEP year_modern // D M YYYY(19XX-20XX)
    | day (SPACE_SEP of_sep?)? month_str (SPACE_SEP? COMMA)? (SPACE_SEP of_sep?)? year // D of? Month ,? of? Y
    | month SPACE_SEP day SPACE_SEP year_modern // M D YYYY(19XX-20XX)
    | month_str SPACE_SEP day (SPACE_SEP? COMMA SPACE_SEP? | SPACE_SEP) of_sep? year // Month D ,? of? Y
    | year SPACE_SEP month_str (SPACE_SEP of_sep)? day // Y Month of? D
    | day date_sep month // D/M
    | month date_sep day // M/D
    | day SPACE_SEP of_sep? month_str // D of? Month
    | month_str SPACE_SEP? (THE SPACE_SEP)? day // Month the? D
    | month_str SPACE_SEP? of_sep? year // Month of? Y
    | year_APEX // 'YY
    | year_modern // YYYY(19XX-20XX)
    | month_str // Month
    | day_week // WeekDay
    ;

date_sep : DASH | SLASH | DOT ;
of_sep : OF SPACE_SEP ;

date_canonical : year_num DASH month_num DASH day_num_canonical ;

// -----
// -- LITERALS
// -----

date_unit_literal : day_lit | week_lit | weekend_lit | month_lit | year_lit ;

day_lit     : DAY ;
week_lit    : WEEK ;
weekend_lit : WEEKEND ;
month_lit   : MONTH ;
year_lit    : YEAR ;

// -----
// -- DAY
// -----

day
    : day_week (SPACE_SEP (THE SPACE_SEP)?)? (day_num | day_str)
    | day_week SPACE_SEP? COMMA (SPACE_SEP (THE SPACE_SEP)?)? (day_num | day_str)
    | day_num
    | day_str
    ;

day_num : day_num_th | day_num_canonical | n_0_31 ;

day_num_canonical : n_00_31 ;

day_num_th
    : (N_1 | N_01 | N_21 | N_31) SPACE_SEP? DAY_ST
    | (N_2 | N_02 | N_22) SPACE_SEP? DAY_ND
    | (N_3 | N_03 | NS_23) SPACE_SEP? DAY_RD
    | n_0_31 SPACE_SEP? DAY_TH
    ;

day_str
    : ns_1  | ns_2  | ns_3  | ns_4  | ns_5  | ns_6  | ns_7  | ns_8  | ns_9  | ns_10
    | ns_11 | ns_12 | ns_13 | ns_14 | ns_15 | ns_16 | ns_17 | ns_18 | ns_19 | ns_20
    | ns_21 | ns_22 | ns_23 | ns_24 | ns_25 | ns_26 | ns_27 | ns_28 | ns_29 | ns_30
    | ns_31
    ;

day_week : day_mon | day_tue | day_wed | day_thu | day_fri | day_sat | day_sun ;

day_mon : MON | MON_ABBR ;
day_tue : TUE | TUE_ABBR ;
day_wed : WED | WED_ABBR ;
day_thu : THU | THU_ABBR ;
day_fri : FRI | FRI_ABBR ;
day_sat : SAT | SAT_ABBR ;
day_sun : SUN | SUN_ABBR ;

// -----
// -- MONTH
// -----

month : month_num | month_str ;

month_num : n_0_12 ;

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
// -- YEAR
// -----

year : year_APEX | year_num ;

year_APEX : APEX SPACE_SEP? n_0_99 ;

year_num    : year_modern | year_abbr | year_full ;
year_modern : N_1900_2099 ;
year_abbr   : n_0_99 ;
year_full   : n_100_9999 ;

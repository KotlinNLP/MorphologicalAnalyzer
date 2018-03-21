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
    | month_str SPACE_SEP? day // Month D
    | month_str SPACE_SEP? year // Month Y
    | year_APEX // 'YY
    | year_modern // YYYY(19XX-20XX)
    | month_str // Month
    | day_week // WeekDay
    ;

date_sep : DASH | SLASH | DOT ;
of_sep : OF SPACE_SEP ;

date_canonical : year_num DASH month_num DASH day_num_canonical ;

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
    : day_week (SPACE_SEP (THE SPACE_SEP)?)? (day_num | day_str)
    | day_week SPACE_SEP? COMMA (SPACE_SEP (THE SPACE_SEP)?)? (day_num | day_str)
    | (THE SPACE_SEP)? (day_num | day_str)
    ;

day_num : day_num_th | day_num_canonical | d_0_31 ;

day_num_canonical
    : D_01 | D_21 | D_31
    | D_02 | D_22
    | D_03 | D_23
    ;

day_num_th
    : (D_1 | D_01 | D_21 | D_31) SPACE_SEP? DAY_ST
    | (D_2 | D_02 | D_22) SPACE_SEP? DAY_ND
    | (D_3 | D_03 | D_23) SPACE_SEP? DAY_RD
    | d_0_31 SPACE_SEP? DAY_TH
    ;

day_str
    : n_s_1  | n_s_2  | n_s_3  | n_s_4  | n_s_5  | n_s_6  | n_s_7  | n_s_8  | n_s_9 | n_s_10
    | n_s_11 | n_s_12 | n_s_13 | n_s_14 | n_s_15 | n_s_16 | n_s_17 | n_s_18 | n_s_19 | n_s_20
    | n_s_21 | n_s_22 | n_s_23 | n_s_24 | n_s_25 | n_s_26 | n_s_27 | n_s_28 | n_s_29 | n_s_30
    | n_s_31
    ;

day_week : day_mon | day_tue | day_wed | day_thu | day_fri | day_sat | day_sun ;

day_mon : MON | MON_ABBR ;
day_tue : TUE | TUE_ABBR ;
day_wed : WED | WED_ABBR ;
day_thu : THU | THU_ABBR ;
day_fri : FRI | FRI_ABBR ;
day_sat : SAT | SAT_ABBR ;
day_sun : SUN | SUN_ABBR ;
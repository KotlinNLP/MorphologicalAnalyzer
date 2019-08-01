grammar NumbersFR;

import NUMLexerFR, NUMParser;

d_thdiv : DOT;
d_decdiv : COMMA;

w_one_million: W_ONE_MILLION;
w_million: W_MILLION;
w_one_billion: W_ONE_BILLION;
w_billion: W_BILLION;
w_one_trillion: W_ONE_TRILLION;
w_trillion: W_TRILLION;
w_one_quadrillion: W_ONE_QUADRILLION;
w_quadrillion: W_QUADRILLION;

w_ws_hypen: (WS+ | HYPEN);

n_tens_20    : (w_20 w_ws_hypen AND w_ws_hypen w_1 | w_20 HYPEN w_2 | w_20 HYPEN w_3 | w_20 HYPEN w_4 | w_20 HYPEN w_5 | w_20 HYPEN w_6 | w_20 HYPEN w_7 | w_20 HYPEN w_8 | w_20 HYPEN w_9 | w_20 w_ws_hypen AND w_ws_hypen d_unit | w_20 );
n_tens_30    : (w_30 w_ws_hypen AND w_ws_hypen w_1 | w_30 HYPEN w_2 | w_30 HYPEN w_3 | w_30 HYPEN w_4 | w_30 HYPEN w_5 | w_30 HYPEN w_6 | w_30 HYPEN w_7 | w_30 HYPEN w_8 | w_30 HYPEN w_9 | w_30 w_ws_hypen AND w_ws_hypen d_unit | w_30 );
n_tens_40    : (w_40 w_ws_hypen AND w_ws_hypen w_1 | w_40 HYPEN w_2 | w_40 HYPEN w_3 | w_40 HYPEN w_4 | w_40 HYPEN w_5 | w_40 HYPEN w_6 | w_40 HYPEN w_7 | w_40 HYPEN w_8 | w_40 HYPEN w_9 | w_40 w_ws_hypen AND w_ws_hypen d_unit | w_40 );
n_tens_50    : (w_50 w_ws_hypen AND w_ws_hypen w_1 | w_50 HYPEN w_2 | w_50 HYPEN w_3 | w_50 HYPEN w_4 | w_50 HYPEN w_5 | w_50 HYPEN w_6 | w_50 HYPEN w_7 | w_50 HYPEN w_8 | w_50 HYPEN w_9 | w_50 w_ws_hypen AND w_ws_hypen d_unit | w_50 );
n_tens_60    : (w_60 w_ws_hypen AND w_ws_hypen w_1 | w_60 HYPEN w_2 | w_60 HYPEN w_3 | w_60 HYPEN w_4 | w_60 HYPEN w_5 | w_60 HYPEN w_6 | w_60 HYPEN w_7 | w_60 HYPEN w_8 | w_60 HYPEN w_9 | w_60 w_ws_hypen AND w_ws_hypen d_unit | w_60 );
n_tens_70    : (w_60 w_ws_hypen AND w_ws_hypen w_11 | w_60 HYPEN w_12 | w_60 HYPEN w_13 | w_60 HYPEN w_14 | w_60 HYPEN w_15 | w_60 HYPEN w_16 | w_60 HYPEN w_17 | w_60 w_18 | w_60 HYPEN w_19 | w_60 HYPEN w_10);
n_tens_80    : (w_80 HYPEN w_1 | w_80 HYPEN w_2 | w_80 HYPEN w_3 | w_80 HYPEN w_4 | w_80 HYPEN w_5 | w_80 HYPEN w_6 | w_80 HYPEN w_7 | w_80 HYPEN w_8 | w_80 HYPEN w_9 | w_80 w_ws_hypen AND w_ws_hypen d_unit | w_80 W_S);
n_tens_90    : (w_80 HYPEN w_11 | w_80 HYPEN w_12 | w_80 HYPEN w_13 | w_80 HYPEN w_14 | w_80 HYPEN w_15 | w_80 HYPEN w_16 | w_80 HYPEN w_17 | w_80 HYPEN w_18 | w_80 HYPEN w_19 | w_80 HYPEN w_10);

w_unit   : (w_0 | w_1 | w_2 | w_3 | w_4 | w_5 | w_6 | w_7 | w_8 | w_9) ;
ten_pref  : (w_2 | w_3 | w_4 | w_5 | w_6 | w_7 | w_8 | w_9);
d_ten_pref: (D_2 | D_3 | D_4 | D_5 | D_6 | D_7 | D_8 | D_9);

// from 100 to 999
hundred: (ten_pref w_ws_hypen | d_ten_pref w_ws_hypen)? W_HUNDRED (((w_ws_hypen (n_1_99 | max_2_digits))?));

// from 1000 to 99999
thousand: W_THOUSAND (w_ws_hypen n_1_999)?
		| (thousand_pref | max_2_digits) w_ws_hypen W_THOUSAND (w_ws_hypen n_1_999)?;

// from 100k to 999k999
hundredthousand:
    (hundred | max_3_digits) w_ws_hypen W_THOUSAND (w_ws_hypen n_1_999)? ;

million:
	(w_1 | D_1) w_ws_hypen w_one_million (w_ws_hypen (max_6_digits_with_div | n_1_999k | max_6_digits))? # one_million
	|
	(max_6_digits_with_div | n_1_999k | max_6_digits) w_ws_hypen w_million (w_ws_hypen ((max_6_digits_with_div | n_1_999k | max_6_digits)))? # millions
;

million_prefix:
	(w_1 | D_1) w_ws_hypen w_one_million # one_million_prefix
	|
	(max_6_digits_with_div | n_1_999k | max_6_digits) w_ws_hypen w_million # millions_prefix
;

billion:
	(w_1 | D_1) w_ws_hypen w_one_billion (w_ws_hypen (max_9_digits_with_div | n_1_999m | max_9_digits))? # one_billion
	|
	(max_6_digits_with_div | n_1_999k | max_6_digits) w_ws_hypen w_billion (w_ws_hypen (max_9_digits_with_div | n_1_999m | max_9_digits))? # billions
;
billion_prefix:
	(w_1 | D_1) w_ws_hypen w_one_billion # one_billion_prefix
	|
	(max_6_digits_with_div | n_1_999k | max_6_digits) w_ws_hypen w_billion # billions_prefix
;

trillion:
	(w_1 | D_1) w_ws_hypen w_one_trillion w_ws_hypen n_1_999i? # one_trillion
	|
	(max_6_digits_with_div | n_1_999k | max_6_digits) w_ws_hypen w_trillion (w_ws_hypen n_1_999i)? # trillions
;
trillion_prefix:
	(w_1 | D_1) w_ws_hypen w_one_trillion # one_trillion_prefix
	|
	(max_6_digits_with_div | n_1_999k | max_6_digits) w_ws_hypen w_trillion # trillions_prefix
;

quadrillion:
	(w_1 | D_1) w_ws_hypen w_one_quadrillion w_ws_hypen n_1_999b? # one_quadrillion
	|
	(max_6_digits_with_div | n_1_999k | max_6_digits) w_ws_hypen w_quadrillion (w_ws_hypen n_1_999b)? # quadrillions
;
quadrillion_prefix:
	(w_1 | D_1) w_ws_hypen w_one_quadrillion # one_quadrillion_prefix
	|
	(max_6_digits_with_div | n_1_999k | max_6_digits) w_ws_hypen w_quadrillion # quadrillions_prefix
;

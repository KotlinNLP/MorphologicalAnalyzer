grammar NumbersIT;

import NUMLexerIT, NUMParser;

d_thdiv : DOT;
d_decdiv : COMMA;

w_20_pref: W_20_PREF;

w_one_million: W_ONE_MILLION;
w_million: W_MILLION;
w_one_billion: W_ONE_BILLION;
w_billion: W_BILLION;
w_one_trillion: W_ONE_TRILLION;
w_trillion: W_TRILLION;
w_one_quadrillion: W_ONE_QUADRILLION;
w_quadrillion: W_QUADRILLION;

n_tens_20    : (w_20_pref w_1 | w_20_pref w_1_art | w_20 WS* w_2 | w_20 WS* w_3 | w_20 WS* w_4 | w_20 WS* w_5 | w_20 WS* w_6 | w_20 WS* w_7 | w_20_pref w_8 | w_20 WS* w_9 | w_20 WS d_unit | w_20);
n_tens_30    : (w_30 w_1 | w_30 w_1_art | w_30 W_A WS* w_2 | w_30 W_A WS* w_3 | w_30 W_A WS* w_4 | w_30 W_A WS* w_5 | w_30 W_A WS* w_6 | w_30 W_A WS* w_7 | w_30 w_8 | w_30 W_A WS* w_9 | w_30 W_A WS d_unit | w_30 W_A);
n_tens_40    : (w_40 w_1 | w_40 w_1_art | w_40 W_A WS* w_2 | w_40 W_A WS* w_3 | w_40 W_A WS* w_4 | w_40 W_A WS* w_5 | w_40 W_A WS* w_6 | w_40 W_A WS* w_7 | w_40 w_8 | w_40 W_A WS* w_9 | w_40 W_A WS d_unit | w_40 W_A);
n_tens_50    : (w_50 w_1 | w_50 w_1_art | w_50 W_A WS* w_2 | w_50 W_A WS* w_3 | w_50 W_A WS* w_4 | w_50 W_A WS* w_5 | w_50 W_A WS* w_6 | w_50 W_A WS* w_7 | w_50 w_8 | w_50 W_A WS* w_9 | w_50 W_A WS d_unit | w_50 W_A);
n_tens_60    : (w_60 w_1 | w_60 w_1_art | w_60 W_A WS* w_2 | w_60 W_A WS* w_3 | w_60 W_A WS* w_4 | w_60 W_A WS* w_5 | w_60 W_A WS* w_6 | w_60 W_A WS* w_7 | w_60 w_8 | w_60 W_A WS* w_9 | w_60 W_A WS d_unit | w_60 W_A);
n_tens_70    : (w_70 w_1 | w_70 w_1_art | w_70 W_A WS* w_2 | w_70 W_A WS* w_3 | w_70 W_A WS* w_4 | w_70 W_A WS* w_5 | w_70 W_A WS* w_6 | w_70 W_A WS* w_7 | w_70 w_8 | w_70 W_A WS* w_9 | w_70 W_A WS d_unit | w_70 W_A);
n_tens_80    : (w_80 w_1 | w_80 w_1_art | w_80 W_A WS* w_2 | w_80 W_A WS* w_3 | w_80 W_A WS* w_4 | w_80 W_A WS* w_5 | w_80 W_A WS* w_6 | w_80 W_A WS* w_7 | w_80 w_8 | w_80 W_A WS* w_9 | w_80 W_A WS d_unit | w_80 W_A);
n_tens_90    : (w_90 w_1 | w_90 w_1_art | w_90 W_A WS* w_2 | w_90 W_A WS* w_3 | w_90 W_A WS* w_4 | w_90 W_A WS* w_5 | w_90 W_A WS* w_6 | w_90 W_A WS* w_7 | w_90 w_8 | w_90 W_A WS* w_9 | w_90 W_A WS d_unit | w_90 W_A);

w_unit    : (w_0 | w_1 | w_1_art | w_2 | w_3 | w_4 | w_5 | w_6 | w_7 | w_8 | w_9) ;
ten_pref  : (w_2 | w_3 | w_4 | w_5 | w_6 | w_7 | w_8 | w_9);
d_ten_pref: (D_2 | D_3 | D_4 | D_5 | D_6 | D_7 | D_8 | D_9);

// from 100 to 999
hundred: (ten_pref WS? | d_ten_pref WS?)? W_HUNDRED (n_tens_80 | (W_O (WS? (n_1_99 | max_2_digits))?));

// from 1000 to 99999
thousand: W_A_THOUSAND (WS? n_1_999)?
		| (thousand_pref | max_2_digits) WS? W_THOUSAND (WS? n_1_999)?;

million:
	(w_1_art | D_1) WS? w_one_million (WS? (max_6_digits_with_div | n_1_999k | max_6_digits))? # one_million
	|
	(max_6_digits_with_div | n_1_999k | max_6_digits) WS? w_million (WS? ((max_6_digits_with_div | n_1_999k | max_6_digits)))? # millions
;

million_prefix:
	(w_1_art | D_1) WS? w_one_million # one_million_prefix
	|
	(max_6_digits_with_div | n_1_999k | max_6_digits) WS? w_million # millions_prefix
;

billion:
	(w_1_art | D_1) WS? w_one_billion (WS? (max_9_digits_with_div | n_1_999m | max_9_digits))? # one_billion
	|
	(max_6_digits_with_div | n_1_999k | max_6_digits) WS? w_billion (WS? (max_9_digits_with_div | n_1_999m | max_9_digits))? # billions
;
billion_prefix:
	(w_1_art | D_1) WS? w_one_billion # one_billion_prefix
	|
	(max_6_digits_with_div | n_1_999k | max_6_digits) WS? w_billion # billions_prefix
;

trillion: 
	(w_1_art | D_1) WS? w_one_trillion (WS? (n_1_999i))? # one_trillion
	|
	(max_6_digits_with_div | n_1_999k | max_6_digits) WS? w_trillion (WS? n_1_999i)? # trillions
;                    
trillion_prefix:
	(w_1_art | D_1) WS? w_one_trillion # one_trillion_prefix
	|
	(max_6_digits_with_div | n_1_999k | max_6_digits) WS? w_trillion # trillions_prefix
;

quadrillion:
	(w_1_art | D_1) WS? w_one_quadrillion (WS? (n_1_999b))? # one_quadrillion
	|
	(max_6_digits_with_div | n_1_999k | max_6_digits) WS? w_quadrillion (WS? n_1_999b)? # quadrillions
;
quadrillion_prefix:
	(w_1_art | D_1) WS? w_one_quadrillion # one_quadrillion_prefix
	|
	(max_6_digits_with_div | n_1_999k | max_6_digits) WS? w_quadrillion # quadrillions_prefix
;
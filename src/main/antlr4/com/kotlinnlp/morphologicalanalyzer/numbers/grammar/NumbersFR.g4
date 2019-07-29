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

n_tens_20    : (w_20 WS* AND WS* w_1 | w_20 HYPEN w_2 | w_20 HYPEN w_3 | w_20 HYPEN w_4 | w_20 HYPEN w_5 | w_20 HYPEN w_6 | w_20 HYPEN w_7 | w_20 HYPEN w_8 | w_20 HYPEN w_9 | w_20 WS d_unit | w_20 );
n_tens_30    : (w_30 WS* AND WS* w_1 | w_30 HYPEN w_2 | w_30 HYPEN w_3 | w_30 HYPEN w_4 | w_30 HYPEN w_5 | w_30 HYPEN w_6 | w_30 HYPEN w_7 | w_30 HYPEN w_8 | w_30 HYPEN w_9 | w_30 WS d_unit | w_30 );
n_tens_40    : (w_40 WS* AND WS* w_1 | w_40 HYPEN w_2 | w_40 HYPEN w_3 | w_40 HYPEN w_4 | w_40 HYPEN w_5 | w_40 HYPEN w_6 | w_40 HYPEN w_7 | w_40 HYPEN w_8 | w_40 HYPEN w_9 | w_40 WS d_unit | w_40 );
n_tens_50    : (w_50 WS* AND WS* w_1 | w_50 HYPEN w_2 | w_50 HYPEN w_3 | w_50 HYPEN w_4 | w_50 HYPEN w_5 | w_50 HYPEN w_6 | w_50 HYPEN w_7 | w_50 HYPEN w_8 | w_50 HYPEN w_9 | w_50 WS d_unit | w_50 );
n_tens_60    : (w_60 WS* AND WS* w_1 | w_60 HYPEN w_2 | w_60 HYPEN w_3 | w_60 HYPEN w_4 | w_60 HYPEN w_5 | w_60 HYPEN w_6 | w_60 HYPEN w_7 | w_60 HYPEN w_8 | w_60 HYPEN w_9 | w_60 WS d_unit | w_60 );
n_tens_70    : (w_70 w_1 | w_70 HYPEN w_2 | w_70 HYPEN w_3 | w_70 HYPEN w_4 | w_70 HYPEN w_5 | w_70 HYPEN w_6 | w_70 HYPEN w_7 | w_70 w_8 | w_70 HYPEN w_9 | w_70 WS d_unit | w_70 );
n_tens_80    : (w_80 w_1 | w_80 HYPEN w_2 | w_80 HYPEN w_3 | w_80 HYPEN w_4 | w_80 HYPEN w_5 | w_80 HYPEN w_6 | w_80 HYPEN w_7 | w_80 w_8 | w_80 HYPEN w_9 | w_80 WS d_unit | w_80 );
n_tens_90    : (w_90 w_1 | w_90 HYPEN w_2 | w_90 HYPEN w_3 | w_90 HYPEN w_4 | w_90 HYPEN w_5 | w_90 HYPEN w_6 | w_90 HYPEN w_7 | w_90 w_8 | w_90 HYPEN w_9 | w_90 WS d_unit | w_90 );

w_unit   : (w_0 | w_1 | w_2 | w_3 | w_4 | w_5 | w_6 | w_7 | w_8 | w_9) ;
ten_pref  : (w_2 | w_3 | w_4 | w_5 | w_6 | w_7 | w_8 | w_9);
d_ten_pref: (D_2 | D_3 | D_4 | D_5 | D_6 | D_7 | D_8 | D_9);

// from 100 to 999
hundred: (ten_pref WS? | d_ten_pref WS?)? W_HUNDRED (n_tens_80 | ((WS? (n_1_99 | max_2_digits))?));

// from 1000 to 99999
thousand: W_THOUSAND (WS? n_1_999)?
		| (thousand_pref | max_2_digits) WS? W_THOUSAND (WS? n_1_999)?;

million:
	(w_1 | D_1) WS? w_one_million (WS? (max_6_digits_with_div | n_1_999k | max_6_digits))? # one_million
	|
	(max_6_digits_with_div | n_1_999k | max_6_digits) WS? w_million (WS? ((max_6_digits_with_div | n_1_999k | max_6_digits)))? # millions
;

million_prefix:
	(w_1 | D_1) WS? w_one_million # one_million_prefix
	|
	(max_6_digits_with_div | n_1_999k | max_6_digits) WS? w_million # millions_prefix
;

billion:
	(w_1 | D_1) WS? w_one_billion (WS? (max_9_digits_with_div | n_1_999m | max_9_digits))? # one_billion
	|
	(max_6_digits_with_div | n_1_999k | max_6_digits) WS? w_billion (WS? (max_9_digits_with_div | n_1_999m | max_9_digits))? # billions
;
billion_prefix:
	(w_1 | D_1) WS? w_one_billion # one_billion_prefix
	|
	(max_6_digits_with_div | n_1_999k | max_6_digits) WS? w_billion # billions_prefix
;

trillion:
	(w_1 | D_1) WS? w_one_trillion WS? n_1_999i? # one_trillion
	|
	(max_6_digits_with_div | n_1_999k | max_6_digits) WS? w_trillion (WS? n_1_999i)? # trillions
;
trillion_prefix:
	(w_1 | D_1) WS? w_one_trillion # one_trillion_prefix
	|
	(max_6_digits_with_div | n_1_999k | max_6_digits) WS? w_trillion # trillions_prefix
;

quadrillion:
	(w_1 | D_1) WS? w_one_quadrillion WS? n_1_999b? # one_quadrillion
	|
	(max_6_digits_with_div | n_1_999k | max_6_digits) WS? w_quadrillion (WS? n_1_999b)? # quadrillions
;
quadrillion_prefix:
	(w_1 | D_1) WS? w_one_quadrillion # one_quadrillion_prefix
	|
	(max_6_digits_with_div | n_1_999k | max_6_digits) WS? w_quadrillion # quadrillions_prefix
;

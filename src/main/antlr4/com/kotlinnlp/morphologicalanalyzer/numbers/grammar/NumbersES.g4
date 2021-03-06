grammar NumbersES;

import NUMLexerES, NUMParser;

d_thdiv : DOT;
d_decdiv : COMMA;

w_20_pref: W_20_PREF;
w_1_acc: W_1_ACC;
w_2_acc: W_2_ACC;
w_3_acc: W_3_ACC;
w_6_acc: W_6_ACC;

w_hundred_100: W_HUNDRED_100;
w_hundred_200: W_HUNDRED_200;
w_hundred_300: W_HUNDRED_300;
w_hundred_400: W_HUNDRED_400;
w_hundred_500: W_HUNDRED_500;
w_hundred_600: W_HUNDRED_600;
w_hundred_700: W_HUNDRED_700;
w_hundred_800: W_HUNDRED_800;
w_hundred_900: W_HUNDRED_900;

w_one_million: W_ONE_MILLION;
w_million: W_MILLION;
w_one_trillion: W_ONE_TRILLION;
w_trillion: W_TRILLION;

n_tens_20    : (w_20_pref w_1 | w_20_pref w_1_art | w_20_pref w_1_acc | w_20_pref w_2_acc | w_20_pref w_3_acc | w_20_pref w_4 | w_20_pref w_5 | w_20_pref w_6 | w_20_pref w_6_acc | w_20_pref w_7 | w_20_pref w_8 | w_20_pref w_9 | w_20_pref WS+ d_unit | w_20);
n_tens_30    : (w_30 WS+ AND WS+ w_1 | w_30 WS+ AND WS+ w_1_art | w_30 WS+ AND WS+ w_2 | w_30 WS+ AND WS+ w_3 | w_30 WS+ AND WS+ w_4 | w_30 WS+ AND WS+ w_5 | w_30 WS+ AND WS+ w_6 | w_30 WS+ AND WS+ w_7 | w_30 WS+ AND WS+ w_8 | w_30 WS+ AND WS+ w_9 | w_30 WS+ AND WS+ d_unit | w_30);
n_tens_40    : (w_40 WS+ AND WS+ w_1 | w_40 WS+ AND WS+ w_1_art | w_40 WS+ AND WS+ w_2 | w_40 WS+ AND WS+ w_3 | w_40 WS+ AND WS+ w_4 | w_40 WS+ AND WS+ w_5 | w_40 WS+ AND WS+ w_6 | w_40 WS+ AND WS+ w_7 | w_40 WS+ AND WS+ w_8 | w_40 WS+ AND WS+ w_9 | w_40 WS+ AND WS+ d_unit | w_40);
n_tens_50    : (w_50 WS+ AND WS+ w_1 | w_50 WS+ AND WS+ w_1_art | w_50 WS+ AND WS+ w_2 | w_50 WS+ AND WS+ w_3 | w_50 WS+ AND WS+ w_4 | w_50 WS+ AND WS+ w_5 | w_50 WS+ AND WS+ w_6 | w_50 WS+ AND WS+ w_7 | w_50 WS+ AND WS+ w_8 | w_50 WS+ AND WS+ w_9 | w_50 WS+ AND WS+ d_unit | w_50);
n_tens_60    : (w_60 WS+ AND WS+ w_1 | w_60 WS+ AND WS+ w_1_art | w_60 WS+ AND WS+ w_2 | w_60 WS+ AND WS+ w_3 | w_60 WS+ AND WS+ w_4 | w_60 WS+ AND WS+ w_5 | w_60 WS+ AND WS+ w_6 | w_60 WS+ AND WS+ w_7 | w_60 WS+ AND WS+ w_8 | w_60 WS+ AND WS+ w_9 | w_60 WS+ AND WS+ d_unit | w_60);
n_tens_70    : (w_70 WS+ AND WS+ w_1 | w_70 WS+ AND WS+ w_1_art | w_70 WS+ AND WS+ w_2 | w_70 WS+ AND WS+ w_3 | w_70 WS+ AND WS+ w_4 | w_70 WS+ AND WS+ w_5 | w_70 WS+ AND WS+ w_6 | w_70 WS+ AND WS+ w_7 | w_70 WS+ AND WS+ w_8 | w_70 WS+ AND WS+ w_9 | w_70 WS+ AND WS+ d_unit | w_70);
n_tens_80    : (w_80 WS+ AND WS+ w_1 | w_80 WS+ AND WS+ w_1_art | w_80 WS+ AND WS+ w_2 | w_80 WS+ AND WS+ w_3 | w_80 WS+ AND WS+ w_4 | w_80 WS+ AND WS+ w_5 | w_80 WS+ AND WS+ w_6 | w_80 WS+ AND WS+ w_7 | w_80 WS+ AND WS+ w_8 | w_80 WS+ AND WS+ w_9 | w_80 WS+ AND WS+ d_unit | w_80);
n_tens_90    : (w_90 WS+ AND WS+ w_1 | w_90 WS+ AND WS+ w_1_art | w_90 WS+ AND WS+ w_2 | w_90 WS+ AND WS+ w_3 | w_90 WS+ AND WS+ w_4 | w_90 WS+ AND WS+ w_5 | w_90 WS+ AND WS+ w_6 | w_90 WS+ AND WS+ w_7 | w_90 WS+ AND WS+ w_8 | w_90 WS+ AND WS+ w_9 | w_90 WS+ AND WS+ d_unit | w_90);

w_unit    : (w_0 | w_1 | w_1_art | w_2 | w_3 | w_4 | w_5 | w_6 | w_7 | w_8 | w_9);
ten_pref  : (w_2 | w_3 | w_4 | w_5 | w_6 | w_7 | w_8 | w_9);
d_ten_pref: (D_2 | D_3 | D_4 | D_5 | D_6 | D_7 | D_8 | D_9);

// from 100 to 999
hundred: (W_HUNDRED | (ten_pref WS?| d_ten_pref WS?)? w_hundred_100 | w_hundred_200 | w_hundred_300 | w_hundred_400 | w_hundred_500 | w_hundred_600 | w_hundred_700 | w_hundred_800 | w_hundred_900) (WS? (n_1_99 | max_2_digits))?;

// from 1000 to 99999
thousand: W_THOUSAND (WS? n_1_999)?
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
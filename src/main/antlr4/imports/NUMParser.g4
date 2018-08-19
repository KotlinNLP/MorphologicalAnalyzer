grammar NUMParser;

import NUMLexerEN;

d_thdiv : COMMA;
d_decdiv : DOT;

w_1_art: W_1_ART;
w_0: W_0;
w_1: W_1;
w_2: W_2;
w_3: W_3;
w_4: W_4;
w_5: W_5;
w_6: W_6;
w_7: W_7;
w_8: W_8;
w_9: W_9;
w_10: W_10;
w_11: W_11;
w_12: W_12;
w_13: W_13;
w_14: W_14;
w_15: W_15;
w_16: W_16;
w_17: W_17;
w_18: W_18;
w_19: W_19;
w_20: W_20;
w_30: W_30;
w_40: W_40;
w_50: W_50;
w_60: W_60;
w_70: W_70;
w_80: W_80;
w_90: W_90;
w_one_million: W_MILLION;
w_million: W_MILLION;
w_one_billion: W_BILLION;
w_billion: W_BILLION;
w_one_trillion: W_TRILLION;
w_trillion: W_TRILLION;
w_one_quadrillion: W_QUADRILLION;
w_quadrillion: W_QUADRILLION;

n_tens_10 : (w_10 | w_11 | w_12 | w_13 | w_14 | w_15 | w_16 | w_17 | w_18 | w_19);
n_tens_20 : (w_20 WS* w_1 | w_20 WS* w_2 | w_20 WS* w_3 | w_20 WS* w_4 | w_20 WS* w_5 | w_20 WS* w_6 | w_20 WS* w_7 | w_20 WS* w_8 | w_20 WS* w_9 | w_20 WS d_unit | w_20);
n_tens_30 : (w_30 WS* w_1 | w_30 WS* w_2 | w_30 WS* w_3 | w_30 WS* w_4 | w_30 WS* w_5 | w_30 WS* w_6 | w_30 WS* w_7 | w_30 w_8 | w_30 WS* w_9 | w_30 WS d_unit | w_30);
n_tens_40 : (w_40 WS* w_1 | w_40 WS* w_2 | w_40 WS* w_3 | w_40 WS* w_4 | w_40 WS* w_5 | w_40 WS* w_6 | w_40 WS* w_7 | w_40 w_8 | w_40 WS* w_9 | w_40 WS d_unit | w_40);
n_tens_50 : (w_50 WS*  w_1 | w_50 WS* w_2 | w_50 WS* w_3 | w_50 WS* w_4 | w_50 WS* w_5 | w_50 WS* w_6 | w_50 WS* w_7 | w_50 w_8 | w_50 WS* w_9 | w_50 WS d_unit | w_50);
n_tens_60 : (w_60 WS* w_1 | w_60 WS* w_2 | w_60 WS* w_3 | w_60 WS* w_4 | w_60 WS* w_5 | w_60 WS* w_6 | w_60 WS* w_7 | w_60 w_8 | w_60 WS* w_9 | w_60 WS d_unit | w_60);
n_tens_70 : (w_70 WS* w_1 | w_70 WS* w_2 | w_70 WS* w_3 | w_70 WS* w_4 | w_70 WS* w_5 | w_70 WS* w_6 | w_70 WS* w_7 | w_70 w_8 | w_70 WS* w_9 | w_70 WS d_unit | w_70);
n_tens_80 : (w_80 WS* w_1 | w_80 WS* w_2 | w_80 WS* w_3 | w_80 WS* w_4 | w_80 WS* w_5 | w_80 WS* w_6 | w_80 WS* w_7 | w_80 w_8 | w_80 WS* w_9 | w_80 WS d_unit | w_80);
n_tens_90 : (w_90 WS* w_1 | w_90 WS* w_2 | w_90 WS* w_3 | w_90 WS* w_4 | w_90 WS* w_5 | w_90 WS* w_6 | w_90 WS* w_7 | w_90 w_8 | w_90 WS* w_9 | w_90 WS d_unit | w_90);

d_unit: (D_0 | D_1 | D_2 | D_3 | D_4 | D_5 | D_6 | D_7 | D_8 | D_9) ;
w_unit: (w_0 | w_1 | w_2 | w_3 | w_4 | w_5 | w_6 | w_7 | w_8 | w_9) ;
d_frag: d_unit d_unit d_unit ;

extra_word_dividers: (d_decdiv | d_thdiv)+;

seq_of_w_unit: w_unit (WS w_unit)+ ;
integ : d_unit+ ( d_thdiv d_frag )+ | num;
dec   : integ d_decdiv (seq_of_w_unit | num);
num   : d_unit+ ;

root: not_word? text? not_word? EOF;

text: number_or_string (not_word number_or_string)*;

not_word: (WORDDIV | extra_word_dividers | WS | EOL)+;

number_or_string: number | string;

string: ~(WS | EOL | WORDDIV)+;

integ_list: integ WS? COMMA WS? integ (WS? COMMA WS? integ)+;

number: word_number ( (WS W_WORDDECDIV WS) (seq_of_w_unit | word_number | digit_number) )? | digit_number ( (WS W_WORDDECDIV WS) (seq_of_w_unit | word_number | digit_number) ) ?;

digit_number: dec | integ;

word_number: w_0 | of_rules | base_number;

base_number: quadrillion | trillion | billion | million | hundredthousand | thousand | hundred | n_1_99;

ten_pref  : (w_1 | w_2 | w_3 | w_4 | w_5 | w_6 | w_7 | w_8 | w_9);
d_ten_pref: (D_1 | D_2 | D_3 | D_4 | D_5 | D_6 | D_7 | D_8 | D_9);

x_3_digits: d_unit d_unit d_unit;
max_2_digits: d_unit d_unit | d_unit;
max_3_digits: d_unit d_unit d_unit | d_unit d_unit | d_unit;
max_6_digits: max_3_digits (d_thdiv x_3_digits) | max_3_digits max_3_digits?;
max_6_digits_with_div: max_3_digits (d_thdiv x_3_digits);
max_9_digits: max_3_digits d_thdiv x_3_digits (d_thdiv x_3_digits)? | max_3_digits max_3_digits? max_3_digits?;
max_9_digits_with_div: max_3_digits d_thdiv x_3_digits (d_thdiv x_3_digits)?;

// from 10 to 99
n_10_99   : (n_tens_10 | n_tens_20 | n_tens_30 | n_tens_40 | n_tens_50 | n_tens_60 | n_tens_70 | n_tens_80 | n_tens_90);
// from 1 to 99
n_1_99    : (n_10_99 | w_unit);

// from 100 to 999
hundred: (ten_pref WS? | d_ten_pref WS?) W_HUNDRED (WS? (n_1_99 | max_2_digits))?;

// from 1 to 999
n_1_999: hundred | n_1_99 | max_3_digits;

thousand_pref: ten_pref | n_10_99;

// from 1000 to 99999
thousand: (thousand_pref | max_2_digits) WS? W_THOUSAND (WS? n_1_999)?;

// from 1 to 99999
n_1_99999:  thousand | n_1_999;

// from 100k to 999k999
hundredthousand: 
	(hundred | max_3_digits) WS? W_THOUSAND (WS? n_1_999)? ;

// from 1 to 999k999
n_1_999k: hundredthousand | n_1_99999;

// MILLIONS (m)
// from 1m to 999m
million:
	(w_1 | w_1_art | D_1) WS? w_one_million (WS? (max_6_digits_with_div | n_1_999k | max_6_digits))? # one_million
	|
	(max_6_digits_with_div | n_1_999k | max_6_digits) WS? w_million (WS? ((max_6_digits_with_div | n_1_999k | max_6_digits)))? # millions
;
million_prefix:
	(w_1 | w_1_art | D_1) WS? w_one_million # one_million_prefix
	|
	(max_6_digits_with_div | n_1_999k | max_6_digits) WS? w_million # millions_prefix
;

// from 1 to 999m999k999
n_1_999m: million | n_1_999k;

// BILLION (i)
// from 1i to (999m999k999)i 999m999k999
billion:
	(w_1 | w_1_art | D_1) WS? w_one_billion (WS? (max_9_digits_with_div | n_1_999m | max_9_digits))? # one_billion
	|
	(max_6_digits_with_div | n_1_999k | max_6_digits) WS? w_billion (WS? (max_9_digits_with_div | n_1_999m | max_9_digits))? # billions
;
billion_prefix:
	(w_1 | w_1_art | D_1) WS? w_one_billion # one_billion_prefix
	|
	(max_6_digits_with_div | n_1_999k | max_6_digits) WS? w_billion # billions_prefix
;

// from 1 to (999m999k999)i999m999k999
n_1_999i: billion | n_1_999m;

// TRILLION (b)
// from 1b to (999i999m999k999)b 999i999m999k999
trillion:
	(w_1 | w_1_art | D_1) WS? w_one_trillion WS? n_1_999i? # one_trillion
	|
	(max_6_digits_with_div | n_1_999k | max_6_digits) WS? w_trillion (WS? n_1_999i)? # trillions
;
trillion_prefix:
	(w_1 | w_1_art | D_1) WS? w_one_trillion # one_trillion_prefix
	|
	(max_6_digits_with_div | n_1_999k | max_6_digits) WS? w_trillion # trillions_prefix
;

// from 1 to (999m999k999)i999m999k999
n_1_999b: trillion | n_1_999i;

// QUADRILLION (B)
// from 1B to (999b999i999m999k999)B 999b999i999m999k999
quadrillion:
	(w_1 | w_1_art | D_1) WS? w_one_quadrillion WS? n_1_999b? # one_quadrillion
	|
	(max_6_digits_with_div | n_1_999k | max_6_digits) WS? w_quadrillion (WS? n_1_999b)? # quadrillions
;
quadrillion_prefix:
	(w_1 | w_1_art | D_1) WS? w_one_quadrillion # one_quadrillion_prefix
	|
	(max_6_digits_with_div | n_1_999k | max_6_digits) WS? w_quadrillion # quadrillions_prefix
;

// from 1 to (999b999i999m999k999)B999m999k999
n_1_999t: quadrillion | n_1_999b;

// OF rules (millions of trillions, hundred of millions of trillions, ...)
prefix_of_rules: million_prefix | billion_prefix | trillion_prefix | quadrillion_prefix;

of_rules_body: (WS? W_OF WS? (w_million | w_billion | w_trillion | w_quadrillion))+;

of_rules: prefix_of_rules of_rules_body (WS? base_number)?;

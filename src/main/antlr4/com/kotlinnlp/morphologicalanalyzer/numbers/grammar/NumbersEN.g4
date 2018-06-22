grammar NumbersEN;

import NUMLexerEN, NUMParser;

tens_sep: (WS* (HYPEN WS*)?)?;

w_1_art: W_1_ART;

n_tens_20    : (w_20 tens_sep w_1 | w_20 tens_sep w_2 | w_20 tens_sep w_3 | w_20 tens_sep w_4 | w_20 tens_sep w_5 | w_20 tens_sep w_6 | w_20 tens_sep w_7 | w_20 tens_sep w_8 | w_20 tens_sep w_9 | w_20 WS d_unit | w_20);
n_tens_30    : (w_30 tens_sep w_1 | w_30 w_1_art | w_30 tens_sep w_2 | w_30 tens_sep w_3 | w_30 tens_sep w_4 | w_30 tens_sep w_5 | w_30 tens_sep w_6 | w_30 tens_sep w_7 | w_30 tens_sep w_8 | w_30 tens_sep w_9 | w_30 WS d_unit | w_30);
n_tens_40    : (w_40 tens_sep w_1 | w_40 tens_sep w_2 | w_40 tens_sep w_3 | w_40 tens_sep w_4 | w_40 tens_sep w_5 | w_40 tens_sep w_6 | w_40 tens_sep w_7 | w_40 tens_sep w_8 | w_40 tens_sep w_9 | w_40 WS d_unit | w_40);
n_tens_50    : (w_50 tens_sep w_1 | w_50 tens_sep w_2 | w_50 tens_sep w_3 | w_50 tens_sep w_4 | w_50 tens_sep w_5 | w_50 tens_sep w_6 | w_50 tens_sep w_7 | w_50 tens_sep w_8 | w_50 tens_sep w_9 | w_50 WS d_unit | w_50);
n_tens_60    : (w_60 tens_sep w_1 | w_60 tens_sep w_2 | w_60 tens_sep w_3 | w_60 tens_sep w_4 | w_60 tens_sep w_5 | w_60 tens_sep w_6 | w_60 tens_sep w_7 | w_60 tens_sep w_8 | w_60 tens_sep w_9 | w_60 WS d_unit | w_60);
n_tens_70    : (w_70 tens_sep w_1 | w_70 tens_sep w_2 | w_70 tens_sep w_3 | w_70 tens_sep w_4 | w_70 tens_sep w_5 | w_70 tens_sep w_6 | w_70 tens_sep w_7 | w_70 tens_sep w_8 | w_70 tens_sep w_9 | w_70 WS d_unit | w_70);
n_tens_80    : (w_80 tens_sep w_1 | w_80 tens_sep w_2 | w_80 tens_sep w_3 | w_80 tens_sep w_4 | w_80 tens_sep w_5 | w_80 tens_sep w_6 | w_80 tens_sep w_7 | w_80 tens_sep w_8 | w_80 tens_sep w_9 | w_80 WS d_unit | w_80);
n_tens_90    : (w_90 tens_sep w_1 | w_90 tens_sep w_2 | w_90 tens_sep w_3 | w_90 tens_sep w_4 | w_90 tens_sep w_5 | w_90 tens_sep w_6 | w_90 tens_sep w_7 | w_90 tens_sep w_8 | w_90 tens_sep w_9 | w_90 WS d_unit | w_90);

ten_pref  : (w_1_art | w_1 | w_2 | w_3 | w_4 | w_5 | w_6 | w_7 | w_8 | w_9);

// from 100 to 999
hundred: (ten_pref WS? | d_ten_pref WS?) W_HUNDRED (((WS? AND WS?) | WS)? (n_1_99 | max_2_digits))?;

// from 1000 to 99999
thousand: (thousand_pref | max_2_digits) WS? W_THOUSAND (((WS? AND WS?) | WS)? n_1_999)?;
grammar NumbersParser;

import LexerEN, NumbersLexer;

// -----
// -- Digits numbers
// -----

digit    : N_0 | N_1 | N_2 | N_3 | N_4 | N_5 | N_6 | N_7 | N_8 | N_9 ;
n_0_12   : n_00_12 | digit ;
n_0_24   : n_0_12 | N_13_19 | N_20 | N_21 | N_22 | N_23 | N_24 ;
n_0_31   : n_0_24 | N_25_29 | N_30 | N_31 ;
n_0_59   : n_0_31 | N_32_39 | N_40_59 ;
n_0_99   : n_0_59 | N_60_99 ;
n_0_999  : n_0_99 | N_100_999 ;
n_0_9999 : n_0_99 | n_100_9999 ;

n_00_12 : N_00 | N_01 | N_02 | N_03 | N_04_09 | N_10_12 ;
n_00_24 : n_00_12 | N_13_19 | N_20 | N_21 | N_22 | N_23 | N_24 ;
n_00_31 : n_00_24 | N_25_29 | N_30 | N_31 ;
n_00_59 : n_00_31 | N_32_39 | N_40_59 ;
n_00_99 : n_00_59 | N_60_99 ;

n_000_999 : N_000_099 | N_100_999 ;

n_100_9999 : N_100_999 | N_1000_1899 | N_1900_2099 | N_2100_9999 ;

// -----
// -- String numbers (ordinal and cardinal)
// -----

ns_1  : NS_1  | NS_ORD_1 ;
ns_2  : NS_2  | NS_ORD_2 ;
ns_3  : NS_3  | NS_ORD_3 ;
ns_4  : NS_4  | NS_ORD_4 ;
ns_5  : NS_5  | NS_ORD_5 ;
ns_6  : NS_6  | NS_ORD_6 ;
ns_7  : NS_7  | NS_ORD_7 ;
ns_8  : NS_8  | NS_ORD_8 ;
ns_9  : NS_9  | NS_ORD_9 ;
ns_10 : NS_10 | NS_ORD_10 ;
ns_11 : NS_11 | NS_ORD_11 ;
ns_12 : NS_12 | NS_ORD_12 ;
ns_13 : NS_13 | NS_ORD_13 ;
ns_14 : NS_14 | NS_ORD_14 ;
ns_15 : NS_15 | NS_ORD_15 ;
ns_16 : NS_16 | NS_ORD_16 ;
ns_17 : NS_17 | NS_ORD_17 ;
ns_18 : NS_18 | NS_ORD_18 ;
ns_19 : NS_19 | NS_ORD_19 ;
ns_20 : NS_20 | NS_ORD_20 ;
ns_21 : NS_21 | NS_ORD_21 ;
ns_22 : NS_22 | NS_ORD_22 ;
ns_23 : NS_23 | NS_ORD_23 ;
ns_24 : NS_24 | NS_ORD_24 ;
ns_25 : NS_25 | NS_ORD_25 ;
ns_26 : NS_26 | NS_ORD_26 ;
ns_27 : NS_27 | NS_ORD_27 ;
ns_28 : NS_28 | NS_ORD_28 ;
ns_29 : NS_29 | NS_ORD_29 ;
ns_30 : NS_30 | NS_ORD_30 ;
ns_31 : NS_31 | NS_ORD_31 ;

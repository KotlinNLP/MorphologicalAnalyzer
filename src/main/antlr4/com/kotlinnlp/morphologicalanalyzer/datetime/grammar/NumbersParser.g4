grammar NumbersParser;

import NumbersLexer;

// -----
// -- Parsed numbers
// -----

d_0_12 : D_00_09 | D_10_12 | DIGIT ;
d_0_24 : d_0_12 | D_13_19 | D_20_24 ;
d_0_31 : d_0_24 | D_25_29 | D_30_31 ;
d_0_59 : d_0_31 | D_32_39 | D_40_59 ;
d_0_99 : d_0_59 | D_60_99 ;

d_0_9999 : d_0_99 | D_100_9999 ;

grammar Numbers;

import Symbols;

// -----
// -- Numbers with 1 digit
// -----

DIGIT : [0-9] ;

// -----
// -- Numbers with 2 digits
// -----

D_00_09 : '0' DIGIT ;
D_10_12 : '1' [12] ;
D_13_19 : '1' [3-9] ;
D_20_24 : '2' [0-4] ;
D_25_29 : '2' [5-9] ;
D_30_31 : '3' [01] ;
D_32_39 : '3' [2-9] ;
D_40_59 : [4-5] DIGIT ;
D_60_99 : [6-9] DIGIT ;

// -----
// -- Numbers with more then 2 digits
// -----

D_100_9999 : [1-9] DIGIT DIGIT DIGIT? ;

// -----
// -- Parsed numbers
// -----

d_0_12 : D_00_09 | D_10_12 | DIGIT ;
d_0_24 : d_0_12 | D_13_19 | D_20_24 ;
d_0_31 : d_0_24 | D_25_29 | D_30_31 ;
d_0_59 : d_0_31 | D_32_39 | D_40_59 ;
d_0_99 : d_0_59 | D_60_99 ;

d_0_9999 : d_0_99 | D_100_9999 ;

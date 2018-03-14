grammar Times;

import Symbols, Numbers;

time : hour time_sep min ;

time_sep : SPACE_SEP | COLON | SPACE_SEP AND SPACE_SEP ;

hour : d_0_24 ;
min  : d_0_59 ;

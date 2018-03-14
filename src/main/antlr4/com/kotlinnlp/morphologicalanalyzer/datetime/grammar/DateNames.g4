lexer grammar DateNames;

import Symbols;

MON : [Mm]'onday' ;
TUE : [Tt]'uesday'  ;
WED : [Ww]'ednesday' ;
THU : [Tt]'hursday' ;
FRI : [Ff]'riday' ;
SAT : [Ss]'aturday' ;
SUN : [Ss]'unday' ;

MON_ABBR : [Mm]'on' DOT? ;
TUE_ABBR : [Tt]'ue' DOT?  ;
WED_ABBR : [Ww]'ed' DOT? ;
THU_ABBR : [Tt]'hu' DOT? ;
FRI_ABBR : [Ff]'ri' DOT? ;
SAT_ABBR : [Ss]'at' DOT? ;
SUN_ABBR : [Ss]'un' DOT? ;

JAN : [Jj]'anuary' ;
FEB : [Ff]'ebruary' ;
MAR : [Mm]'arch' ;
APR : [Aa]'pril' ;
MAY : [Mm]'ay' ;
JUN : [Jj]'une' ;
JUL : [Jj]'uly' ;
AUG : [Aa]'ugust' ;
SEP : [Ss]'eptember' ;
OCT : [Oo]'ctober' ;
NOV : [Nn]'ovember' ;
DEC : [Dd]'ecember' ;

JAN_ABBR : [Jj]'an' DOT? ;
FEB_ABBR : [Ff]'eb' DOT? ;
MAR_ABBR : [Mm]'ar' DOT? ;
APR_ABBR : [Aa]'pr' DOT? ;
MAY_ABBR : [Mm]'ay' ;
JUN_ABBR : [Jj]'un' DOT? ;
JUL_ABBR : [Jj]'ul' DOT? ;
AUG_ABBR : [Aa]'ug' DOT? ;
SEP_ABBR : [Ss]'ep' 't'? DOT? ;
OCT_ABBR : [Oo]'ct' DOT? ;
NOV_ABBR : [Nn]'ov' DOT? ;
DEC_ABBR : [Dd]'ec' DOT? ;

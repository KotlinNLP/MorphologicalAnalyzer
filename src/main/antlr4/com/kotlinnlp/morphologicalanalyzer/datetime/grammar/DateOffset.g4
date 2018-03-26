grammar DateOffset;

import LexerEN, Date, Offset;

date_offset
    : date_offset_date_ref WS (OF WS)? offset
    | date_offset_date_ref WS FROM WS offset
    | offset WS? EN_POSSESSIVE WS date_offset_date_ref
    ;

date_offset_date_ref : date ;

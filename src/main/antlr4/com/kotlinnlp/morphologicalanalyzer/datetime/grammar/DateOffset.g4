grammar DateOffset;

import LexerEN, Date, Offset;

date_offset
    : date_offset_date_ref WS (OF WS)? offset
    ;

date_offset_date_ref : date ;

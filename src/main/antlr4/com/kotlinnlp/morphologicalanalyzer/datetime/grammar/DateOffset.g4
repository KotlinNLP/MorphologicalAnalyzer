grammar DateOffset;

import LexerEN, Date, Offset;

date_offset
    : date_offset_date_ref WS (OF WS)? offset
    | date_offset_date_ref WS FROM WS offset
    | offset WS? EN_POSSESSIVE WS date_offset_date_ref
    | offset WS date_offset_time_ref // e.g. "tomorrow evening"
    | date_offset_tonight
    | date_offset_time
    ;

date_offset_date_ref : date ;
date_offset_time_ref : ((IN | AT) WS)? generic_time ; // e.g. "morning", "evening"

date_offset_tonight : TONIGHT ;

date_offset_time
    : offset_single_prefix WS date_offset_time_ref
    | offset_pos_prefix WS offset_units WS date_offset_time_ref
    | date_offset_time_ref WS offset_double_suffix
    | offset_units WS date_offset_time_ref WS (offset_pos_suffix | offset_neg_suffix)
    ;
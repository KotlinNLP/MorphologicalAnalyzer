grammar DateOrdinal;

import LexerEN, NumbersParser, Date, Offset;

// E.g. "The second week of Sep. 2015", "The last week of August", "The first day of the next year"
date_ordinal : ordinal_prefix WS ordinal_unit WS (OF | IN) WS ordinal_ref ;

// -----
// -- Prefix
// -----

ordinal_prefix : LAST | ordinal_prefix_number ;

ordinal_prefix_number
    : ns_1  | ns_2  | ns_3  | ns_4  | ns_5  | ns_6  | ns_7  | ns_8  | ns_9  | ns_10
    | ns_11 | ns_12 | ns_13 | ns_14 | ns_15 | ns_16 | ns_17 | ns_18 | ns_19 | ns_20
    | ns_21 | ns_22 | ns_23 | ns_24 | ns_25 | ns_26 | ns_27 | ns_28 | ns_29 | ns_30
    ;

// -----
// -- Date unit
// -----

ordinal_unit : ordinal_date_unit | date_unit_literal ;
ordinal_date_unit         : date ;
ordinal_date_unit_literal : date_unit_literal ;

// -----
// -- Ref date-time
// -----

ordinal_ref : ordinal_date_ref | ordinal_offset_ref ;
ordinal_date_ref   : date ;
ordinal_offset_ref : offset ;

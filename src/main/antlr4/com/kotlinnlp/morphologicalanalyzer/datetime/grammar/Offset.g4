grammar Offset;

import LexerEN, Date, Time;

offset
    : offset_single_prefix WS offset_ref
    | offset_pos_prefix WS offset_numerable
    | offset_ref WS offset_double_suffix
    | offset_numerable WS (offset_pos_suffix | offset_neg_suffix)
    | special_offset
    ;

special_offset : now | today | yesterday | tomorrow | day_after_tomorrow | day_before_yesterday ;

now
    : NOW
    | HOUR // only for IT
    ;

today                : TODAY ;
yesterday            : YESTERDAY ;
tomorrow             : TOMORROW ;
day_after_tomorrow   : DAY_AFTER_TOMORROW ;
day_before_yesterday : DAY_BEFORE_YESTERDAY ;

offset_numerable : offset_units WS offset_ref ;

// -----
// -- Offset Reference
// -----

offset_ref : offset_date_ref | date_time_literal ;

offset_date_ref : date ;
date_time_literal : date_unit_literal | time_unit_literal ;

// -----
// -- Prefix
// -----

offset_single_prefix : offset_single_pos_prefix | offset_single_neg_prefix | offset_single_zero_prefix ;
offset_single_zero_prefix : THIS ; // a prefix that means implicitly an offset of 0 units (+0)
offset_single_pos_prefix  : NEXT ; // a prefix that means implicitly a positive offset of 1 unit (+1)
offset_single_neg_prefix  : LAST | PREV | PAST ; // a prefix that means implicitly a negative offset of 1 unit (-1)

// -----
// -- Suffix
// -----

offset_double_suffix : offset_double_pos_suffix | offset_double_neg_suffix ;
offset_double_pos_suffix : AFTER WS NEXT ; // a prefix that means implicitly a positive offset of 2 units (+2)
offset_double_neg_suffix : BEFORE WS LAST ; // a prefix that means implicitly a negative offset of 2 units (-2)

offset_pos_suffix : HENCE ;
offset_neg_suffix : AGO ;

// -----
// -- Units
// -----

offset_units : offset_units_digits | offset_units_str ;
offset_units_digits : n_0_9999 ;
offset_units_str : ns_all ;

// -----
// -- Prefix
// -----

offset_pos_prefix : IN ;

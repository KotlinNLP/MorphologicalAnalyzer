grammar Offset;

import LexerEN, Date, Time;

offset
    : offset_single_prefix SPACE_SEP offset_ref
    | offset_prefix SPACE_SEP offset_units SPACE_SEP offset_ref
    | offset_ref SPACE_SEP offset_double_pos_suffix
    ;

// -----
// -- Offset Reference
// -----

offset_ref : offset_date_ref | date_time_literal ;

offset_date_ref : date ;
date_time_literal : date_unit_literal | time_unit_literal ;

// -----
// -- Unit Prefix
// -----

offset_single_prefix : offset_single_pos_prefix | offset_single_neg_prefix | offset_single_zero_prefix ;
offset_single_zero_prefix : THIS ; // a prefix that means implicitly an offset of 0 units (+0)
offset_single_pos_prefix  : NEXT | THIS ; // a prefix that means implicitly a positive offset of 1 unit (+1)
offset_single_neg_prefix  : LAST | PREV ; // a prefix that means implicitly a negative offset of 1 unit (-1)

// -----
// -- Dobule Prefix
// -----

offset_double_suffix : offset_double_pos_suffix | offset_double_neg_suffix ;
offset_double_pos_suffix : AFTER SPACE_SEP NEXT ; // a prefix that means implicitly a positive offset of 2 units (+2)
offset_double_neg_suffix : BEFORE SPACE_SEP LAST ; // a prefix that means implicitly a negative offset of 2 units (-2)

// -----
// -- Units
// -----

offset_units : offset_units_digits | offset_units_str ;
offset_units_digits : n_0_9999 ;
offset_units_str : ns_all ;

// -----
// -- Prefix
// -----

offset_prefix : offset_pos_prefix | offset_neg_prefix ;
offset_pos_prefix : IN ;
offset_neg_prefix : FROM ;

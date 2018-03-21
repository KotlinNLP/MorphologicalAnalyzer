grammar Offset;

import LexerEN, Date, Time;

offset
    : offset_unit_prefix SPACE_SEP (date | date_time_literal)
    | offset_prefix SPACE_SEP offset_units SPACE_SEP date_time_literal
    ;

offset_unit_prefix : offset_unit_pos_prefix | offset_unit_neg_prefix ;
offset_unit_pos_prefix : NEXT | THIS ; // a prefix that means implicitly a single positive offset unit (+1)
offset_unit_neg_prefix : LAST | PREV ; // a prefix that means implicitly a single negative offset unit (-1)

offset_units : n_0_9999 ;

offset_prefix : offset_pos_prefix | offset_neg_prefix ;
offset_pos_prefix : IN ;
offset_neg_prefix : FROM ;

date_time_literal : date_unit_literal | time_unit_literal ;
